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

import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListAllSurveysResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

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
    /** Add entity button */
    private Button addEntity;

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
        entities.setSelectionMode(SelectionMode.NONE);
        List<EntityDetails> details = getFilteredEntityDetails();
        if (details != null && details.size() > 0) {
            entities.setItems(details);
            entities.addColumn(EntityDetails::getName).setCaption(getString("lang.entities.column.name"));
            entities.addColumn(EntityDetails::getDescription).setCaption(getString("lang.entities.column.description"));
            entities.addColumn(EntityDetails::getCreator).setCaption(getString("lang.entities.column.creator"));
            Column<EntityDetails, String> column = entities.addColumn(entitydetail -> getSids(entitydetail))
                    .setCaption(getString("lang.entities.column.sid"));
            column.setId(COLUMN_SIDS);
            column.setHidable(true);
            column.setHidden(true);
            column = entities.addColumn(entitydetail -> getAssessors(entitydetail)).setCaption(
                    getString("lang.entities.column.assesors"));
            column.setId(COLUMN_ASSESSORS);
            column.setHidable(true);
            column.setHidden(true);
            entities.addItemClickListener(event -> handleEvent(event));
            entities.setHeightByRows(details.size() > 0 ? details.size() : 1);
        } else {
            LOG.warn("no survey details found");
        }
    }

    /**
     * Handles click events.
     * 
     * @param event
     *            representing the click.
     */
    private void handleEvent(ItemClick<EntityDetails> event) {
        if (!event.getMouseEventDetails().isDoubleClick()) {
            // not a double click
            return;
        }
        EntityDetails details = event.getItem();
        if (event.getColumn().getId() == null) {
            // not a editable column
            return;
        }
        switch (event.getColumn().getId()) {
        case COLUMN_SIDS:
            editSids(details);
            break;
        case COLUMN_ASSESSORS:
            editAssessors(details);
        default:
            break;
        }
    }

    private void editSids(EntityDetails details) {
        Window subWindowNewEntity = new Window(getString("lang.window.newentity.editsids.title"));
        subWindowNewEntity.setModal(true);
        VerticalLayout subContent = new VerticalLayout();
        // form a list of surveys as name/sid strings
        // preselect the surveys that exist already//
        // getMainUI().getSatApiClient().getSurveys().
        // selectSids.setI
        TwinColSelect<String> selectSids = new TwinColSelect<>(getString("lang.window.newentity.editsids.sids"));
        selectSids.setData(details);
        ListAllSurveysResponse resp = getMainUI().getSatApiClient().getSurveys();
        if (!indicateSuccess(resp)) {
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
        editButton.addClickListener(this::editSurveys);
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
    private void editSurveys(ClickEvent event) {
        @SuppressWarnings("unchecked")
        TwinColSelect<String> select = ((TwinColSelect<String>) ((VerticalLayout) event.getButton().getParent())
                .getComponent(0));
        EntityDetails details = (EntityDetails) select.getData();
        LOG.debug("Original surveys " + details.getSids());
        details.getSids().clear();
        details.getSids().addAll(select.getSelectedItems());
        LOG.debug("New set of surveys " + details.getSids());
        // TODO: update
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

    private void editAssessors(EntityDetails details) {
        Notification.show("editAssessors");
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
        indicateSuccess(getMainUI().getSatApiClient().createEntity(name, description,
                getMainUI().getUser().getDetails().getPrincipalId()));
        ((Window) event.getButton().getParent().getParent()).close();
        entities.setItems(getFilteredEntityDetails());
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
     * Generates cell containing survey information.
     * 
     * @param details
     *            of the entity
     * @return surveys
     */
    private String getSids(EntityDetails details) {
        String sids = "";
        if (details == null || details.getSids() == null) {
            return sids;
        }
        for (String sid : details.getSids()) {
            sids += sid + " ";
        }
        return sids;
    }

    /**
     * Generates cell containing assessor information.
     * 
     * @param details
     *            of the entity
     * @return assessors
     */
    private String getAssessors(EntityDetails details) {
        String attributes = "";
        if (details == null || details.getAssessors() == null) {
            return attributes;
        }
        for (String key : details.getAssessors().keySet()) {
            attributes += key + "=" + details.getAssessors().get(key) + " ";
        }
        return attributes;
    }

    /**
     * Method filters out entities not belonging to user. Admin is shown all
     * entities.
     * 
     * @return filtered list of entities.
     */
    private List<EntityDetails> getFilteredEntityDetails() {

        List<EntityDetails> details = null;
        if (getMainUI().getSatApiClient() != null && getMainUI().getSatApiClient().getEntities() != null) {
            details = getMainUI().getSatApiClient().getEntities().getEntities();
        } else {
            LOG.warn("unable to parse entitydetails");
            return details;
        }
        UserDetails user = getMainUI().getUser().getDetails();
        if (getMainUI().getRole().isAdmin(user)) {
            return details;
        }
        List<EntityDetails> userSpecificList = new ArrayList<EntityDetails>();
        for (int i = 0; i < details.size(); i++) {
            if (user.getPrincipalId() != null && user.getPrincipalId().equals(details.get(i).getCreator())) {
                userSpecificList.add(details.get(i));
            }
        }
        return userSpecificList;

    }

}
