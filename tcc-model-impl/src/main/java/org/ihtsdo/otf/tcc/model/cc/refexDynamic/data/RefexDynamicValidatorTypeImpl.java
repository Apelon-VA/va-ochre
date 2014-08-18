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

import java.math.BigDecimal;
import org.ihtsdo.otf.tcc.api.coordinate.ViewCoordinate;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicValidatorType;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicDoubleBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicFloatBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicIntegerBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicLongBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicNidBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicUUIDBI;
import org.ihtsdo.otf.tcc.api.store.Ts;

/**
 * {@link RefexDynamicValidatorTypeImpl}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
@SuppressWarnings("deprecation")
public class RefexDynamicValidatorTypeImpl
{
	/**
	 * These are all defined from the perspective of the userData - so for passesValidator to return true -
	 * userData must be LESS_THAN validatorDefinitionData, for example.
	 * 
	 * @param userData
	 * @param validatorDefinitionData
	 * @param vc - The View Coordinate - not needed for some types of validations. Null allowed when unneeded (for math based tests, for example)
	 * @return
	 */
	public static boolean passesValidator(RefexDynamicValidatorType type, RefexDynamicDataBI userData, RefexDynamicDataBI validatorDefinitionData, ViewCoordinate vc)
	{
		if (type == RefexDynamicValidatorType.DROOLS)
		{
			//TODO [VALIDATOR] implement Drools
			throw new RuntimeException("Not implemented");
		}
		else if (type == RefexDynamicValidatorType.IS_CHILD_OF || type == RefexDynamicValidatorType.IS_KIND_OF)
		{
			try
			{
				int childNid;
				int parentNid;

				if (userData instanceof RefexDynamicUUIDBI)
				{
					childNid = Ts.get().getNidForUuids(((RefexDynamicUUIDBI) userData).getDataUUID());
				}
				else if (userData instanceof RefexDynamicNidBI)
				{
					childNid = ((RefexDynamicNidBI) userData).getDataNid();
				}
				else
				{
					throw new RuntimeException("Userdata is invalid for a IS_CHILD_OF or IS_KIND_OF comparison");
				}

				if (validatorDefinitionData instanceof RefexDynamicUUIDBI)
				{
					parentNid = Ts.get().getNidForUuids(((RefexDynamicUUIDBI) validatorDefinitionData).getDataUUID());
				}
				else if (validatorDefinitionData instanceof RefexDynamicNidBI)
				{
					parentNid = ((RefexDynamicNidBI) validatorDefinitionData).getDataNid();
				}
				else
				{
					throw new RuntimeException("Validator DefinitionData is invalid for a IS_CHILD_OF or IS_KIND_OF comparison");
				}

				return (type == RefexDynamicValidatorType.IS_CHILD_OF ? Ts.get().isChildOf(childNid, parentNid, vc) : Ts.get().isKindOf(childNid, parentNid, vc));
			}
			catch (Exception e)
			{
				throw new RuntimeException("Failure executing validator", e);
			}
		}
		else
		{
			Number userDataNumber = readNumber(userData);
			Number validatorDefinitionDataNumber;
			if (type == RefexDynamicValidatorType.INTERVAL)
			{
				boolean leftInclusive;
				boolean rightInclusive;

				String s = validatorDefinitionData.getDataObject().toString().trim();

				if (s.charAt(0) == '[')
				{
					leftInclusive = true;
				}
				else if (s.charAt(0) == '(')
				{
					leftInclusive = false;
				}
				else
				{
					throw new RuntimeException("Invalid INTERVAL definition in the validator definition data - char 0 should be [ or (");
				}
				if (s.charAt(s.length() - 1) == ']')
				{
					rightInclusive = true;
				}
				else if (s.charAt(s.length() - 1) == ')')
				{
					rightInclusive = false;
				}
				else
				{
					throw new RuntimeException("Invalid INTERVAL definition in the validator definition data - last char should be ] or )");
				}

				String numeric = s.substring(1, s.length() - 1);
				numeric = numeric.replaceAll("\\s", "");

				int pos = numeric.indexOf(',');
				Number left = null;
				Number right = null;
				if (pos == 0)
				{
					//left is null (- infinity)
					right = parseUnknown(numeric.substring(1, numeric.length()));
				}
				else if (pos > 0)
				{
					left = parseUnknown(numeric.substring(0, pos));
					if (numeric.length() > pos)
					{
						right = parseUnknown(numeric.substring(pos + 1));
					}
				}
				else
				{
					throw new RuntimeException("Invalid INTERVAL definition in the validator definition data");
				}

				if (left != null)
				{
					int compareLeft = compare(userDataNumber, left);
					if ((!leftInclusive && compareLeft == 0) || compareLeft < 0)
					{
						return false;
					}
				}
				if (right != null)
				{
					int compareRight = compare(userDataNumber, right);
					if ((!rightInclusive && compareRight == 0) || compareRight > 0)
					{
						return false;
					}
				}
				return true;
			}
			else
			{
				validatorDefinitionDataNumber = readNumber(validatorDefinitionData);
				int compareResult = compare(userDataNumber, validatorDefinitionDataNumber);

				switch (type)
				{
					case LESS_THAN:
						return compareResult < 0;
					case GREATER_THAN:
						return compareResult > 0;
					case GREATER_THAN_OR_EQUAL:
						return compareResult >= 0;
					case LESS_THAN_OR_EQUAL:
						return compareResult <= 0;
					default:
						throw new RuntimeException("oops");
				}
			}
		}
	}

	private static Number parseUnknown(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e)
		{
			//noop
		}
		try
		{
			return Long.parseLong(value);
		}
		catch (Exception e)
		{
			//noop
		}
		try
		{
			return Float.parseFloat(value);
		}
		catch (Exception e)
		{
			//noop
		}
		try
		{
			return Double.parseDouble(value);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unexpected data passed in to parseUnknown (" + value + ")");
		}
	}

	private static Number readNumber(RefexDynamicDataBI value)
	{
		if (value instanceof RefexDynamicDoubleBI)
		{
			return Double.valueOf(((RefexDynamicDoubleBI) value).getDataDouble());
		}
		else if (value instanceof RefexDynamicFloatBI)
		{
			return Float.valueOf(((RefexDynamicFloatBI) value).getDataFloat());
		}
		else if (value instanceof RefexDynamicIntegerBI)
		{
			return Integer.valueOf(((RefexDynamicIntegerBI) value).getDataInteger());
		}
		else if (value instanceof RefexDynamicLongBI)
		{
			return Long.valueOf(((RefexDynamicLongBI) value).getDataLong());
		}
		else
		{
			throw new RuntimeException("Unexpected data type passed in to readNumber (" + value + ")");
		}
	}

	private static int compare(final Number x, final Number y)
	{
		if (isSpecial(x) || isSpecial(y))
		{
			return Double.compare(x.doubleValue(), y.doubleValue());
		}
		else
		{
			return toBigDecimal(x).compareTo(toBigDecimal(y));
		}
	}

	private static boolean isSpecial(final Number x)
	{
		boolean specialDouble = x instanceof Double && (Double.isNaN((Double) x) || Double.isInfinite((Double) x));
		boolean specialFloat = x instanceof Float && (Float.isNaN((Float) x) || Float.isInfinite((Float) x));
		return specialDouble || specialFloat;
	}

	private static BigDecimal toBigDecimal(final Number number)
	{
		if (number instanceof Integer || number instanceof Long)
		{
			return new BigDecimal(number.longValue());
		}
		else if (number instanceof Float || number instanceof Double)
		{
			return new BigDecimal(number.doubleValue());
		}
		else
		{
			throw new RuntimeException("Unexpected data type passed in to toBigDecimal (" + number.getClass() + ")");
		}
	}
}
