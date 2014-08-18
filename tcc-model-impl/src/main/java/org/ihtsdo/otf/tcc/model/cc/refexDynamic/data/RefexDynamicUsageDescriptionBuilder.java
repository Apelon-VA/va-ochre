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
package org.ihtsdo.otf.tcc.model.cc.refexDynamic.data;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.UUID;
import org.ihtsdo.otf.tcc.api.blueprint.ComponentProperty;
import org.ihtsdo.otf.tcc.api.blueprint.ConceptCB;
import org.ihtsdo.otf.tcc.api.blueprint.DescriptionCAB;
import org.ihtsdo.otf.tcc.api.blueprint.IdDirective;
import org.ihtsdo.otf.tcc.api.blueprint.InvalidCAB;
import org.ihtsdo.otf.tcc.api.blueprint.RefexCAB;
import org.ihtsdo.otf.tcc.api.blueprint.RefexDirective;
import org.ihtsdo.otf.tcc.api.blueprint.RefexDynamicCAB;
import org.ihtsdo.otf.tcc.api.concept.ConceptChronicleBI;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.coordinate.EditCoordinate;
import org.ihtsdo.otf.tcc.api.coordinate.StandardViewCoordinates;
import org.ihtsdo.otf.tcc.api.lang.LanguageCode;
import org.ihtsdo.otf.tcc.api.metadata.binding.RefexDynamic;
import org.ihtsdo.otf.tcc.api.metadata.binding.Snomed;
import org.ihtsdo.otf.tcc.api.metadata.binding.SnomedMetadataRf2;
import org.ihtsdo.otf.tcc.api.metadata.binding.TermAux;
import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicColumnInfo;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataType;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicUsageDescription;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicBooleanBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicByteArrayBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicDoubleBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicFloatBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicIntegerBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicLongBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicNidBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicStringBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicUUIDBI;
import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexBoolean;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexInteger;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexString;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.data.dataTypes.RefexUUID;

