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
package org.objectweb.proactive.extensions.calcium.examples.blast;

import org.apache.log4j.Logger;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.objectweb.proactive.extensions.calcium.muscle.Execute;
import org.objectweb.proactive.extensions.calcium.stateness.StateFul;
import org.objectweb.proactive.extensions.calcium.system.PrefetchFilesMatching;
import org.objectweb.proactive.extensions.calcium.system.SkeletonSystem;
import org.objectweb.proactive.extensions.calcium.system.WSpace;


@StateFul(value = false)
@PrefetchFilesMatching(name = "query.*|formatdb")
public class ExecuteFormatQuery implements Execute<BlastParams, BlastParams> {
    /**
     * 
     */
    private static final long serialVersionUID = 42L;
    static Logger logger = ProActiveLogger.getLogger(Loggers.SKELETONS_APPLICATION);

    public BlastParams execute(BlastParams param, SkeletonSystem system) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing Format Query");
        }

        //Put the native program in the wspace
        WSpace wspace = system.getWorkingSpace();

        //File formatDB = wspace.copyInto(param.formatProg);

        //Execute the native command
        String args = param.getFormatQueryString();
        system.execCommand(param.formatProg, args);

        //File[] index = wspace.listFiles(new FormatDBOutputFilter());
        //param.queryIndexFiles = index;

        //TODO keep a reference on the index files??????????
        return param;
    }
}
