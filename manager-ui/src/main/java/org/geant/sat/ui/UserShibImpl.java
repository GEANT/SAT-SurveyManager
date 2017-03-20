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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.geant.sat.api.SatApiClient;
import org.geant.sat.api.dto.UserDetails;
import org.geant.sat.api.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinService;

/**
 * Parses user information from attributes/headers. The class is inteded to be
 * used to consume information provided by shibboleth sp.
 */
public class UserShibImpl implements User {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(UserShibImpl.class);

    /** key to store user details to. */
    private static final String DETAILS_KEY = "org.geant.sat.ui.UserShibImpl.DETAILS_KEY";
    /** key to store user principal to. */
    private static final String PRINCIPAL_KEY = "org.geant.sat.ui.UserShibImpl.PRINCIPAL_KEY";

    /** Flag to use either attributes or headers to parse data. */
    private boolean useHeaders;
    /** Key to attribute/header indicating successfull authentication. */
    private String authenticationKey;
    /** Value to match authentication field value against. */
    private String authenticationRegExpValue;
    /** Key to user principal field in authentication attributes. */
    private String userPrincipalKey;
    /** attribute/header fields populated as user authentication attributes. */
    private Map<String, String> keyValueToAttributeValue;
    /** User attributes. */
    private Map<String, String> attributes = new HashMap<String, String>();
    /** provides the back-end user information. */
    private SatApiClient satApiClient;
    

    /**
     * If set to true, headers are used for parsing user data.
     * 
     * @param headers
     *            true if headers should be used.
     */
    public void setUseHeaders(boolean headers) {
        this.useHeaders = headers;
    }

    /**
     * Set map indicating which attribute/header fields should be populated as
     * given user attributes.
     * 
     * @param key2Attribute
     *            field to user attribute map.
     */
    public void setKeyValueToAttributeValue(Map<String, String> key2Attribute) {
        this.keyValueToAttributeValue = key2Attribute;
    }

    /**
     * Set the user principal field name.
     * 
     * @param key
     *            user principal field name
     */
    public void setUserPrincipalKey(String key) {
        this.userPrincipalKey = key;
    }

    /**
     * Set the header/attribute field indicating user is authenticated.
     * 
     * @param key
     *            field name.
     */
    public void setAuthenticationKey(String key) {
        LOG.debug("using " + key + " as authentication field key");
        this.authenticationKey = key;
    }

    /**
     * Set the allowed authentication field values as regexp.
     * 
     * @param regExpValue
     *            authentication field value as regexp.
     */
    public void setAuthenticationRegExpValue(String regExpValue) {
        LOG.debug("using " + regExpValue + " as authentication field value matcher");
        this.authenticationRegExpValue = regExpValue;
    }

    /**
     * Returns true if user is authenticated.
     */
    @Override
    public boolean isAuthenticated() {
        if (VaadinService.getCurrentRequest().getWrappedSession().getAttribute(PRINCIPAL_KEY) == null) {
            updateUserInformation();
        }
        return VaadinService.getCurrentRequest().getWrappedSession().getAttribute(PRINCIPAL_KEY) != null;
    }

    /**
     * Get names of attribute/header fields.
     * 
     * @return names enumeration.
     */
    private Enumeration<String> getNames() {
        if (useHeaders) {
            return VaadinService.getCurrentRequest().getHeaderNames();
        }
        return VaadinService.getCurrentRequest().getAttributeNames();
    }

    /**
     * Get value of attribute/header field.
     * 
     * @param name
     *            of the field.
     * @return value of the field.
     */
    private Object getValue(String name) {
        if (useHeaders) {
            return VaadinService.getCurrentRequest().getHeader(name);
        }
        return VaadinService.getCurrentRequest().getAttribute(name);
    }

    /**
     * Updates and sets user information.
     */
    private void updateUserInformation() {
        if (!mapUserToSat(parseUser())) {
            clearUserData();
        }
    }

    /**
     * Clears user data.
     */
    private void clearUserData() {
        LOG.debug("Clearing user data");
        attributes.clear();
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute(DETAILS_KEY, null);
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute(PRINCIPAL_KEY, null);
    }

