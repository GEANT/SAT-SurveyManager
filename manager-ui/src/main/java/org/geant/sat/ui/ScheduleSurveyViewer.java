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
import org.geant.sat.api.dto.ListAllSurveysResponse;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.ui.utils.AssessorDetailsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.Grid;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.Window;

//TODO: Much of the code is shared by ScheduleSurveyViewer & EntityListViewer, REFACTOR

/** class implementing view to survey questions. */
@SuppressWarnings("serial")
@DesignRoot
public class ScheduleSurveyViewer extends AbstractSurveyVerticalLayout implements ClickListener {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSurveyViewer.class);
    private Button cancel;
    private Button send;

    /** Column name for sids column. */
    private static final String COLUMN_SIDS = "sids";
    /** Column name for assessors column. */
    private static final String COLUMN_ASSESSORS = "assessors";

    private List<EntityDetails> selectedDetails = new ArrayList<EntityDetails>();
    /** Table showing entities to be scheduled */
    private Grid<EntityDetails> entities;

    /**
     * Constructor for entity based initialization. Preselects one entity, it's
     * surveys and assessors.
     * 
     * @param ui
     *            main ui class.
     * @param details
     *            question details.
     */
    ScheduleSurveyViewer(MainUI ui, Set<EntityDetails> details) {
        super(ui);
        Design.read(this);
        selectedDetails.addAll(details);
        entities.setItems(details);
        entities.addColumn(EntityDetails::getName).setCaption(getString("lang.entities.column.name"));
        entities.addColumn(EntityDetails::getDescription).setCaption(getString("lang.entities.column.description"))
                .setHidable(true).setHidden(true);
        entities.addColumn(EntityDetails::getCreator).setCaption(getString("lang.entities.column.creator"))
                .setHidable(true).setHidden(true);
        entities.addColumn(entitydetail -> getSurveys(entitydetail)).setCaption(getString("lang.entities.column.sid"))
                .setId(COLUMN_SIDS).setHidable(true).setHidden(false).setStyleGenerator(entitydetail -> "active");
        entities.addColumn(entitydetail -> getAssessors(entitydetail))
                .setCaption(getString("lang.entities.column.assesors")).setHidable(true).setHidden(false)
                .setId(COLUMN_ASSESSORS).setStyleGenerator(entitydetail -> "active");
        entities.addComponentColumn(entitydetail -> getValidCB(entitydetail)).setCaption(
                getString("lang.users.column.roles.admin"));
        entities.addItemClickListener(event -> handleEvent(event));
        entities.setHeightByRows(details.size() > 0 ? details.size() : 1);
        send.setEnabled(sentActive());
        cancel.setCaption(getString("lang.scheduler.button.cancel"));
        cancel.addClickListener(this);
        send.setCaption(getString("lang.scheduler.button.send"));
        send.addClickListener(this);

    }

    /**
     * Generates checkbox showing validity of item to be sent.
     * 
     * @param details
     *            of the user
     * @return checkbox showing validity of item to be sent
     */
    private Component getValidCB(EntityDetails details) {
        CheckBox cb = new CheckBox();
        cb.setValue(details.getSids().size() > 0 && details.getAssessors().size() > 0);
        cb.setEnabled(false);
        return cb;
    }

    /**
     * Handles click events.
     * 
     * @param event
     *            representing the click.
     */
    private void handleEvent(ItemClick<EntityDetails> event) {
        EntityDetails details = event.getItem();
        if (event.getColumn().getId() == null) {
            // not a editable column
            return;
        }
        switch (event.getColumn().getId()) {
        case COLUMN_SIDS:
            editSurveys(details);
            break;
        case COLUMN_ASSESSORS:
            editAssessors(details);
            break;
        default:
            break;
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == send) {
            sendSurveys(event);
            return;
        }
        if (event.getButton() == cancel) {
            ((Window) getParent()).close();
            return;
        }

    }

    public void sendSurveys(ClickEvent event) {
        LOG.debug("Sending surveys");
        final String principalId = getMainUI().getUser().getDetails().getPrincipalId();
        if (!verifySuccess(getMainUI().getSatApiClient().instantiateSurveys(selectedDetails, principalId))) {
            Notification.show(getString("lang.window.surveyschedule.failed"), Notification.Type.WARNING_MESSAGE);
            return;
        }
        Notification.show(getString("lang.window.surveyschedule.sent"), Notification.Type.HUMANIZED_MESSAGE);
        ((Window) getParent()).close();

    }

    /**
     * Creates a subwindow for editing survey details of entity.
     * 
     * @param details
     *            entity
     */
    private void editSurveys(EntityDetails details) {
        Window subWindowNewEntity = new Window(getString("lang.window.surveyschedule.editsids.title"));
        subWindowNewEntity.setModal(true);
        VerticalLayout subContent = new VerticalLayout();
        TwinColSelect<String> selectSids = new TwinColSelect<>(getString("lang.window.newentity.editsids.sids"));
        selectSids.setData(details);
        ListAllSurveysResponse resp = getMainUI().getSatApiClient().getSurveys();
        if (!verifySuccess(resp)) {
            return;
        }
        List<SurveyDetails> surveyDetails = resp.getSurveys();
        // parse active sids
        List<String> activeSurveyDetails = new ArrayList<String>();
        for (SurveyDetails surveyDetail : surveyDetails) {
            if (surveyDetail.getActive()) {
                activeSurveyDetails.add(surveyDetail.getSid());
            }
        }
        selectSids.setItems(activeSurveyDetails);
        // set current sids as selection
        selectSids.updateSelection(details.getSids(), new HashSet<String>());
        subContent.addComponent(selectSids, 0);
        Button editButton = new Button(getString("lang.window.newentity.buttonModify"));
        subContent.addComponent(editButton, 1);
        editButton.addClickListener(this::editedSurveys);
        Button cancelButton = new Button(getString("lang.window.newentity.buttonCancel"));
        subContent.addComponent(cancelButton, 2);
        cancelButton.addClickListener(this::canceledEditSurveys);
        subWindowNewEntity.setContent(subContent);
        getMainUI().addWindow(subWindowNewEntity);
    }

    /**
     * Edits the list of surveys of a entity.
     * 
     * @param event
     *            button click event.
     */
    private void editedSurveys(ClickEvent event) {
        @SuppressWarnings("unchecked")
        TwinColSelect<String> select = (TwinColSelect<String>) ((VerticalLayout) event.getButton().getParent())
                .getComponent(0);
        EntityDetails details = (EntityDetails) select.getData();
        LOG.debug("Original surveys " + details.getSids());
        details.getSids().clear();
        details.getSids().addAll(select.getSelectedItems());
        LOG.debug("New set of surveys " + details.getSids());
        send.setEnabled(sentActive());
        entities.setItems(selectedDetails);
        ((Window) event.getButton().getParent().getParent()).close();
    }

    /**
     * Closes edit surveys window.
     * 
     * @param event
     *            button click event.
     */
    private void canceledEditSurveys(ClickEvent event) {
        ((Window) event.getButton().getParent().getParent()).close();
    }

    /**
     * Creates a subwindow for editing assessor details of entity.
     * 
     * @param details
     *            entity
     */
    private void editAssessors(EntityDetails details) {
        Window subWindowNewEntity = new Window(getString("lang.window.surveyschedule.editassessors.title"));
        subWindowNewEntity.setWidth("80%");
        subWindowNewEntity.setModal(true);
        VerticalLayout subContent = new VerticalLayout();
        subContent.setWidth("100%");
        TwinColSelect<AssessorDetails> selectAssessors = new TwinColSelect<>(
                getString("lang.window.newentity.editassessors.assessors"));
        selectAssessors.setItemCaptionGenerator(new AssessorDetailsHelper());
        selectAssessors.setWidth("100%");
        selectAssessors.setData(details);
        ListAssessorsResponse resp = getMainUI().getSatApiClient().getAssessors();
        if (!verifySuccess(resp)) {
            return;
        }
        List<AssessorDetails> assessorDetails = resp.getAssessors();
        selectAssessors.setItems(assessorDetails);
        selectAssessors.updateSelection(AssessorDetailsHelper.selectionToSet(assessorDetails, details.getAssessors()),
                new HashSet<AssessorDetails>());
        subContent.addComponent(selectAssessors, 0);
        Button editButton = new Button(getString("lang.window.newentity.buttonModify"));
        subContent.addComponent(editButton, 1);
        editButton.addClickListener(this::editedAssessors);
        Button cancelButton = new Button(getString("lang.window.newentity.buttonCancel"));
        subContent.addComponent(cancelButton, 2);
        cancelButton.addClickListener(this::canceledEditAssessors);
        subWindowNewEntity.setContent(subContent);
        getMainUI().addWindow(subWindowNewEntity);
    }

    /**
     * Edits the list of surveys of a entity.
     * 
     * @param event
     *            button click event.
     */
    private void editedAssessors(ClickEvent event) {
        @SuppressWarnings("unchecked")
        TwinColSelect<AssessorDetails> select = (TwinColSelect<AssessorDetails>) ((VerticalLayout) event.getButton()
                .getParent()).getComponent(0);
        EntityDetails details = (EntityDetails) select.getData();
        LOG.debug("Selected items " + select.getSelectedItems());
        details.getAssessors().clear();
        details.getAssessors().addAll(select.getSelectedItems());
        send.setEnabled(sentActive());
        entities.setItems(selectedDetails);
        ((Window) event.getButton().getParent().getParent()).close();
    }

    /**
     * Closes edit surveys window.
     * 
     * @param event
     *            button click event.
     */
    private void canceledEditAssessors(ClickEvent event) {
        ((Window) event.getButton().getParent().getParent()).close();
    }

    /**
     * Helper for setting state of send button.
     * 
     * @return
     */
    private boolean sentActive() {
        for (EntityDetails details : selectedDetails) {
            if (details.getAssessors().size() == 0 || details.getSids().size() == 0) {
                return false;
            }
        }
        return true;
    }

}
