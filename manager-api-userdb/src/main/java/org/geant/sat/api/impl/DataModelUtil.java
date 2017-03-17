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

    /** The internal (result) column name for attribute name. */
    public static final String INT_COLUMN_NAME_ATTRIBUTE_NAME = "attrName";

    /** The internal (result) column name for attribute value. */
    public static final String INT_COLUMN_NAME_ATTRIBUTE_VALUE = "attrValue";

    /** The internal (result) column name for role name. */
    public static final String INT_COLUMN_NAME_ROLE_NAME = "roleName";
    
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
