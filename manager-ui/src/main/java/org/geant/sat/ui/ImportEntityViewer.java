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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.ui.utils.AssessorDetailsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

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
    private Grid<EntityDetails> availableEntities;
    /** List of entities to be selected. */
    private List<EntityDetails> availableEntitiesSource;
    /** button to cancel import. */
    private Button cancelButton;
    /** button to import. */
    private Button importButton;
    /** button to select from entities for import. */
    private Button addToBasketButton;
    /** button to open view of entities to import. */
    private Button viewBasketButton;
    /** field to filter entities by */
    private TextField availableEntitiesFilter;

    /** Grid showing entities. */
    private Object basketEntities;
    /** handle for helper window. */
    private Object basketWindow;

    /** List of selected entities. */
    Set<EntityDetails> entitiesSelection = new HashSet<EntityDetails>();

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
        // Initialize buttons and text fields
        metadataUrl.setCaption(getString("lang.importer.text.url"));
        fetchContent.setCaption(getString("lang.importer.button.fetch"));
        fetchContent.addClickListener(this);
        cancelButton.setCaption(getString("lang.importer.button.cancel"));
        cancelButton.addClickListener(this);
        importButton.setCaption(getString("lang.importer.button.import"));
        importButton.addClickListener(this);
        importButton.setEnabled(false);
        addToBasketButton.setCaption(getString("lang.importer.button.selectforimport"));
        addToBasketButton.addClickListener(this);
        addToBasketButton.setEnabled(false);
        viewBasketButton.setVisible(false);
        viewBasketButton.addStyleName(ValoTheme.BUTTON_LINK);
        viewBasketButton.addClickListener(this);
        availableEntitiesFilter.setCaption(getString("lang.importer.text.filter"));
        availableEntitiesFilter.addValueChangeListener(this);
        availableEntitiesFilter.setEnabled(false);
        // Initialize entity selection grid
        availableEntities.setCaption(getString("lang.importer.selection.available"));
        availableEntities.setSelectionMode(SelectionMode.MULTI);
        availableEntities.addSelectionListener(event -> {
            addToBasketButton.setEnabled(event.getAllSelectedItems().size() > 0);
        });
        availableEntities.addColumn(EntityDetails::getName).setCaption(getString("lang.entities.column.name"));
        availableEntities.addColumn(EntityDetails::getDescription)
                .setCaption(getString("lang.entities.column.description")).setHidable(true);
        availableEntities.addColumn(entitydetail -> getAssessors(entitydetail))
                .setCaption(getString("lang.entities.column.assesors")).setHidable(true).setHidden(true);
        // Initialize entity basket grid
        basketEntities = new Grid<EntityDetails>();
        ((Grid<EntityDetails>) basketEntities).setCaption(getString("lang.importer.selection"));
        ((Grid<EntityDetails>) basketEntities).setItems(entitiesSelection);
        ((Grid<EntityDetails>) basketEntities).addColumn(EntityDetails::getName).setCaption(
                getString("lang.entities.column.name"));
        ((Grid<EntityDetails>) basketEntities).addColumn(EntityDetails::getDescription)
                .setCaption(getString("lang.entities.column.description")).setHidable(true).setHidden(true);
        ((Grid<EntityDetails>) basketEntities).addColumn(entitydetail -> getAssessors(entitydetail))
                .setCaption(getString("lang.entities.column.assesors")).setHidable(true).setHidden(true);
        ((Grid<EntityDetails>) basketEntities).addColumn(entitiesSelection -> getString("lang.button.remove"),
                new ButtonRenderer(clickEvent -> {
                    Notification.show(getString("lang.itemremoved"), Notification.Type.HUMANIZED_MESSAGE);
                    entitiesSelection.remove(clickEvent.getItem());
                    ((Grid<EntityDetails>) basketEntities).setItems(entitiesSelection);
                    importButton.setEnabled(entitiesSelection.size() > 0);
                    viewBasketButton.setVisible(entitiesSelection.size() > 0);
                }));

    }

    /**
     * Generates cell containing assessor information.
     * 
     * @param details
     *            of the entity
     * @return assessors
     */
    private String getAssessors(EntityDetails details) {
        String assessors = "";
        if (details == null || details.getAssessors() == null) {
            return assessors;
        }
        for (AssessorDetails assDetails : details.getAssessors()) {
            assessors += AssessorDetailsHelper.display(assDetails) + " ";
        }
        return assessors;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == viewBasketButton) {
            if (basketWindow == null) {
                basketWindow = new Window();
                ((Window) basketWindow).setClosable(false);
                ((Window) basketWindow).setCaption(getString("lang.importer.basket"));
                ((Grid<EntityDetails>) basketEntities).setWidth("100%");
                ((Window) basketWindow).setWidth("40%");
                ((Window) basketWindow).setContent(((Grid<EntityDetails>) basketEntities));
                getMainUI().addWindow((Window) basketWindow);
            }
        }
        if (event.getButton() == cancelButton) {
            if (basketWindow != null) {
                ((Window) basketWindow).close();
            }
            ((Window) getParent()).close();
            return;
        }
        if (event.getButton() == importButton) {
            ListEntitiesResponse resp = getMainUI().getSatApiClient().storeEntities(
                    new ArrayList<EntityDetails>(entitiesSelection));
            if (!verifySuccess(resp)) {
                return;
            }
            if (basketWindow != null) {
                ((Window) basketWindow).close();
            }
            ((Window) getParent()).close();
            return;
        }
        if (event.getButton() == fetchContent) {
            String url = metadataUrl.getValue();
            LOG.debug("About to read entities from url " + url);
            ListEntitiesResponse resp = getMainUI().getSatApiClient().previewEntities(samlIdentifier, url, url,
                    getMainUI().getUser().getDetails().getPrincipalId());
            if (!verifySuccess(resp)) {
                return;
            }
            availableEntitiesSource = resp.getEntities();
            availableEntities.setItems(availableEntitiesSource);
            availableEntitiesFilter.setEnabled(availableEntitiesSource.size() > 0);
            availableEntitiesFilter.setValue("");
            return;
        }
        if (event.getButton() == addToBasketButton) {
            LOG.debug("Adding items " + availableEntities.getSelectedItems());
            if (availableEntities.getSelectedItems().size() == 0) {
                return;
            }
            entitiesSelection.addAll(availableEntities.getSelectedItems());
            ((Grid<EntityDetails>) basketEntities).setItems(entitiesSelection);
            Notification.show(getString("lang.itemadded"), Notification.Type.HUMANIZED_MESSAGE);
            importButton.setEnabled(true);
            viewBasketButton.setVisible(true);
            viewBasketButton.setCaption(String.format(getString("lang.importer.button.viewbasket"),
                    entitiesSelection.size()));
        }
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        if (event.getSource() == availableEntitiesFilter) {
            performFiltering();
        }

    }

    /**
     * Filters lists of available entities based on each of the substrings
     * contained either in entity name, description or assessor value.
     */
    private void performFiltering() {
        String filterValue = availableEntitiesFilter.getValue().trim();
        if (filterValue.length() == 0) {
            availableEntities.setItems(availableEntitiesSource);
            return;
        }
        List<EntityDetails> filteredEntities = new ArrayList<EntityDetails>();
        LOG.debug("filtering by string {}", filterValue);
        String[] searchValues = filterValue.toLowerCase().split(" ");
        // each of the values must be found
        for (EntityDetails details : availableEntitiesSource) {
            boolean notFound = false;
            outerloop: for (String searchValue : searchValues) {
                if (details.getName().toLowerCase().contains(searchValue)) {
                    LOG.debug("Match found in entity {}", details.getName());
                    continue;
                }
                if (details.getDescription() != null && details.getDescription().toLowerCase().contains(searchValue)) {
                    LOG.debug("Match found for entity {} in entity description {}", details.getName(),
                            details.getDescription());
                    continue;
                }
                for (AssessorDetails ad : details.getAssessors()) {
                    if (ad.getValue() != null && ad.getValue().toLowerCase().contains(searchValue)) {
                        LOG.debug("Match found for entity {} in assessor {}", details.getName(), ad.getValue());
                        continue outerloop;
                    }
                }
                notFound = true;
                break;

            }
            if (!notFound) {
                filteredEntities.add(details);
            }
        }
        LOG.debug("filtering decreased entities from {} to {}", availableEntitiesSource.size(), filteredEntities.size());
        availableEntities.setItems(filteredEntities);

    }

}
