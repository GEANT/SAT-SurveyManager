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
 * This class contains information for one survey token.
 */
public class SurveyTokenDetails {

    /** The survey identifier. */
    private String surveyId;
    
    /** The token for the survey. */
    private String token;
    
    /** The timestamp when the survey was completed, or 'N' if it has not. */
    private String completed;

    /**
     * Get the survey identifier.
     * @return The survey identifier.
     */
    public String getSurveyId() {
        return surveyId;
    }

    /**
     * Set the survey identifier.
     * @param sid What to set.
     */
    public void setSurveyId(String sid) {
        this.surveyId = sid;
    }

    /**
     * Get the token for the survey.
     * @return The token for the survey.
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the token for the survey.
     * @param value What to set.
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Get the timestamp when the survey was completed, or 'N' if it has not.
     * @return The timestamp when the survey was completed, or 'N' if it has not.
     */
    public String getCompleted() {
        return completed;
    }

    /**
     * Set the timestamp when the survey was completed, or 'N' if it has not.
     * @param complete The timestamp when the survey was completed, or 'N' if it has not.
     */
    public void setCompleted(String complete) {
        this.completed = complete;
    }
    
    
}
