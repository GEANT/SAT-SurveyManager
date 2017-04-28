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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListAssessorsResponse;
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

    private ListSelect<String> selectedEntity;
    private TwinColSelect<String> selectAssessors;
    private List<EntityDetails> details;

    ScheduleSurveyAssessorsViewer(MainUI ui, List<EntityDetails> selectedDetails) {
        super(ui);
        Design.read(this);
        LOG.debug("Selecting assessors for entities for scheduling");
        details = selectedDetails;
        selectedEntity.setCaption(getString("lang.scheduler.entities.picker.caption"));
        // init entity selection
        initializeEntitySelector();
        initializeAssessorSelector();
    }

    @SuppressWarnings("unchecked")
    private void initializeEntitySelector() {
        if (details.size() < 2) {
            selectedEntity.setVisible(false);
            return;
        }
        Set<String> entities = new HashSet<String>();
        String selected = null;
        for (EntityDetails entityDetails : details) {
            entities.add(entityDetails.getId() + ":" + entityDetails.getName());
            if (selected == null) {
                selected = entityDetails.getId() + ":" + entityDetails.getName();
            }
        }
        if (entities.size() > 0) {
            selectedEntity.setItems(entities);
        }
        selectedEntity.addValueChangeListener(new EntityChanged());
        selectedEntity.select(selected);
    }

    @SuppressWarnings("unchecked")
    private void initializeAssessorSelector() {
        if (details.size() == 0) {
            selectAssessors.setVisible(false);
            return;
        }

        ListAssessorsResponse resp = getMainUI().getSatApiClient().getAssessors();
        if (!verifySuccess(resp)) {
            // what should we do?
        }
        List<AssessorDetails> assessorDetails = resp.getAssessors();
        List<String> selectableAssessorDetails = new ArrayList<String>();
        for (AssessorDetails assessorDetail : assessorDetails) {
            selectableAssessorDetails.add(assessorDetail.getId() + ":" + assessorDetail.getValue());
        }
        selectAssessors.setItems(selectableAssessorDetails);
        selectAssessors.addValueChangeListener(new AssessorChanged());
        EntityDetails selected = details.get(0);
        Set<String> selectedAssessorDetails = new HashSet<String>();
        for (AssessorDetails assessorDetail : selected.getAssessors()) {
            selectedAssessorDetails.add(assessorDetail.getId() + ":" + assessorDetail.getValue());
        }
        LOG.debug("Initial selection " + selectedAssessorDetails);
        selectAssessors.updateSelection(selectedAssessorDetails, new HashSet<String>());
    }

    private String resolveSelectedId() {
        if (details.size() == 0) {
            return "";
        }
        if (details.size() == 1) {
            return details.get(0).getId();
        }
        return selectedEntity.getSelectedItems().iterator().next().split(":")[0];
    }

    private List<String> resolveSelectedAssessors() {
        List<String> ret = new ArrayList<String>();
        for (String selected : selectAssessors.getSelectedItems()) {
            ret.add(selected.split(":")[0]);
        }
        return ret;
    }

    private class EntityChanged implements ValueChangeListener {

        @Override
        public void valueChange(ValueChangeEvent event) {
            if (!event.isUserOriginated()) {
                return;
            }
            String id = resolveSelectedId();
            for (EntityDetails selected : details) {
                if (selected.getId().equals(id)) {
                    selectAssessors.deselectAll();
                    Set<String> selectedAssessorDetails = new HashSet<String>();
                    for (AssessorDetails assessorDetail : selected.getAssessors()) {
                        selectedAssessorDetails.add(assessorDetail.getId() + ":" + assessorDetail.getValue());
                    }
                    LOG.debug("Setting assessors " + selectedAssessorDetails + " to ui control for " + id);
                    selectAssessors.updateSelection(selectedAssessorDetails, new HashSet<String>());
                }
            }

        }
    }

    private class AssessorChanged implements ValueChangeListener {

        @Override
        public void valueChange(ValueChangeEvent event) {
            if (!event.isUserOriginated()) {
                return;
            }
            String id = resolveSelectedId();
            ListAssessorsResponse resp = getMainUI().getSatApiClient().getAssessors();
            if (!verifySuccess(resp)) {
                // what should we do?
            }
            for (EntityDetails selected : details) {
                if (selected.getId().equals(id)) {
                    selected.getAssessors().clear();
                    List<String> ids = resolveSelectedAssessors();
                    for (AssessorDetails assessorDetails : resp.getAssessors()) {
                        if (ids.contains(assessorDetails.getId())) {
                            LOG.debug("Setting assessor " + assessorDetails.getValue() + " to record for " + id);
                            selected.getAssessors().add(assessorDetails);
                        }
                    }
                }
            }

        }

    }

}
