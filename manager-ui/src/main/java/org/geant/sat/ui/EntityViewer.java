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
import java.util.List;

import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.declarative.Design;

/**
 * View to list all entities.
 */
@SuppressWarnings("serial")
@DesignRoot
public class EntityViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(EntityViewer.class);

    /** Table showing entities. */
    Grid<EntityDetails> entities;

    /**
     * Constructor. Populates the table with survey information.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     */
    public EntityViewer(MainUI ui) {
        super(ui);
        Design.read(this);
        entities.setSelectionMode(SelectionMode.NONE);
        List<EntityDetails> details = getFilteredEntityDetails();
        if (details != null && details.size() > 0) {
            entities.setItems(details);
            entities.addColumn(EntityDetails::getName).setCaption(getString("lang.entities.column.name"));
            entities.addColumn(EntityDetails::getCreator).setCaption(getString("lang.entities.column.creator"));
            Column<EntityDetails, String> column = entities.addColumn(entitydetail -> getSids(entitydetail))
                    .setCaption(getString("lang.entities.column.sid"));
            column.setHidable(true);
            column.setHidden(true);
            column = entities.addColumn(entitydetail -> getAssessors(entitydetail)).setCaption(
                    getString("lang.entities.column.assesors"));
            column.setHidable(true);
            column.setHidden(true);
            entities.setHeightByRows(details.size() > 0 ? details.size() : 1);
        } else {
            LOG.warn("no survey details found");
        }
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
