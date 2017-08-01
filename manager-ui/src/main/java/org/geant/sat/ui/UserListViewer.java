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
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.declarative.Design;

/**
 * Viewer to list information on user.
 * 
 * @param <V>
 */
@SuppressWarnings("serial")
@DesignRoot
public class UserListViewer<V> extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(UserListViewer.class);

    /** Table showing user profile. */
    private Grid<UserDetails> users;

    /**
     * Constructor. Populates the table with user information.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     */
    public UserListViewer(MainUI ui) {
        super(ui);
        Design.read(this);
        users.setSelectionMode(SelectionMode.NONE);
        List<UserDetails> details = getUserDetails();
        users.setItems(details);
        users.addColumn(UserDetails::getPrincipalId).setCaption(getString("lang.users.column.principal"));
        users.addColumn(UserDetails::getSurveyPrincipalId).setCaption(getString("lang.users.column.surveyprincipal"));
        users.addComponentColumn(userdetail -> getAdminCB(userdetail)).setCaption(
                getString("lang.users.column.roles.admin"));
        users.addComponentColumn(userdetail -> getAssessmentCoordinatorCB(userdetail)).setCaption(
                getString("lang.users.column.roles.assessmentcoordinator"));
        users.addComponentColumn(userdetail -> getSurveyOwnerCB(userdetail)).setCaption(
                getString("lang.users.column.roles.surveyowner"));
        users.addColumn(userdetail -> getRoles(userdetail)).setHidable(true).setHidden(true)
                .setCaption(getString("lang.users.column.roles"));
        users.addColumn(userdetail -> getAttributes(userdetail)).setHidable(true).setHidden(true)
                .setCaption(getString("lang.users.column.attributes"));
        // atleast one, at most 20 and fit to size otherwise
        users.setHeightByRows(details.size() > 0 ? details.size() : 1);

    }

    /**
     * Helper method to ensure user list is not null.
     * 
     * @return user details
     */
    private List<UserDetails> getUserDetails() {
        List<UserDetails> details = new ArrayList<UserDetails>();
        ListUsersResponse resp = getMainUI().getSatApiClient().getUsers();
        if (!verifySuccess(resp)) {
            return details;
        }
        List<UserDetails> satDetails = resp.getUsers();
        return satDetails == null ? details : satDetails;
    }

    /**
     * Generates checkbox representing admin role information.
     * 
     * @param details
     *            of the user
     * @return checkbox representing the admin role
     */
    private Component getAdminCB(UserDetails details) {
        CheckBox cb = new CheckBox();
        cb.setValue(getMainUI().getRole().isAdmin(details));
        cb.setEnabled(false);
        return cb;
    }

    /**
     * Generates checkbox representing assessment coordinator role information.
     * 
     * @param details
     *            of the user
     * @return checkbox representing the assessment coordinator role
     */
    private Component getAssessmentCoordinatorCB(UserDetails details) {
        CheckBox cb = new CheckBox();
        cb.setValue(getMainUI().getRole().isAssessmentCoordinator(details));
        if (details.getPrincipalId() == null) {
            cb.setEnabled(false);
            return cb;
        }
        cb.addValueChangeListener(clickEvent -> {
            if (getMainUI().getRole().isAssessmentCoordinator(details)) {
                LOG.debug("removing role " + getMainUI().getRole().getAssessmentCoordinatorRoleName());
                details.getRoles().remove(getMainUI().getRole().getAssessmentCoordinatorRoleName());
            } else {
                LOG.debug("Adding role " + getMainUI().getRole().getAssessmentCoordinatorRoleName());
                details.getRoles().add(getMainUI().getRole().getAssessmentCoordinatorRoleName());
            }
            verifySuccess(getMainUI().getSatApiClient().updateUser(details));
        });
        return cb;
    }

    /**
     * Generates checkbox representing survey owner role information.
     * 
     * @param details
     *            of the user
     * @return checkbox representing the survey owner role
     */
    private Component getSurveyOwnerCB(UserDetails details) {
        CheckBox cb = new CheckBox();
        cb.setValue(getMainUI().getRole().isSurveyOwner(details));
        cb.setEnabled(false);
        return cb;
    }

    /**
     * Generates cell containing role information.
     * 
     * @param details
     *            of the user
     * @return roles
     */
    private String getRoles(UserDetails details) {
        String roles = "";
        if (details == null || details.getRoles() == null) {
            return roles;
        }
        for (String role : details.getRoles()) {
            roles += role + " ";
        }
        return roles;
    }

    /**
     * Generates cell containing attribute information.
     * 
     * @param details
     *            of the user.
     * @return attributes
     */
    private String getAttributes(UserDetails details) {
        String attributes = "";
        if (details == null || details.getAttributes() == null) {
            return attributes;
        }
        for (String key : details.getAttributes().keySet()) {
            attributes += key + "=" + details.getAttributes().get(key) + " ";
        }
        return attributes;
    }

}
