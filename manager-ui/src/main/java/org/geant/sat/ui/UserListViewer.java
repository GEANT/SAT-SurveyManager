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
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.renderers.HtmlRenderer;

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

    /** Column name for admin column. */
    private static final String COLUMN_ADMIN = "admin";
    /** Column name for assessment coordinator column. */
    private static final String COLUMN_AC = "ac";
    /** Column name for survey owner column. */
    private static final String COLUMN_SO = "so";

    /** Table showing user profile. */
    Grid<UserDetails> users;

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
        Column<UserDetails, String> column = users.addColumn(userdetail -> isAdmin(userdetail), new HtmlRenderer())
                .setCaption(getString("lang.users.column.roles.admin"));
        column.setId(COLUMN_ADMIN);
        column.setStyleGenerator(userdetail -> "disabled");
        column = users.addColumn(userdetail -> isAssessmentCoordinator(userdetail), new HtmlRenderer()).setCaption(
                getString("lang.users.column.roles.assessmentcoordinator"));
        column.setId(COLUMN_AC);
        column.setStyleGenerator(userdetail -> "active");
        column = users.addColumn(userdetail -> isSurveyOwner(userdetail), new HtmlRenderer()).setCaption(
                getString("lang.users.column.roles.surveyowner"));
        column.setId(COLUMN_SO);
        column.setStyleGenerator(userdetail -> "disabled");
        column = users.addColumn(userdetail -> getRoles(userdetail)).setCaption(getString("lang.users.column.roles"));
        column.setHidable(true);
        column.setHidden(true);
        column = users.addColumn(userdetail -> getAttributes(userdetail)).setCaption(
                getString("lang.users.column.attributes"));
        column.setHidable(true);
        column.setHidden(true);
        users.addItemClickListener(event -> handleEvent(event));
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
        if (!indicateSuccess(resp)) {
            return details;
        }
        List<UserDetails> satDetails = resp.getUsers();
        return satDetails == null ? details : satDetails;
    }

    /**
     * Handles click events.
     * 
     * @param event
     *            representing the click.
     */
    private void handleEvent(ItemClick<UserDetails> event) {
        UserDetails details = event.getItem();
        if (event.getColumn().getId() == null) {
            // not a editable column
            return;
        }
        if (!getMainUI().getRole().isAdmin(getMainUI().getUser().getDetails())) {
            // Editing only for admin user.
            return;
        }
        if (details.getPrincipalId() == null) {
            Notification.show(getString("lang.notification.unmappedusersuneditable"));
            return;
        }

        switch (event.getColumn().getId()) {
        case COLUMN_ADMIN:
            Notification.show(getString("lang.notification.limesurveyparameter"));
            break;
        case COLUMN_AC:
            if (getMainUI().getRole().isAssessmentCoordinator(details)) {
                LOG.debug("removing role " + getMainUI().getRole().getAssessmentCoordinatorRoleName());
                details.getRoles().remove(getMainUI().getRole().getAssessmentCoordinatorRoleName());
            } else {
                LOG.debug("Adding role " + getMainUI().getRole().getAssessmentCoordinatorRoleName());
                details.getRoles().add(getMainUI().getRole().getAssessmentCoordinatorRoleName());
            }
            indicateSuccess(getMainUI().getSatApiClient().updateUser(details));
            users.setItems(getUserDetails());
            break;
        case COLUMN_SO:
            Notification.show(getString("lang.notification.limesurveyparameter"));
            break;
        default:
            break;
        }

    }

    /**
     * Generates cell containing admin role information.
     * 
     * @param details
     *            of the user
     * @return icon representing the state
     */
    private String isAdmin(UserDetails details) {
        if (getMainUI().getRole().isAdmin(details)) {
            return VaadinIcons.CHECK_SQUARE_O.getHtml();
        } else {
            return VaadinIcons.THIN_SQUARE.getHtml();
        }

    }

    /**
     * Generates cell containing assessment coordinator role information.
     * 
     * @param details
     *            of the user
     * @return roles
     */
    private String isAssessmentCoordinator(UserDetails details) {
        if (getMainUI().getRole().isAssessmentCoordinator(details)) {
            return VaadinIcons.CHECK_SQUARE_O.getHtml();
        } else {
            return VaadinIcons.THIN_SQUARE.getHtml();
        }

    }

    /**
     * Generates cell containing survey owner role information.
     * 
     * @param details
     *            of the user
     * @return icon representing the state
     */
    private String isSurveyOwner(UserDetails details) {
        if (getMainUI().getRole().isSurveyOwner(details)) {
            return VaadinIcons.CHECK_SQUARE_O.getHtml();
        } else {
            return VaadinIcons.THIN_SQUARE.getHtml();
        }

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
