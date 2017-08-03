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

import org.geant.sat.api.dto.AbstractConnectorResponse;
import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.UserDetails;
import org.geant.sat.ui.utils.AssessorDetailsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.VerticalLayout;

/** abstract base class for vertical layouts. */
@SuppressWarnings("serial")
public abstract class AbstractSurveyVerticalLayout extends VerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSurveyVerticalLayout.class);

    /** main ui containing information shared by all views. */
    private MainUI mainUI;

    /**
     * Constructor.
     * 
     * @param ui
     *            main ui
     */
    AbstractSurveyVerticalLayout(MainUI ui) {
        mainUI = ui;

    }

    /**
     * Returns main ui. Main ui contains informations shared by all views.
     * 
     * @return main ui instance
     */
    protected MainUI getMainUI() {
        return mainUI;
    }

    /**
     * Fetched language string by key. If matching string is not found the key
     * itself is returned.
     * 
     * @param key
     *            by which the string is searched for
     * @return language string or key
     */
    protected String getString(String key) {
        String value;
        if (mainUI == null || mainUI.getStrings() == null) {
            return key;
        }
        value = mainUI.getStrings().getString(key);
        return value == null ? key : value;
    }

    /**
     * Checks for Sat Api error response.
     * 
     * @param response
     *            to check for.
     * @return false if error occurred.
     */
    public boolean verifySuccess(AbstractConnectorResponse response) {
        return getMainUI().indicateSuccess(response);
    }

    /**
     * Method filters out entities not belonging to user. Admin is shown all
     * entities.
     * 
     * @return filtered list of entities.
     */
    protected List<EntityDetails> getFilteredEntityDetails() {

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

    /**
     * Generates string containing survey information.
     * 
     * @param details
     *            of the entity
     * @return surveys
     */
    protected String getSurveys(EntityDetails details) {
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
     * Generates string containing assessor information.
     * 
     * @param details
     *            of the entity
     * @return assessors
     */
    protected String getAssessors(EntityDetails details) {
        String assessors = "";
        if (details == null || details.getAssessors() == null) {
            return assessors;
        }
        for (AssessorDetails assDetails : details.getAssessors()) {
            assessors += AssessorDetailsHelper.display(assDetails) + " ";
        }
        return assessors;
    }

}
