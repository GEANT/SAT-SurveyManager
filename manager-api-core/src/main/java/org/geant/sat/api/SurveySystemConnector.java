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
package org.geant.sat.api;

import org.geant.sat.api.dto.AnswersResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.QuestionsResponse;
import org.geant.sat.api.dto.ListAllSurveysResponse;

/**
 * An interface for the connectors communicating with the survey system.
 */
public interface SurveySystemConnector {

    /**
     * List all surveys in the survey system.
     * @return All the surveys in the survey system.
     * @throws SurveySystemConnectorException In the case of any errors.
     */
    public ListAllSurveysResponse listSurveys() throws SurveySystemConnectorException;

    /**
     * List all questions in the survey system for one survey.
     * @param sid The survey identifier.
     * @return All the questions in the survey system for one survey.
     * @throws SurveySystemConnectorException In the case of any errors.
     */
    public QuestionsResponse listQuestions(final String sid) throws SurveySystemConnectorException;

    /**
     * List all the answers in the survey system for one survey.
     * @param sid The survey identifier.
     * @return All the answers in the survey system for one survey.
     * @throws SurveySystemConnectorException In the case of any errors.
     */
    public AnswersResponse listAnswers(final String sid) throws SurveySystemConnectorException;

    /**
     * List all the users in the survey system.
     * @return All the users in the survey system.
     */
    public ListUsersResponse listUsers();

}