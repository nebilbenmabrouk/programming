package org.objectweb.proactive.core.component.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.body.migration.MigrationException;
import org.objectweb.proactive.core.body.request.ServeException;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ProActiveInterface;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.collectiveitfs.GatherBindingChecker;
import org.objectweb.proactive.core.component.collectiveitfs.GatherRequestsQueues;
import org.objectweb.proactive.core.component.exceptions.ParameterDispatchException;
import org.objectweb.proactive.core.component.identity.ProActiveComponent;
import org.objectweb.proactive.core.component.representative.ItfID;
import org.objectweb.proactive.core.component.request.ComponentRequest;
import org.objectweb.proactive.core.component.type.ProActiveInterfaceType;
import org.objectweb.proactive.core.component.type.ProActiveTypeFactoryImpl;
import org.objectweb.proactive.core.node.Node;


public class GathercastControllerImpl
    extends AbstractProActiveController
    implements GathercastController {
    private Map<String, List<ItfID>> bindingsOnServerItfs = new HashMap<String, List<ItfID>>();
    private Map<String, ProActiveInterface> gatherItfs = new HashMap<String, ProActiveInterface>();
    private GatherRequestsQueues gatherRequestsHandler;

    public GathercastControllerImpl(Component owner) {
        super(owner);
    }

    /*
     * @see org.objectweb.proactive.core.component.controller.AbstractCollectiveInterfaceController#init()
     */
    public void init() {
        if (gatherRequestsHandler == null) {
            gatherRequestsHandler = new GatherRequestsQueues((ProActiveComponent) owner);
            List<Object> interfaces = Arrays.asList(owner.getFcInterfaces());
            Iterator<Object> it = interfaces.iterator();

            while (it.hasNext()) {
            	Interface itf = (Interface)it.next();
            	// gather mechanism currently only offered for functional interfaces
            	if (!Utils.isControllerInterfaceName(itf.getFcItfName())) {
            		addManagedInterface((ProActiveInterface) itf);
            	}
            }
        }
    }

    private boolean addManagedInterface(ProActiveInterface itf) {
        if (gatherItfs.containsKey(itf.getFcItfName())) {
            return false;
        }

        ProActiveInterfaceType itfType = (ProActiveInterfaceType) itf.getFcItfType();

        if (itfType.isFcGathercastItf()) {
            gatherItfs.put(itf.getFcItfName(), itf);
        } else {
            return false;
        }

        return true;
    }

    protected Method searchMatchingMethod(Method clientSideMethod,
        Method[] serverSideMethods, boolean clientItfIsMulticast,
        boolean serverItfIsGathercast, ProActiveInterface serverSideItf) {
        return searchMatchingMethod(clientSideMethod, serverSideMethods,
            clientItfIsMulticast);
    }

    /*
     * @see org.objectweb.proactive.core.component.controller.AbstractCollectiveInterfaceController#searchMatchingMethod(java.lang.reflect.Method, java.lang.reflect.Method[])
     */
    protected Method searchMatchingMethod(Method clientSideMethod,
        Method[] serverSideMethods, boolean clientItfIsMulticast) {
        try {
            return GatherBindingChecker.searchMatchingMethod(clientSideMethod,
                serverSideMethods, clientItfIsMulticast);
        } catch (ParameterDispatchException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @see org.objectweb.proactive.core.component.controller.AbstractProActiveController#setControllerItfType()
     */
    @Override
    protected void setControllerItfType() {
        try {
            setItfType(ProActiveTypeFactoryImpl.instance()
                                               .createFcItfType(Constants.GATHERCAST_CONTROLLER,
                    GathercastController.class.getName(), TypeFactory.SERVER,
                    TypeFactory.MANDATORY, TypeFactory.SINGLE));
        } catch (InstantiationException e) {
            throw new ProActiveRuntimeException(
                "cannot create controller type for controller " +
                this.getClass().getName());
        }
    }

    /*
     * @see org.objectweb.proactive.core.component.controller.GatherController#handleRequestOnGatherItf(org.objectweb.proactive.core.component.request.ComponentRequest)
     */
    public Object handleRequestOnGatherItf(ComponentRequest r)
        throws ServeException {
        if (gatherRequestsHandler == null) {
            init();
        }
        return gatherRequestsHandler.addRequest(r);
    }

    /*
     * TODO : throw exception when binding already exists? (this would make the method synchronous)
     * @see org.objectweb.proactive.core.component.controller.ProActiveBindingController#addedBindingOnServerItf(org.objectweb.proactive.core.component.ProActiveInterface, org.objectweb.proactive.core.component.ProActiveInterface)
     */
    public void addedBindingOnServerItf(String serverItfName,
        ProActiveComponent sender, String clientItfName) {
        ItfID itfID = new ItfID(clientItfName, sender.getID());
        if (bindingsOnServerItfs.containsKey(serverItfName)) {
            if (bindingsOnServerItfs.get(serverItfName).contains(itfID)) {
                throw new ProActiveRuntimeException(
                    "trying to add twice the binding of client interface " +
                    clientItfName + " on server interface " + serverItfName);
            }
            bindingsOnServerItfs.get(serverItfName).add(itfID);
        } else {
            List<ItfID> connectedClientItfs = new ArrayList<ItfID>();
            connectedClientItfs.add(itfID);
            bindingsOnServerItfs.put(serverItfName, connectedClientItfs);
        }
    }

    /*
     *
     * @see org.objectweb.proactive.core.component.controller.ProActiveBindingController#removedBindingOnServerItf(java.lang.String, org.objectweb.proactive.core.component.identity.ProActiveComponent, java.lang.String)
     */
    public void removedBindingOnServerItf(String serverItfName,
        ProActiveComponent owner, String clientItfName) {
        ItfID itfID = new ItfID(clientItfName, owner.getID());
        if (bindingsOnServerItfs.containsKey(serverItfName)) {
            List<ItfID> connectedClientItfs = bindingsOnServerItfs.get(serverItfName);
            if (connectedClientItfs.contains(itfID)) {
                connectedClientItfs.remove(itfID);
            } else {
                controllerLogger.error(
                    "could not remove binding on server interface " +
                    serverItfName +
                    " because owner component is not listed as connected components");
            }
        } else {
            controllerLogger.error(
                "could not remove binding on server interface " +
                serverItfName +
                " because there is no component listed as connected on this server interface");
        }
    }

    public List<ItfID> getConnectedClientItfs(String serverItfName) {
        return bindingsOnServerItfs.get(serverItfName);
    }

    @Override
    public void migrateDependentActiveObjectsTo(Node node)
        throws MigrationException {
        if (gatherRequestsHandler != null) {
            gatherRequestsHandler.migrateFuturesHandlersTo(node);
        }
    }
    
	public void ensureCompatibility(ProActiveInterfaceType clientItfType, ProActiveInterface itf) throws IllegalBindingException {
		// nothing to do in this version
		
	}

	private void writeObject(java.io.ObjectOutputStream out)
        throws java.io.IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
