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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;

/**
 * Unit tests for {@link EntityResponse}.
 */
public class EntityResponseTest extends AbstractConnectorResponseTest {
    
    private EntityResponse response;
    
    private String id;
    
    private String name;

    private String description;
    
    private String creator;
    
    private AssessorDetails assessor;

    private String sid;

    public void initVariables() {
        id = "mockId";
        name = "mockName";
        description = "mockDescription";
        creator = "mockCreator";
        assessor = new AssessorResponseTest().initializeDetails();
        sid = "mockSid";
    }

    @BeforeMethod
    public void initTests() {
        response = new EntityResponse();
    }
    
    @Test
    public void testInitialized() {
        Assert.assertNull(response.getEntity());
        Assert.assertNull(response.getErrorMessage());
    }
    
    @Test
    public void testError() {
        super.testError(EntityResponse.class);
    }
    
    @Test
    public void testWithDetails() {
        final EntityDetails details = initializeDetails();
        response.setEntity(details);
        Gson gson = new Gson();
        final String encoded = gson.toJson(response);
        final EntityResponse decodedResponse = gson.fromJson(encoded, EntityResponse.class);
        Assert.assertNull(decodedResponse.getErrorMessage());
        Assert.assertNotNull(decodedResponse.getEntity());
        assertDetails(decodedResponse.getEntity());
    }

    public EntityDetails initializeDetails() {
        initVariables();
        final EntityDetails details = new EntityDetails();
        details.setId(id);
        details.setCreator(creator);
        details.setDescription(description);
        details.setName(name);
        final List<AssessorDetails> assessors = new ArrayList<>();
        assessors.add(assessor);
        details.setAssessors(assessors);
        final Set<String> sids = new HashSet<>();
        sids.add(sid);
        details.setSids(sids);
        return details;
    }
    
    public void assertDetails(final EntityDetails details) {
        Assert.assertEquals(details.getSids().size(), 1);
        Assert.assertTrue(details.getSids().contains(sid));
        Assert.assertEquals(details.getCreator(), creator);
        Assert.assertEquals(details.getId(), id);
        Assert.assertEquals(details.getDescription(), description);
        Assert.assertEquals(details.getName(), name);
        Assert.assertNotNull(details.getAssessors());
        Assert.assertEquals(details.getAssessors().size(), 1);
        final AssessorResponseTest assessorTest = new AssessorResponseTest();
        assessorTest.assertDetails(details.getAssessors().get(0));
    }
}
