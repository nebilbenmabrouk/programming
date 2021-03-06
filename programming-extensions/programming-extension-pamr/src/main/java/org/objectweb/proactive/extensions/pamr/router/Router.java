/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.objectweb.proactive.extensions.pamr.router;

import java.net.InetAddress;

import org.objectweb.proactive.annotation.PublicAPI;


/** A ProActive message router 
 * 
 *  
 * A router receives messages from client and forward them to another client.
 * 
 * @since ProActive 4.1.0
 */
@PublicAPI
public abstract class Router {

    static public Router createAndStart(RouterConfig config) throws Exception {
        // config is now immutable
        config.setReadOnly();

        RouterImpl r = new RouterImpl(config);

        Thread rThread = new Thread(r);
        rThread.setName("Router: select");
        if (config.isDaemon()) {
            rThread.setDaemon(config.isDaemon());
        }
        rThread.start();

        return r;
    }

    /** Returns the port on which the router is, or was, listening */
    abstract public int getPort();

    /** Returns the {@link InetAddress} on which the router is, or was, listening */
    abstract public InetAddress getInetAddr();

    /** Stops the router
     * 
     * Terminates all the threads and unbind all the sockets.
     */
    abstract public void stop();
}
