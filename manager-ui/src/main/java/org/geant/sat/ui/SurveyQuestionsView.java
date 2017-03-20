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

import org.geant.sat.api.dto.QuestionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.DesignRoot;
//import com.vaadin.v7.data.Item;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.declarative.Design;

/** class implementing view to survey questions. */
@SuppressWarnings("serial")
@DesignRoot
public class SurveyQuestionsView extends AbstractSurveyVerticalLayout {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SurveyQuestionsView.class);

    /** Table showing survey questions. */
    Grid<QuestionDetails> questions;

    /**
     * Constructor.
     * @param ui main ui class.
     * @param details question details.
     */
    SurveyQuestionsView(MainUI ui, QuestionDetails[] details) {
        super(ui);
        Design.read(this);
        questions.setSelectionMode(SelectionMode.NONE);
        if (details != null && details.length > 0) {
            questions.setItems(details);
            questions.addColumn(QuestionDetails::getQuestionOrder).setCaption(getString("lang.questions.column.order"));
            questions.addColumn(QuestionDetails::getTitle).setCaption(getString("lang.questions.column.title"));
            questions.addColumn(QuestionDetails::getQuestion).setCaption(getString("lang.questions.column.question"));
            questions.addColumn(QuestionDetails::getMandatory).setCaption(getString("lang.questions.column.mandatory"));
            questions.addColumn(QuestionDetails::getType).setCaption(getString("lang.questions.column.type"));
            questions.setHeightByRows(details.length > 0 ? details.length : 1);
        } else {
            LOG.warn("no question details found");
        }
    }

}
