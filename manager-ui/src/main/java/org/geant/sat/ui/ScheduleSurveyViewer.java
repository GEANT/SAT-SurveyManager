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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.themes.ValoTheme;

/** class implementing view to survey questions. */
@SuppressWarnings("serial")
@DesignRoot
public class ScheduleSurveyViewer extends AbstractSurveyVerticalLayout implements SelectedTabChangeListener,
        ClickListener {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSurveyViewer.class);

    /** Panel for tab views. */
    private Panel workPanel;

    /** Table showing survey questions. */
    private TabSheet phases;
    /** initial position of the tab */
    private int position = 0;
    private Button back;
    private Button next;
    private Button cancel;
    private Button send;

    private List<EntityDetails> selectedDetails = new ArrayList<EntityDetails>();

    private Tab entities;
    private Tab surveys;
    private Tab assessors;
    private Tab review;

    /**
     * Constructor for entity based initialization. Preselects one entity, it's
     * surveys and assessors.
     * 
     * @param ui
     *            main ui class.
     * @param details
     *            question details.
     */
    ScheduleSurveyViewer(MainUI ui, EntityDetails details) {
        super(ui);
        Design.read(this);
        selectedDetails.add(details);
        back.setEnabled(false);
        send.setVisible(false);
        back.addClickListener(this);
        back.setCaption(getString("lang.scheduler.button.back"));
        next.addClickListener(this);
        cancel.setCaption(getString("lang.scheduler.button.cancel"));
        cancel.addClickListener(this);
        send.setCaption(getString("lang.scheduler.button.send"));
        send.addClickListener(this);
        next.setCaption(getString("lang.scheduler.button.next"));
        entities = phases.addTab(new Label(getString("lang.scheduler.tab.entities")));
        surveys = phases.addTab(new Label(getString("lang.scheduler.tab.surveys")));
        assessors = phases.addTab(new Label(getString("lang.scheduler.tab.assessors")));
        review = phases.addTab(new Label(getString("lang.scheduler.tab.review")));
        workPanel.setContent(new ScheduleSurveyEntitiesViewer(getMainUI(), selectedDetails));
        phases.addSelectedTabChangeListener(this);
        phases.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        phases.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        phases.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
    }

    @Override
    public void selectedTabChange(SelectedTabChangeEvent event) {

        Tab selectedTab = phases.getTab(phases.getSelectedTab());
        if (selectedTab.equals(entities)) {
            back.setEnabled(false);
            next.setEnabled(true);
            position = 0;
            workPanel.setContent(new ScheduleSurveyEntitiesViewer(getMainUI(), selectedDetails));
            return;
        }
        if (selectedTab.equals(surveys)) {
            back.setEnabled(true);
            next.setEnabled(true);
            send.setVisible(false);
            position = 1;
            workPanel.setContent(new ScheduleSurveyReviewViewer(getMainUI(), selectedDetails));
            return;
        }
        if (selectedTab.equals(assessors)) {
            back.setEnabled(true);
            next.setEnabled(true);
            send.setVisible(false);
            position = 2;
            workPanel.setContent(new ScheduleSurveyReviewViewer(getMainUI(), selectedDetails));
            return;
        }
        if (selectedTab.equals(review)) {
            back.setEnabled(true);
            next.setEnabled(false);
            send.setVisible(true);
            position = 3;
            workPanel.setContent(new ScheduleSurveyReviewViewer(getMainUI(), selectedDetails));
            return;
        }

    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == back) {
            phases.setSelectedTab(--position);
            return;
        }
        if (event.getButton() == next) {
            phases.setSelectedTab(++position);
            return;
        }

    }

}
