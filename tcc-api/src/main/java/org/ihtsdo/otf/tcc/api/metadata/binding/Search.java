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

/**
 * Search
 * 
 * @author <a href="mailto:joel.kniaz@gmail.com">Joel Kniaz</a>
 */
package org.ihtsdo.otf.tcc.api.metadata.binding;

import java.util.UUID;

import org.ihtsdo.otf.tcc.api.spec.ConceptSpec;

/**
 * Search
 * 
 * @author <a href="mailto:joel.kniaz@gmail.com">Joel Kniaz</a>
 *
 */
public class Search {

	//This concept doesn't need to be in the taxonomy, it is just used as salt for generating other UUIDs
	public static ConceptSpec SEARCH_NAMESPACE = new ConceptSpec("Search Namespace", 
			UUID.fromString("3c92adee-13dc-5c6a-bfe1-acf0d31d05d7"));

	//an organizational concept for all of the new concepts being added to the Refset Auxiliary Concept tree
	public static ConceptSpec SEARCH_TYPES = new ConceptSpec("search refex types", 
			UUID.fromString("d2db2e2a-2d4d-5705-b164-65ee5c1ece58"), 
			RefexDynamic.REFEX_DYNAMIC_TYPES);
	
	public static ConceptSpec SEARCH_PERSISTABLE = new ConceptSpec("Persistable Searches", 
			UUID.fromString("80d39126-7814-5812-b01f-d6cda1d86496"), 
			SEARCH_TYPES);
	
	//TODO [JOEL] - Please see the new ConceptSpecWithDescriptions class that I added - which will allow you to put the definition here as well for 
	//your columns.  See RefexDynamic for usage...
	
	public static ConceptSpec SEARCH_GLOBAL_ATTRIBUTES = new ConceptSpec("Search Global Attributes", 
			UUID.fromString("27316605-16ea-536e-9acd-40f0277e20ad"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_GLOBAL_ATTRIBUTES_COLUMNS = new ConceptSpec("Search Global Attributes Columns", 
			UUID.fromString("6774d7b9-42af-5fbc-9e1d-565fc2f3ed57"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_GLOBAL_ATTRIBUTES_VIEW_COORDINATE_COLUMN = new ConceptSpec("vc", 
			UUID.fromString("5010f18f-c469-5315-8c5e-f7d9b65373c5"),
			SEARCH_GLOBAL_ATTRIBUTES_COLUMNS);
	public static ConceptSpec SEARCH_GLOBAL_ATTRIBUTES_MAX_RESULTS_COLUMN = new ConceptSpec("max", 
			UUID.fromString("63981b45-bbbe-5247-b571-d7fee02aad79"),
			SEARCH_GLOBAL_ATTRIBUTES_COLUMNS);
	public static ConceptSpec SEARCH_GLOBAL_ATTRIBUTES_DROOLS_EXPR_COLUMN = new ConceptSpec("drools", 
			UUID.fromString("c0091cf4-f063-5964-85c5-0fdf14b5bb00"),
			SEARCH_GLOBAL_ATTRIBUTES_COLUMNS);
	
	public static ConceptSpec SEARCH_FILTER_ATTRIBUTES = new ConceptSpec("Search Filter Attributes", 
			UUID.fromString("b3ac9404-883b-5ba4-b65f-b629970ecc17"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_FILTER_ATTRIBUTES_FILTER_ORDER_COLUMN = new ConceptSpec("order", 
			UUID.fromString("795bade0-9ffb-54ef-8385-8570b4f708cf"),
			SEARCH_TYPES);
	
	public static ConceptSpec SEARCH_LUCENE_FILTER = new ConceptSpec("Search Lucene Filter", 
			UUID.fromString("4ece37d7-1ae0-5c5e-b475-f8e3bdce4d86"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_LUCENE_FILTER_COLUMNS = new ConceptSpec("Search Lucene Filter Columns", 
			UUID.fromString("6774d7b9-42af-5fbc-9e1d-565fc2f3ed57"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_LUCENE_FILTER_PARAMETER_COLUMN = new ConceptSpec("param",
			UUID.fromString("e28f2c45-1c0b-569a-a329-304ea04ade17"),
			SEARCH_LUCENE_FILTER_COLUMNS);
	
	public static ConceptSpec SEARCH_REGEXP_FILTER = new ConceptSpec("Search RegExp Filter", 
			UUID.fromString("39c21ff8-cd48-5ac8-8110-40b7d8b30e61"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_REGEXP_FILTER_COLUMNS = new ConceptSpec("Search RegExp Filter Columns", 
			UUID.fromString("982c6007-42f6-5034-92c1-6d298b00ad7e"),
			SEARCH_TYPES);
	public static ConceptSpec SEARCH_REGEXP_FILTER_PARAMETER_COLUMN = new ConceptSpec("param",
			UUID.fromString("e8e707f1-bea2-534b-bbb4-78212fb22dc9"),
			SEARCH_REGEXP_FILTER_COLUMNS);
	/**
	 * 
	 */
	private Search() {
	}
}
