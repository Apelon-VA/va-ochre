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
package org.ihtsdo.otf.tcc.ddo.store;

import java.io.IOException;
import java.util.UUID;
import org.ihtsdo.otf.tcc.ddo.ComponentReference;
import org.ihtsdo.otf.tcc.ddo.concept.ConceptChronicleDdo;
import org.ihtsdo.otf.tcc.ddo.fetchpolicy.RefexPolicy;
import org.ihtsdo.otf.tcc.ddo.fetchpolicy.RelationshipPolicy;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.store.TerminologySnapshotDI;
import org.ihtsdo.otf.tcc.api.coordinate.ViewCoordinate;

/**
 *
 * @author kec
 */
public interface FxTerminologySnapshotDI extends TerminologySnapshotDI {
   
   ConceptChronicleDdo getFxConcept(UUID conceptUUID) throws IOException, ContradictionException;

   ConceptChronicleDdo getFxConcept(ComponentReference ref, RefexPolicy refexPolicy, RelationshipPolicy relationshipPolicy)
           throws IOException, ContradictionException;

   ConceptChronicleDdo getFxConcept(UUID conceptUUID, RefexPolicy refexPolicy, RelationshipPolicy relationshipPolicy)
           throws IOException, ContradictionException;

    
}
