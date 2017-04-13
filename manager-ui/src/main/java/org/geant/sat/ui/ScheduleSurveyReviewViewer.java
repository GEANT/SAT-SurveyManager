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

import java.util.List;

import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.declarative.Design;

/** class implementing review view to survey scheduler. */
@SuppressWarnings("serial")
@DesignRoot
public class ScheduleSurveyReviewViewer extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSurveyReviewViewer.class);

    private TextArea text;

    ScheduleSurveyReviewViewer(MainUI ui, List<EntityDetails> selectedDetails) {
        super(ui);
        Design.read(this);
        String reviewText = "";
        LOG.debug("Parsing survey scheduling information");
        text.setCaption(getString("lang.scheduler.review.caption"));
        for (EntityDetails entity : selectedDetails) {
            reviewText = "\n" + getString("lang.scheduler.review.entity") + " " + entity.getName();
            if (entity.getSids().size() == 0) {
                reviewText += "\n" + getString("lang.scheduler.review.nosurveys");
            } else {
                reviewText += "\n" + getString("lang.scheduler.review.surveys") + ": ";
                for (String sid : entity.getSids())
                    reviewText += sid + " ";
            }
            if (entity.getAssessors().size() == 0) {
                reviewText += "\n" + getString("lang.scheduler.review.noassessors");
            } else {
                reviewText += "\n" + getString("lang.scheduler.review.assessors") + ": ";
                for (AssessorDetails assessor : entity.getAssessors())
                    reviewText += assessor.getValue() + " ";

            }

        }
        LOG.debug("review text:" + reviewText);
        text.setValue(reviewText);
    }

}
