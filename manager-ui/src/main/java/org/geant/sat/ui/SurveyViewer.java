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
import java.util.Iterator;
import java.util.List;

import org.geant.sat.api.dto.ListTokensResponse;
import org.geant.sat.api.dto.QuestionDetails;
import org.geant.sat.api.dto.QuestionsResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.api.dto.TokenDetails;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
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
    private Grid<SurveyDetails> surveys;
    /** List survey instances button. */
    private Button scheduledSurveys;
    /** Token details for selected survey. */
    List<TokenDetails> tokenDetails;

    /**
     * Constructor. Populates the table with survey information.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     */
    public SurveyViewer(MainUI ui) {
        super(ui);
        Design.read(this);
        surveys.setSelectionMode(SelectionMode.SINGLE);
        scheduledSurveys.setCaption(getString("lang.entities.button.scheduled"));
        scheduledSurveys.addClickListener(this::scheduledSurveys);
        scheduledSurveys.setEnabled(false);
        surveys.addSelectionListener(event -> scheduledSurveys.setEnabled(setTokenDetails(event)));
        List<SurveyDetails> details = getFilteredSurveyDetails();
        if (details != null && details.size() > 0) {
            surveys.setItems(details);
            surveys.addColumn(SurveyDetails::getTitle).setCaption(getString("lang.surveys.column.title"));
            surveys.addColumn(SurveyDetails::getSid).setCaption(getString("lang.surveys.column.sid"));
            surveys.addColumn(SurveyDetails::getOwner).setCaption(getString("lang.surveys.column.owner"));
            surveys.addComponentColumn(surveydetail -> getActiveCB(surveydetail)).setCaption(
                    getString("lang.surveys.column.active"));
            surveys.setHeightByRows(details.size() > 0 ? details.size() : 1);
        } else {
            LOG.warn("no survey details found");
        }
    }

    private boolean setTokenDetails(SelectionEvent<SurveyDetails> event) {
        Iterator<SurveyDetails> iter = surveys.getSelectedItems().iterator();
        if (!iter.hasNext()) {
            return false;
        }
        ListTokensResponse tokens = getMainUI().getSatApiClient().getTokens(iter.next().getSid());
        if (!verifySuccess(tokens)) {
            return false;
        }
        tokenDetails = tokens.getTokens();
        if (tokenDetails.size() == 0) {
            Notification.show(getString("lang.notification.noitems"), Notification.Type.HUMANIZED_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Generates checkbox.
     * 
     * @param details
     *            of the user
     * @return checkbox representing the state
     */
    private Component getActiveCB(SurveyDetails details) {
        CheckBox cb = new CheckBox();
        cb.setValue(details.getActive());
        cb.addValueChangeListener(clickEvent -> {
            details.setActive(!details.getActive());
            verifySuccess(getMainUI().getSatApiClient().updateSurvey(details));
        });
        return cb;
    }

    /**
     * Method filters out surveys not belonging to user. Admin and Assessement
     * Coordinator are show all surveys.
     * 
     * @return filtered list of surveys.
     */
    private List<SurveyDetails> getFilteredSurveyDetails() {

        List<SurveyDetails> details = null;
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
        for (int i = 0; i < details.size(); i++) {
            // Here is assumption that surveyowner cannot match principal of a
            // different user
            if (user.getPrincipalId() != null && user.getPrincipalId().equals(details.get(i).getOwner())) {
                userSpecificList.add(details.get(i));
            }
        }
        return userSpecificList;
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
    private List<QuestionDetails> resolveQuestionDetails(String sid, MainUI ui) {
        if (sid == null) {
            LOG.debug("questions resolved with null sid");
            return null;
        }
        QuestionsResponse qr = ui.getSatApiClient().getQuestions(sid);
        if (!verifySuccess(qr)) {
            LOG.debug("No questions for  sid" + sid);
            return null;
        }
        return qr.getQuestions();
    }

    /**
     * Scheduled surveys click handler - creates a sub window with list of
     * surveys to control/monitor.
     * 
     * @param event
     *            button click event.
     */
    private void scheduledSurveys(ClickEvent event) {
        if (tokenDetails == null) {
            LOG.debug("tokenDetails is null, should not happen");
        }
        ScheduledSurveysWindow scheduledSurveysWindow = new ScheduledSurveysWindow(getMainUI(),
                getString("lang.window.scheduledsurveys.title"), tokenDetails);
        scheduledSurveysWindow.setModal(true);
        getMainUI().addWindow(scheduledSurveysWindow);

    }

}
