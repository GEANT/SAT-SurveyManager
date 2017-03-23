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

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.geant.sat.api.dto.UserDetails;

/**
 * Unit tests for {@link RoleImpl}.
 */
public class RoleImplTest {

    RoleImpl roleImpl;
    UserDetails userDetails;

    @BeforeMethod
    public void initTests() {
        userDetails = new UserDetails();
        Set<String> roles = new HashSet<String>();
        roles.add("role1");
        userDetails.setRoles(roles);
        roleImpl = new RoleImpl();
        roleImpl.setAdminRoleName("adminName");
        roleImpl.setAssessmentCoordinatorRoleName("assessmentCoordinatorRoleName");
        roleImpl.setSurveyOwnerRoleName("surveyOwnerRoleName");
    }

    @Test
    public void testSetters() {
        Assert.assertEquals(roleImpl.getAdminRoleName(), "adminName");
        Assert.assertEquals(roleImpl.getAssessmentCoordinatorRoleName(), "assessmentCoordinatorRoleName");
        Assert.assertEquals(roleImpl.getSurveyOwnerRoleName(), "surveyOwnerRoleName");
    }

    @Test
    public void testRoles() {
        Assert.assertTrue(!roleImpl.isAdmin(null));
        Assert.assertTrue(!roleImpl.isAssessmentCoordinator(null));
        Assert.assertTrue(!roleImpl.isSurveyOwner(null));
        Assert.assertTrue(!roleImpl.isAdmin(userDetails));
        Assert.assertTrue(!roleImpl.isAssessmentCoordinator(userDetails));
        Assert.assertTrue(!roleImpl.isSurveyOwner(userDetails));
        Set<String> roles = new HashSet<String>();
        roles.add("adminName");
        roles.add("assessmentCoordinatorRoleName");
        roles.add("surveyOwnerRoleName");
        userDetails.setRoles(roles);
        Assert.assertTrue(roleImpl.isAdmin(userDetails));
        Assert.assertTrue(roleImpl.isAssessmentCoordinator(userDetails));
        Assert.assertTrue(roleImpl.isSurveyOwner(userDetails));

    }

}
