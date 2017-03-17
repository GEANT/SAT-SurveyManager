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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

/**
 * A {@link ResultSetExtractor} which parses the user details and constructs a list of all users.
 */
public class UserDetailsExtractor implements ResultSetExtractor<ListUsersResponse> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(UserDetailsExtractor.class);

    /** {@inheritDoc} */
    @Override
    public ListUsersResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
        final ListUsersResponse response = new ListUsersResponse();
        while (rs.next()) {
            final String principalId = rs.getString(DataModelUtil.COLUMN_NAME_USER_PRINCIPAL_ID);
            log.debug("Processing principal {}", principalId);
            if (getUser(response, principalId) == null) {
                log.trace("Principal did not exist in the response yet");
                final UserDetails newDetails = new UserDetails();
                newDetails.setPrincipalId(principalId);
                response.getUsers().add(newDetails);
            } else {
                log.trace("Updating the existing principal in the response");
            }
            final UserDetails details = getUser(response, principalId);
            addAttribute(details, rs.getString(DataModelUtil.INT_COLUMN_NAME_ATTRIBUTE_NAME),
                    rs.getString(DataModelUtil.INT_COLUMN_NAME_ATTRIBUTE_VALUE));
            addRole(details, rs.getString(DataModelUtil.INT_COLUMN_NAME_ROLE_NAME));
        }
        return response;
    }

    /**
     * Adds an attribute and its value to the given user details.
     * @param details The user details to be updated.
     * @param attrName The attribute name.
     * @param attrValue The attribute value.
     */
    protected void addAttribute(final UserDetails details, final String attrName, final String attrValue) {
        if (!StringUtils.isEmpty(attrName) && !StringUtils.isEmpty(attrValue)) {
            log.debug("Adding an attribute {} with value {} for user {}", attrName, attrValue,
                    details.getPrincipalId());
            if (details.getAttributes() == null) {
                details.setAttributes(new HashMap<>());
                log.trace("Created a new map for the attributes");
            }
            details.getAttributes().put(attrName, attrValue);
        } else {
            log.debug("Attribute name or value was null, skipping it");
        }
    }

    /**
     * Adds a role to the given user details.
     * @param details The user details to be updated.
     * @param role The role to be added.
     */
    protected void addRole(final UserDetails details, final String role) {
        if (!StringUtils.isEmpty(role)) {
            if (details.getRoles() == null) {
                details.setRoles(new HashSet<>());
                log.trace("Created a new set for the roles");
            }
            log.debug("Adding a role {} for user {}", role, details.getPrincipalId());
            details.getRoles().add(role);
        } else {
            log.debug("Attribute name or value was null, skipping it");
        }
    }

    /**
     * Get one user's details from the list of users' details. 
     * @param response The response containing many users' details.
     * @param principalId The principal identifier to be fetched from the response.
     * @return The user details corresponding to the given principal identifier.
     */
    protected UserDetails getUser(final ListUsersResponse response, final String principalId) {
        final List<UserDetails> users = response.getUsers();
        if (users == null) {
            response.setUsers(new ArrayList<>());
            return null;
        }
        for (final UserDetails details : users) {
            if (details != null && principalId.equalsIgnoreCase(details.getPrincipalId())) {
                return details;
            }
        }
        return null;
    }
}
