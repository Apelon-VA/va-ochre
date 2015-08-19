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
package gov.vha.isaac.ochre.model.sememe.dataTypes;

import gov.vha.isaac.ochre.api.component.sememe.version.dynamicSememe.dataTypes.DynamicSememeBooleanBI;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 
 * {@link DynamicSememeBoolean}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class DynamicSememeBoolean extends DynamicSememeData implements DynamicSememeBooleanBI {

	private ObjectProperty<Boolean> property_;

	protected DynamicSememeBoolean(byte[] data)
	{
		super(data);
	}
	
	protected DynamicSememeBoolean(byte[] data, int assemblageSequence, int columnNumber)
	{
		super(data, assemblageSequence, columnNumber);
	}
	
	public DynamicSememeBoolean(boolean b) {
		super();
		data_ = (b ? new byte[] { 1 } : new byte[] { 0 });
	}

	/**
	 * @see org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicBooleanBI#getDataBooleanProperty()
	 */
	@Override
	public ReadOnlyObjectProperty<Boolean> getDataBooleanProperty()  {
		if (property_ == null) {
			property_ = new SimpleObjectProperty<Boolean>(null, getName(), getDataBoolean());
		}
		return property_;
	}

	/**
	 * @see org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataBI#getDataObject()
	 */
	@Override
	public ReadOnlyObjectProperty<?> getDataObjectProperty() {
		return getDataBooleanProperty();
	}

	/**
	 * @see org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicBooleanBI#getDataBoolean()
	 */
	@Override
	public boolean getDataBoolean() {
		return data_[0] == 1 ? true : false;
	}

	/**
	 * @see org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataBI#getDataObject()
	 */
	@Override
	public Object getDataObject() {
		return getDataBoolean();
	}
}
