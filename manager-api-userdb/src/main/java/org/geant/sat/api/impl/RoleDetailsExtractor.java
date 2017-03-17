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
import java.util.ArrayList;

import org.geant.sat.api.dto.ListRolesResponse;
import org.geant.sat.api.dto.RoleDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * A {@link ResultSetExtractor} which parses the role details and constructs a list of all roles.
 */
public class RoleDetailsExtractor implements ResultSetExtractor<ListRolesResponse> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(RoleDetailsExtractor.class);

    /** {@inheritDoc} */
    @Override
    public ListRolesResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
        final ListRolesResponse response = new ListRolesResponse();
        while (rs.next()) {
            final String name = rs.getString(DataModelUtil.COLUMN_NAME_ROLE_NAME);
            final String description = rs.getString(DataModelUtil.COLUMN_NAME_ROLE_DESCRIPTION);
            log.debug("Found role name {} with description {}", name, description);
            addRole(response, name, description);
        }
        return response;
    }

    /**
     * Adds one role to the list of roles.
     * @param response The response containing all the roles.
     * @param name The role name to be added.
     * @param description The role description to be added.
     */
    protected void addRole(final ListRolesResponse response, final String name, final String description) {
        if (response.getRoles() == null) {
            response.setRoles(new ArrayList<>());
        }
        final RoleDetails details = new RoleDetails();
        details.setName(name);
        details.setDescription(description);
        response.getRoles().add(details);
    }
}
