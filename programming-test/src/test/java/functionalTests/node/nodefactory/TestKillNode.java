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
package functionalTests.node.nodefactory;

import java.rmi.AlreadyBoundException;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.core.node.NodeFactory;

import functionalTests.FunctionalTest;


public class TestKillNode extends FunctionalTest {
    /* PROACTIVE-573 reported that killNode was buggy */
    @Test
    public void test() throws NodeException, AlreadyBoundException {
        Node node = NodeFactory.createLocalNode("PROACTIVE-573", false, null);
        node = NodeFactory.getNode(node.getNodeInformation().getURL());
        Assert.assertNotNull(node);
        NodeFactory.killNode(node.getNodeInformation().getURL());
        try {
            node = NodeFactory.getNode(node.getNodeInformation().getURL());
            Assert.fail("The previous line must throw a NodeException");
        } catch (NodeException e) {
            logger.info("Exception catched, everything is fine");
        }
    }
}
