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
 * Unit tests for {@link ListAllSurveysResponse}.
 */
public class ListAllSurveysResponseTest extends SurveyResponseTest {

    ListAllSurveysResponse response;
    
    @BeforeMethod
    public void initTests() {
        response = new ListAllSurveysResponse();
        initVariables();
    }
    
    @Test
    public void testInitialized() {
        Assert.assertNotNull(response.getSurveys());
        Assert.assertNull(response.getErrorMessage());
        Assert.assertTrue(response.getSurveys().isEmpty());
    }
    
    @Test
    public void testError() {
        super.testError(ListAllSurveysResponse.class);
    }
    
    @Test
    public void testWithDetails() {
        final SurveyDetails details = initializeDetails();
        final List<SurveyDetails> surveys = new ArrayList<>();
        surveys.add(details);
        response.setSurveys(surveys);
        Gson gson = new Gson();
        final String encoded = gson.toJson(response);
        final ListAllSurveysResponse decodedResponse = gson.fromJson(encoded, ListAllSurveysResponse.class);
        Assert.assertNull(decodedResponse.getErrorMessage());
        Assert.assertEquals(decodedResponse.getSurveys().size(), 1);
        assertDetails(decodedResponse.getSurveys().get(0));
    }
}
