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

import java.util.Map;

/**
 * The details for an answer in the survey system.
 */
public class AnswerDetails {

    /** The identifier. */
    private String id;

    /** The submission date. */
    private String submitDate;

    /** The language. */
    private String startLanguage;

    /** The token used for answering. */
    private String token;

    /** The starting date of the answering process. */
    private String startDate;

    /** All the answers. */
    private Map<String, String> answers;

    /**
     * Get the identifier.
     * @return The identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the identifier.
     * @param newId What to set.
     */
    public void setId(String newId) {
        this.id = newId;
    }

    /**
     * Get the submission date.
     * @return The submission date.
     */
    public String getSubmitDate() {
        return submitDate;
    }

    /**
     * Set the submission date.
     * @param newSubmitDate What to set.
     */
    public void setSubmitDate(String newSubmitDate) {
        this.submitDate = newSubmitDate;
    }

    /**
     * Get the language.
     * @return The language.
     */
    public String getStartLanguage() {
        return startLanguage;
    }

    /**
     * Set the language.
     * @param newStartLanguage What to set.
     */
    public void setStartLanguage(String newStartLanguage) {
        this.startLanguage = newStartLanguage;
    }

    /**
     * Get the token used for answering.
     * @return The token used for answering.
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the token used for answering.
     * @param newToken What to set.
     */
    public void setToken(String newToken) {
        this.token = newToken;
    }

    /**
     * Get the start date for answering.
     * @return The start date for answering.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set the start date for answering.
     * @param newStartDate What to set.
     */
    public void setStartDate(String newStartDate) {
        this.startDate = newStartDate;
    }

    /**
     * Get all the answers.
     * @return All the answers.
     */
    public Map<String, String> getAnswers() {
        return answers;
    }

    /**
     * Set all the answers.
     * @param newAnswers What to set.
     */
    public void setAnswers(Map<String, String> newAnswers) {
        this.answers = newAnswers;
    }
}
