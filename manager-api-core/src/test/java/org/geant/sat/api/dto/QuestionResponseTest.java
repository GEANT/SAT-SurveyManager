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

package org.geant.sat.api.dto;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;

/**
 * Unit tests for {@link QuestionResponse}.
 */
public class QuestionResponseTest extends AbstractConnectorResponseTest {

    QuestionsResponse response;
    
    private String qid;

    private String parentQid;

    private String sid;

    private String gid;

    private String type;

    private String title;

    private String question;

    private boolean mandatory;

    private String questionOrder;

    private String language;
    
    @BeforeMethod
    public void initTests() {
        response = new QuestionsResponse();
    }
    
    public void initVariables() {
        qid = "mockQid";
        parentQid = "mockParentQid";
        sid = "mockSid";
        gid = "mockGid";
        type = "mockType";
        title = "mockTitle";
        question = "mockQuestion";
        mandatory = true;
        questionOrder = "mockOrder";
        language = "en";
    }
    
    @Test
    public void testInitialized() {
        Assert.assertNotNull(response.getQuestions());
        Assert.assertTrue(response.getQuestions().isEmpty());
        Assert.assertNull(response.getErrorMessage());
    }
    
    @Test
    public void testError() {
        super.testError(QuestionsResponse.class);
    }
    
    @Test
    public void testWithDetails() {
        final QuestionDetails details = initializeDetails();
        final List<QuestionDetails> questions = new ArrayList<>();
        questions.add(details);
        response.setQuestions(questions);
        Gson gson = new Gson();
        final String encoded = gson.toJson(response);
        final QuestionsResponse decodedResponse = gson.fromJson(encoded, QuestionsResponse.class);
        Assert.assertNull(decodedResponse.getErrorMessage());
        Assert.assertNotNull(decodedResponse.getQuestions());
        Assert.assertEquals(decodedResponse.getQuestions().size(), 1);
        assertDetails(decodedResponse.getQuestions().get(0));
    }

    public QuestionDetails initializeDetails() {
        initVariables();
        final QuestionDetails details = new QuestionDetails();
        details.setQid(qid);
        details.setParentQid(parentQid);
        details.setSid(sid);
        details.setGid(gid);
        details.setType(type);
        details.setTitle(title);
        details.setQuestion(question);
        details.setMandatory(mandatory);
        details.setQuestionOrder(questionOrder);
        details.setLanguage(language);
        return details;
    }
    
    public void assertDetails(final QuestionDetails details) {
        initVariables();
        Assert.assertEquals(details.getQid(), qid);
        Assert.assertEquals(details.getParentQid(), parentQid);
        Assert.assertEquals(details.getSid(), sid);
        Assert.assertEquals(details.getGid(), gid);
        Assert.assertEquals(details.getType(), type);
        Assert.assertEquals(details.getTitle(), title);
        Assert.assertEquals(details.getQuestion(), question);
        Assert.assertEquals(details.getMandatory(), mandatory);
        Assert.assertEquals(details.getQuestionOrder(), questionOrder);
        Assert.assertEquals(details.getLanguage(), language);
    }
}