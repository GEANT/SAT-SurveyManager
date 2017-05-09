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
 * The information details for an {@link EntityImporter}.
 */
public class EntityImporterDetails {

    /** The unique identifier for the importer. */
    private String id;
    
    /** The human-readable description for the importer. */
    private String importerDescription;
    
    /** The human-readable description for the input. */
    private String inputDescription;
    
    /**
     * Get the unique identifier for the importer.
     * @return The unique identifier for the importer.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the unique identifier for the importer.
     * @param value What to set.
     */
    public void setId(final String value) {
        id = value;
    }
    
    /**
     * Get the human-readable description for the importer.
     * @return The human-readable description for the importer.
     */
    public String getImporterDescription() {
        return importerDescription;
    }
    
    /**
     * Set the human-readable description for the importer.
     * @param description What to set.
     */
    public void setImporterDescription(final String description) {
        importerDescription = description;
    }
    
    /**
     * Get the human-readable description for the input.
     * @return The human-readable description for the input.
     */
    public String getInputDescription() {
        return inputDescription;
    }
    
    /**
     * Set the human-readable description for the input.
     * @param description What to set.
     */
    public void setInputDescription(final String description) {
        inputDescription = description;
    }
}
