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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WebBrowser;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import org.geant.sat.api.SatApiClient;
import org.geant.sat.api.dto.AbstractConnectorResponse;

/**
 * Main ui class. Configures the navigation, sets language properties and shares
 * information like user to sub views.
 *
 */
@SuppressWarnings("serial")
@Theme("mytheme")
@StyleSheet("mytheme-theme-ui.css")
@SpringUI
public class MainUI extends UI {

    /** Main view. */
    protected static final String MAINVIEW = "main";
    /** Start view. */
    protected static final String STARTVIEW = "start";

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MainUI.class);

    /** Sat api client. */
    @Resource(name = "satApiClient")
    private SatApiClient satApiClient;
    /** User implementation. */
    @Resource(name = "applicationUser")
    private User user;
    /** Role implementation. */
    @Resource(name = "applicationRole")
    private Role role;
    /** Navigation. */
    private Navigator navigator;
    /** Language resource. */
    private ResourceBundle languageBundle;

    /** path to limesurvey application. */
    @Resource(name = "limesurveyPath")
    private String limesurveyPath;

    /**
     * Method to get path for the Limesurvey instance.
     * 
     * @return Base path for the Limesurvey instance.
     */
    public String getLimesurveyPath() {
        return limesurveyPath;
    }

    /**
     * Method to get Sat Api Client to perform Survey Assessment Operations.
     * 
     * @return Sat Api Client.
     */
    public SatApiClient getSatApiClient() {
        return satApiClient;
    }

    /**
     * Checks for Sat Api error response.
     * 
     * @param response
     *            to check for.
     * @return false if error occurred.
     */
    public boolean indicateSuccess(AbstractConnectorResponse response) {
        if (response.getErrorMessage() != null) {
            LOG.error("Error occurred:" + response.getErrorMessage());
            Notification.show(getStrings().getString("lang.errornotification"), response.getErrorMessage(),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Method to get Language resource.
     * 
     * @return Language resource.
     */
    public ResourceBundle getStrings() {
        return languageBundle;
    }

    /**
     * Method to get User information.
     * 
     * @return user information.
     */
    public User getUser() {
        return user;
    }

    /**
     * Method to get Role information.
     * 
     * @return Role information.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Method to load language resources.
     */
    protected void initLanguageBundle() {
        Locale locale = Locale.ENGLISH;
        if (Page.getCurrent() != null && Page.getCurrent().getWebBrowser() != null
                && Page.getCurrent().getWebBrowser().getLocale() != null) {
            locale = Page.getCurrent().getWebBrowser().getLocale();
        }
        try {
            languageBundle = ResourceBundle.getBundle("Language", locale);
        } catch (MissingResourceException e) {
            LOG.debug("language file for " + locale + " not found, reverting to default");
            languageBundle = ResourceBundle.getBundle("Language", Locale.ROOT);
        }
    }

    /**
     * Initialize Main UI.
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        addStyleName(ValoTheme.UI_WITH_MENU);
        initLanguageBundle();
        user.setSatApiClient(satApiClient);
        navigator = new Navigator(this, this);
        StartView startView = new StartView(this);
        MainView mainView = new MainView(this);
        navigator.addView(STARTVIEW, startView);
        navigator.addView(MAINVIEW, mainView);
        navigator.navigateTo(STARTVIEW);
    }

    /**
     * Weblistener.
     */
    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {

    }

    /**
     * Configuration.
     */
    @Configuration
    @EnableVaadin
    public static class MyConfiguration {
    }

    /**
     * Webservlet.
     */
    @WebServlet(urlPatterns = "/main", name = "MainUIServlet", asyncSupported = true)
    public static class MainViewServlet extends SpringVaadinServlet {
    }

}