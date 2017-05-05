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

package org.geant.sat.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.geant.sat.api.dto.ListAllSurveysResponse;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link LimeSurveyConnector}.
 */
public class LimeSurveyConnectorTest {
    
    public static final String JSON_FILE_DIRECTORY = "src/test/resources/org/geant/sat/api/";
    
    public static final String JSON_FILE_CHARSET = "UTF-8";
 
    @Test
    public void testUpdateSessionKey() throws Exception {
        LimeSurveyConnector connector = initializeMockConnector(JSON_FILE_DIRECTORY + "updateSessionKey.json");
        connector.updateSessionKey();
    }

    @Test(expectedExceptions = SurveySystemConnectorException.class)
    public void tesUpdateInvalidSessionKey() throws Exception {
        LimeSurveyConnector connector = initializeMockConnector(JSON_FILE_DIRECTORY + "invalidSessionKey.json");
        connector.updateSessionKey();
    }

    @Test
    public void testGenerateToken() throws Exception {
        LimeSurveyConnector connector = initializeMockConnector(JSON_FILE_DIRECTORY + "addParticipants.json");
        Assert.assertEquals(connector.generateToken("mock"), "6aU3S5D3SQwKU41");
    }

    @Test(expectedExceptions = SurveySystemConnectorException.class)
    public void testGenerateTokenInvalidKey() throws Exception {
        LimeSurveyConnector connector = initializeMockConnector(JSON_FILE_DIRECTORY + "invalidSessionKey.json");
        connector.generateToken("mock");
    }
    
    @Test
    public void testListSurveys() throws Exception {
        Map<String, String> matchers = new HashMap<>();
        matchers.put("list_surveys", JSON_FILE_DIRECTORY + "listSurveys.json");
        matchers.put("list_users", JSON_FILE_DIRECTORY + "listUsers.json");
        matchers.put("get_survey_properties", JSON_FILE_DIRECTORY + "surveyProperties.json");
        LimeSurveyConnector connector = initializeMockConnector(matchers);
        ListAllSurveysResponse response = connector.listSurveys();
        Assert.assertNull(response.getErrorMessage());
        Assert.assertEquals(response.getSurveys().size(), 1);
    }
    
    protected LimeSurveyConnector initializeMockConnector(final String resultJson) throws Exception {
        LimeSurveyConnector connector = Mockito.spy(new LimeSurveyConnector());
        String result = FileUtils.readFileToString(new File(resultJson), JSON_FILE_CHARSET);
        Mockito.doReturn(result).when(connector).getContents(Mockito.anyString(), Mockito.anyString(), 
                Mockito.anyBoolean());
        Mockito.doReturn(result).when(connector).getContents(Mockito.anyString(), Mockito.isNull(), 
                Mockito.anyBoolean());
        return connector;
    }
    
    protected LimeSurveyConnector initializeMockConnector(final Map<String, String> matchers) throws Exception {
        LimeSurveyConnector connector = Mockito.spy(new LimeSurveyConnector());
        for (String key : matchers.keySet()) {
            String result = FileUtils.readFileToString(new File(matchers.get(key)), JSON_FILE_CHARSET);
            Mockito.doReturn(result).when(connector).getContents(Mockito.matches(key), Mockito.anyString(), 
                    Mockito.anyBoolean());
            Mockito.doReturn(result).when(connector).getContents(Mockito.matches(key), Mockito.isNull(), 
                    Mockito.anyBoolean());
        }
        return connector;
        
    }
}
