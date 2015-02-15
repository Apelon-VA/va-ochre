/*
 *  Copyright 2010 maestro.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.ihtsdo.otf.tcc.api.relationship;

/**
 * Enumeration that defines a relationship assertion type matching policy. 
 * @author maestro
 */
public enum RelAssertionType {
    /**
     * Match only stated relationships. 
     */
    STATED("stated"),
    
    /**
     * Match only inferred relationships.
     */
    INFERRED("inferred"),
    
    /**
     * Match inferred relationships if at least one exists on the concept;
     * Otherwise match stated relationships. 
     */
    INFERRED_THEN_STATED("inferred then stated");

    String displayName;

    private RelAssertionType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }


}