/**
 * {@link RefexDynamicUsageDescriptionBuilder}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
@SuppressWarnings("deprecation")
public class RefexDynamicUsageDescriptionBuilder
{
	
	/**
	 * Just calls {@link RefexDynamicUsageDescription#read(int)
	 */
	public static RefexDynamicUsageDescription readRefexDynamicUsageDescriptionConcept(int nid) throws IOException, ContradictionException
	{
		return RefexDynamicUsageDescription.read(nid);
	}
	
	/**
	 * See {@link RefexDynamicUsageDescription} for the full details on what this builds.
	 * 
	 * Does all the work to create a new concept that is suitable for use as an Assemblage Concept for a new style Dynamic Refex.
	 * 
	 * The concept will be created under the concept {@link RefexDynamic#REFEX_DYNAMIC_TYPES} if a parent is not specified
	 * 
	 * //TODO [REFEX] figure out language details (how we know what language to put on the name/description
	 * @param parentConcept - option - if null, uses {@link RefexDynamic#REFEX_DYNAMIC_TYPES}
	 * @throws InvalidCAB 
	 * @throws PropertyVetoException 
	 */
	public static RefexDynamicUsageDescription createNewRefexDynamicUsageDescriptionConcept(String refexFSN, String refexPreferredTerm, 
			String refexDescription, RefexDynamicColumnInfo[] columns, UUID parentConcept, boolean annotationStyle) throws 
			IOException, ContradictionException, InvalidCAB, PropertyVetoException
	{
		LanguageCode lc = LanguageCode.EN_US;
		UUID isA = Snomed.IS_A.getUuids()[0];
		IdDirective idDir = IdDirective.GENERATE_HASH;
		UUID module = TermAux.TERM_AUX_MODULE.getUuids()[0];
		UUID parents[] = new UUID[] { parentConcept == null ? RefexDynamic.REFEX_DYNAMIC_IDENTITY.getUuids()[0] : parentConcept };

		ConceptCB cab = new ConceptCB(refexFSN, refexPreferredTerm, lc, isA, idDir, module, parents);
		cab.setAnnotationRefexExtensionIdentity(annotationStyle);
		
		DescriptionCAB dCab = new DescriptionCAB(cab.getComponentUuid(), Snomed.SYNONYM_DESCRIPTION_TYPE.getUuids()[0], lc, refexDescription, false,
				IdDirective.GENERATE_HASH);
		dCab.getProperties().put(ComponentProperty.MODULE_ID, module);
		
		//Mark it as acceptable
		RefexCAB rCabAcceptable = new RefexCAB(RefexType.CID, dCab.getComponentUuid(), 
				Snomed.US_LANGUAGE_REFEX.getUuids()[0], IdDirective.GENERATE_HASH, RefexDirective.EXCLUDE);
		rCabAcceptable.put(ComponentProperty.COMPONENT_EXTENSION_1_ID, SnomedMetadataRf2.ACCEPTABLE_RF2.getUuids()[0]);
		rCabAcceptable.getProperties().put(ComponentProperty.MODULE_ID, module);
		dCab.addAnnotationBlueprint(rCabAcceptable);
		
		RefexDynamicCAB descriptionMarker = new RefexDynamicCAB(dCab.getComponentUuid(), RefexDynamic.REFEX_DYNAMIC_DEFINITION_DESCRIPTION.getUuids()[0]);
		dCab.addAnnotationBlueprint(descriptionMarker);
	
		cab.addDescriptionCAB(dCab);
		
		if (columns != null)
		{
			//Ensure that we process in column order - we don't always keep track of that later - we depend on the data being stored in the right order.
			TreeSet<RefexDynamicColumnInfo> sortedColumns = new TreeSet<>(Arrays.asList(columns));
			
			for (RefexDynamicColumnInfo ci : sortedColumns)
			{
				RefexDynamicCAB rCab = new RefexDynamicCAB(cab.getComponentUuid(), RefexDynamic.REFEX_DYNAMIC_DEFINITION.getUuids()[0]);
				
				RefexDynamicDataBI[] data = new RefexDynamicDataBI[7];
				
				data[0] = new RefexInteger(ci.getColumnOrder());
				data[1] = new RefexUUID(ci.getColumnDescriptionConcept());
				if (RefexDynamicDataType.UNKNOWN == ci.getColumnDataType())
				{
					throw new InvalidCAB("Error in column - if default value is provided, the type cannot be polymorphic");
				}
				data[2] = new RefexString(ci.getColumnDataType().name());
				data[3] = convertPolymorphicDataColumn(ci.getDefaultColumnValue(), ci.getColumnDataType());
				data[4] = new RefexBoolean(ci.isColumnRequired());
				data[5] = (ci.getValidator() == null ? null : new RefexString(ci.getValidator().name()));
				data[6] = (ci.getValidatorData() == null ? null : convertPolymorphicDataColumn(ci.getValidatorData(), ci.getValidatorData().getRefexDataType()));
				rCab.setData(data);
				//TODO file a another bug, this API is atrocious.  If you put the annotation on the concept, it gets silently ignored.
				cab.getConceptAttributeAB().addAnnotationBlueprint(rCab);
			}
		}
		
		//Build this on the lowest level path, otherwise, othercode that references this will fail (as it doesn't know about custom paths)
		ConceptChronicleBI newCon = Ts.get().getTerminologyBuilder(
				new EditCoordinate(TermAux.USER.getLenient().getConceptNid(), 
						TermAux.TERM_AUX_MODULE.getLenient().getNid(), 
						TermAux.WB_AUX_PATH.getLenient().getConceptNid()), 
				StandardViewCoordinates.getWbAuxiliary()).construct(cab);
		Ts.get().addUncommitted(newCon);
		Ts.get().commit(newCon);
		
		return new RefexDynamicUsageDescription(newCon.getConceptNid());
	}
	
	private static RefexDynamicDataBI convertPolymorphicDataColumn(RefexDynamicDataBI defaultValue, RefexDynamicDataType columnType) 
			throws PropertyVetoException, InvalidCAB
	{
		RefexDynamicDataBI result;
		
		if (defaultValue != null)
		{
			try
			{
				if (RefexDynamicDataType.BOOLEAN == columnType)
				{
					result = (RefexDynamicBooleanBI)defaultValue;
				}
				else if (RefexDynamicDataType.BYTEARRAY == columnType)
				{
					result = (RefexDynamicByteArrayBI)defaultValue;
				}
				else if (RefexDynamicDataType.DOUBLE == columnType)
				{
					result = (RefexDynamicDoubleBI)defaultValue;
				}
				else if (RefexDynamicDataType.FLOAT == columnType)
				{
					result = (RefexDynamicFloatBI)defaultValue;
				}
				else if (RefexDynamicDataType.INTEGER == columnType)
				{
					result = (RefexDynamicIntegerBI)defaultValue;
				}
				else if (RefexDynamicDataType.LONG == columnType)
				{
					result = (RefexDynamicLongBI)defaultValue;
				}
				else if (RefexDynamicDataType.NID == columnType)
				{
					result = (RefexDynamicNidBI)defaultValue;
				}
				else if (RefexDynamicDataType.STRING == columnType)
				{
					result = (RefexDynamicStringBI)defaultValue;
				}
				else if (RefexDynamicDataType.UUID == columnType)
				{
					result = (RefexDynamicUUIDBI)defaultValue;
				}
				else if (RefexDynamicDataType.POLYMORPHIC == columnType)
				{
					throw new InvalidCAB("Error in column - if default value is provided, the type cannot be polymorphic");
				}
				else
				{
					throw new InvalidCAB("Actually, the implementation is broken.  Ooops.");
				}
			}
			catch (ClassCastException e)
			{
				throw new InvalidCAB("Error in column - if default value is provided, the type must be compatible with the the column descriptor type");
			}
		}
		else
		{
			result = null;
		}
		return result;
	}
}
