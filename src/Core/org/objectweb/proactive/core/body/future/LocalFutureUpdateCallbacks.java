package org.objectweb.proactive.core.body.future;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.core.body.BodyImpl;
import org.objectweb.proactive.core.mop.MethodCall;
import org.objectweb.proactive.core.security.exceptions.RenegotiateSessionException;


/**
 * A callback is method declared as 'void myCallback(FutureResult fr)'
 * It is added using addFutureCallback(myFuture, "myCallback"), and will be
 * queued when the future is updated on ProActive.getBodyOnThis()
 * Callbacks are local, so are not copied when a future is serialized.
 */
public class LocalFutureUpdateCallbacks {
    private BodyImpl body;
    private Collection<Method> methods;
    private FutureProxy future;

    LocalFutureUpdateCallbacks(FutureProxy future) {
        try {
            this.body = (BodyImpl) ProActive.getBodyOnThis();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Can only be called in a body");
        }
        this.methods = new LinkedList<Method>();
        this.future = future;
    }

    void add(String methodName) {
        if (ProActive.getBodyOnThis() != this.body) {
            throw new IllegalStateException("Callbacks added by different " +
                "bodies on the same future, this cannot be possible" +
                "without breaking the no-sharing property");
        }
        Object target = this.body.getReifiedObject();
        Class<?> c = target.getClass();
        Method m;
        try {
            m = c.getMethod(methodName, FutureResult.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot find method: " +
                c.getName() + "." + methodName + "(FutureResult)", e);
        }
        this.methods.add(m);
    }

    void run() {
        FutureResult[] args = new FutureResult[] { this.future.getFutureResult() };

        for (Method m : this.methods) {
            MethodCall mc = MethodCall.getMethodCall(m, args, null);

            try {
                this.body.sendRequest(mc, null, this.body);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RenegotiateSessionException e) {
                e.printStackTrace();
            }
        }
    }
}
