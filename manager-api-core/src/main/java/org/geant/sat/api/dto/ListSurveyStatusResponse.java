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

import java.util.ArrayList;
import java.util.List;

/**
 * A response containing details for instantiated survey statuses.
 */
public class ListSurveyStatusResponse extends AbstractConnectorResponse {
    
    /** The details for questions. */
    private List<QuestionDetails> questions = new ArrayList<>();

    /** The details for instantiated survey status. */
    private List<SurveyStatusDetails> statuses = new ArrayList<>();

    /**
     * Get the details for questions.
     * @return The details for questions.
     */
    public List<QuestionDetails> getQuestions() {
        return questions;
    }

    /**
     * Set the details for questions.
     * @param newQuestions What to set.
     */
    public void setQuestions(List<QuestionDetails> newQuestions) {
        this.questions = newQuestions;
    }
    
    /**
     * Get the details for instantiated survey status.
     * @return The details for instantiated survey status.
     */
    public List<SurveyStatusDetails> getStatuses() {
        return statuses;
    }
    
    /**
     * Set the details for instantiated survey status.
     * @param details What to set.
     */
    public void setTokens(List<SurveyStatusDetails> details) {
        statuses = details;
    }
}
