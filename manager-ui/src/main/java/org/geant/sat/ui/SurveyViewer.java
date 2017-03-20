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

import org.geant.sat.api.dto.QuestionDetails;
import org.geant.sat.api.dto.QuestionsResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.declarative.Design;

/**
 * View to list all surveys.
 */
@SuppressWarnings("serial")
@DesignRoot
public class SurveyViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SurveyViewer.class);
    /** id for column showing survey names. */

    /** Table showing surveys. */
    Grid<SurveyDetails> surveys;

    /**
     * Constructor. Populates the table with survey information.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     */
    public SurveyViewer(MainUI ui) {
        super(ui);
        Design.read(this);
        surveys.setSelectionMode(SelectionMode.NONE);
        SurveyDetails[] details = getFilteredSurveyDetails();
        if (details != null && details.length > 0) {
            surveys.setItems(details);
            surveys.addColumn(SurveyDetails::getTitle).setCaption(getString("lang.surveys.column.title"));
            surveys.addColumn(SurveyDetails::getSid).setCaption(getString("lang.surveys.column.sid"));
            surveys.addColumn(SurveyDetails::getOwner).setCaption(getString("lang.surveys.column.owner"));
            surveys.addColumn(SurveyDetails::getActive).setCaption(getString("lang.surveys.column.active"));

            surveys.setHeightByRows(details.length > 0 ? details.length : 1);
        } else {
            LOG.warn("no survey details found");
        }
    }

    /**
     * Method filters out surveys not belonging to user. Admin and Assessement
     * Coordinator are show all surveys.
     * 
     * @return filtered list of surveys.
     */
    private SurveyDetails[] getFilteredSurveyDetails() {

        SurveyDetails[] details = null;
        if (getMainUI().getSatApiClient() != null && getMainUI().getSatApiClient().getSurveys() != null) {
            details = getMainUI().getSatApiClient().getSurveys().getSurveys();
        } else {
            LOG.warn("unable to parse surveydetails");
            return details;
        }
        UserDetails user = getMainUI().getUser().getDetails();
        if (getMainUI().getRole().isAdmin(user) || getMainUI().getRole().isAssessmentCoordinator(user)) {
            return details;
        }
        List<SurveyDetails> userSpecificList = new ArrayList<SurveyDetails>();
        for (int i = 0; i < details.length; i++) {
            // Here is assumption that surveyowner cannot match principal of a
            // different user
            if (user.getPrincipalId() != null && user.getPrincipalId().equals(details[i].getOwner())) {
                userSpecificList.add(details[i]);
            }
        }
        return userSpecificList.toArray(new SurveyDetails[userSpecificList.size()]);

    }

    /**
     * Method to resolve question details.
     * 
     * @param sid
     *            for survey
     * @param ui
     *            Main ui for shared data.
     * @return question details if they exist, otherwise null
     */
    @SuppressWarnings("unused")
    private QuestionDetails[] resolveQuestionDetails(String sid, MainUI ui) {
        if (sid == null) {
            LOG.debug("questions resolved with null sid");
            return null;
        }
        QuestionsResponse qr = ui.getSatApiClient().getQuestions(sid);
        if (qr == null) {
            LOG.debug("No questions for  sid" + sid);
            return null;
        }
        return qr.getQuestions();
    }

}
