/*
 * Copyright 2014 International Health Terminology Standards Development Organisation.
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

package org.ihtsdo.otf.tcc.datastore;

import org.ihtsdo.otf.tcc.model.cc.concept.ConceptDataFactory;
import org.ihtsdo.otf.tcc.model.cc.concept.ConceptDataSimpleReference;
import org.ihtsdo.otf.tcc.model.cc.concept.I_ManageConceptData;

/**
 *
 * @author aimeefurber
 */
public class SimpleConceptDataFactory implements ConceptDataFactory{

    @Override
    public I_ManageConceptData getConceptDataManager() {
        return new ConceptDataSimpleReference();
    }
    
}
