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
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.vha.isaac.ochre.util;

/**
 * {@link Interval}
 * 
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
public class Interval
{
	private boolean leftInclusive_, rightInclusive_;
	private Number left_, right_;
	
	public Interval(Number left, boolean leftInclusive, Number right, boolean rightInclusive)
	{
		this.left_ = left;
		this.right_ = right;
		this.leftInclusive_ = leftInclusive;
		this.rightInclusive_ = rightInclusive;
	}
	
	public Interval(String parseFrom)
	{
		String s = parseFrom.trim();

		if (s.charAt(0) == '[')
		{
			leftInclusive_ = true;
		}
		else if (s.charAt(0) == '(')
		{
			leftInclusive_ = false;
		}
		else
		{
			throw new NumberFormatException("Invalid INTERVAL definition in the validator definition data - char 0 should be [ or (");
		}
		if (s.charAt(s.length() - 1) == ']')
		{
			rightInclusive_ = true;
		}
		else if (s.charAt(s.length() - 1) == ')')
		{
			rightInclusive_ = false;
		}
		else
		{
			throw new NumberFormatException("Invalid INTERVAL definition in the validator definition data - last char should be ] or )");
		}

		String numeric = s.substring(1, s.length() - 1);
		numeric = numeric.replaceAll("\\s", "");

		int pos = numeric.indexOf(',');
		if (pos == 0)
		{
			//left is null (- infinity)
			right_ = NumericUtils.parseUnknown(numeric.substring(1, numeric.length()));
		}
		else if (pos > 0)
		{
			left_ = NumericUtils.parseUnknown(numeric.substring(0, pos));
			if (numeric.length() > (pos + 1))
			{
				right_ = NumericUtils.parseUnknown(numeric.substring(pos + 1));
			}
		}
		else
		{
			throw new NumberFormatException("Invalid INTERVAL definition in the validator definition data");
		}
		
		//make sure interval is properly specified
		if (left_ != null && right_ != null)
		{
			if (NumericUtils.compare(left_, right_) > 0)
			{
				throw new NumberFormatException("Invalid INTERVAL definition the left value should be <= the right value");
			}
		}
	}
	
	
	/**
	 * @return the leftInclusive
	 */
	public boolean isLeftInclusive()
	{
		return leftInclusive_;
	}
	/**
	 * @return the rightInclusive
	 */
	public boolean isRightInclusive()
	{
		return rightInclusive_;
	}
	/**
	 * @return the left
	 */
	public Number getLeft()
	{
		return left_;
	}
	/**
	 * @return the right
	 */
	public Number getRight()
	{
		return right_;
	}
}