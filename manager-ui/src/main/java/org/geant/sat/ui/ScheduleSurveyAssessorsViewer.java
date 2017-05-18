/*
 * GÉANT BSD Software License
 *
 * Copyright (c) 2017 - 2020, GÉANT
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the GÉANT nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * Disclaimer:
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.geant.sat.ui;

import java.util.HashSet;
import java.util.List;

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.geant.sat.ui.utils.AssessorDetailsHelper;
import org.geant.sat.ui.utils.EntityDetailsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.declarative.Design;

/** class implementing view to pick entities for survey scheduling. */
@SuppressWarnings({ "serial", "rawtypes" })
@DesignRoot
public class ScheduleSurveyAssessorsViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSurveyAssessorsViewer.class);
    /** ui component to select entities. */
    private ListSelect<EntityDetails> selectedEntity;
    /** the selected entities record. */
    private List<EntityDetails> details;
    /** ui component for selecting assessors for entity. */
    private TwinColSelect<AssessorDetails> selectAssessors;
    /** all available assessors. */
    private List<AssessorDetails> assessorDetails;

    /**
     * constructor.
     * 
     * @param ui
     *            main ui.
     * @param selectedDetails
     *            entity configuration selected by the user.
     */
    ScheduleSurveyAssessorsViewer(MainUI ui, List<EntityDetails> selectedDetails) {
        super(ui);
        Design.read(this);
        LOG.debug("Selecting assessors for entities for scheduling");
        details = selectedDetails;
        ListAssessorsResponse resp = getMainUI().getSatApiClient().getAssessors();
        if (!verifySuccess(resp)) {
            // TODO what should we do?
        }
        assessorDetails = resp.getAssessors();
        initializeEntitySelector();
        initializeAssessorSelector();
    }

    /**
     * Initializes entity selector component.
     * 
     */
    @SuppressWarnings("unchecked")
    private void initializeEntitySelector() {
        if (details.size() < 2) {
            selectedEntity.setVisible(false);
            return;
        }
        selectedEntity.setCaption(getString("lang.scheduler.entities.select.caption"));
        selectedEntity.setItems(details);
        selectedEntity.setItemCaptionGenerator(new EntityDetailsHelper());
        selectedEntity.addValueChangeListener(new EntityChanged());
        selectedEntity.select(details.get(0));
    }

    /**
     * Initializes the assessor selector component.
     */
    @SuppressWarnings("unchecked")
    private void initializeAssessorSelector() {
        if (details.size() == 0) {
            selectAssessors.setVisible(false);
            return;
        }
        selectAssessors.setCaption(getString("lang.scheduler.assessors.picker.caption"));
        selectAssessors.setItems(assessorDetails);
        selectAssessors.setItemCaptionGenerator(new AssessorDetailsHelper());
        selectAssessors.addValueChangeListener(new AssessorChanged());
        EntityDetails selected = details.get(0);
        selectAssessors.updateSelection(AssessorDetailsHelper.selectionToSet(assessorDetails, selected.getAssessors()),
                new HashSet<AssessorDetails>());
    }

    /** class implementing entity selection actions. */
    private class EntityChanged implements ValueChangeListener {

        @Override
        public void valueChange(ValueChangeEvent event) {
            if (!event.isUserOriginated()) {
                return;
            }
            selectAssessors.deselectAll();
            selectAssessors.updateSelection(
                    AssessorDetailsHelper.selectionToSet(assessorDetails, selectedEntity.getSelectedItems().iterator()
                            .next().getAssessors()), new HashSet<AssessorDetails>());
        }
    }

    /** class implementing assessor selection actions. */
    private class AssessorChanged implements ValueChangeListener {

        @Override
        public void valueChange(ValueChangeEvent event) {
            if (!event.isUserOriginated()) {
                return;
            }
            EntityDetails selected = details.get(0);
            if (details.size() > 1) {
                selected = selectedEntity.getSelectedItems().iterator().next();
            }
            selected.getAssessors().clear();
            selected.getAssessors().addAll(selectAssessors.getSelectedItems());
        }
    }
}
