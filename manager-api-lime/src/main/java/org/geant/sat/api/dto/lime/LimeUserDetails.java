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
package org.geant.sat.api.dto.lime;

import com.google.gson.annotations.SerializedName;

/**
 * Wrapper class for Limesurvey user details.
 */
public class LimeUserDetails {

    /** The user identifier. */
    private String uid;

    /** The username. */
    @SerializedName("users_name")
    private String username;

    /** The full name for the user. */
    @SerializedName("full_name")
    private String fullName;

    /** The parent identifier for the user. */
    @SerializedName("parent_id")
    private String parentId;

    /** The email address for the user. */
    private String email;

    /** The HTML editor mode for the user. */
    @SerializedName("htmleditormode")
    private String htmlEditorMode;

    /** The template editor mode for the user. */
    @SerializedName("templateeditormode")
    private String templateEditorMode;

    /** The question selector mode for the user. */
    @SerializedName("questionselectormode")
    private String questionSelectorMode;

    /** The one time password for the user. */
    @SerializedName("one_time_pw")
    private String otp;

    /** The preferred date format for the user. */
    @SerializedName("dateformat")
    private String dateFormat;

    /** The creation timestamp for the user. */
    private String created;

    /** The last modification timestamp for the user. */
    private String modified;

    /** The language for the user. */
    private String language;

    /** The set of permissions for the user. */
    private LimePermission[] permissions;

    /**
     * Get the user identifier.
     * @return The user identifier.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Set the user identifier.
     * @param newUid What to set.
     */
    public void setUid(String newUid) {
        this.uid = newUid;
    }

    /**
     * Get the username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     * @param newUsername What to set.
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Get the full name for the user.
     * @return The full name for the user.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set the full name for the user.
     * @param newFullName What to set.
     */
    public void setFullName(String newFullName) {
        this.fullName = newFullName;
    }

    /**
     * Get the parent identifier for the user.
     * @return The parent identifier for the user.
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Set the parent identifier for the user.
     * @param newParentId What to set.
     */
    public void setParentId(String newParentId) {
        this.parentId = newParentId;
    }

    /**
     * Get the email address for the user.
     * @return The email address for the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address for the user.
     * @param newEmail What to set.
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Get the HTML editor mode for the user.
     * @return The HTML editor mode for the user.
     */
    public String getHtmlEditorMode() {
        return htmlEditorMode;
    }

    /**
     * Set the HTML editor mode for the user.
     * @param newHtmlEditorMode What to set.
     */
    public void setHtmlEditorMode(String newHtmlEditorMode) {
        this.htmlEditorMode = newHtmlEditorMode;
    }

    /**
     * Get the template editor mode for the user.
     * @return The template editor mode for the user.
     */
    public String getTemplateEditorMode() {
        return templateEditorMode;
    }

    /**
     * Set the template editor mode for the user.
     * @param newTemplateEditorMode What to set.
     */
    public void setTemplateEditorMode(String newTemplateEditorMode) {
        this.templateEditorMode = newTemplateEditorMode;
    }

    /**
     * Get the question selector mode for the user.
     * @return The question selector mode for the user.
     */
    public String getQuestionSelectorMode() {
        return questionSelectorMode;
    }

    /**
     * Set the question selector mode for the user.
     * @param newQuestionSelectorMode What to set.
     */
    public void setQuestionSelectorMode(String newQuestionSelectorMode) {
        this.questionSelectorMode = newQuestionSelectorMode;
    }

    /**
     * Get the one time password for the user.
     * @return The one time password for the user.
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Set the one time password for the user.
     * @param newOtp What to set.
     */
    public void setOtp(String newOtp) {
        this.otp = newOtp;
    }

    /**
     * Get the preferred date format for the user.
     * @return The preferred date format for the user.
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Set the preferred date format for the user.
     * @param newDateFormat What to set.
     */
    public void setDateFormat(String newDateFormat) {
        this.dateFormat = newDateFormat;
    }

    /**
     * Get the preferred date format for the user.
     * @return The preferred date format for the user.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Set the preferred date format for the user.
     * @param newCreated What to set.
     */
    public void setCreated(String newCreated) {
        this.created = newCreated;
    }

    /**
     * Get the last modification timestamp for the user.
     * @return The last modification timestamp for the user.
     */
    public String getModified() {
        return modified;
    }

    /**
     * Set the last modification timestamp for the user.
     * @param newModified What to set.
     */
    public void setModified(String newModified) {
        this.modified = newModified;
    }

    /**
     * Get the language for the user.
     * @return The language for the user.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language for the user.
     * @param newLanguage What to set.
     */
    public void setLanguage(String newLanguage) {
        this.language = newLanguage;
    }

    /**
     * Get the set of permissions for the user.
     * @return The set of permissions for the user.
     */
    public LimePermission[] getPermissions() {
        return permissions;
    }

    /**
     * Set the set of permissions for the user.
     * @param newPermissions What to set.
     */
    public void setPermissions(LimePermission[] newPermissions) {
        this.permissions = newPermissions;
    }
}
