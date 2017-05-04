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

import org.geant.sat.api.dto.ListTokensResponse;
import org.geant.sat.api.dto.TokenDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * A {@link ResultSetExtractor} which parses the survey token details and constructs a list of all tokens.
 */
public class TokenDetailsExtractor implements ResultSetExtractor<ListTokensResponse> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AssessorDetailsExtractor.class);
    
    /** {@inheritDoc} */
    @Override
    public ListTokensResponse extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final ListTokensResponse response = new ListTokensResponse();
        while (rs.next()) {
            final String id = rs.getString(DataModelUtil.TABLE_NAME_ASSESSOR_TOKEN + ".id");
            log.debug("Found a token id={} from the result set.", id);
            final String entityId = rs.getString(DataModelUtil.TABLE_NAME_ASSESSOR_TOKEN + "." 
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_SURVEY_ENTITY_ID);
            final String assessorId = rs.getString(DataModelUtil.TABLE_NAME_ASSESSOR_TOKEN + "." 
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_SURVEY_ASSESSOR_ID);
            final int eventId = rs.getInt(DataModelUtil.TABLE_NAME_ASSESSOR_TOKEN + "." 
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_SURVEY_EVENT_ID);
            final String token = rs.getString(DataModelUtil.TABLE_NAME_ASSESSOR_TOKEN + "." 
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_SURVEY_TOKEN);
            final String userId = rs.getString(DataModelUtil.TABLE_NAME_USER + "." 
                    + DataModelUtil.COLUMN_NAME_USER_PRINCIPAL_ID);
            final TokenDetails details = new TokenDetails();
            details.setAssessorId(assessorId);
            details.setEntityId(entityId);
            details.setEventId(eventId);
            details.setToken(token);
            details.setPrincipalId(userId);
            response.getTokens().add(details);
        }
        return response;
    }
}
