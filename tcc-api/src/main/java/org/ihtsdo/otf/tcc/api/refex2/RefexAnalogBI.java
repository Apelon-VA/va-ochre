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

package org.ihtsdo.otf.tcc.api.refex2;

import java.beans.PropertyVetoException;
import java.io.IOException;
import org.ihtsdo.otf.tcc.api.AnalogBI;

/**
 *
 * @author kec
 */
@SuppressWarnings("deprecation")
public interface RefexAnalogBI<A extends RefexAnalogBI<A>> extends RefexVersionBI<A>, AnalogBI
{

	/**
	 * Assemblage an assembled collection of objects. Used to identify the Refex that this item is a member of.
	 * Used instead of RefexExtensionId because of confusion with the component the Refex Extends, or the ReferencedComponentId.
	 * 
	 * @param assemblageNid
	 * @throws IOException
	 * @throws PropertyVetoException
	 */
	void setAssemblageNid(int assemblageNid) throws IOException, PropertyVetoException;

	/**
	 * 
	 * @param refexNid
	 * @throws IOException
	 * @throws PropertyVetoException
	 * @deprecated use setAssemblageNid instead.
	 */
	@Deprecated
	void setRefexExtensionNid(int refexNid) throws IOException, PropertyVetoException;

	void setReferencedComponentNid(int componentNid) throws IOException, PropertyVetoException;
}
