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
package functionalTests.activeobject.miscellaneous.fifocrash;

import java.io.Serializable;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.EndActive;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;


public class AOCrash2 implements Serializable, EndActive {

    /**
     * 
     */
    private static final long serialVersionUID = 42L;
    private boolean aliveCalled = false;

    public AOCrash2() {

    }

    public StringWrapper foo2() {
        StringBuilder sb = new StringBuilder();
        for (long i = 0L; i < 30000000L; i++) {
            sb.append("X");
        }
        return new StringWrapper(sb.toString());
    }

    public BooleanWrapper alive() {
        aliveCalled = true;
        return new BooleanWrapper(true);
    }

    public void endActivity(Body body) {
        if (!aliveCalled) {
            throw new RuntimeException("Unexpected End of Activity");
        } else {
            System.out.println("Expected end of activity for " + this.getClass().getName());
        }
    }

}
