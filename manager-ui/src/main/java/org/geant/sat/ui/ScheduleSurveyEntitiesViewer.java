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

package org.geant.sat.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geant.sat.api.dto.EntityDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.declarative.Design;

/** class implementing view to pick entities for survey scheduling. */
@SuppressWarnings({ "serial", "rawtypes" })
@DesignRoot
public class ScheduleSurveyEntitiesViewer extends AbstractSurveyVerticalLayout implements ValueChangeListener {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSurveyEntitiesViewer.class);

    private TwinColSelect<String> selectEntities;
    private List<EntityDetails> details;

    @SuppressWarnings("unchecked")
    ScheduleSurveyEntitiesViewer(MainUI ui, List<EntityDetails> selectedDetails) {
        super(ui);
        Design.read(this);
        LOG.debug("Selecting entities for survey scheduling");
        selectEntities.setCaption(getString("lang.scheduler.entities.picker.caption"));
        details = selectedDetails;
        Set<String> entities = new HashSet<String>();
        for (EntityDetails entityDetails : getFilteredEntityDetails()) {
            entities.add(entityDetails.getId() + ":" + entityDetails.getName());
        }
        selectEntities.setItems(entities);
        Set<String> selectedEntities = new HashSet<String>();
        for (EntityDetails entityDetails : selectedDetails) {
            selectedEntities.add(entityDetails.getId() + ":" + entityDetails.getName());
        }
        selectEntities.updateSelection(selectedEntities, new HashSet<String>());
        selectEntities.addValueChangeListener(this);

    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        // make sure each selected is recorded
        for (String selectedEntity : selectEntities.getSelectedItems()) {
            String id = selectedEntity.split(":")[0];
            boolean found = false;
            for (EntityDetails entityDetails : details) {
                if (entityDetails.getId().equals(id)) {
                    found = true;
                }
            }
            if (!found) {
                LOG.debug("add item " + id);
                for (EntityDetails entityDetails : getFilteredEntityDetails()) {
                    if (entityDetails.getId().equals(id)) {
                        details.add(entityDetails);
                        // there may have been at most one modification
                        return;
                    }
                }
            }
        }
        // make sure each recorded is still selected
        for (EntityDetails entityDetails : details) {
            boolean found = false;
            for (String selectedEntity : selectEntities.getSelectedItems()) {
                String id = selectedEntity.split(":")[0];
                if (entityDetails.getId().equals(id)) {
                    found = true;
                }
            }
            if (!found) {
                LOG.debug("remove item " + entityDetails.getId());
                details.remove(entityDetails);
                // there may have been at most one modification
                return;
            }
        }

    }

}
