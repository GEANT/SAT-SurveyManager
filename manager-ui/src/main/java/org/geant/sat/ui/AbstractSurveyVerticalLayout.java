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

import org.geant.sat.api.dto.AbstractConnectorResponse;

import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/** abstract base class for vertical layouts. */
@SuppressWarnings("serial")
public abstract class AbstractSurveyVerticalLayout extends VerticalLayout {

    /** main ui containing information shared by all views. */
    private MainUI mainUI;

    /**
     * Constructor.
     * 
     * @param ui
     *            main ui
     */
    AbstractSurveyVerticalLayout(MainUI ui) {
        mainUI = ui;

    }

    /**
     * Returns main ui. Main ui contains informations shared by all views.
     * 
     * @return main ui instance
     */
    protected MainUI getMainUI() {
        return mainUI;
    }

    /**
     * Fetched language string by key. If matching string is not found the key
     * itself is returned.
     * 
     * @param key
     *            by which the string is searched for
     * @return language string or key
     */
    protected String getString(String key) {
        String value;
        if (mainUI == null || mainUI.getStrings() == null) {
            return key;
        }
        value = mainUI.getStrings().getString(key);
        return value == null ? key : value;
    }

    /**
     * Checks for Sat Api error response.
     * 
     * @param response
     *            to check for.
     * @return false if error occurred.
     */
    public boolean indicateSuccess(AbstractConnectorResponse response) {
        return getMainUI().indicateSuccess(response);
    }

}
