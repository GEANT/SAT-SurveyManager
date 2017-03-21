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

import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.api.dto.ListRolesResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.RoleDetails;
import org.geant.sat.api.dto.UserDetails;

/**
 * An interface for the connectors communicating with the Survey Manager user database.
 */
public interface UserDatabaseConnector {

    /**
     * List all the users in the database.
     * @return All the users in the database.
     */
    public ListUsersResponse listUsers();

    /**
     * Get the details for one user in the database.
     * @param principalId The user identifier.
     * @return The details for the given user.
     */
    public UserDetails getUserDetails(final String principalId);

    /**
     * Updates an existing, or creates a new user with the given details. 
     * @param user The user details.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public void updateUserDetails(final UserDetails user) throws SurveySystemConnectorException;

    /**
     * List all the roles in the database.
     * @return All the roles in the database.
     */
    public ListRolesResponse listRoles();

    /**
     * Get the details for one role in the database.
     * @param roleName The role name.
     * @return The details for the given role.
     */
    public RoleDetails getRoleDetails(final String roleName);

    /**
     * Updates an existing, or creates a new role with the given details.
     * @param role The role details.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public void updateRoleDetails(final RoleDetails role) throws SurveySystemConnectorException;
    
    /**
     * List all the entities in the database.
     * @return All the entities in the database.
     */
    public ListEntitiesResponse listEntities();
}
