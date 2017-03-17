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
package org.geant.sat.api.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class contains information for a user in the Survey Manager database.
 */
public class UserDetails {

    /** The principal identifier. */
    private String principalId;

    /** The principal identifier in the survey system. */
    private String surveyPrincipalId;

    /** The attributes for the user. */
    private Map<String, String> attributes = new HashMap<>();

    /** *The roles for the user. */
    private Set<String> roles = new HashSet<>();

    /**
     * Get the principal identifier.
     * @return The principal identifier.
     */
    public String getPrincipalId() {
        return principalId;
    }

    /**
     * Set the principal identifier.
     * @param id What to set.
     */
    public void setPrincipalId(final String id) {
        this.principalId = id;
    }

    /**
     * Get the principal identifier in the survey system.
     * @return The principal identifier in the survey system.
     */
    public String getSurveyPrincipalId() {
        return surveyPrincipalId;
    }

    /**
     * Set the principal identifier in the survey system.
     * @param id What to set.
     */
    public void setSurveyPrincipalId(final String id) {
        this.surveyPrincipalId = id;
    }

    /**
     * Get the user attributes.
     * @return The user attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Set the user attributes.
     * @param newAttributes What to set.
     */
    public void setAttributes(final Map<String, String> newAttributes) {
        this.attributes = newAttributes;
    }

    /** 
     * Get the user roles.
     * @return The user roles.
     */
    public Set<String> getRoles() {
        return roles;
    }

    /** 
     * Set the user roles.
     * @param newRoles What to set.
     */
    public void setRoles(final Set<String> newRoles) {
        this.roles = newRoles;
    }

}
