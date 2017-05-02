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

/**
 * Contact information for one participant.
 */
public class ParticipantInfo {

    /** The first name. */
    private String firstname;
    
    /** The last name. */
    private String lastname;
    
    /** The email address. */
    private String email;

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
}
