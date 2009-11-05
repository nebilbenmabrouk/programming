/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL version 2 of
 * the License.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.component;

import org.objectweb.proactive.annotation.PublicAPI;


/**
 * Constant strings used throughout the components implementation
 *
 * @author The ProActive Team
 */
@PublicAPI
public interface Constants {
    public final static String CONTENT_CONTROLLER = "content-controller";
    public final static String LIFECYCLE_CONTROLLER = "lifecycle-controller";
    public final static String BINDING_CONTROLLER = "binding-controller";
    public final static String NAME_CONTROLLER = "name-controller";
    public final static String ATTRIBUTE_CONTROLLER = "attribute-controller";
    public final static String SUPER_CONTROLLER = "super-controller";
    public final static String MULTICAST_CONTROLLER = "multicast-controller";
    public final static String GATHERCAST_CONTROLLER = "gathercast-controller";
    public final static String MIGRATION_CONTROLLER = "migration-controller";
    public final static String COMPONENT = "component";
    public final static String CYCLIC_NODE_SUFFIX = "-cyclicInstanceNumber-";
    public final static String MEMBRANE_CONTROLLER = "membrane-controller";
    public final static String REQUEST_PRIORITY_CONTROLLER = "request-priority-controller";
    public final static String CONTROLLER_STATE_DUPLICATION = "controller-state-duplication-controller";
    public final static String HOST_SETTER_CONTROLLER = "host-setter-controller";
    public final static String REQUEST_QUEUE_CONTROLLER = "request-queue-controller";
    public final static String AUTONOMIC_CONTROLLER = "autonomic-controller";
    public final static String MONITOR_CONTROLLER = "monitor-controller";

    // hierarchical types of component
    public final static String COMPOSITE = "composite";
    public final static String PRIMITIVE = "primitive";
    public final static String PARALLEL = "parallel";
    public final static boolean SYNCHRONOUS = true;
    public final static boolean WITHOUT_CONFIG_FILE = false;
}
