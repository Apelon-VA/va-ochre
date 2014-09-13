/*
 * Copyright 2012 International Health Terminology Standards Development Organisation.
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
package org.ihtsdo.otf.tcc.api.metadata.binding;

import java.util.UUID;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicColumnInfo;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicDataType;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.RefexDynamicUsageDescription;
import org.ihtsdo.otf.tcc.api.spec.ConceptSpec;
import org.ihtsdo.otf.tcc.api.spec.ConceptSpecWithDescriptions;
import org.ihtsdo.otf.tcc.api.spec.DynamicRefexConceptSpec;

/**
 * 
 * {@link RefexDynamic}
 * 
 * If you want to understand how these various concepts work together, see  the class description for
 * {@link RefexDynamicUsageDescription}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class RefexDynamic
{
	/*
	 * !!!NOTE !!!
	 * Need to be careful about the order of definition of these elemnents - especially where they reference each other.
	 * Otherwise, you get null pointer exceptions in the class loader at runtime.
	 * 
	 * From top -down - make sure everything is defined before it is used elsewhere in this class.
	 */
	
	public static ConceptSpec UNKNOWN_CONCEPT = new ConceptSpec("unknown (null) concept", 
			UUID.fromString("00000000-0000-0000-C000-000000000046"));
	
	//Above this point - these concepts already exist - and come from SCT, or elsewhere in the source code.
	//Below this point, all of these concepts are new (unless otherwise specified), and are being created to support the RefexDynamic code.
	
	//This concept doesn't need to be in the taxonomy, it is just used as salt for generating other UUIDs
	public static ConceptSpec REFEX_DYNAMIC_NAMESPACE = new ConceptSpec("Refex Dynamic Namespace", 
			UUID.fromString("9c76af37-671c-59a3-93bf-dfe0c5c58bfa"));
	
	//an organizational concept for all of the new concepts being added to the Refset Auxiliary Concept tree
	public static ConceptSpec REFEX_DYNAMIC_TYPES = new ConceptSpec("dynamic refex types", 
			UUID.fromString("647b6283-7c5f-53ff-a5f7-a40c865b1ef0"), 
			Taxonomies.REFSET_AUX);
	
	//An organizational concept which serves as a parent concept for any column types that are defined
	//within the system.
	public static ConceptSpec REFEX_DYNAMIC_COLUMNS = new ConceptSpec("dynamic refex columns", 
			UUID.fromString("a6767545-14d4-50f7-9522-2ddc37c2f676"),
			REFEX_DYNAMIC_TYPES);

	//The seven column types we need for describing column types
	public static ConceptSpecWithDescriptions REFEX_COLUMN_ORDER = new ConceptSpecWithDescriptions("column order", 
			UUID.fromString("8c501747-846a-5cea-8fd6-c9dd3dfc674f"),
			new String[] {"column order"},
			new String[] {"Stores the column order of this column within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	public static ConceptSpecWithDescriptions REFEX_COLUMN_NAME = new ConceptSpecWithDescriptions("column name", 
			UUID.fromString("89c0ded2-fd69-5654-a386-ded850d258a1"),
			new String[] {"column name"},
			new String[] {"Stores the concept reference to the concept that defines the name of this column within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	public static ConceptSpecWithDescriptions REFEX_COLUMN_TYPE = new ConceptSpecWithDescriptions("column type", 
			UUID.fromString("dbfd9bd2-b84f-574a-ab9e-64ba3bb94793"),
			new String[] {"column type"},
			new String[] {"Stores the data type of this column within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	public static ConceptSpecWithDescriptions REFEX_COLUMN_DEFAULT_VALUE = new ConceptSpecWithDescriptions("column default value", 
			UUID.fromString("4d3e79aa-ab74-5858-beb3-15e0888986cb"),
			new String[] {"column default value"},
			new String[] {"Stores the (optional) default value of this column within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	public static ConceptSpecWithDescriptions REFEX_COLUMN_REQUIRED = new ConceptSpecWithDescriptions("column required", 
			UUID.fromString("8a89ef19-bd5a-5e25-aa57-1172fbb437b6"),
			new String[] {"column required"},
			new String[] {"Stores the (optional) flag to specify that this column is manditory within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	public static ConceptSpecWithDescriptions REFEX_COLUMN_VALIDATOR = new ConceptSpecWithDescriptions("column validator", 
			UUID.fromString("f295c3ba-d416-563d-8427-8b5d3e324192"),
			new String[] {"column validator"},
			new String[] {"Stores the (optional) validator type which will be applied to user supplied data of this column within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	public static ConceptSpecWithDescriptions REFEX_COLUMN_VALIDATOR_DATA = new ConceptSpecWithDescriptions("column validator data", 
			UUID.fromString("50ea8378-8355-5a5d-bae2-ce7c10e92636"),
			new String[] {"column validator data"},
			new String[] {"Stores the (optional) validator data which will be used by the validator to check the user input of this column within a Dynamic Refex Definition"},
			REFEX_DYNAMIC_COLUMNS);
	
	//used for index config
	public static ConceptSpecWithDescriptions REFEX_COLUMN_COLUMNS_TO_INDEX = new ConceptSpecWithDescriptions("columns to index", 
			UUID.fromString("cede7677-3759-5dce-b28b-20a40fddf5d6"),
			new String[] {"columns to index"},
			new String[] {"Contains a String which has a comma seperated list of the column positions within the referenced refex assemblage which should have their values indexed."},
			REFEX_DYNAMIC_COLUMNS);
	
	//This is the extended description type that must be attached to a description within a concept to make the concept valid for use as an 
	//assemblage concept for RefexDynamic refexes.  The description annotated with this type describes the intent of 
	//using the concept containing the description as an assemblage concept.
	public static DynamicRefexConceptSpec REFEX_DYNAMIC_DEFINITION_DESCRIPTION = new DynamicRefexConceptSpec("dynamic refex definition description", 
			UUID.fromString("21d300f2-b2d8-5586-916b-0e7ac88d5bea"),
			new String[] {"dynamic refex definition description"},
			new String[0],
			true, 
			"This is the extended description type that must be attached to a description within a concept to make the concept valid for use as an "
				+ "assemblage concept for RefexDynamic refexes.  The description annotated with this type describes the intent of "
				+ "using the concept containing the description as an assemblage concept.", 
			new RefexDynamicColumnInfo[0],
			REFEX_DYNAMIC_TYPES);
	
	//This is the assemblage type that is usually present on a concept when it is used as an assemblage itself to describe the attached data - the attached
	//refex using this for an assemblage will describe a data column that is to be attached with the refex.  This assemblage type wouldn't be used if there was 
	//no data to attach.
	public static DynamicRefexConceptSpec REFEX_DYNAMIC_DEFINITION = new DynamicRefexConceptSpec("dynamic refex definition", 
			UUID.fromString("a40fb48c-d755-5eaa-a725-4c4ebc9b9e6e"),
			new String[] {"dynamic refex definition"},
			new String[0],
			true, 
			"This concept is used as an assemblage for defining new Refex extensions.  "
				+ "The attached data columns describe what columns are required to define a new Refex.",
			new RefexDynamicColumnInfo[] {
				new RefexDynamicColumnInfo(0, RefexDynamic.REFEX_COLUMN_ORDER.getUuids()[0], RefexDynamicDataType.INTEGER, null, true, null, null),
				new RefexDynamicColumnInfo(1, RefexDynamic.REFEX_COLUMN_NAME.getUuids()[0], RefexDynamicDataType.UUID, null, true, null, null),
				new RefexDynamicColumnInfo(2, RefexDynamic.REFEX_COLUMN_TYPE.getUuids()[0], RefexDynamicDataType.STRING, null, true, null, null),
				new RefexDynamicColumnInfo(3, RefexDynamic.REFEX_COLUMN_DEFAULT_VALUE.getUuids()[0], RefexDynamicDataType.POLYMORPHIC, null, false, null, null),
				new RefexDynamicColumnInfo(4, RefexDynamic.REFEX_COLUMN_REQUIRED.getUuids()[0], RefexDynamicDataType.BOOLEAN, null, false, null, null),
				new RefexDynamicColumnInfo(5, RefexDynamic.REFEX_COLUMN_VALIDATOR.getUuids()[0], RefexDynamicDataType.STRING, null, false, null, null),
				new RefexDynamicColumnInfo(6, RefexDynamic.REFEX_COLUMN_VALIDATOR_DATA.getUuids()[0], RefexDynamicDataType.POLYMORPHIC, null, false, null, null)},
			REFEX_DYNAMIC_TYPES);
	
	//This is the assemblage type that is used to record the current configuration of the Indexer for Dynamic Refexes.
	//this is ALSO the concept that stores (as a member list) dynamic refex instances (of assemblage type itself) which define which other 
	//dynamic refexes should be indexed within the system. 
	public static DynamicRefexConceptSpec REFEX_DYNAMIC_INDEX_CONFIGURATION = new DynamicRefexConceptSpec("dynamic refex index configuration", 
			UUID.fromString("980e092e-6df4-593f-a756-3d31b4f21a6c"),
			new String[] {"dynamic refex index configuration"},
			new String[0],
			false, 
			"A Dynamic Refex which contains the indexer configuration for Dynamic Refexes within ISAAC.  "
				+ "The referenced component ID will be the assemblage being configured for indexing.", 
			new RefexDynamicColumnInfo[] {
				new RefexDynamicColumnInfo(0, RefexDynamic.REFEX_COLUMN_COLUMNS_TO_INDEX.getUuids()[0], RefexDynamicDataType.STRING, null, false, null, null)},
			REFEX_DYNAMIC_TYPES);
	
	//An organizational concept which serves as a parent concept for the allowed types of data columns
	public static ConceptSpec REFEX_DATA_TYPES = new ConceptSpec("refex dynamic column data types", 
			UUID.fromString("0cb295ea-71c9-5137-8662-66373eecd0dc"),
			REFEX_DYNAMIC_TYPES);
	
	//An organizational concept which serves as a parent concept for dynamic refexes defined in the system
	//(unless they choose to put them some where else, this isn't required, is only for convenience)
	public static ConceptSpec REFEX_DYNAMIC_IDENTITY = new ConceptSpec("Dynamic Refexes", 
			UUID.fromString("c946a9ad-4bb4-5041-8be3-c3977812ae8e"),
			TermAux.REFSET_IDENTITY);

	//New data types
	
	public static ConceptSpec REFEX_DT_NID = new ConceptSpec("nid", 
			UUID.fromString("d1a17272-9785-51aa-8bde-cc556ab32ebb"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_BOOLEAN = new ConceptSpec("boolean", 
			UUID.fromString("08f2fb74-980d-5157-b92c-4ff1eac6a506"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_LONG = new ConceptSpec("long", 
			UUID.fromString("dea8cdf1-de75-5991-9791-79714e4a964d"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_BYTE_ARRAY = new ConceptSpec("byte array", 
			UUID.fromString("9a84fecf-708d-5de4-9c5f-e17973229e0f"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_FLOAT = new ConceptSpec("float", 
			UUID.fromString("fb591801-7b37-525d-980d-98a1c63ceee0"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_DOUBLE = new ConceptSpec("double", 
			UUID.fromString("7172e6ac-a05a-5a34-8275-aef430b18207"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_POLYMORPHIC = new ConceptSpec("polymorphic", 
			UUID.fromString("3d634fd6-1498-5e8b-b914-e75b42018397"),
			REFEX_DATA_TYPES);
	
	//The following data types already exist, but I'm also adding them to our hierarchy for clarity
	//(assuming you generate concepts from this constants file and load them into the DB, anyway)
	public static ConceptSpec REFEX_DT_STRING = new ConceptSpec("String (foundation metadata concept)", 
			UUID.fromString("a46aaf11-b37a-32d6-abdc-707f084ec8f5"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_INTEGER = new ConceptSpec("Signed integer (foundation metadata concept)", 
			UUID.fromString("1d1c2073-d98b-3dd3-8aad-a19c65aa5a0c"),
			REFEX_DATA_TYPES);
	public static ConceptSpec REFEX_DT_UUID = new ConceptSpec("Universally Unique Identifier (foundation metadata concept)", 
			UUID.fromString("845274b5-9644-3799-94c6-e0ea37e7d1a4"),
			REFEX_DATA_TYPES);
}
