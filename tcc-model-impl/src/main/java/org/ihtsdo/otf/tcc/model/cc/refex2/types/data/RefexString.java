/*
 * Copyright 2010 International Health Terminology Standards Development Organisation.
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

package org.ihtsdo.otf.tcc.model.cc.refex2.types.data;

import java.beans.PropertyVetoException;
import org.ihtsdo.otf.tcc.api.refex2.types.RefexDataType;
import org.ihtsdo.otf.tcc.model.cc.refex2.types.RefexData;

/**
 * 
 * {@link RefexString}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class RefexString extends RefexData
{
	public RefexString()
	{
		super(RefexDataType.STRING);
	}
	
	protected RefexString(RefexDataType type)
	{
		super(type);
	}
	
	public void setDataString(String string) throws PropertyVetoException
	{
		data_ = string;
	}

	public String getDataString()
	{
		return (String) data_;
	}
}
