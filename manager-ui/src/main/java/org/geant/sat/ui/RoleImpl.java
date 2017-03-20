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

import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class implementing roles. */
public class RoleImpl implements Role {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(RoleImpl.class);

    /** Name of the admin role. */
    private String adminRole = "admin";
    /** Name of the survey owner role. */
    private String surveyOwnerRole = "survey_owner";
    /** Name of the assesment coordinator role. */
    private String assessmentCoordinatorRole = "assessment_coordinator";

    /**
     * Set the name of the admin role.
     * 
     * @param name
     *            admin role name
     */
    public void setAdminRoleName(String name) {
        this.adminRole = name;
    }

    /**
     * Set the name of the survey owner role.
     * 
     * @param name
     *            survey owner role name
     */
    public void setSurveyOwnerRoleName(String name) {
        this.surveyOwnerRole = name;
    }

    /**
     * Set the name of the asssessment coordinator role.
     * 
     * @param name
     *            asssessment coordinator role name
     */
    public void setAssessmentCoordinatorRoleName(String name) {
        this.assessmentCoordinatorRole = name;
    }

    /**
     * Check if the user has a specific role defined.
     * 
     * @param role
     *            to be checked
     * @param details
     *            user to check the role for
     * @return true if user has a specific role, false otherwise
     */
    private boolean checkRole(String role, UserDetails details) {
        if (details == null) {
            LOG.warn("User has no details");
            return false;
        }
        if (details.getRoles() == null) {
            LOG.debug("User has no roles");
            return false;
        }
        if (role == null) {
            LOG.warn("role name not set");
            return false;
        }
        return details.getRoles().contains(role);
    }

    @Override
    public String getAdminRoleName() {
        return adminRole;
    }

    @Override
    public String getSurveyOwnerRoleName() {
        return surveyOwnerRole;
    }

    @Override
    public String getAssessmentCoordinatorRoleName() {
        return assessmentCoordinatorRole;
    }

    @Override
    public boolean isAdmin(UserDetails details) {
        LOG.debug("Resolving role " + adminRole);
        return checkRole(adminRole, details);
    }

    @Override
    public boolean isSurveyOwner(UserDetails details) {
        LOG.debug("Resolving role " + surveyOwnerRole);
        return checkRole(surveyOwnerRole, details);
    }

    @Override
    public boolean isAssessmentCoordinator(UserDetails details) {
        LOG.debug("Resolving role " + assessmentCoordinatorRole);
        return checkRole(assessmentCoordinatorRole, details);
    }

}
