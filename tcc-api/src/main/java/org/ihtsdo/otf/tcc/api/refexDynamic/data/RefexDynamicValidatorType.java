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
package org.ihtsdo.otf.tcc.api.refexDynamic.data;

import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicDoubleBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicFloatBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicIntegerBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicLongBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicNidBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicStringBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.data.dataTypes.RefexDynamicUUIDBI;


/**
 * {@link RefexDynamicValidatorType}
 * See {@link RefexDynamicValidatorTypeImpl} for implementations of these tests in the tcc-model-impl project.
 * 
 * The acceptable validatorData object type(s) for the following fields:
 * {@link RefexDynamicValidatorType#LESS_THAN}
 * {@link RefexDynamicValidatorType#GREATER_THAN}
 * {@link RefexDynamicValidatorType#LESS_THAN_OR_EQUAL}
 * {@link RefexDynamicValidatorType#GREATER_THAN_OR_EQUAL}
 * 
 * are one of ( {@link RefexDynamicIntegerBI}, {@link RefexDynamicLongBI}, {@link RefexDynamicFloatBI}, {@link RefexDynamicDoubleBI})
 * 
 * {@link RefexDynamicValidatorType#INTERVAL} - Should be a {@link RefexDynamicStringBI} with valid interval notation - such as "[4,6)"
 * 
 * And for the following two:
 * {@link RefexDynamicValidatorType#IS_CHILD_OF}
 * {@link RefexDynamicValidatorType#IS_KIND_OF}
 * The validatorData should be either an {@link RefexDynamicNidBI} or a {@link RefexDynamicUUIDBI}.
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public enum RefexDynamicValidatorType
{
	LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL,  //Standard math stuff 
	INTERVAL, //math interval notation - such as [5,10)
	DROOLS, //TBD 
	IS_CHILD_OF, //OTF is child of - which only includes immediate (not recursive) children on the 'Is A' relationship. 
	IS_KIND_OF //OTF kind of - which is child of - but recursive, and self (heart disease is a kind-of heart disease);
}
