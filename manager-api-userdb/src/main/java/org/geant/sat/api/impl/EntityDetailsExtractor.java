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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * A {@link ResultSetExtractor} which parses the entity details and constructs a list of all entities.
 */
public class EntityDetailsExtractor implements ResultSetExtractor<ListEntitiesResponse> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(EntityDetailsExtractor.class);
    
    /** {@inheritDoc} */
    @Override
    public ListEntitiesResponse extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final ListEntitiesResponse response = new ListEntitiesResponse();
        while (rs.next()) {
            final String name = rs.getString(DataModelUtil.COLUMN_NAME_ENTITY_NAME);
            log.debug("Found an entity {} from the result set.", name);
            final String description = rs.getString(DataModelUtil.COLUMN_NAME_ENTITY_DESCRIPTION);
            final String creator = rs.getString(DataModelUtil.COLUMN_NAME_USER_PRINCIPAL_ID);
            final String assessorValue = rs.getString(DataModelUtil.COLUMN_NAME_ASSESSOR_VALUE);
            final String assessorType = rs.getString(DataModelUtil.COLUMN_NAME_ASSESSOR_TYPE_TYPE);
            final String surveyId = rs.getString(DataModelUtil.COLUMN_NAME_ENTITY_SURVEY_SURVEY_ID);
            final EntityDetails details = getExistingOrCreateNew(response, name);
            details.setDescription(description);
            details.setCreator(creator);
            details.getAssessors().put(assessorType, assessorValue);
            details.getSids().add(surveyId);
        }
        return response;
    }
    
    /**
     * Get the existing entity details from the response if it was found with the same name, or creates a new one
     * if it wasn't.
     * @param response The response to be searched from.
     * @param name The entity name to be searched.
     * @return The existing or new entity details.
     */
    protected EntityDetails getExistingOrCreateNew(final ListEntitiesResponse response, final String name) {
        for (final EntityDetails details : response.getEntities()) {
            if (name.equals(details.getName())) {
                log.debug("Found existing details for {} from the response", name);
                return details;
            }
        }
        final EntityDetails details = new EntityDetails();
        details.setName(name);
        response.getEntities().add(details);
        return details;
    }
}
