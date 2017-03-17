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
 * The wrapper class for the Limesurvey permissions.
 */
public class LimePermission {

    /** The identifier. */
    private String id;
    
    /** The entity. */
    private String entity;
    
    /** The entity identifier. */
    @SerializedName("entity_id")
    private String entityId;
    
    /** The permission. */
    private String permission;
    
    /** The create permission. */
    @SerializedName("create_p")
    private boolean creator;
    
    /** The read permission. */
    @SerializedName("read_p")
    private boolean reader;
    
    /** The update permission. */
    @SerializedName("update_p")
    private boolean updator;
    
    /** The delete permission. */
    @SerializedName("delete_p")
    private boolean deletor;
    
    /** The import permission. */
    @SerializedName("import_p")
    private boolean importer;
    
    /** The export permission. */
    @SerializedName("export_p")
    private boolean exporter;

    /**
     * Get the identifier.
     * @return The identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the identifier.
     * @param newId What to set.
     */
    public void setId(String newId) {
        this.id = newId;
    }

    /** 
     * Get the entity.
     * @return The entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Set the entity.
     * @param newEntity What to set.
     */
    public void setEntity(String newEntity) {
        this.entity = newEntity;
    }

    /**
     * Get the entity identifier.
     * @return The entity identifier.
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Set the entity identifier.
     * @param newEntityId What to set.
     */
    public void setEntityId(String newEntityId) {
        this.entityId = newEntityId;
    }

    /**
     * Get the permission.
     * @return The permission.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission.
     * @param newPermission What to set.
     */
    public void setPermission(String newPermission) {
        this.permission = newPermission;
    }

    /**
     * Get whether creator privilege exists.
     * @return Whether creator privilege exists.
     */
    public boolean isCreator() {
        return creator;
    }

    /**
     * Set whether creator privilege exists.
     * @param newCreator What to set.
     */
    public void setCreator(boolean newCreator) {
        this.creator = newCreator;
    }

    /**
     * Get whether reader privilege exists.
     * @return Whether reader privilege exists.
     */
    public boolean isReader() {
        return reader;
    }

    /**
     * Set whether reader privilege exists.
     * @param newReader What to set.
     */
    public void setReader(boolean newReader) {
        this.reader = newReader;
    }

    /**
     * Get whether updator privilege exists.
     * @return Whether updator privilege exists.
     */
    public boolean isUpdator() {
        return updator;
    }

    /**
     * Set whether updator privilege exists.
     * @param newUpdator What to set.
     */
    public void setUpdator(boolean newUpdator) {
        this.updator = newUpdator;
    }

    /**
     * Get whether deletor privilege exists.
     * @return Whether deletor privilege exists.
     */
    public boolean isDeletor() {
        return deletor;
    }

    /**
     * Set whether deletor privilege exists.
     * @param newDeletor What to set.
     */
    public void setDeletor(boolean newDeletor) {
        this.deletor = newDeletor;
    }

    /**
     * Get whether importer privilege exists.
     * @return Whether importer privilege exists.
     */
    public boolean isImporter() {
        return importer;
    }

    /**
     * Set whether importer privilege exists.
     * @param newImporter What to set.
     */
    public void setImporter(boolean newImporter) {
        this.importer = newImporter;
    }

    /**
     * Get whether exporter privilege exists.
     * @return Whether exporter privilege exists.
     */
    public boolean isExporter() {
        return exporter;
    }

    /**
     * Set whether exporter privilege exists.
     * @param newExporter What to set.
     */
    public void setExporter(boolean newExporter) {
        this.exporter = newExporter;
    }
}
