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

/**
 * Constants and helpers reflecting the data model.
 */
public final class DataModelUtil {

    /** The table name for the user table. */
    public static final String TABLE_NAME_USER = "user";

    /** The table name for the attribute table. */
    public static final String TABLE_NAME_ATTRIBUTE = "attribute";

    /** The table name for the role table. */
    public static final String TABLE_NAME_ROLE = "role";

    /** The table name for the table containing users links to attributes. */
    public static final String TABLE_NAME_USER_ATTRIBUTE = "userAttribute";

    /** The table name for the table containing users links to roles. */
    public static final String TABLE_NAME_USER_ROLE = "userRole";
    
    /** The table name for the entity table. */
    public static final String TABLE_NAME_ENTITY = "entity";
    
    /** The table name for the assessor table. */
    public static final String TABLE_NAME_ASSESSOR = "assessor";
    
    /** The table name for the table containing assessor type definitions. */
    public static final String TABLE_NAME_ASSESSOR_TYPE = "assessorType";
    
    /** The table name for the table containing entities links to assessors. */
    public static final String TABLE_NAME_ENTITY_ASSESSOR = "entityAssessor";
    
    /** The table name for the table containing entities links to surveys. */
    public static final String TABLE_NAME_ENTITY_SURVEY = "entitySurvey";

    /** The column name for the start timestamp. */
    public static final String COLUMN_NAME_START = "start";

    /** The column name for the end timestamp. */
    public static final String COLUMN_NAME_END = "end";

    /** The column name for principal ID in the user table. */
    public static final String COLUMN_NAME_USER_PRINCIPAL_ID = "applicationPrincipal";

    /** The column name for survey principal ID in the user table. */
    public static final String COLUMN_NAME_USER_SURVEY_USER_ID = "surveyApplicationPrincipal";

    /** The column name for ID in the user table. */
    public static final String COLUMN_NAME_USER_ID = "id";
    
    /** The column name for ID in the attribute table. */
    public static final String COLUMN_NAME_ATTRIBUTE_ID = "id";

    /** The column name for name in the attribute table. */
    public static final String COLUMN_NAME_ATTRIBUTE_NAME = "name";

    /** The column name for description in the attribute table. */
    public static final String COLUMN_NAME_ATTRIBUTE_DESCRIPTION = "description";

    /** The column name for user ID in the user to attribute table. */
    public static final String COLUMN_NAME_USER_ATTRIBUTE_USER_ID = "userId";

    /** The column name for ID in the user to attribute table. */
    public static final String COLUMN_NAME_USER_ATTRIBUTE_ID = "id";

    /** The column name for attribute ID in the user to attribute table. */
    public static final String COLUMN_NAME_USER_ATTRIBUTE_ATTRIBUTE_ID = "attributeId";

    /** The column name for attribute value in the user to attribute table. */
    public static final String COLUMN_NAME_USER_ATTRIBUTE_VALUE = "value";

    /** The column name for user ID in the user to role table. */
    public static final String COLUMN_NAME_USER_ROLE_USER_ID = "userId";

    /** The column name for ID in the user to role table. */
    public static final String COLUMN_NAME_USER_ROLE_ID = "id";

    /** The column name for role ID in the user to role table. */
    public static final String COLUMN_NAME_USER_ROLE_ROLE_ID = "roleId";

    /** The column name for role ID in the role table. */
    public static final String COLUMN_NAME_ROLE_ID = "id";

    /** The column name for role name in the role table. */
    public static final String COLUMN_NAME_ROLE_NAME = "name";

    /** The column name for role description in the role table. */
    public static final String COLUMN_NAME_ROLE_DESCRIPTION = "description";

    /** The column name for entity id (unique) in the entity table. */
    public static final String COLUMN_NAME_ENTITY_ID = "id";
    
    /** The column name for entity name in the entity table. */
    public static final String COLUMN_NAME_ENTITY_NAME = "name";
    
    /** The column name for entity description in the entity table. */
    public static final String COLUMN_NAME_ENTITY_DESCRIPTION = "description";
    
    /** The column name for user id in the entity table. */
    public static final String COLUMN_NAME_ENTITY_USER_ID = "userId";

    /** The column name for assessor id in the assessor table. */
    public static final String COLUMN_NAME_ASSESSOR_ID = "id";

    /** The column name for assessor value in the assessor table. */
    public static final String COLUMN_NAME_ASSESSOR_VALUE = "value";
    
    /** The column name for assessor type id in the assessor table. */
    public static final String COLUMN_NAME_ASSESSOR_TYPEID = "assessorTypeId";
    
