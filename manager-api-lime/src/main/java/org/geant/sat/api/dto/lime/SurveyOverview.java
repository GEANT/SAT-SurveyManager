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
package org.geant.sat.api.dto.lime;

import com.google.gson.annotations.SerializedName;

/**
 * Wrapper class for overview about survey.
 */
public class SurveyOverview {

    /** The survey identifier. */
    private String sid;

    /** The survey title. */
    @SerializedName("surveyls_title")
    private String title;

    /** Whether the survey is active. */
    private String active;
    
    /** The expiration timestamp (yyyy-mm-dd hh:MM:ss). */
    private String expires;

    /**
     * Get the survey identifier.
     * @return The survey identifier.
     */
    public String getSid() {
        return sid;
    }

    /**
     * Set the survey identifier.
     * @param newSid What to set.
     */
    public void setSid(String newSid) {
        this.sid = newSid;
    }

    /**
     * Get the survey title.
     * @return The survey title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the survey title.
     * @param newTitle What to set.
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Get whether the survey is active.
     * @return Whether the survey is active.
     */
    public String getActive() {
        return active;
    }

    /**
     * Set whether the survey is active.
     * @param newActive What to set.
     */
    public void setActive(String newActive) {
        this.active = newActive;
    }
    
    /**
     * Get the expiration timestamp.
     * @return The expiration timestamp.
     */
    public String getExpires() {
        return expires;
    }
    
    /**
     * Set the expiration timestamp.
     * @param expiration What to set.
     */
    public void setExpires(final String expiration) {
        expires = expiration;
    }
}
