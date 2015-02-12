/*
 * Copyright 2012 International Health Terminology Standards Development Organisation.
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
package org.ihtsdo.otf.tcc.model.cc;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.ihtsdo.otf.tcc.lookup.Hk2Looker;
import org.ihtsdo.otf.tcc.model.cc.termstore.PersistentStoreI;

import javax.inject.Inject;

/**
 *
 * @author kec
 */


public class PersistentStore {

    private static PersistentStore singleton;

    private PersistentStoreI persistentStoreImplementation;

    private PersistentStore() {
        this.persistentStoreImplementation = Hk2Looker.get().getService(PersistentStoreI.class);
    }

    public static PersistentStoreI get() {
        if (singleton == null) {
            singleton = new PersistentStore();
        }
        return singleton.persistentStoreImplementation;
    }
}