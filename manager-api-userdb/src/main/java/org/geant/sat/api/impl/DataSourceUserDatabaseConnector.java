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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.geant.sat.api.SurveySystemConnectorException;
import org.geant.sat.api.UserDatabaseConnector;
import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.api.dto.ListRolesResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.RoleDetails;
import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

/**
 * The user database connector implementation exploiting {@link DataSource}.
 */
public class DataSourceUserDatabaseConnector implements UserDatabaseConnector {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(DataSourceUserDatabaseConnector.class);

    /** The Spring ${link JdbcTemplate} exploiting the {@link DataSource}. */
    private JdbcTemplate jdbcTemplate;

    /**
     * Set the {@link DataSource} for this connector.
     * 
     * @param dataSource
     *            What to set.
     */
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** {@inheritDoc} */
    @Override
    public ListUsersResponse listUsers() {
        log.debug("Fetching all users from the database");
        final String query = DataModelUtil.buildListUsersQuery() + ";";
        log.trace("Built query {}", query);
        final ListUsersResponse response = jdbcTemplate.query(query, new UserDetailsExtractor());
        return response;
    }

    /** {@inheritDoc} */
    @Override
    public ListRolesResponse listRoles() {
        log.debug("Fetching all roles from the database");
        final String query = DataModelUtil.buildListRolesQuery() + ";";
        log.trace("Built query {}", query);
        return jdbcTemplate.query(query, new RoleDetailsExtractor());
    }
    
    /** {@inheritDoc} */
    @Override
    public ListEntitiesResponse listEntities() {
        log.debug("Fetching all entities from the database");
        final String query = DataModelUtil.buildEntitiesQuery() + ";";
        log.trace("Built query {}", query);
        return jdbcTemplate.query(query, new EntityDetailsExtractor());
    }
    
    /** {@inheritDoc} */
    @Override
    public ListAssessorsResponse listAssessors() {
        log.debug("Fetching all assessors from the database");
        final String query = DataModelUtil.buildAssessorsQuery() + ";";
        log.trace("Built query {}", query);
        return jdbcTemplate.query(query, new AssessorDetailsExtractor());
    }    
    
