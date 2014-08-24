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
package org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes;

import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataType;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.RefexDynamicData;

/**
 * {@link RefexDynamicTypeToClassUtility}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
public class RefexDynamicTypeToClassUtility
{
	public static RefexDynamicData typeToClass(RefexDynamicDataType type, byte[] data, int assemblageNid, int columnNumber) 
	{
		if (RefexDynamicDataType.NID == type) {
			return new RefexDynamicNid(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.STRING == type) {
			return new RefexDynamicString(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.INTEGER == type) {
			return new RefexDynamicInteger(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.BOOLEAN == type) {
			return new RefexDynamicBoolean(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.LONG == type) {
			return new RefexDynamicLong(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.BYTEARRAY == type) {
			return new RefexDynamicByteArray(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.FLOAT == type) {
			return new RefexDynamicFloat(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.DOUBLE == type) {
			return new RefexDynamicDouble(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.UUID == type) {
			return new RefexDynamicUUID(data, assemblageNid, columnNumber);
		}
		if (RefexDynamicDataType.POLYMORPHIC == type || RefexDynamicDataType.UNKNOWN == type) {
			throw new RuntimeException("No implementation exists for type unknown");
		}
		throw new RuntimeException("Implementation error");
	}
}