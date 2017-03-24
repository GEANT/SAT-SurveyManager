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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;

/**
 * Unit tests for {@link UserResponse}.
 */
public class UserResponseTest extends AbstractConnectorResponseTest {

    UserResponse response;
    
    String principalId;

    String surveyPrincipalId;

    String attributeName;
    
    String attributeValue;
    
    String role;
    
    @BeforeMethod
    public void initTests() {
        response = new UserResponse();
    }
    
    public void initVariables() {
        principalId = "mockPrincipalId";
        surveyPrincipalId = "mockSurveyPrincipalId";
        attributeName = "attributeName";
        attributeValue = "attributeValue";
        role = "mockRole";
    }
    
    @Test
    public void testInitialized() {
        Assert.assertNull(response.getUser());
        Assert.assertNull(response.getErrorMessage());
    }
    
    @Test
    public void testError() {
        super.testError(UserResponse.class);
    }
    
    @Test
    public void testWithDetails() {
        final UserDetails details = initializeDetails();
        response.setUser(details);
        Gson gson = new Gson();
        final String encoded = gson.toJson(response);
        final UserResponse decodedResponse = gson.fromJson(encoded, UserResponse.class);
        Assert.assertNull(decodedResponse.getErrorMessage());
        Assert.assertNotNull(decodedResponse.getUser());
        assertDetails(decodedResponse.getUser());
    }

    public UserDetails initializeDetails() {
        initVariables();
        final UserDetails details = new UserDetails();
        details.setPrincipalId(principalId);
        details.setSurveyPrincipalId(surveyPrincipalId);
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(attributeName, attributeValue);
        details.setAttributes(attributes);
        final Set<String> roles = new HashSet<>();
        roles.add(role);
        details.setRoles(roles);
        return details;
    }
    
    public void assertDetails(final UserDetails details) {
        initVariables();
        Assert.assertEquals(details.getPrincipalId(), principalId);
        Assert.assertEquals(details.getSurveyPrincipalId(), surveyPrincipalId);
        Assert.assertEquals(details.getAttributes().size(), 1);
        Assert.assertEquals(details.getAttributes().get(attributeName), attributeValue);
        Assert.assertEquals(details.getRoles().size(), 1);
        Assert.assertTrue(details.getRoles().contains(role));
    }
}