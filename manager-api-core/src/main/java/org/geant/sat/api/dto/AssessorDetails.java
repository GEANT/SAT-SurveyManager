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

package org.geant.sat.api.dto;

/**
 * The details for an assessor in the database.
 */
public class AssessorDetails {

    /** The unique identifier for the assessor. */
    private String id;

    /** The assessor value. */
    private String value;
    
    /** The assessor description. */
    private String description;
    
    /** The assessor type. */
    private String type;
    
    /** The assessor type description. */
    private String typeDescription;

    /**
     * Get the unique identifier for the assessor.
     * @return The unique identifier for the assessor.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the unique identifier for the assessor.
     * @param newId What to set.
     */
    public void setId(final String newId) {
        id = newId;
    }

    /**
     * Get the assessor value.
     * @return The assessor value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the assessor value.
     * @param newValue What to set.
     */
    public void setValue(String newValue) {
        this.value = newValue;
    }

    /**
     * Get the assessor description.
     * @return The assessor description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the assessor description.
     * @param newDescription What to set.
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Get the assessor type.
     * @return The assessor type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the assessor type.
     * @param newType What to set.
     */
    public void setType(String newType) {
        this.type = newType;
    }

    /**
     * Get the assessor type description.
     * @return The assessor type description.
     */
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * Set the assessor type description.
     * @param newTypeDescription What to set.
     */
    public void setTypeDescription(String newTypeDescription) {
        this.typeDescription = newTypeDescription;
    }
    
    
    
}
