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

package org.geant.sat.api.impl;

import java.util.List;
import java.util.Set;

import org.geant.sat.api.AssessorNotifier;
import org.geant.sat.api.AssessorNotifierException;
import org.geant.sat.api.SurveySystemConnector;
import org.geant.sat.api.SurveySystemConnectorException;
import org.geant.sat.api.UserDatabaseConnector;
import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.api.dto.TokenDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation for {@link AssessorNotifier}.
 */
public class AssessorNotifierImpl implements AssessorNotifier {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AssessorNotifierImpl.class);
    
    /** The user database connector. */
    private UserDatabaseConnector userDbConnector;
    
    /** The survey system connector. */
    private SurveySystemConnector surveyConnector;
    
    /**
     * Set the user database connector.
     * @param connector The user database connector.
     */
    public void setUserDatabaseConnector(final UserDatabaseConnector connector) {
        userDbConnector = connector;
    }
    
    /**
     * Set the survey system connector.
     * @param connector The survey system connector.
     */
    public void setSurveySystemConnector(final SurveySystemConnector connector) {
        surveyConnector = connector;
    }

    /** {@inheritDoc} */
    @Override
    public ListEntitiesResponse notifyInstantantion(final List<EntityDetails> entities, final String principalId) 
            throws AssessorNotifierException {
        final ListEntitiesResponse response = new ListEntitiesResponse();
        final StringBuilder errorMessage = new StringBuilder();
        for (final EntityDetails details : entities) {
            log.debug("Starting to instantiate entity {}", details.getId());
            final Set<String> sids = details.getSids();
            if (sids == null || sids.isEmpty()) {
                log.warn("Entity {} has no surveys attached", details.getId());
                errorMessage.append("Entity " + details.getId() + " has no surveys attached! ");
            }
            final List<AssessorDetails> assessors = details.getAssessors();
            if (assessors == null || assessors.isEmpty()) {
                log.warn("Entity {} has no assessors defined", details.getId());
                errorMessage.append("Entity " + details.getId() + " has no assessors defined! ");
            }
            int eventId = 0;
            for (final String sid : sids) {
                log.debug("Starting to instantiate survey {} for entity {}", sid, details.getId());
                for (final AssessorDetails assessor : assessors) {
                    if ("email".equals(assessor.getType())) {
                        log.debug("Sending invitation to {} with value {}", assessor.getId(), assessor.getValue());
                        try {
                            final String token = surveyConnector.generateToken(sid);
                            log.trace("eventId before the database operation is set to {}", eventId);
                            eventId = userDbConnector.addSurveyToken(token, details.getId(), assessor.getId(), 
                                    principalId, sid, eventId);
                            log.trace("eventId is now set to {}", eventId);
                        } catch (SurveySystemConnectorException e) {
                            log.error("Could not store a new token for survey {}", sid);
                            throw new AssessorNotifierException("Could not store a new token for survey " + sid, e);
                        }
                    } else {
                        log.warn("Could not send an invitation for type {}", assessor.getType());
                        errorMessage.append("Could not send an invitation for type " + assessor.getType() + " ");
                    }
                }
            }
            response.getEntities().add(details);
        }
        if (errorMessage.length() > 0) {
            response.setErrorMessage(errorMessage.toString().trim());
        }
        return response;
    }
}
