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

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.api.dto.ListRolesResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.RoleDetails;
import org.geant.sat.api.dto.ListTokensResponse;
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
    
    /**
     * Create a new entity to the database.
     * @param name The name of the entity.
     * @param description The description of the entity.
     * @param creator The creator of the entity.
     * @return The entity details.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public EntityDetails createNewEntity(final String name, final String description, final String creator)
            throws SurveySystemConnectorException;
    
    /**
     * Updates an existing entity with the given details.
     * @param entity The entity details.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public void updateEntityDetails(final EntityDetails entity) throws SurveySystemConnectorException;

    /**
     * Updates an existing assessor with the given details.
     * @param entity The assessor details.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public void updateAssessorDetails(final AssessorDetails assessor) throws SurveySystemConnectorException;

    /**
     * List all the assessors in the database.
     * @return All the assessors in the database.
     */
    public ListAssessorsResponse listAssessors();

    /**
     * Create a new assessor to the database.
     * @param type The type of the assessor.
     * @param value The value of the assessor (corresponding to the type).
     * @param description The description for the assessor.
     * @return The details for the assessor.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public AssessorDetails createNewAssessor(final String type, final String value, final String description)
            throws SurveySystemConnectorException;
    
    /**
     * Adds a new survey token to the database.
     * @param token The token to be added.
     * @param entityId The entity identifier.
     * @param assessorId The assessor identifier.
     * @param principalId The principal identifier for the one instantiating the operation.
     * @param sid The survey identifier.
     * @param eventId The event identifier related to this operation. If set to 0, new is created and returned by
     * this method.
     * @return The event identifier related to this operation.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public int addSurveyToken(final String token, final String entityId, final String assessorId, 
            final String principalId, final String sid, final int eventId) throws SurveySystemConnectorException;
    
    /**
     * List all survey tokens for the given survey.
     * @param sid The survey identifier.
     * @return All the survey tokens for the given survey.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public ListTokensResponse listSurveyTokens(final String sid) throws SurveySystemConnectorException;

}