    /** The column name for description in the assessor table. */
    public static final String COLUMN_NAME_ASSESSOR_DESCRIPTION = "description";
    
    /** The column name for id in the assessor type table. */
    public static final String COLUMN_NAME_ASSESSOR_TYPE_ID = "id";

    /** The column name for type in the assessor type table. */
    public static final String COLUMN_NAME_ASSESSOR_TYPE_TYPE = "type";
    
    /** The column name for description in the assessor type table. */
    public static final String COLUMN_NAME_ASSESSOR_TYPE_DESCRIPTION = "description";
    
    /** The column name for entity id in the assessor to entity table. */
    public static final String COLUMN_NAME_ENTITY_ASSESSOR_ENTITY_ID = "entityId";
    
    /** The column name for assessor id in the assessor to entity table. */
    public static final String COLUMN_NAME_ENTITY_ASSESSOR_ASSESSOR_ID = "assessorId";
    
    /** The column name for entity id in the survey to entity table. */
    public static final String COLUMN_NAME_ENTITY_SURVEY_ENTITY_ID = "entityId";
    
    /** The column name for survey id in the survey to entity table. */
    public static final String COLUMN_NAME_ENTITY_SURVEY_SURVEY_ID = "surveyId";
    
    /** The internal (result) column name for attribute name. */
    public static final String INT_COLUMN_NAME_ATTRIBUTE_NAME = "attrName";

    /** The internal (result) column name for attribute value. */
    public static final String INT_COLUMN_NAME_ATTRIBUTE_VALUE = "attrValue";

    /** The internal (result) column name for role name. */
    public static final String INT_COLUMN_NAME_ROLE_NAME = "roleName";
    
    /** The internal (result) column_name for assessor ids. */
    public static final String INT_COLUMN_NAME_ASSESSOR_ID = "intAssessorId";
    
    /**
     * Constructor.
     */
    private DataModelUtil() {
        // no op
    }

