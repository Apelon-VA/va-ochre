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
package org.ihtsdo.otf.tcc.model.cc.refexDynamic.data;

import java.beans.PropertyVetoException;
import java.io.IOException;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicValidatorType;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexDouble;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexFloat;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexInteger;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexLong;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexString;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link RefexDynamicValidatorTypeImplTest}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class RefexDynamicValidatorTypeImplTest
{
	@Test
	public void testOne() throws PropertyVetoException, IOException, ContradictionException
	{
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexDouble(5.0), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexDouble(1.0), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexInteger(5), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexInteger(1), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexFloat(5.0f), new RefexFloat(3.0f), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexDouble(1.0), new RefexFloat(3.0f), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexLong(Long.MAX_VALUE), new RefexLong(30), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexLong(1), new RefexLong(3), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexLong(1), new RefexLong(1), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN.passesValidator(new RefexLong(1), new RefexDouble(1), null));
	}
	
	@Test
	public void testTwo() throws PropertyVetoException, IOException, ContradictionException
	{
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexDouble(5.0), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexDouble(1.0), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexInteger(5), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexInteger(1), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexFloat(5.0f), new RefexFloat(3.0f), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexDouble(1.0), new RefexFloat(3.0f), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexLong(Long.MAX_VALUE), new RefexLong(30), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexLong(1), new RefexLong(3), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexLong(1), new RefexLong(1), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN.passesValidator(new RefexLong(1), new RefexDouble(1), null));
	}
	
	@Test
	public void testThree() throws PropertyVetoException, IOException, ContradictionException
	{
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexDouble(5.0), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexDouble(1.0), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexInteger(5), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexInteger(1), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexFloat(5.0f), new RefexFloat(3.0f), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexDouble(1.0), new RefexFloat(3.0f), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexLong(Long.MAX_VALUE), new RefexLong(30), null));
		Assert.assertFalse(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexLong(1), new RefexLong(3), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexLong(1), new RefexLong(1), null));
		Assert.assertTrue(RefexDynamicValidatorType.GREATER_THAN_OR_EQUAL.passesValidator(new RefexLong(1), new RefexDouble(1), null));
		
	}
	
	@Test
	public void testFour() throws PropertyVetoException, IOException, ContradictionException
	{
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexDouble(5.0), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexDouble(1.0), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexInteger(5), new RefexDouble(3.0), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexInteger(1), new RefexDouble(3.0), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexFloat(5.0f), new RefexFloat(3.0f), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexDouble(1.0), new RefexFloat(3.0f), null));
		Assert.assertFalse(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexLong(Long.MAX_VALUE), new RefexLong(30), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexLong(1), new RefexLong(3), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexLong(1), new RefexLong(1), null));
		Assert.assertTrue(RefexDynamicValidatorType.LESS_THAN_OR_EQUAL.passesValidator(new RefexLong(1), new RefexDouble(1), null));
	}
	
	@Test
	public void testInterval() throws PropertyVetoException, IOException, ContradictionException
	{
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexDouble(5.0), new RefexString("[4, 7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexDouble(5.0), new RefexString("[4.0, 7.7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexFloat(5.0f), new RefexString("[4, 7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexFloat(5.0f), new RefexString("[4.0, 7.7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexLong(5l), new RefexString("[4, 7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexLong(5l), new RefexString("[4.0, 7.7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(5), new RefexString("[4, 7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(5), new RefexString("[4.0, 7.7]"), null));
		
		Assert.assertFalse(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(Integer.MAX_VALUE), 
				new RefexString("[4.0, 7.7]"), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(Integer.MAX_VALUE), 
				new RefexString("[4.0,2147483647]"), null));
		Assert.assertFalse(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(Integer.MAX_VALUE), 
				new RefexString(" [4.0 , 2147483647) "), null));
		
		Assert.assertFalse(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexDouble(Double.MIN_VALUE), 
				new RefexString(" [4.0 , 2147483647) "), null));
		Assert.assertTrue(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(Integer.MIN_VALUE), 
				new RefexString(" [-2147483648 , 2147483647) "), null));
		Assert.assertFalse(RefexDynamicValidatorType.INTERVAL.passesValidator(new RefexInteger(Integer.MIN_VALUE), 
				new RefexString(" (-2147483648 , 2147483647) "), null));
		
	}
}
