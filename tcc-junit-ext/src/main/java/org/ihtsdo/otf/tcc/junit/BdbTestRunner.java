package org.ihtsdo.otf.tcc.junit;

//~--- non-JDK imports --------------------------------------------------------

import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.datastore.Bdb;
import org.ihtsdo.otf.tcc.datastore.BdbTerminologyStore;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

//~--- JDK imports ------------------------------------------------------------

/*
* Copyright 2011 International Health Terminology Standards Development Organisation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */
import java.io.File;
import java.io.IOException;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kec
 */
public class BdbTestRunner extends BlockJUnit4ClassRunner {
    private static boolean addHook      = true;
    private static String  bdbLocation  = null;
    private static File    buildDirFile = new File("target");

    public BdbTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        buildDirFile = getBuildDirectory();
        System.out.println("Build directory: " + buildDirFile.getAbsolutePath());

        BdbTestRunnerConfig annotation = klass.getAnnotation(BdbTestRunnerConfig.class);

        if (annotation == null) {
            throw new InitializationError("You must specify a BdbTestRunnerConfig annotation for the test");
        }

        File dbDir = new File(buildDirFile, annotation.bdbLocation());
        System.setProperty(BdbTerminologyStore.BDB_LOCATION_PROPERTY, dbDir.getAbsolutePath());

        if ((bdbLocation != null) &&!bdbLocation.equals(dbDir.getAbsolutePath())) {
            try {
                Bdb.close();
                bdbLocation = null;
            } catch (InterruptedException | ExecutionException ex) {
                throw new InitializationError(ex);
            }
        }

        if (bdbLocation == null) {
            try {
                Bdb.selectJeProperties(dbDir, dbDir);
            } catch (IOException ex) {
                throw new InitializationError(ex);
            }


            BdbTerminologyStore store = new BdbTerminologyStore();

            bdbLocation = dbDir.getAbsolutePath();
        }

        System.out.println("Created BdbTestRunner for: " + klass);

        if (addHook) {
            addHook = false;
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Ts.get().cancel();
                        Bdb.close();
                    } catch (InterruptedException | ExecutionException | IOException ex) {
                        Logger.getLogger(BdbTestRunner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }, "Shutdown hook"));
        }
    }

    public static File getBuildDirectory() {
        String surefireClassPath = System.getProperty("surefire.test.class.path");

        if (surefireClassPath != null) {
            String[] surefireClassPathParts = surefireClassPath.split(":");

            return new File(surefireClassPathParts[0].replaceAll("test-classes$", ""));
        }

        return new File("target");
    }
}