    /** {@inheritDoc} */
    @Override
    public synchronized EntityDetails createNewEntity(final String name, final String description, 
            final String creator) throws SurveySystemConnectorException {
        log.debug("Creating a new entity with name {}", name);
        final Long userId = getUserId(creator);
        log.debug("Found userId {} for {}", userId, creator);
        final Long id = getEntityId(name, userId);
        if (id != null) {
            log.error("The entity {} already exists with id {}", name, id);
            throw new SurveySystemConnectorException("The entity already exists for the same user");
        }
        final String update = "INSERT INTO " + DataModelUtil.TABLE_NAME_ENTITY + " ("
                + DataModelUtil.COLUMN_NAME_ENTITY_NAME + ", "
                + DataModelUtil.COLUMN_NAME_ENTITY_DESCRIPTION + ", "
                + DataModelUtil.COLUMN_NAME_ENTITY_USER_ID + ") VALUES (?, ?, ?);";
        final Object[] params = new Object[] { name, description, userId };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.BIGINT };
        int rowId = jdbcTemplate.update(update, params, types);
        if (rowId < 0) {
            log.error("Obtained rowId {} after the SQL operation", rowId);
            throw new SurveySystemConnectorException("Unexpected response from the database");
        }
        final String query = DataModelUtil.buildEntitiesQuery() + " AND " + DataModelUtil.TABLE_NAME_ENTITY + "." 
                + DataModelUtil.COLUMN_NAME_ENTITY_ID + "=" + getEntityId(name, userId);
        log.trace("Built query {}", query);
        final ListEntitiesResponse response = jdbcTemplate.query(query, new EntityDetailsExtractor());
        return response.getEntities().get(0);
    }
    
    /** {@inheritDoc} */
    @Override
    public synchronized void updateEntityDetails(final EntityDetails entity) throws SurveySystemConnectorException {
        final String id = entity.getId();
        log.debug("Updating an existing entity with id {}", id);
        final EntityDetails storedDetails = getStoredEntityDetails(id);
        if (storedDetails == null) {
            throw new SurveySystemConnectorException("Could not find existing entity with id " + id);
        }
        if (!entity.getName().equals(storedDetails.getName())
                || !entity.getDescription().equals(storedDetails.getDescription())) {
            log.debug("Updating the entity table");
            final String update = "UPDATE " + DataModelUtil.TABLE_NAME_ENTITY + " SET "
                    + DataModelUtil.COLUMN_NAME_ENTITY_NAME + "=?, " 
                    + DataModelUtil.COLUMN_NAME_ENTITY_DESCRIPTION + "=? WHERE " 
                    + DataModelUtil.COLUMN_NAME_ENTITY_ID + "=?";
            final Object[] params = new Object[] { entity.getName(), entity.getDescription(), id };
            int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
            int rowId = jdbcTemplate.update(update, params, types);
            if (rowId < 1) {
                log.error("Could not update entity details for {} with clause {}", id, update);
                throw new SurveySystemConnectorException("Could not update entity details for " + id);
            }
        }
        final Set<String> storedSids = storedDetails.getSids();
        final Set<String> sids = entity.getSids();
        for (final String sid : storedDetails.getSids()) {
            if (!sids.contains(sid)) {
                log.debug("Invalidating survey ID {} for entity {}", sid, id);
                final String update = "UPDATE " + DataModelUtil.TABLE_NAME_ENTITY_SURVEY + " SET "
                        + DataModelUtil.COLUMN_NAME_END + "=? WHERE " 
                        + DataModelUtil.COLUMN_NAME_ENTITY_SURVEY_ENTITY_ID + "=? AND " 
                        + DataModelUtil.COLUMN_NAME_ENTITY_SURVEY_SURVEY_ID + "=?";
                final Object[] params = new Object[] { new Timestamp(System.currentTimeMillis()), id, sid };
                int[] types = new int[] { Types.TIMESTAMP, Types.BIGINT, Types.BIGINT };
                int rowId = jdbcTemplate.update(update, params, types);
                if (rowId < 1) {
                    log.error("Could not update entity survey details for {} with clause {}", id, update);
                    throw new SurveySystemConnectorException("Could not update entity survey details for " + id);
                }             
            }
        }
        for (final String sid : sids) {
            if (storedSids.contains(sid)) {
                log.debug("Adding survey ID {} for entity {}", sid, id);
                final String update = "INSERT INTO " + DataModelUtil.TABLE_NAME_ENTITY_SURVEY + " ("
                        + DataModelUtil.COLUMN_NAME_ENTITY_SURVEY_ENTITY_ID + ", " 
                        + DataModelUtil.COLUMN_NAME_ENTITY_SURVEY_SURVEY_ID + ") VALUES (?,?)";
                final Object[] params = new Object[] { id, sid };
                int[] types = new int[] { Types.BIGINT, Types.VARCHAR };
                int rowId = jdbcTemplate.update(update, params, types);
                if (rowId < 1) {
                    log.error("Could not add entity survey details for {} with clause {}", id, update);
                    throw new SurveySystemConnectorException("Could not add entity survey details for " + id);
                }             
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void updateAssessorDetails(final AssessorDetails assessor) 
            throws SurveySystemConnectorException {
        final String id = assessor.getId();
        log.debug("Updating an existing assessor with id {}", id);
        final AssessorDetails storedDetails = getStoredAssessorDetails(id);
        if (storedDetails == null) {
            throw new SurveySystemConnectorException("Could not find existing assessor with id " + id);
        }
        if (!assessor.getValue().equals(storedDetails.getValue()) 
                || !assessor.getDescription().equals(storedDetails.getDescription())) {
            final String update = "UPDATE " + DataModelUtil.TABLE_NAME_ASSESSOR + " SET "
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_VALUE + "=?, " 
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_DESCRIPTION + "=? WHERE " 
                    + DataModelUtil.COLUMN_NAME_ASSESSOR_ID + "=?";
            final Object[] params = new Object[] { assessor.getValue(), assessor.getDescription(), id };
            int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
            int rowId = jdbcTemplate.update(update, params, types);
            if (rowId < 1) {
                log.error("Could not update assessor details for {} with clause {}", id, update);
                throw new SurveySystemConnectorException("Could not update assessor details for " + id);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public AssessorDetails createNewAssessor(final String type, final String value, final String description)
            throws SurveySystemConnectorException {
        log.debug("Creating a new assessor type {} with value {}", type, value);
        final Long typeId = getAssessorTypeId(type);
        if (typeId == null) {
            log.error("The assessor type {} does not exist in the database!", type);
            throw new SurveySystemConnectorException("The assessor type " + type + " does not exist in the database");
        }
        final Long assessorId = getAssessorId(value, typeId);
        if (assessorId != null) {
            log.error("The assessor with type {} and value {} already exists!", type, value);
            throw new SurveySystemConnectorException("The assessor with the same type and value already exists");
        }
        final String update = "INSERT INTO " + DataModelUtil.TABLE_NAME_ASSESSOR + " ("
                + DataModelUtil.COLUMN_NAME_ASSESSOR_TYPEID + ", "
                + DataModelUtil.COLUMN_NAME_ASSESSOR_VALUE + ", "
                + DataModelUtil.COLUMN_NAME_ASSESSOR_DESCRIPTION + ") VALUES (?, ?, ?)";
        final Object[] params = new Object[] { typeId, value, description };
        int[] types = new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR };
        int rowId = jdbcTemplate.update(update, params, types);
        if (rowId < 0) {
            log.error("Obtained rowId {} after the SQL operation", rowId);
            throw new SurveySystemConnectorException("Unexpected response from the database");
        }
        final String query = DataModelUtil.buildEntitiesQuery() + " AND " + DataModelUtil.TABLE_NAME_ENTITY + "." 
                + DataModelUtil.COLUMN_NAME_ASSESSOR_ID + "=" + getAssessorId(value, typeId);
        log.trace("Built query {}", query);
        final ListAssessorsResponse response = jdbcTemplate.query(query, new AssessorDetailsExtractor());
        return response.getAssessors().get(0);
    }

    /** {@inheritDoc} */
    @Override
    public UserDetails getUserDetails(final String principalId) {
        log.debug("Fetching user details for {}", principalId);
        final String query = DataModelUtil.buildListUsersQuery() + " WHERE "
                + DataModelUtil.COLUMN_NAME_USER_PRINCIPAL_ID + "='" + principalId + "';";
        log.trace("Built query {}", query);
        final ListUsersResponse response = jdbcTemplate.query(query, new UserDetailsExtractor());
        final List<UserDetails> users = response.getUsers();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public RoleDetails getRoleDetails(final String name) {
        log.debug("Fetching role details for {}", name);
        final String query = DataModelUtil.buildListRolesQuery() + " WHERE " + DataModelUtil.COLUMN_NAME_ROLE_NAME
                + "='" + name + "';";
        log.trace("Built query {}", query);
        final ListRolesResponse response = jdbcTemplate.query(query, new RoleDetailsExtractor());
        final List<RoleDetails> roles = response.getRoles();
        if (roles != null && !roles.isEmpty()) {
            return roles.get(0);
        }
        return null;
    }

    /**
     * Updates the user details in the database. A new one is created if no
     * existing user exists.
     * @param details The user details to be updated/created.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public synchronized void updateUserDetails(final UserDetails details) 
            throws SurveySystemConnectorException {
        final String principalId = details.getPrincipalId();
        log.debug("Updating user details for principal {}", principalId);
        if (StringUtils.isEmpty(principalId)) {
            log.warn("principalId was missing from the input for updateUserDetails");
            throw new SurveySystemConnectorException("Missing principalId");
        }
        if (getUserId(principalId) == null) {
            log.debug("The user {} doesn't exist, creating a new one", principalId);
            int row = createNewUser(details);
            if (row < 1) {
                log.error("Unexpected row number ({}) returned for the new user", row);
                throw new SurveySystemConnectorException("Error while creating a new user");
            }
        }
        final Long userId = getUserId(principalId);
        final Map<String, String> attributes = details.getAttributes();
        if (attributes != null) {
            final Map<Long, String> mapped = new HashMap<>();
            for (final String key : attributes.keySet()) {
                mapped.put(getAttributeId(key), attributes.get(key));
            }
            for (final Long key : mapped.keySet()) {
                String update = "INSERT INTO " + DataModelUtil.TABLE_NAME_USER_ATTRIBUTE + " ("
                        + DataModelUtil.COLUMN_NAME_USER_ATTRIBUTE_USER_ID + ", "
                        + DataModelUtil.COLUMN_NAME_USER_ATTRIBUTE_ATTRIBUTE_ID + ", "
                        + DataModelUtil.COLUMN_NAME_USER_ATTRIBUTE_VALUE + ") VALUES (?, ?, ?);";
                Object[] params = new Object[] { userId, key, mapped.get(key) };
                int[] types = new int[] { Types.BIGINT, Types.BIGINT, Types.VARCHAR };
                // TODO: should update, not always insert
                int rowId = jdbcTemplate.update(update, params, types);
                if (rowId < 0) {
                    log.error("Obtained rowId {} after the SQL operation", rowId);
                    throw new SurveySystemConnectorException("Unexpected response from the database");
                }
            }
        }
        final Set<String> roles = details.getRoles();
        final Set<String> storedRoles = getUserDetails(principalId).getRoles();
        updateRoles(userId, roles, storedRoles);
    }

    /**
     * Update the roles for the given user.
     * @param userId The user identifier.
     * @param roles The set of roles to be stored.
     * @param storedRoles The set of roles already stored.
     */
    protected synchronized void updateRoles(final Long userId, final Set<String> roles, final Set<String> storedRoles) {
        final Set<Long> toBeAdded = new HashSet<>();
        final Set<Long> toBeRemoved = new HashSet<>();
        for (final String role : storedRoles) {
            if (!roles.contains(role)) {
                toBeRemoved.add(getRoleId(role));
            }
        }
        for (final String role : roles) {
            if (!storedRoles.contains(role)) {
                toBeAdded.add(getRoleId(role));
            }
        }
        for (final Long roleId : toBeAdded) {
            final String update = "INSERT INTO " + DataModelUtil.TABLE_NAME_USER_ROLE + " ("
                    + DataModelUtil.COLUMN_NAME_USER_ROLE_USER_ID + ", " + DataModelUtil.COLUMN_NAME_USER_ROLE_ROLE_ID
                    + ") VALUES (?, ?);";
            final Object[] params = new Object[] { userId, roleId };
            int[] types = new int[] { Types.BIGINT, Types.BIGINT };
            int rowId = jdbcTemplate.update(update, params, types);
            if (rowId < 1) {
                log.error("Could not store the role for user {} with clause {}", userId, update);
            }
        }
        for (final Long roleId : toBeRemoved) {
            final String update = "UPDATE " + DataModelUtil.TABLE_NAME_USER_ROLE + " SET "
                    + DataModelUtil.COLUMN_NAME_END + "=? WHERE " + DataModelUtil.COLUMN_NAME_USER_ROLE_ROLE_ID
                    + "=? AND " + DataModelUtil.COLUMN_NAME_USER_ROLE_USER_ID + "=?;";
            final Object[] params = new Object[] { new Timestamp(System.currentTimeMillis()), roleId, userId };
            int[] types = new int[] { Types.TIMESTAMP, Types.BIGINT, Types.BIGINT };
            int rowId = jdbcTemplate.update(update, params, types);
            if (rowId < 1) {
                log.error("Could not invalidate the role for user {} with clause {}", userId, update);
            }
        }
    }

    /**
     * Updates role details, or create them if it didn't exist beforehand.
     * @param details The details for the role.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    public synchronized void updateRoleDetails(final RoleDetails details) throws SurveySystemConnectorException {
        final String name = details.getName();
        final String description = details.getDescription();
        final Long roleId = getExistingRoleId(name);
        if (roleId == null) {
            // create a new one
            if (createNewRole(name, description) == null) {
                throw new SurveySystemConnectorException("Could not create the role " + name);
            }
        } else {
            // update existing one
            final String update = "UPDATE " + DataModelUtil.TABLE_NAME_ROLE + " SET "
                    + DataModelUtil.COLUMN_NAME_ROLE_DESCRIPTION + "=? WHERE " + DataModelUtil.COLUMN_NAME_ROLE_ID
                    + "=?;";
            final Object[] params;
            if (description == null) {
                params = new Object[] { "", roleId };
            } else {
                params = new Object[] { description, roleId };
            }
            int[] types = new int[] { Types.VARCHAR, Types.BIGINT };
            int rowId = jdbcTemplate.update(update, params, types);
            if (rowId < 1) {
                throw new SurveySystemConnectorException("Could not update the role " + name);
            }
        }
    }

    /**
     * Get the user's database ID via the principal identifier.
     * @param principalId The principal identifier for the user.
     * @return The user's database ID.
     */
    protected Long getUserId(final String principalId) {
        final String query = "SELECT " + DataModelUtil.COLUMN_NAME_USER_ID + " FROM user WHERE "
                + DataModelUtil.COLUMN_NAME_USER_PRINCIPAL_ID + "='" + principalId + "'";
        log.trace("Created a query {}", query);
        return jdbcTemplate.query(query, new IdExtractor(DataModelUtil.COLUMN_NAME_USER_ID));
    }
    
    /**
     * Get the assessor type's database ID via the type name.
     * @param assessorType The assessor type name.
     * @return The assessor type's database ID.
     */
    protected Long getAssessorTypeId(final String assessorType) {
        final String query = "SELECT " + DataModelUtil.COLUMN_NAME_ASSESSOR_TYPE_ID + " FROM "
                + DataModelUtil.TABLE_NAME_ASSESSOR_TYPE + " WHERE " + DataModelUtil.COLUMN_NAME_ASSESSOR_TYPE_TYPE
                + "='" + assessorType + "'";
        log.trace("Created a query {}", query);
        return jdbcTemplate.query(query, new IdExtractor(DataModelUtil.COLUMN_NAME_ASSESSOR_TYPE_ID));
    }
    
    /**
     * Get the assessor's database ID via the type ID and value.
     * @param value The assessor value.
     * @param assessorTypeId The assessor type's database ID.
     * @return The assessor's database ID.
     */
    protected Long getAssessorId(final String value, final Long assessorTypeId) {
        final String query = "SELECT " + DataModelUtil.COLUMN_NAME_ASSESSOR_ID + " FROM "
                + DataModelUtil.TABLE_NAME_ASSESSOR + " WHERE " + DataModelUtil.COLUMN_NAME_ASSESSOR_TYPEID
                + "=" + assessorTypeId + " AND " + DataModelUtil.COLUMN_NAME_ASSESSOR_VALUE + "='" + value + "'";
        log.trace("Created a query {}", query);
        return jdbcTemplate.query(query, new IdExtractor(DataModelUtil.COLUMN_NAME_ASSESSOR_ID));
    }
    
    /**
     * Get the entity's database ID via the entity name and user ID.
     * @param name The name of the entity.
     * @param userId The creator of the entity.
     * @return The entity's database ID.
     */
    protected Long getEntityId(final String name, final Long userId) {
        final String query = "SELECT " + DataModelUtil.COLUMN_NAME_ENTITY_ID + " FROM "
                + DataModelUtil.TABLE_NAME_ENTITY + " WHERE " + DataModelUtil.COLUMN_NAME_ENTITY_NAME + "='"
                + name + "' AND " + DataModelUtil.COLUMN_NAME_ENTITY_USER_ID + "=" + userId + " AND "
                + DataModelUtil.COLUMN_NAME_END + " IS NULL";
        log.trace("Created a query {}", query);
        return jdbcTemplate.query(query, new IdExtractor(DataModelUtil.COLUMN_NAME_ENTITY_ID));
    }

    /**
     * Get the entity details from the database.
     * @param id The database ID for the entity details.
     * @return The entity details, or null if it couldn't be fetched.
     */
    protected EntityDetails getStoredEntityDetails(final String id) {
        final String query = DataModelUtil.buildEntitiesViaIdQuery(id);
        log.trace("Created a query {}", query);
        final ListEntitiesResponse response = jdbcTemplate.query(query, new EntityDetailsExtractor());
        if (response.getErrorMessage() != null || response.getEntities().isEmpty()) {
            log.error("Unexpected response or the entity query {}", query);
            return null;
        }
        return response.getEntities().get(0);
    }
    
    /**
     * Get the assessor details from the database.
     * @param id The database ID for the entity details.
     * @return The entity details, or null if it couldn't be fetched.
     */
    protected AssessorDetails getStoredAssessorDetails(final String id) {
        final String query = DataModelUtil.buildAssessorsViaIdQuery(id);
        log.trace("Created a query {}", query);
        final ListAssessorsResponse response = jdbcTemplate.query(query, new AssessorDetailsExtractor());
        if (response.getErrorMessage() != null || response.getAssessors().isEmpty()) {
            log.error("Unexpected response or the assessor query {}", query);
            return null;
        }
        return response.getAssessors().get(0);
    }

    /**
     * Get the attribute's database ID via its name. A new attribute is created if it didn't exist.
     * @param attribute The attribute name.
     * @return The attribute's database ID.
     */
    protected synchronized Long getAttributeId(final String attribute) {
        final Long existing = getExistingAttributeId(attribute);
        if (existing == null) {
            return createNewAttribute(attribute, null);
        }
        return existing;
    }

    /**
     * Create a new attribute to the database.
     * @param attribute The attribute name.
     * @param description The attribute description.
     * @return The attribute's database ID.
     */
    protected synchronized Long createNewAttribute(final String attribute, final String description) {
        final String update = "INSERT INTO " + DataModelUtil.TABLE_NAME_ATTRIBUTE + " ("
                + DataModelUtil.COLUMN_NAME_ATTRIBUTE_NAME + ", " + DataModelUtil.COLUMN_NAME_ATTRIBUTE_DESCRIPTION
                + ") VALUES (?,?)";
        final Object[] params;
        if (description == null) {
            params = new Object[] { attribute, "" };
        } else {
            params = new Object[] { attribute, description };
        }
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
        int rowId = jdbcTemplate.update(update, params, types);
        if (rowId < 1) {
            return null;
        }
        return getExistingAttributeId(attribute);
    }

    /**
     * Get the database ID for the attribute.
     * @param attribute The attribute name.
     * @return The attribute's database ID.
     */
    protected Long getExistingAttributeId(final String attribute) {
        final String query = "SELECT " + DataModelUtil.COLUMN_NAME_ATTRIBUTE_ID + " FROM "
                + DataModelUtil.TABLE_NAME_ATTRIBUTE + " WHERE " + DataModelUtil.COLUMN_NAME_ATTRIBUTE_NAME + "='"
                + attribute + "';";
        return jdbcTemplate.query(query, new IdExtractor(DataModelUtil.COLUMN_NAME_ATTRIBUTE_ID));
    }

    /**
     * Creates a new user to the database.
     * @param details The details for the user.
     * @return The row ID for the created user.
     */
    protected int createNewUser(final UserDetails details) {
        final String template = "INSERT INTO " + DataModelUtil.TABLE_NAME_USER + " ("
                + DataModelUtil.COLUMN_NAME_USER_PRINCIPAL_ID + ", " + DataModelUtil.COLUMN_NAME_USER_SURVEY_USER_ID
                + ") VALUES (?,?);";
        Object[] params = new Object[] { details.getPrincipalId(), "" };
        // survey principal id is left empty
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
        return jdbcTemplate.update(template, params, types);
    }

    /**
     * Gets the database ID for the given role. New one is created if the role is not yet found.
     * @param role The role name.
     * @return The database ID corresponding to the given role.
     */
    protected synchronized Long getRoleId(final String role) {
        final Long existing = getExistingRoleId(role);
        if (existing == null) {
            return createNewRole(role, null);
        }
        return existing;
    }

    /**
     * Create a new role to the database.
     * @param role The role name.
     * @param description The role description.
     * @return The role's database ID.
     */
    protected synchronized Long createNewRole(final String role, final String description) {
        final String template = "INSERT INTO " + DataModelUtil.TABLE_NAME_ROLE + " ("
                + DataModelUtil.COLUMN_NAME_ROLE_NAME + ", " + DataModelUtil.COLUMN_NAME_ROLE_DESCRIPTION
                + ") VALUES (?,?)";
        final Object[] params;
        if (description == null) {
            params = new Object[] { role, "" };
        } else {
            params = new Object[] { role, description };
        }
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
        int rowId = jdbcTemplate.update(template, params, types);
        if (rowId < 1) {
            return null;
        }
        return getExistingRoleId(role);
    }

    /**
     * Get the database ID for existing role.
     * @param role The role name.
     * @return The database ID corresponding to the role.
     */
    protected Long getExistingRoleId(final String role) {
        final String query = "SELECT " + DataModelUtil.COLUMN_NAME_ROLE_ID + " FROM " + DataModelUtil.TABLE_NAME_ROLE
                + " WHERE " + DataModelUtil.COLUMN_NAME_ROLE_NAME + "='" + role + "' AND "
                + DataModelUtil.COLUMN_NAME_END + " IS NULL;";
        return jdbcTemplate.query(query, new IdExtractor(DataModelUtil.COLUMN_NAME_ROLE_ID));
    }

    /**
     * Simple {@link ResultSetExtractor} which returns the value of the given column name.
     */
    private class IdExtractor implements ResultSetExtractor<Long> {

        /** The column name whose value is extracted. */
        private String columnName;

        /**
         * Constructor.
         * @param column The column name whose value is extracted.
         */
        public IdExtractor(final String column) {
            columnName = column;
        }

        /** {@inheritDoc} */
        @Override
        public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return rs.getLong(columnName);
            } else {
                log.debug("Could not find any results");
                return null;
            }
        }

    }
}