    /**
     * Builds an SQL query clause for fetching all users from the database.
     * @return The SQL query clause.
     */
    public static String buildListUsersQuery() {
        final StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(TABLE_NAME_USER + "." + COLUMN_NAME_USER_PRINCIPAL_ID + " AS " + COLUMN_NAME_USER_PRINCIPAL_ID);
        sb.append(", ");
        sb.append(TABLE_NAME_ATTRIBUTE + ".name AS " + INT_COLUMN_NAME_ATTRIBUTE_NAME);
        sb.append(", ");
        sb.append(TABLE_NAME_USER_ATTRIBUTE + ".value AS " + INT_COLUMN_NAME_ATTRIBUTE_VALUE);
        sb.append(", ");
        sb.append(TABLE_NAME_ROLE + ".name AS " + INT_COLUMN_NAME_ROLE_NAME);
        sb.append(" FROM " + TABLE_NAME_USER + " LEFT OUTER JOIN " + TABLE_NAME_USER_ATTRIBUTE);
        sb.append(" ON " + TABLE_NAME_USER + ".id = " + TABLE_NAME_USER_ATTRIBUTE + ".userId");
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_ATTRIBUTE);
        sb.append(" ON " + TABLE_NAME_USER_ATTRIBUTE + ".attributeID = " + TABLE_NAME_ATTRIBUTE + ".id");
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_USER_ROLE);
        sb.append(" ON " + TABLE_NAME_USER + ".id = " + TABLE_NAME_USER_ROLE + ".userId");
        sb.append(" AND " + TABLE_NAME_USER_ROLE + "." + COLUMN_NAME_END + " IS NULL");
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_ROLE);
        sb.append(" ON " + TABLE_NAME_USER_ROLE + ".roleId = " + TABLE_NAME_ROLE + ".id");
        return sb.toString();
    }
    
    /**
     * Builds an SQL query clause for fetching all entities from the database.
     * @return The SQL query clause.
     */
    public static String buildEntitiesQuery() {
        final StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(TABLE_NAME_ENTITY + "." + COLUMN_NAME_ENTITY_ID + ", ");
        sb.append(TABLE_NAME_ENTITY + "." + COLUMN_NAME_ENTITY_NAME + ", ");
        sb.append(TABLE_NAME_ENTITY + "." + COLUMN_NAME_ENTITY_DESCRIPTION + ", ");
        sb.append(TABLE_NAME_USER + "." + COLUMN_NAME_USER_PRINCIPAL_ID + ", ");
        sb.append(TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_ASSESSOR_ID + " AS " + INT_COLUMN_NAME_ASSESSOR_ID + ", ");
        sb.append(TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_ASSESSOR_VALUE + ", ");
        sb.append(TABLE_NAME_ASSESSOR_TYPE + "." + COLUMN_NAME_ASSESSOR_TYPE_TYPE + ", ");
        sb.append(TABLE_NAME_ENTITY_SURVEY + "." + COLUMN_NAME_ENTITY_SURVEY_SURVEY_ID);
        sb.append(" FROM " + TABLE_NAME_ENTITY + " LEFT OUTER JOIN " + TABLE_NAME_USER);
        sb.append(" ON " + TABLE_NAME_ENTITY + "." + COLUMN_NAME_ENTITY_USER_ID + " = " + TABLE_NAME_USER + ".id");
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_ENTITY_ASSESSOR);
        sb.append(" ON " + TABLE_NAME_ENTITY + ".id = " + TABLE_NAME_ENTITY_ASSESSOR + "." + COLUMN_NAME_ENTITY_ASSESSOR_ENTITY_ID);
        sb.append(" AND " + TABLE_NAME_ENTITY_ASSESSOR + "." + COLUMN_NAME_END + " IS NULL");
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_ASSESSOR);
        sb.append(" ON " + TABLE_NAME_ASSESSOR + ".id = " + TABLE_NAME_ENTITY_ASSESSOR + "." + COLUMN_NAME_ENTITY_ASSESSOR_ASSESSOR_ID);
        sb.append(" AND " + TABLE_NAME_ENTITY_ASSESSOR + "." + COLUMN_NAME_END + " IS NULL");
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_ASSESSOR_TYPE);
        sb.append(" ON " + TABLE_NAME_ASSESSOR_TYPE + ".id = " + TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_ASSESSOR_TYPEID);
        sb.append(" LEFT OUTER JOIN " + TABLE_NAME_ENTITY_SURVEY);
        sb.append(" ON " + TABLE_NAME_ENTITY_SURVEY + "." + COLUMN_NAME_ENTITY_SURVEY_ENTITY_ID + " = " + TABLE_NAME_ENTITY + ".id");
        sb.append(" AND " + TABLE_NAME_ENTITY_SURVEY + "." + COLUMN_NAME_END + " IS NULL");
        sb.append(" WHERE " + TABLE_NAME_ENTITY + "." + COLUMN_NAME_END + " IS NULL");
        return sb.toString();
    }
    
    /**
     * Builds an SQL query clause for fetching entity details from the database.
     * @param id The entity id whose details are to be fetched.
     * @return The SQL query clause.
     */
    public static String buildEntitiesViaIdQuery(final String id) {
        return buildEntitiesQuery() + " AND " + TABLE_NAME_ENTITY + "." + COLUMN_NAME_ENTITY_ID + "=" + id;
    }
    
    /**
     * Builds an SQL query clause for fetching all assessors from the database.
     * @return The SQL query clause.
     */
    public static String buildAssessorsQuery() {
        final StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(TABLE_NAME_ASSESSOR + ".id, ");
        sb.append(TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_ASSESSOR_VALUE + ", ");
        sb.append(TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_ASSESSOR_DESCRIPTION + ", ");
        sb.append(TABLE_NAME_ASSESSOR_TYPE + "." + COLUMN_NAME_ASSESSOR_TYPE_TYPE);
        sb.append(" FROM " + TABLE_NAME_ASSESSOR + " LEFT OUTER JOIN " + TABLE_NAME_ASSESSOR_TYPE);
        sb.append(" ON " + TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_ASSESSOR_TYPEID + "=" + TABLE_NAME_ASSESSOR_TYPE + ".id");
        sb.append(" AND " + TABLE_NAME_ASSESSOR_TYPE + "." + COLUMN_NAME_END + " IS NULL");
        sb.append(" WHERE " + TABLE_NAME_ASSESSOR + "." + COLUMN_NAME_END + " IS NULL");
        return sb.toString();
    }
    
    /**
     * Builds an SQL query clause for fetching assessor details from the database.
     * @param id The assessor id whose details are to be fetched.
     * @return The SQL query clause.
     */
    public static String buildAssessorsViaIdQuery(final String id) {
        return buildEntitiesQuery() + " AND " + TABLE_NAME_ASSESSOR + ".id=" + id;
    }

    /**
     * Builds an SQL query clause for fetching all roles from the database.
     * @return The SQL query clause.
     */
    public static String buildListRolesQuery() {
        final StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(TABLE_NAME_ROLE + "." + COLUMN_NAME_ROLE_NAME);
        sb.append(", ");
        sb.append(TABLE_NAME_ROLE + "." + COLUMN_NAME_ROLE_DESCRIPTION);
        sb.append(" FROM " + TABLE_NAME_ROLE);
        return sb.toString();
    }
}
