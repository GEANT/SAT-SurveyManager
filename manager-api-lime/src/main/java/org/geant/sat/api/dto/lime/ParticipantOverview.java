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
 * Overview for one participant.
 */
public class ParticipantOverview {
    
    /** The token identifier. */
    private String tid;
    
    /** The token value. */
    private String token;
    
    /** The flag for the completion of the survey. */
    private String completed;
    
    /** The contact information. */
    @SerializedName("participant_info")
    private ParticipantInfo participantInfo;

    /**
     * Get the token identifier.
     * @return The token identifier.
     */
    public String getTid() {
        return tid;
    }

    /**
     * Set the token identifier.
     * @param id What to set.
     */
    public void setTid(final String id) {
        this.tid = id;
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
    public void setToken(final String value) {
        this.token = value;
    }

    /**
     * Get the contact information.
     * @return The contact information.
     */
    public ParticipantInfo getParticipantInfo() {
        return participantInfo;
    }

    /**
     * Set the contact information.
     * @param info What to set.
     */
    public void setParticipantInfo(final ParticipantInfo info) {
        this.participantInfo = info;
    }

    /**
     * Get the flag for the completion of the survey.
     * @return The flag for the completion of the survey.
     */
    public String getCompleted() {
        return completed;
    }

    /**
     * Set the flag for the completion of the survey.
     * @param isCompleted What to set.
     */
    public void setCompleted(String isCompleted) {
        this.completed = isCompleted;
    }

}
