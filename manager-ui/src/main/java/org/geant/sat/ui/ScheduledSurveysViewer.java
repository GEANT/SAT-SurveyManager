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

import org.geant.sat.api.dto.TokenDetails;
import org.geant.sat.api.dto.UserDetails;
import org.geant.sat.ui.utils.AssessorStringIDHelper;
import org.geant.sat.ui.utils.EntityStringIDHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.declarative.Design;

/**
 * View to list all surveys.
 */
@SuppressWarnings("serial")
@DesignRoot
public class ScheduledSurveysViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledSurveysViewer.class);

    /** Table showing surveys. */
    private Grid<TokenDetails> scheduledSurveys;

    /**
     * Constructor. Populates the table with scheduled surveys information.
     * 
     * @details Survey instance details.
     * @param ui
     *            Main ui instance to access shared data.
     */
    public ScheduledSurveysViewer(MainUI ui, List<TokenDetails> details) {
        super(ui);
        Design.read(this);
        scheduledSurveys.setSelectionMode(SelectionMode.SINGLE);
        List<TokenDetails> filteredDetails = getFilteredSurveyIntanceDetails(details);
        if (details != null && details.size() > 0) {
            scheduledSurveys.setItems(filteredDetails);
            scheduledSurveys.addColumn(surveydetail -> EntityStringIDHelper.display(surveydetail.getEntityId(), ui))
                    .setCaption(getString("lang.schduledsurveys.column.entity"));
            scheduledSurveys.addColumn(TokenDetails::isCompleted).setCaption(
                    getString("lang.schduledsurveys.column.complete"));
            scheduledSurveys.addColumn(TokenDetails::isValid)
                    .setCaption(getString("lang.schduledsurveys.column.valid"));
            scheduledSurveys
                    .addColumn(surveydetail -> AssessorStringIDHelper.display(surveydetail.getAssessorId(), ui))
                    .setCaption(getString("lang.schduledsurveys.column.assessor"));
            scheduledSurveys.addColumn(TokenDetails::getPrincipalId).setCaption(
                    getString("lang.schduledsurveys.column.creator"));
            scheduledSurveys.addColumn(TokenDetails::getToken).setCaption(
                    getString("lang.schduledsurveys.column.token"));
            scheduledSurveys.setHeightByRows(details.size() > 0 ? details.size() : 1);
        } else {
            Notification.show(getString("lang.notification.noitems"), Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    /**
     * Method filters out survey instances not created by the user. Admin and
     * Assessement Coordinator are show all survey instances.
     * 
     * @param details
     *            survey instances.
     * @return filtered list of scheduled surveys.
     */
    private List<TokenDetails> getFilteredSurveyIntanceDetails(List<TokenDetails> details) {

        UserDetails user = getMainUI().getUser().getDetails();
        if (getMainUI().getRole().isAdmin(user) || getMainUI().getRole().isAssessmentCoordinator(user)) {
            return details;
        }
        List<TokenDetails> userSpecificList = new ArrayList<TokenDetails>();
        for (int i = 0; i < details.size(); i++) {
            if (user.getPrincipalId() != null && user.getPrincipalId().equals(details.get(i).getPrincipalId())) {
                userSpecificList.add(details.get(i));
            }
        }
        return userSpecificList;

    }

}
