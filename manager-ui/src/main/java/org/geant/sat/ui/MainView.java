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

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Main work view implementing menu and controlling sub work views.
 *
 */
@SuppressWarnings("serial")
@DesignRoot
public class MainView extends AbstractSurveyVerticalLayout implements View {

    /** Menu name for view surveys. */
    private static final String MENU_SURVEYS = "surveys";
    /** Menu name for view users. */
    private static final String MENU_USERS = "users";
    /** Menu name for view profile. */
    private static final String MENU_PROFILE = "profile";
    /** Menu name for view about. */
    private static final String MENU_ABOUT = "about";
    /** Menu name for view Limesurvey. */
    private static final String MENU_LIMESURVEY = "limesurvey";
    /** Menu name for view Entities. */
    private static final String MENU_ENTITIES = "entities";

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(MainView.class);

    /** Layout for menu. */
    VerticalLayout menuContent;
    /** Panel for work views. */
    Panel workPanel;

    /**
     * Constructor. Initializes menu.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     */
    public MainView(MainUI ui) {
        super(ui);
        Design.read(this);
        addStyleName(ValoTheme.MENU_PART_LARGE_ICONS);
        // Menu items are set by roles.
        // TODO: Make a configurable bean for role & view visibility

        // Surveys are shown to admin,ass coord. and survey owner.
        if (getMainUI().getRole().isAdmin(getMainUI().getUser().getDetails())
                || getMainUI().getRole().isAssessmentCoordinator(getMainUI().getUser().getDetails())
                || getMainUI().getRole().isSurveyOwner(getMainUI().getUser().getDetails())) {
            menuContent.addComponent(createMenuButton(getString("lang.surveys"), MENU_SURVEYS));
        }
        // Entities are shown to admin, and ass coord.
        if (getMainUI().getRole().isAdmin(getMainUI().getUser().getDetails())
                || getMainUI().getRole().isAssessmentCoordinator(getMainUI().getUser().getDetails())) {
            menuContent.addComponent(createMenuButton(getString("lang.entities"), MENU_ENTITIES));
        }
        // User list is shown to admin only
        if (getMainUI().getRole().isAdmin(getMainUI().getUser().getDetails())) {
            menuContent.addComponent(createMenuButton(getString("lang.users"), MENU_USERS));
        }
        menuContent.addComponent(createMenuButton(getString("lang.profile"), MENU_PROFILE));
        menuContent.addComponent(createMenuButton(getString("lang.about"), MENU_ABOUT));
        if (getMainUI().getRole().isSurveyOwner(getMainUI().getUser().getDetails())) {
            menuContent.addComponent(createMenuButton(getString("lang.limesurvey"), MENU_LIMESURVEY));
        }
    }

    /**
     * Create button for menu.
     * 
     * @param caption
     *            for the button.
     * @param menuListenerName
     *            listener for button.
     * @return Button as Component.
     */
    private Component createMenuButton(String caption, String menuListenerName) {
        Button button = new Button(caption, new ButtonListener(menuListenerName));
        button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        return (Component) button;
    }

    /** {@inheritDocs}. */
    @SuppressWarnings("rawtypes")
    @Override
    public void enter(ViewChangeEvent event) {
        if (MENU_ENTITIES.equals(event.getParameters())) {
            workPanel.setContent(new EntityListViewer((MainUI) getUI()));
            return;
        }
        if (MENU_SURVEYS.equals(event.getParameters())) {
            workPanel.setContent(new SurveyViewer((MainUI) getUI()));
            return;
        }
        if (MENU_USERS.equals(event.getParameters())) {
            workPanel.setContent(new UserListViewer((MainUI) getUI()));
            return;
        }
        if (MENU_PROFILE.equals(event.getParameters())) {
            workPanel.setContent(new ProfileViewer((MainUI) getUI(), ((MainUI) getUI()).getUser().getDetails()));
            return;
        }
        if (MENU_ABOUT.equals(event.getParameters())) {
            workPanel.setContent(new AboutViewer());
            return;
        }
        if (MENU_LIMESURVEY.equals(event.getParameters())) {
            final String hostUrl = getBaseUrl(((MainUI) getUI()).getPage().getLocation());
            log.debug("Using the following baseUrl: {}", hostUrl);
            final String limesurveyPath = ((MainUI) getUI()).getLimesurveyPath();
            log.debug("The limesurvey path is {}", limesurveyPath);
            final BrowserFrame browserFrame = new BrowserFrame("Browser", new ExternalResource(hostUrl + limesurveyPath
                    + "admin/"));
            browserFrame.setHeight("100%");
            browserFrame.setWidth("100%");
            workPanel.setContent(browserFrame);
            return;
        }
        workPanel.setContent(new AboutViewer());
        return;

    }

    /**
     * Get the base URL (scheme + host + port if exists) for the given URI.
     * 
     * @param location
     *            The URI to be parsed.
     * @return The base URL.
     */
    protected static String getBaseUrl(final URI location) {
        final StringBuffer buffer = new StringBuffer(location.getScheme()).append("://");
        buffer.append(location.getHost());
        if (location.getPort() > 0) {
            buffer.append(":").append(location.getPort());
        }
        return buffer.toString();
    }

    /**
     * Menu button listener.
     */
    class ButtonListener implements Button.ClickListener {

        /** Menu item name. */
        private String menuitem;

        /**
         * Constructor.
         * 
         * @param name
         *            Name for menu item.
         */
        public ButtonListener(String name) {
            this.menuitem = name;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            // Navigate to a specific state
            getUI().getNavigator().navigateTo(MainUI.MAINVIEW + "/" + menuitem);
        }
    }

    /**
     * Simple about view.
     */
    @DesignRoot
    class AboutViewer extends VerticalLayout {

        /** text string 1. */
        Label text1;
        /** text string 2. */
        Label text2;

        /**
         * Constructor.
         */
        public AboutViewer() {
            Design.read(this);
            text1.setValue(getString("lang.aboutview.text1"));
            text2.setValue(getString("lang.aboutview.text2"));
        }
    }

}
