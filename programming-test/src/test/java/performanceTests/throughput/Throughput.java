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
package performanceTests.throughput;

import java.io.Serializable;

import org.junit.Test;
import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.config.CentralPAPropertyRepository;
import org.objectweb.proactive.core.node.NodeException;

import functionalTests.GCMFunctionalTest;
import performanceTests.HudsonReport;


public abstract class Throughput extends GCMFunctionalTest {

    private Class<?> cl;

    public Throughput(Class<?> cl) throws ProActiveException {
        super(1, 1);
        super.startDeployment();
        this.cl = cl;
    }

    @Test
    public void test() throws ActiveObjectCreationException, NodeException {
        Server server = PAActiveObject.newActive(Server.class, new Object[] {}, super.getANode());
        Client client = PAActiveObject.newActive(Client.class, new Object[] { server });

        double throughput = client.runTest();
        HudsonReport.reportToHudson(this.cl, throughput);
    }

    static public class Server implements Serializable {
        boolean firstRequest = true;

        long count = 0;

        long startTime;

        public Server() {

        }

        public void serve() {
            if (firstRequest) {
                startTime = System.currentTimeMillis();
                firstRequest = false;
            }

            count++;
        }

        public double finish() {
            long endTime = System.currentTimeMillis();
            double throughput = (1000.0 * count) / (endTime - startTime);

            System.out.println("Count: " + count);
            System.out.println("Duration: " + (endTime - startTime));
            System.out.println("Throughput " + throughput);
            return throughput;
        }
    }

    static public class Client implements Serializable {
        private Server server;

        public Client() {

        }

        public Client(Server server) {
            this.server = server;
        }

        public double runTest() {
            // Warmup
            for (int i = 0; i < 1000; i++) {
                server.serve();
            }

            long startTime = System.currentTimeMillis();
            final long testDuration = CentralPAPropertyRepository.PA_TEST_PERF_DURATION.getValue();
            while (true) {
                if (System.currentTimeMillis() - startTime > testDuration)
                    break;

                for (int i = 0; i < 50; i++) {
                    server.serve();
                }
            }
            double throughput = server.finish();

            // startTest must be sync 
            return throughput;
        }
    }
}
