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
 * This class contains information for one instantiated survey status.
 */
public class SurveyStatusDetails {

    /** The answer details. */
    private AnswerDetails answers;
    
    /** The entity details. */
    private EntityDetails entity;
    
    /** The assessor details. */
    private AssessorDetails assessor;

    /**
     * Get the answer details.
     * @return The answers details.
     */
    public AnswerDetails getAnswers() {
        return answers;
    }
   
    /**
     * Set the answer details.
     * @param newAnswers What to set.
     */
    public void setAnswers(final AnswerDetails newAnswers) {
        answers = newAnswers;
    }
    
    /**
     * Get the entity details.
     * @return The entity details.
     */
    public EntityDetails getEntity() {
        return entity;
    }
    
    /**
     * Set the entity details.
     * @param newEntity What to set.
     */
    public void setEntity(final EntityDetails newEntity) {
        entity = newEntity;
    }
    
    /**
     * Get the assessor details.
     * @return The assessor details.
     */
    public AssessorDetails getAssessor() {
        return assessor;
    }
    
    /**
     * Set the assessor details.
     * @param newAssessor What to set.
     */
    public void setAssessor(final AssessorDetails newAssessor) {
        assessor = newAssessor;
    }
}
