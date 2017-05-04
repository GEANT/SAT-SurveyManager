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

/**
 * This class contains information for one token for a survey in the survey system.
 */
public class TokenDetails {
    
    /** The assessor (token user) identifier. */
    private String assessorId;
    
    /** The entity identifier. */
    private String entityId;
    
    /** The survey identifier. */
    private String surveyId;

    /** The creator of this token. */
    private String principalId;
    
    /** The token value. */
    private String token;
    
    /** The creation event identifier. */
    private int eventId;
    
    /** The flag for token being valid in the Survey system. */
    private boolean valid = false;
    
    /** The flag for the survey related to the token being completed in the Survey system. */
    private boolean completed = false;

    /**
     * Get the assessor (token user) identifier.
     * @return The assessor (token user) identifier.
     */
    public String getAssessorId() {
        return assessorId;
    }

    /**
     * Set the assessor (token user) identifier.
     * @param id What to set.
     */
    public void setAssessorId(String id) {
        this.assessorId = id;
    }

    /**
     * Get the entity identifier.
     * @return The entity identifier.
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Set the entity identifier.
     * @param id What to set.
     */
    public void setEntityId(String id) {
        this.entityId = id;
    }

    /**
     * Get the survey identifier.
     * @return The survey identifier.
     */
    public String getSurveyId() {
        return surveyId;
    }

    /**
     * Set the survey identifier.
     * @param id What to set.
     */
    public void setSurveyId(String id) {
        this.surveyId = id;
    }

    /**
     * Get the creator of this token.
     * @return The creator of this token.
     */
    public String getPrincipalId() {
        return principalId;
    }

    /**
     * Set the creator of this token.
     * @param id What to set.
     */
    public void setPrincipalId(String id) {
        this.principalId = id;
    }

    /**
     * Get the token value.
     * @return The token value.
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the token value.
     * @param value What to set.
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Get the flag for token being valid in the Survey system.
     * @return The flag for token being valid in the Survey system.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Set the flag for token being valid in the Survey system.
     * @param validity What to set.
     */
    public void setValid(boolean validity) {
        this.valid = validity;
    }

    /**
     * Get the flag for the survey related to the token being completed in the Survey system.
     * @return The flag for the survey related to the token being completed in the Survey system.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Set the flag for the survey related to the token being completed in the Survey system.
     * @param completion What to set.
     */
    public void setCompleted(boolean completion) {
        this.completed = completion;
    }

    /**
     * Get the creation event identifier.
     * @return The creation event identifier.
     */
    public int getEventId() {
        return eventId;
    }
    
    /**
     * Set the creation event identifier.
     * @param id What to set.
     */
    public void setEventId(int id) {
        eventId = id;
    }
}
