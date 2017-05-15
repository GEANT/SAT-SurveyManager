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

import java.util.ArrayList;
import java.util.List;

import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.ui.utils.EntityDetailsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

/** class implementing view to importing entities */
@SuppressWarnings({ "serial", "rawtypes" })
@DesignRoot
public class ImportEntityViewer extends AbstractSurveyVerticalLayout implements ClickListener, ValueChangeListener {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ImportEntityViewer.class);

    /** field to enter the import url. */
    private TextField metadataUrl;
    /** button for fetching import entity list from import url. */
    private Button fetchContent;
    /** ui component to select entities fetched from import url. */
    private ListSelect<EntityDetails> selectedEntity;
    /** List of entities to be selected. */
    private List<EntityDetails> entities;
    /** button to cancel import. */
    private Button cancelButton;
    /** button to import selection. */
    private Button importButton;
    /** field to filter entities by */
    private TextField entityFilter;

    /**
     * We use now hard coded identifier for saml. TODO replace with final
     * solution.
     */
    private static final String samlIdentifier = "saml2";

    /**
     * Constructor.
     * 
     * @param ui
     *            main ui class.
     */
    @SuppressWarnings("unchecked")
    ImportEntityViewer(MainUI ui) {
        super(ui);
        Design.read(this);
        metadataUrl.setCaption(getString("lang.importer.text.url"));
        fetchContent.setCaption(getString("lang.importer.button.fetch"));
        fetchContent.addClickListener(this);
        cancelButton.setCaption(getString("lang.importer.button.cancel"));
        cancelButton.addClickListener(this);
        importButton.setCaption(getString("lang.importer.button.import"));
        importButton.addClickListener(this);
        importButton.setEnabled(false);
        entityFilter.setCaption(getString("lang.importer.text.filter"));
        entityFilter.addValueChangeListener(this);
        entityFilter.setEnabled(false);
        selectedEntity.setCaption(getString("lang.importer.selection"));
        selectedEntity.setItemCaptionGenerator(new EntityDetailsHelper());
        selectedEntity.addValueChangeListener(this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == cancelButton) {
            ((Window) getParent()).close();
            return;
        }
        if (event.getButton() == fetchContent) {
            String url = metadataUrl.getValue();
            LOG.debug("About to import entities from url " + url);
            ListEntitiesResponse resp = getMainUI().getSatApiClient().previewEntities(samlIdentifier, url,
                    "imported_entity", getMainUI().getUser().getDetails().getPrincipalId());
            if (!verifySuccess(resp)) {
                return;
            }
            entities = resp.getEntities();
            selectedEntity.setItems(entities);
            entityFilter.setEnabled(entities.size() > 0);
            entityFilter.setValue("");
            return;
        }
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        if (event.getSource() == fetchContent) {
            importButton.setEnabled(selectedEntity.getSelectedItems().size() > 0);
            return;
        }
        if (event.getSource() == entityFilter) {
            if (entityFilter.getValue().trim().length() == 0) {
                selectedEntity.setItems(entities);
                return;
            }
            List<EntityDetails> filteredEntities = new ArrayList<EntityDetails>();
            LOG.debug("filtering by string {}", entityFilter.getValue().trim());
            for (EntityDetails details : entities) {
                if (details.getName().contains(entityFilter.getValue().trim())) {
                    filteredEntities.add(details);
                }
            }
            LOG.debug("filtering decreased entities from {} to {}", entities.size(), filteredEntities.size());
            selectedEntity.setItems(filteredEntities);
        }

    }

}
