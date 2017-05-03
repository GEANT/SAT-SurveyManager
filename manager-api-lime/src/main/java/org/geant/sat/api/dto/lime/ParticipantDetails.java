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
 * Details for one participant.
 */
public class ParticipantDetails extends ParticipantOverview {

    /** The flag for the invitation has been sent. */
    private String sent;
    
    /** The flag for the reminder has been sent. */
    @SerializedName("remindersent")
    private String reminderSent;
    
    /** The amount of reminders. */
    @SerializedName("remindercount")
    private int reminderCount;
    
    /** The amount of uses left. */
    @SerializedName("usesleft")
    private int usesLeft;
    
    /** The status of email. */
    @SerializedName("emailstatus")
    private String emailStatus;
    
    /** The participant's identifier. */
    @SerializedName("participant_id")
    private String participantId;
    
    /** The language of the participant. */
    private String language;
    
    /** The flag for being blacklisted. */
    @SerializedName("blacklisted")
    private String blackListed;
    
    /** The start timestamp. */
    @SerializedName("validfrom")
    private String validFrom;
    
    /** The end timestamp. */
    @SerializedName("validuntil")
    private String validUntil;
    
    /** Limesurvey mp identifier. */
    @SerializedName("mpid")
    private String mpId;
    
    /** The first name. */
    private String firstname;
    
    /** The last name. */
    private String lastname;
    
    /** The email address. */
    private String email;

    /**
     * Get the flag for the invitation has been sent.
     * @return The flag for the invitation has been sent.
     */
    public String getSent() {
        return sent;
    }

    /**
     * Set the flag for the invitation has been sent.
     * @param newSent What to set.
     */
    public void setSent(final String newSent) {
        this.sent = newSent;
    }

    /**
     * Get the flag for the reminder has been sent.
     * @return The flag for the reminder has been sent.
     */
    public String getReminderSent() {
        return reminderSent;
    }

    /**
     * Set the flag for the reminder has been sent.
     * @param sent What to set.
     */
    public void setReminderSent(String sent) {
        this.reminderSent = sent;
    }

    /**
     * Get the amount of reminders.
     * @return The amount of reminders.
     */
    public int getReminderCount() {
        return reminderCount;
    }

    /**
     * Set the amount of reminders.
     * @param count What to set.
     */
    public void setReminderCount(int count) {
        this.reminderCount = count;
    }

    /**
     * Get the amount of uses left.
     * @return The amount of uses left.
     */
    public int getUsesLeft() {
        return usesLeft;
    }

    /**
     * Set the amount of uses left.
     * @param uses What to set.
     */
    public void setUsesLeft(int uses) {
        this.usesLeft = uses;
    }

    /**
     * Get the email status.
     * @return The email status.
     */
    public String getEmailStatus() {
        return emailStatus;
    }

    /**
     * Set the email status.
     * @param status What to set.
     */
    public void setEmailStatus(String status) {
        this.emailStatus = status;
    }

    /**
     * Get the participant's identifier.
     * @return The participant's identifier.
     */
    public String getParticipantId() {
        return participantId;
    }

    /**
     * Set the participant's identifier.
     * @param id What to set.
     */
    public void setParticipantId(String id) {
        this.participantId = id;
    }

    /**
     * Get the flag for being blacklisted.
     * @return The flag for being blacklisted.
     */
    public String getBlackListed() {
        return blackListed;
    }

    /**
     * Set the flag for being blacklisted.
     * @param listed What to set.
     */
    public void setBlackListed(String listed) {
        this.blackListed = listed;
    }

    /**
     * Get the start timestamp.
     * @return The start timestamp.
     */
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * Set the start timestamp.
     * @param from What to set.
     */
    public void setValidFrom(String from) {
        this.validFrom = from;
    }

    /**
     * Get the end timestamp.
     * @return The end timestamp.
     */
    public String getValidUntil() {
        return validUntil;
    }

    /**
     * Set the end timestamp.
     * @param until What to set.
     */
    public void setValidUntil(String until) {
        this.validUntil = until;
    }

    /**
     * Get the Limesurvey mpId.
     * @return The Limesurvey mpId.
     */
    public String getMpId() {
        return mpId;
    }

    /**
     * Set the Limesurvey mpId.
     * @param id What to set.
     */
    public void setMpId(String id) {
        this.mpId = id;
    }

    /**
     * Get the first name.
     * @return The first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the first name.
     * @param name What to set.
     */
    public void setFirstname(final String name) {
        this.firstname = name;
    }

    /**
     * Get the last name,
     * @return The last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the last name.
     * @param surname What to set.
     */
    public void setLastname(final String surname) {
        this.lastname = surname;
    }

    /**
     * Get the email address.
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address.
     * @param mail What to set.
     */
    public void setEmail(final String mail) {
        this.email = mail;
    }

    /**
     * Get the participant's language.
     * @return The participant's language.
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * Set the participant's language.
     * @param lang What to set.
     */
    public void setLanguage(String lang) {
        language = lang;
    }
}