    /**
     * Maps user principal to back-end. Creates user if needed.
     * 
     * @param userPrincipal user to map.
     * @return true if user was successfully mapped to sat.
     */
    private boolean mapUserToSat(String userPrincipal) {
        LOG.debug("User authenticated, trying to locate the user from back-end.");
        if (userPrincipal == null) {
            LOG.debug("User not authenticated");
            return false;
        }
        UserResponse resp = satApiClient.getUser(userPrincipal);
        if (resp.getErrorMessage() == null) {
            LOG.debug("User found from back-end, setting the user details.");
            // not updating user, just taking as it is
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute(DETAILS_KEY, resp.getUser());
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute(PRINCIPAL_KEY, userPrincipal);
            LOG.debug("details and principal set for  "
                    + VaadinService.getCurrentRequest().getWrappedSession().getAttribute(PRINCIPAL_KEY));
            return true;
        }
        LOG.debug("Creating a new user");
        UserDetails details = new UserDetails();
        details.setAttributes(attributes);
        details.setPrincipalId(userPrincipal);
        resp = satApiClient.createUser(details);
        if (resp.getErrorMessage() == null) {
            LOG.debug("User creation succeeded, trying to locate the user from back-end.");
            resp = satApiClient.getUser(userPrincipal);
            if (resp.getErrorMessage() == null) {
                LOG.debug("User found from back-end, setting the user details.");
                // freshly created user
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute(DETAILS_KEY, resp.getUser());
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute(PRINCIPAL_KEY, userPrincipal);
                LOG.debug("details and principal set for  "
                        + VaadinService.getCurrentRequest().getWrappedSession().getAttribute(PRINCIPAL_KEY));
                return true;
            }

        }
        LOG.error("Unable to create user " + resp.getErrorMessage());
        return false;
    }

    /**
     * Parse user information from attributes/headers.
     * 
     * @return user principal if the user is authenticated and identified.
     */
    private String parseUser() {
        if (authenticationRegExpValue == null) {
            attributes.clear();
            LOG.error("authentication field value matcher not set, unable to authenticate user");
            return null;
        }
        String userPrincipal = null;
        Enumeration<String> names = getNames();
        boolean authenticated = false;
        while (names.hasMoreElements()) {
            String attributeName = names.nextElement();
            Object value = getValue(attributeName);
            if (value == null || !(value instanceof String)) {
                LOG.warn("attribute value is not string");
                continue;
            }
            // Add value to attribute map
            if (keyValueToAttributeValue != null && keyValueToAttributeValue.containsKey(attributeName)) {
                LOG.debug("Setting user attribute " + keyValueToAttributeValue.get(attributeName) + " to "
                        + (String) value);
                attributes.put(keyValueToAttributeValue.get(attributeName), (String) value);
            }
            if (attributeName.equals(authenticationKey)) {
                LOG.debug("authentication key field found " + authenticationKey);
                LOG.debug("Matching " + (String) value + " to " + authenticationRegExpValue);
                authenticated = ((String) value).matches(authenticationRegExpValue);
                if (!authenticated) {
                    LOG.debug("Authentication not validated, clearing user parameters");
                    return null;
                }
                LOG.debug("Authentication validated");
            }
            if (attributeName.equals(userPrincipalKey)) {
                LOG.debug("user principal field found " + userPrincipalKey);
                userPrincipal = (String) value;
                LOG.debug("user principal set to " + userPrincipal);
            }
        }
        if (!authenticated) {
            LOG.debug("Not authenticated, clearing user parameters");
            return null;
        }
        if (userPrincipal == null) {
            LOG.error("Authenticated but not able to parse user principal, treating authentication as failed");
            return null;
        }
        return userPrincipal;
    }

    @Override
    public void setSatApiClient(SatApiClient client) {
        satApiClient = client;
    }

    @Override
    public UserDetails getDetails() {
        if (VaadinService.getCurrentRequest().getWrappedSession().getAttribute(PRINCIPAL_KEY) == null) {
            LOG.debug("User not authenticated, init authentication");
            updateUserInformation();
        }
        return (UserDetails) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(DETAILS_KEY);
    }

    

}
