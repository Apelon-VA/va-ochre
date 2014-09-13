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
package org.ihtsdo.otf.tcc.api.spec;

import java.util.UUID;
import org.ihtsdo.otf.tcc.api.metadata.binding.Snomed;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicColumnInfo;

/**
 * {@link DynamicRefexConceptSpec}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class DynamicRefexConceptSpec extends ConceptSpecWithDescriptions
{
	private static final long serialVersionUID = 1L;
	private boolean annotationStyle_;
	private String refexDescription_;
	private RefexDynamicColumnInfo[] refexColumns_;

	/**
	 * 
	 * @param fsn - Used as the fsn and preferred synonym
	 * @param uuid
	 * @param parentConcept - used as the destination in a relspec, with a type of {@link Snomed#IS_A} and a source of this spec being created.
	 */
	public DynamicRefexConceptSpec(String fsn, UUID uuid, boolean annotationStyle, String refexDescription,
			RefexDynamicColumnInfo[] columns, ConceptSpec parentConcept)
	{
		this(fsn, uuid, new String[] {fsn}, null, annotationStyle, refexDescription, columns, parentConcept);
	}
	
	/**
	 * @param fsn
	 * @param uuid
	 * @param synonyms
	 * @param definitions
	 * @param annotationStyle - true to build this as an annotation style, false for member style
	 * @param refexDescription - describe the purpose of the use of this refex
	 * @param columns - The definitions of the attached data columns that are allowed on this refex
	 * @param parentConcept - used as the destination in a relspec, with a type of {@link Snomed#IS_A} and a source of this spec being created.
	 */
	public DynamicRefexConceptSpec(String fsn, UUID uuid, String[] synonyms, String[] definitions, boolean annotationStyle, String refexDescription,
			RefexDynamicColumnInfo[] columns, ConceptSpec parentConcept)
	{
		super(fsn, uuid, synonyms, definitions, parentConcept);
		annotationStyle_ = annotationStyle;
		refexDescription_ = refexDescription;
		refexColumns_ = columns;
	}

	/**
	 * @return the annotationStyle_
	 */
	public boolean isAnnotationStyle()
	{
		return annotationStyle_;
	}

	/**
	 * @return the refexDescription_
	 */
	public String getRefexDescription()
	{
		return refexDescription_;
	}

	/**
	 * @return the refexColumns_
	 */
	public RefexDynamicColumnInfo[] getRefexColumns()
	{
		return refexColumns_;
	}
}
