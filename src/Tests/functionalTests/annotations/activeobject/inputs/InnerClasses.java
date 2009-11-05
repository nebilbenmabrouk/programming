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
package functionalTests.annotations.activeobject.inputs;

import org.objectweb.proactive.extensions.annotation.ActiveObject;


@ActiveObject
public class InnerClasses {

    protected javax.swing.JSplitPane verticalSplitPane;

    public InnerClasses() {
    }

    // inner class
    class Dada {
    }

    // inner class
    // ERROR
    @ActiveObject
    class AnnotatedDada {
    }

    public void localInnerClass() {
        // local inner class
        class InnerClass {
        }

        // local inner class
        // ERROR - unfortunately cannot be checked!
        @ActiveObject
        class AnnotatedInnerClass {
        }
    }

    // anonymous inner class
    public InnerClasses(String name, Integer width, Integer height) {
        verticalSplitPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }
        });
    }
}
