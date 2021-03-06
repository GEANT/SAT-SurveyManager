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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.declarative.Design;

//TODO: Much of the code is shared by ScheduleSurveyViewer & EntityListViewer, REFACTOR

/**
 * View to list all entities.
 */
@SuppressWarnings("serial")
@DesignRoot
public class EntityListViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(EntityListViewer.class);

    /** Column name for sids column. */
    private static final String COLUMN_SIDS = "sids";

    /** Column name for assessors column. */
    private static final String COLUMN_ASSESSORS = "assessors";

    /** Table showing entities. */
    private Grid<EntityDetails> entities;
    /** Add entity button. */
    private Button addEntity;
    /** import entity button. */
    private Button importEntity;
    /** schedule survey button. */
    private Button scheduleSurvey;

    /**
     * Constructor. Populates the table with survey information.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     */
    public EntityListViewer(MainUI ui) {
        super(ui);
        Design.read(this);
        addEntity.setCaption(getString("lang.button.add"));
        addEntity.addClickListener(this::addEntity);
        importEntity.setCaption(getString("lang.button.import"));
        importEntity.addClickListener(this::importEntity);
        scheduleSurvey.setCaption(getString("lang.entities.button.schedule"));
        scheduleSurvey.addClickListener(this::scheduleSurvey);
        scheduleSurvey.setEnabled(false);
        entities.setSelectionMode(SelectionMode.MULTI);
        entities.addSelectionListener(event -> scheduleSurvey.setEnabled(event.getAllSelectedItems().size() > 0));
        List<EntityDetails> details = getFilteredEntityDetails();
        if (details != null && details.size() > 0) {
            // entities.setDataProvider(new EntityDetailsHelper(details));
            entities.setItems(details);
        }
        entities.addColumn(EntityDetails::getName).setCaption(getString("lang.entities.column.name"));
        entities.addColumn(EntityDetails::getDescription).setCaption(getString("lang.entities.column.description"))
                .setHidable(true);
        entities.addColumn(EntityDetails::getCreator).setCaption(getString("lang.entities.column.creator"))
                .setHidable(true).setHidden(true);
        entities.addColumn(entitydetail -> getSurveys(entitydetail)).setCaption(getString("lang.entities.column.sid"))
                .setId(COLUMN_SIDS).setHidable(true).setHidden(true).setStyleGenerator(entitydetail -> "active");
        entities.addColumn(entitydetail -> getAssessors(entitydetail))
                .setCaption(getString("lang.entities.column.assesors")).setId(COLUMN_ASSESSORS).setHidable(true)
                .setHidden(true).setStyleGenerator(entitydetail -> "active");
        entities.addItemClickListener(event -> handleEvent(event));
        entities.setHeightByRows(details.size() > 0 ? details.size() : 1);

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

    /**
     * Creates a subwindow for editing survey details of entity.
     * 
     * @param details
     *            entity
     */
    private void editSurveys(EntityDetails details) {
        Window subWindowNewEntity = new Window(getString("lang.window.newentity.editsids.title"));
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
        verifySuccess(getMainUI().getSatApiClient().updateEntity(details));
        entities.setItems(getFilteredEntityDetails());
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
        Window subWindowNewEntity = new Window(getString("lang.window.newentity.editassessors.title"));
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
        verifySuccess(getMainUI().getSatApiClient().updateEntity(details));
        entities.setItems(getFilteredEntityDetails());
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
     * Add entity click handler - creates a sub window for user to enter the
     * data.
     * 
     * @param event
     *            button click event.
     */
    private void addEntity(ClickEvent event) {

        // We create a simple window for user to enter entity information
        // no declarative design used, we form it locally
        Window subWindowNewEntity = new Window(getString("lang.window.newentity.title"));
        subWindowNewEntity.setModal(true);
        subWindowNewEntity.setWidth("50%");
        VerticalLayout subContent = new VerticalLayout();
        TextField field = new TextField(getString("lang.window.newentity.textFieldName"));
        field.setWidth("80%");
        subContent.addComponent(field, 0);
        field = new TextField(getString("lang.window.newentity.textFieldDescription"));
        field.setWidth("80%");
        subContent.addComponent(field, 1);
        Button okButton = new Button(getString("lang.window.newentity.buttonCreate"));
        subContent.addComponent(okButton, 2);
        okButton.addClickListener(this::addedEntity);
        Button cancelButton = new Button(getString("lang.window.newentity.buttonCancel"));
        subContent.addComponent(cancelButton, 3);
        cancelButton.addClickListener(this::canceledEntityAdd);
        subWindowNewEntity.setContent(subContent);
        subWindowNewEntity.center();
        getMainUI().addWindow(subWindowNewEntity);

    }

    /**
     * Adds a new entity.
     * 
     * @param event
     *            button click event.
     */
    private void addedEntity(ClickEvent event) {
        String name = ((TextField) ((VerticalLayout) event.getButton().getParent()).getComponent(0)).getValue();
        String description = ((TextField) ((VerticalLayout) event.getButton().getParent()).getComponent(1)).getValue();
        if (name == null || name.length() == 0 || description == null || description.length() == 0) {
            Notification.show(getString("lang.notification.warning.procedurenotcompleted"),
                    getString("lang.notification.entityrequirements"), Notification.Type.WARNING_MESSAGE);
            return;
        }
        verifySuccess(getMainUI().getSatApiClient().createEntity(name, description,
                getMainUI().getUser().getDetails().getPrincipalId()));
        ((Window) event.getButton().getParent().getParent()).close();
        List<EntityDetails> entityDetails = getFilteredEntityDetails();
        entities.setHeightByRows(entityDetails.size() > 0 ? entityDetails.size() : 1);
        entities.setItems(entityDetails);
    }

    /**
     * Closes add new entity window.
     * 
     * @param event
     *            button click event.
     */
    private void canceledEntityAdd(ClickEvent event) {
        ((Window) event.getButton().getParent().getParent()).close();
    }

    /**
     * Import entity click handler - creates a sub window for user to enter the
     * data.
     * 
     * @param event
     *            button click event.
     */
    private void importEntity(ClickEvent event) {

        // We create a simple window for user to enter entity information
        Window importerWindow = new EntityImporterWindow(getMainUI());
        importerWindow.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(CloseEvent e) {
                List<EntityDetails> refreshedEntities = getFilteredEntityDetails();
                LOG.debug("Updating the entities, new number of entities is {}", refreshedEntities.size());
                entities.setItems(refreshedEntities);
                entities.setHeightByRows(refreshedEntities.size() > 0 ? refreshedEntities.size() : 1);

            }
        });
        getMainUI().addWindow(importerWindow);

    }

    /**
     * Schedule survey click handler - creates a sub window for user to enter
     * the data.
     * 
     * @param event
     *            button click event.
     */
    private void scheduleSurvey(ClickEvent event) {
        SurveySchedulerWindow surveySchedulerWindow = new SurveySchedulerWindow(getMainUI(),
                getString("lang.window.surveyschedule.title"), entities.getSelectedItems());
        surveySchedulerWindow.setModal(true);
        getMainUI().addWindow(surveySchedulerWindow);

    }
}
