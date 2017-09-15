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

package org.geant.sat.ui.utils;

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.geant.sat.ui.MainUI;

import com.vaadin.ui.ItemCaptionGenerator;

/** class implementing helpers for assessor details. */
@SuppressWarnings("serial")
public class AssessorStringIDHelper implements ItemCaptionGenerator<String> {

    /** main ui for accessing api. */
    private MainUI mainUI;

    /**
     * Constructor.
     * 
     * @param ui
     *            main ui for accessing api.
     */
    public AssessorStringIDHelper(MainUI ui) {
        mainUI = ui;
    }

    public String apply(String assessorId) {
        return display(assessorId, mainUI);
    }

    /**
     * Converts assessorId to a human readable string.
     * 
     * @param assessorId
     *            assessor id as string.
     * @ui main ui to access common features.
     * @return assessor display string.
     */
    public static String display(String assessorId, MainUI ui) {
        ListAssessorsResponse response = ui.getSatApiClient().getAssessors();
        if (!ui.indicateSuccess(response)) {
            return null;
        }
        for (AssessorDetails details : response.getAssessors()) {
            if (details.getId().equals(assessorId)) {
                return AssessorDetailsHelper.display(details);
            }
        }
        return null;
    }

}
