/**
 * Copyright Notice
 *
 * This is a work of the U.S. Government and is not subject to copyright
 * protection in the United States. Foreign copyrights may apply.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ihtsdo.otf.tcc.test.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.UUID;
import org.ihtsdo.otf.tcc.api.concept.ConceptVersionBI;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.coordinate.StandardViewCoordinates;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicValidatorType;
import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.junit.BdbTestRunner;
import org.ihtsdo.otf.tcc.junit.BdbTestRunnerConfig;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexDynamicNid;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexDynamicUUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * {@link RefexDynamicValidatorLiveDataTests}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
@SuppressWarnings("deprecation")
@RunWith(BdbTestRunner.class)
@BdbTestRunnerConfig()
public class RefexDynamicValidatorLiveDataTests
{

	public RefexDynamicValidatorLiveDataTests()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void isChildOf() throws IOException, ContradictionException, PropertyVetoException
	{
		ConceptVersionBI centrifugalForceVersion = Ts.get().getConceptVersion(StandardViewCoordinates.getSnomedInferredLatest(),
				UUID.fromString("2b684fe1-8baf-34ef-9d2a-df03142c915a"));

		ConceptVersionBI motionVersion = Ts.get().getConceptVersion(StandardViewCoordinates.getSnomedInferredLatest(),
				UUID.fromString("45a8fde8-535d-3d2a-b76b-95ab67718b41"));
		
		ConceptVersionBI accelerationVersion = Ts.get().getConceptVersion(StandardViewCoordinates.getSnomedInferredLatest(),
				UUID.fromString("6ef49616-e2c7-3557-b7f1-456a2c5a5e54"));

		assertFalse(RefexDynamicValidatorType.IS_CHILD_OF.passesValidator(new RefexDynamicNid(motionVersion.getNid()), new RefexDynamicNid(
				accelerationVersion.getNid()), StandardViewCoordinates.getSnomedInferredLatest()));
		
		assertFalse(RefexDynamicValidatorType.IS_CHILD_OF.passesValidator(new RefexDynamicNid(centrifugalForceVersion.getNid()), new RefexDynamicNid(
				motionVersion.getNid()), StandardViewCoordinates.getSnomedInferredLatest()));
		
		assertTrue(RefexDynamicValidatorType.IS_CHILD_OF.passesValidator(new RefexDynamicNid(accelerationVersion.getNid()), new RefexDynamicNid(
				motionVersion.getNid()), StandardViewCoordinates.getSnomedInferredLatest()));
		
		assertTrue(RefexDynamicValidatorType.IS_CHILD_OF.passesValidator(new RefexDynamicUUID(accelerationVersion.getUUIDs().get(0)),
				new RefexDynamicUUID(motionVersion.getUUIDs().get(0)), StandardViewCoordinates.getSnomedInferredLatest()));
	}
	
	@Test
	public void isKindOf() throws IOException, ContradictionException, PropertyVetoException
	{
		ConceptVersionBI centrifugalForceVersion = Ts.get().getConceptVersion(StandardViewCoordinates.getSnomedInferredLatest(),
				UUID.fromString("2b684fe1-8baf-34ef-9d2a-df03142c915a"));

		ConceptVersionBI motionVersion = Ts.get().getConceptVersion(StandardViewCoordinates.getSnomedInferredLatest(),
				UUID.fromString("45a8fde8-535d-3d2a-b76b-95ab67718b41"));
		
		ConceptVersionBI accelerationVersion = Ts.get().getConceptVersion(StandardViewCoordinates.getSnomedInferredLatest(),
				UUID.fromString("6ef49616-e2c7-3557-b7f1-456a2c5a5e54"));

		assertFalse(RefexDynamicValidatorType.IS_KIND_OF.passesValidator(new RefexDynamicNid(motionVersion.getNid()), new RefexDynamicNid(
				accelerationVersion.getNid()), StandardViewCoordinates.getSnomedInferredLatest()));
		
		assertTrue(RefexDynamicValidatorType.IS_KIND_OF.passesValidator(new RefexDynamicNid(centrifugalForceVersion.getNid()), new RefexDynamicNid(
				motionVersion.getNid()), StandardViewCoordinates.getSnomedInferredLatest()));
		
		assertTrue(RefexDynamicValidatorType.IS_KIND_OF.passesValidator(new RefexDynamicNid(accelerationVersion.getNid()), new RefexDynamicNid(
				motionVersion.getNid()), StandardViewCoordinates.getSnomedInferredLatest()));
		
		assertTrue(RefexDynamicValidatorType.IS_KIND_OF.passesValidator(new RefexDynamicUUID(accelerationVersion.getUUIDs().get(0)),
				new RefexDynamicUUID(motionVersion.getUUIDs().get(0)), StandardViewCoordinates.getSnomedInferredLatest()));
	}
}