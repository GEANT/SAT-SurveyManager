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
import java.util.Map;

import org.geant.sat.api.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

/**
 * Viewer to list information on user.
 */
@SuppressWarnings("serial")
@DesignRoot
public class ProfileViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProfileViewer.class);

    /** user principal. */
    TextField principal;
    /** survey tool principal. */
    TextField surveyPrincipal;
    /** roles grid. */
    Grid<String> roles;
    /** attributes grid. */
    Grid<AttributeList.Attribute> attributes;

    // Grid attributes;

    /**
     * Constructor. Populates the table with user information.
     * 
     * @param ui
     *            Main ui instance to access shared data.
     * @param userDetails
     *            The user details to show.
     */
    public ProfileViewer(MainUI ui, UserDetails userDetails) {
        super(ui);
        Design.read(this);
        principal.setCaption(getString("lang.profile.field.principal"));
        principal.setEnabled(false);
        principal.setValue(userDetails.getPrincipalId());
        surveyPrincipal.setCaption(getString("lang.profile.field.surveyprincipal"));
        surveyPrincipal.setValue(userDetails.getSurveyPrincipalId() != null ? userDetails.getSurveyPrincipalId() : "");
        surveyPrincipal.setEnabled(false);
        roles.setCaption(getString("lang.profile.grid.roles"));
        roles.setSelectionMode(SelectionMode.NONE);
        roles.setItems(userDetails.getRoles());
        roles.addColumn(String::toString);
        roles.setHeightByRows(userDetails.getRoles().size() > 0 ? userDetails.getRoles().size() : 1);
        attributes.setCaption(getString("lang.profile.grid.attributes"));
        attributes.setSelectionMode(SelectionMode.NONE);
        AttributeList al = new AttributeList(userDetails.getAttributes());
        attributes.setItems(al.attributes);
        attributes.addColumn(AttributeList.Attribute::getName);
        attributes.addColumn(AttributeList.Attribute::getValue);
        attributes.setHeightByRows(al.attributes.size() > 0 ? al.attributes.size() : 1);

    }

    /**
     * Helper to list user attributes in grid.
     */
    class AttributeList {

        List<Attribute> attributes = new ArrayList<Attribute>();

        /** Attribute to show in grid. */
        class Attribute {
            /** attribute name. */
            private String name;
            /** attribute value. */
            private String value;

            /**
             * Constructor.
             * 
             * @param n
             *            name of the attribute.
             * @param v
             *            value of the attribute.
             */
            Attribute(String n, String v) {
                name = n;
                value = v;

            }

            /**
             * Returns attribute name.
             * 
             * @return attribute name.
             */
            public String getName() {
                LOG.debug("get Attribute " + name);
                return name;
            }

            /**
             * Returns attribute value.
             * 
             * @return attribute value.
             */
            public String getValue() {
                LOG.debug("get Attribute value" + value);
                return value;
            }
        }

        /**
         * Constructor. Creates a attribute list.
         * 
         * @param attr
         *            map converted to list.
         */
        AttributeList(Map<String, String> attr) {
            for (Map.Entry<String, String> entry : attr.entrySet()) {
                attributes.add(new Attribute(entry.getKey(), entry.getValue()));
            }
        }
    }

}
