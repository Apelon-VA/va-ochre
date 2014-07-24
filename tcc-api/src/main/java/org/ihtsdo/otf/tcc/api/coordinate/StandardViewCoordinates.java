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



package org.ihtsdo.otf.tcc.api.coordinate;

import java.io.IOException;
import java.util.EnumSet;
import java.util.UUID;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionManagerPolicy;
import org.ihtsdo.otf.tcc.api.metadata.binding.Snomed;
import org.ihtsdo.otf.tcc.api.metadata.binding.TermAux;
import org.ihtsdo.otf.tcc.api.relationship.RelAssertionType;
import org.ihtsdo.otf.tcc.api.spec.ConceptSpec;
import org.ihtsdo.otf.tcc.api.spec.SimpleConceptSpecification;
import org.ihtsdo.otf.tcc.api.store.Ts;

/**
 *
 * @author kec
 */
public class StandardViewCoordinates {
   public static ViewCoordinate getSnomedInferredLatest() throws IOException {
      ViewCoordinate snomedVc = new ViewCoordinate(UUID.fromString("0c734870-836a-11e2-9e96-0800200c9a66"),
                                   "SNOMED Infered-Latest", Ts.get().getMetadataVC());
      Position snomedPosition =
         Ts.get().newPosition(Ts.get().getPath(Snomed.SNOMED_RELEASE_PATH.getLenient().getConceptNid()),
                              Long.MAX_VALUE);

      snomedVc.setViewPosition(snomedPosition);
      snomedVc.setRelationshipAssertionType(RelAssertionType.INFERRED);
      snomedVc.setAllowedStatus(EnumSet.of(Status.ACTIVE, Status.INACTIVE));

      return snomedVc;
   }
   public static ViewCoordinate getSnomedStatedLatest() throws IOException {
      ViewCoordinate snomedVc = new ViewCoordinate(UUID.fromString("0c734871-836a-11e2-9e96-0800200c9a66"),
                                   "SNOMED Stated-Latest", Ts.get().getMetadataVC());
      Position snomedPosition =
         Ts.get().newPosition(Ts.get().getPath(Snomed.SNOMED_RELEASE_PATH.getLenient().getConceptNid()),
                              Long.MAX_VALUE);

      snomedVc.setViewPosition(snomedPosition);
      snomedVc.setRelationshipAssertionType(RelAssertionType.STATED);

      return snomedVc;
   }
   
      public static ViewCoordinate getSnomedInferredThenStatedLatest() throws IOException {
      ViewCoordinate snomedVc = new ViewCoordinate(UUID.fromString("0c734872-836a-11e2-9e96-0800200c9a66"),
                                   "SNOMED Inferred then Stated-Latest", Ts.get().getMetadataVC());
      Position snomedPosition =
         Ts.get().newPosition(Ts.get().getPath(Snomed.SNOMED_RELEASE_PATH.getLenient().getConceptNid()),
                              Long.MAX_VALUE);

      snomedVc.setViewPosition(snomedPosition);
      snomedVc.setRelationshipAssertionType(RelAssertionType.INFERRED_THEN_STATED);

      return snomedVc;
   }
      
      public static ViewCoordinate getWbAuxiliary() throws IOException
      {
          ViewCoordinate vc = new ViewCoordinate();
          vc.setName("Snomed Inferred Latest");
          vc.setClassifierSpec(TermAux.IHTSDO_CLASSIFIER);
          vc.setLanguageSpec(Snomed.US_LANGUAGE_REFEX);
          vc.getLangPrefConceptSpecList().add(vc.getLanguageSpec());
          vc.setAllowedStatus(EnumSet.of(Status.ACTIVE));
          vc.setPrecedence(Precedence.PATH);
          Path wbAuxPath = new Path();
          wbAuxPath.setConceptSpec(TermAux.WB_AUX_PATH);
          Position latest = new Position();
          latest.setPath(wbAuxPath);
          // Long.MAX_VALUE == latest
          latest.setTime(Long.MAX_VALUE);
          
          vc.setViewPosition(latest);
          vc.setRelationshipAssertionType(RelAssertionType.INFERRED);
          vc.setContradictionManagerPolicy(ContradictionManagerPolicy.LAST_COMMIT_WINS);
          vc.setLanguageSort(LanguageSort.RF2_LANG_REFEX);
          return vc;
      }
}
