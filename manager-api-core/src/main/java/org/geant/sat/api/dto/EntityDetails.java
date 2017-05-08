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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The details for an entity in the database.
 */
public class EntityDetails {
    
    /** The unique identifier for the entity. */
    private String id;
    
    /** The name of the entity. Doesn't have to be unique. */
    private String name;

    /** The description of the entity. */
    private String description;
    
    /** The creator of the entity. */
    private String creator;
    
    /** The assessors configured to this entity. */
    private List<AssessorDetails> assessors = new ArrayList<>();
    
    /** The survey identifiers configured to this entity. */
    private Set<String> sids = new HashSet<>();

    /**
     * Get the unique identifier for the entity.
     * @return The unique identifier for the entity.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the unique identifier for the entity.
     * @param newId What to set.
     */
    public void setId(final String newId) {
        id = newId;
    }
    
    /**
     * Get the name of the entity.
     * @return The name of the entity.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the entity.
     * @param newName What to set.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Get the description of the entity.
     * @return The description of the entity.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the entity.
     * @param newDescription What to set.
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Set the creator of the entity.
     * @return The creator of the entity.
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Set the creator of the entity.
     * @param newCreator What to set.
     */
    public void setCreator(String newCreator) {
        this.creator = newCreator;
    }

    /**
     * Get the assessors configured to this entity.
     * @return The assessors configured to this entity.
     */
    public List<AssessorDetails> getAssessors() {
        return assessors;
    }

    /**
     * Set the assessors configured to this entity.
     * @param newAssessors What to set.
     */
    public void setAssessors(List<AssessorDetails> newAssessors) {
        this.assessors = newAssessors;
    }

    /**
     * Get the survey identifiers configured to this entity.
     * @return The survey identifiers configured to this entity.
     */
    public Set<String> getSids() {
        return sids;
    }

    /**
     * Set the survey identifiers configured to this entity.
     * @param newSids What to set.
     */
    public void setSids(Set<String> newSids) {
        this.sids = newSids;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Entity (id: '" + getId() + "', name: '" + getName() + "', description: '" + getDescription()
            + "', creator: '" + getCreator() + "', assessors: " + getAssessors() + ", sids: " + getSids() + ").";
    }
}
