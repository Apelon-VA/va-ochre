/*
 * Copyright 2015 U.S. Department of Veterans Affairs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.vha.isaac.ochre.observable.model.coordinate;

import gov.vha.isaac.ochre.api.chronicle.LatestVersion;
import gov.vha.isaac.ochre.api.component.sememe.SememeChronology;
import gov.vha.isaac.ochre.api.component.sememe.version.DescriptionSememe;
import gov.vha.isaac.ochre.api.coordinate.LanguageCoordinate;
import gov.vha.isaac.ochre.api.coordinate.StampCoordinate;
import gov.vha.isaac.ochre.api.observable.coordinate.ObservableLanguageCoordinate;
import gov.vha.isaac.ochre.model.coordinate.LanguageCoordinateImpl;
import gov.vha.isaac.ochre.observable.model.ObservableFields;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableIntegerArray;

/**
 *
 * @author kec
 */
@XmlRootElement(name = "observableLanguageCoordinate")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ObservableLanguageCoordinateImpl extends ObservableCoordinateImpl implements ObservableLanguageCoordinate {

    private LanguageCoordinateImpl languageCoordinate;

    @XmlTransient
    IntegerProperty languageConceptSequenceProperty = null;
    @XmlTransient
    ObjectProperty<ObservableIntegerArray> dialectAssemblagePreferenceListProperty = null;
    @XmlTransient
    ObjectProperty<ObservableIntegerArray> descriptionTypePreferenceListProperty = null;
    
    private ObservableLanguageCoordinateImpl() {
        //for jaxb
    }

    public ObservableLanguageCoordinateImpl(LanguageCoordinate languageCoordinate) {
        this.languageCoordinate = (LanguageCoordinateImpl) languageCoordinate;
    }

    @Override
    public IntegerProperty languageConceptSequenceProperty() {
        if (languageConceptSequenceProperty == null) {
            languageConceptSequenceProperty = new SimpleIntegerProperty(this,
                    ObservableFields.LANGUAGE_SEQUENCE_FOR_LANGUAGE_COORDINATE.toExternalString(),
                    getLanguageConceptSequence());
            addListenerReference(languageCoordinate.setLanguageConceptSequenceProperty(languageConceptSequenceProperty));
        }
        return languageConceptSequenceProperty;
    }

    @Override
    public ObjectProperty<ObservableIntegerArray> dialectAssemblagePreferenceListProperty() {
        if (dialectAssemblagePreferenceListProperty == null) {
            dialectAssemblagePreferenceListProperty = new SimpleObjectProperty<>(this,
                    ObservableFields.DIALECT_ASSEMBLAGE_SEQUENCE_PREFERENCE_LIST_FOR_LANGUAGE_COORDINATE.toExternalString(),
                    FXCollections.observableIntegerArray(getDialectAssemblagePreferenceList()));
            addListenerReference(languageCoordinate.setDialectAssemblagePreferenceListProperty(dialectAssemblagePreferenceListProperty));
        }
        return dialectAssemblagePreferenceListProperty;
    }

    @Override
    public ObjectProperty<ObservableIntegerArray> descriptionTypePreferenceListProperty() {
        if (descriptionTypePreferenceListProperty == null) {
            descriptionTypePreferenceListProperty = new SimpleObjectProperty<>(this,
                    ObservableFields.DESCRIPTION_TYPE_SEQUENCE_PREFERENCE_LIST_FOR_LANGUAGE_COORDINATE.toExternalString(),
                    FXCollections.observableIntegerArray(getDescriptionTypePreferenceList()));
            addListenerReference(languageCoordinate.setDescriptionTypePreferenceListProperty(descriptionTypePreferenceListProperty));
        }
        return descriptionTypePreferenceListProperty;
    }

    @Override
    public int getLanguageConceptSequence() {
        if (languageConceptSequenceProperty != null) {
            return languageConceptSequenceProperty.get();
        }
        return languageCoordinate.getLanguageConceptSequence();
    }

    @Override
    public int[] getDialectAssemblagePreferenceList() {
        if (dialectAssemblagePreferenceListProperty != null) {
            return dialectAssemblagePreferenceListProperty.get().toArray(new int[2]);
        }
        return languageCoordinate.getDialectAssemblagePreferenceList();
    }

    @Override
    public int[] getDescriptionTypePreferenceList() {
        if (descriptionTypePreferenceListProperty != null) {
            return descriptionTypePreferenceListProperty.get().toArray(new int[2]);
        }
        return languageCoordinate.getDescriptionTypePreferenceList();
    }

    @Override
    public Optional<LatestVersion<DescriptionSememe<?>>> getFullySpecifiedDescription(
            List<SememeChronology<? extends DescriptionSememe<?>>> descriptionList, StampCoordinate stampCoordinate)
    {
        return languageCoordinate.getFullySpecifiedDescription(descriptionList, stampCoordinate);
    }

    @Override
    public Optional<LatestVersion<DescriptionSememe<?>>> getPreferredDescription(
            List<SememeChronology<? extends DescriptionSememe<?>>> descriptionList, StampCoordinate stampCoordinate)
    {
        return languageCoordinate.getPreferredDescription(descriptionList, stampCoordinate);
    }

    @Override
    public Optional<LatestVersion<DescriptionSememe<?>>> getDescription(
            List<SememeChronology<? extends DescriptionSememe<?>>> descriptionList, StampCoordinate stampCoordinate)
    {
        return languageCoordinate.getDescription(descriptionList, stampCoordinate);
    }
}
