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

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * A {@link ResultSetExtractor} which parses the assessor details and constructs a list of all assessors.
 */
public class AssessorDetailsExtractor implements ResultSetExtractor<ListAssessorsResponse> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AssessorDetailsExtractor.class);
    
    /** {@inheritDoc} */
    @Override
    public ListAssessorsResponse extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final ListAssessorsResponse response = new ListAssessorsResponse();
        while (rs.next()) {
            final String id = rs.getString(DataModelUtil.COLUMN_NAME_ASSESSOR_ID);
            log.debug("Found an assessor {} from the result set.", id);
            final String value = rs.getString(DataModelUtil.COLUMN_NAME_ASSESSOR_VALUE);
            final String description = rs.getString(DataModelUtil.COLUMN_NAME_ASSESSOR_DESCRIPTION);
            final String type = rs.getString(DataModelUtil.COLUMN_NAME_ASSESSOR_TYPE_TYPE);
            final AssessorDetails details = getExistingOrCreateNew(response, id);
            details.setValue(value);
            details.setDescription(description);
            details.setType(type);
        }
        return response;
    }
    
    /**
     * Get the existing assessor details from the response if it was found with the same name, or creates a new one
     * if it wasn't.
     * @param response The response to be searched from.
     * @param id The entity name to be searched.
     * @return The existing or new assessor details.
     */
    protected AssessorDetails getExistingOrCreateNew(final ListAssessorsResponse response, final String id) {
        for (final AssessorDetails details : response.getAssessors()) {
            if (id.equals(details.getId())) {
                log.debug("Found existing details for {} from the response", id);
                return details;
            }
        }
        final AssessorDetails details = new AssessorDetails();
        details.setId(id);
        response.getAssessors().add(details);
        return details;
    }
}
