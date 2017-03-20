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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geant.sat.api.dto.AnswersResponse;
import org.geant.sat.api.dto.ListRolesResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.QuestionsResponse;
import org.geant.sat.api.dto.RoleDetails;
import org.geant.sat.api.dto.RoleResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.api.dto.SurveyResponse;
import org.geant.sat.api.dto.ListAllSurveysResponse;
import org.geant.sat.api.dto.UserDetails;
import org.geant.sat.api.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The controller and orchestrator for the Survey Manager API.
 */
@Controller
public class RestController {

    /** The survey system connector. */
    @Autowired
    @Qualifier("surveyManager.api.surveyConnector")
    private SurveySystemConnector surveyConnector;

    /** The user database connector. */
    @Autowired
    @Qualifier("surveyManager.api.userDatabaseConnector")
    private UserDatabaseConnector userDbConnector;

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(RestController.class);

    /**
     * List all surveys.
     * @param active Whether only active surveys should be listed.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The list of all surveys.
     */
    @RequestMapping(value = "/surveys", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ListAllSurveysResponse> listSurveys(
            @RequestParam(value = "active", required = false, defaultValue = "false") String active,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /surveys endpoint with parameter active={}", active);
        final ListAllSurveysResponse emptyResponse = new ListAllSurveysResponse();
        try {
            final ListAllSurveysResponse response = surveyConnector.listSurveys();
            if (response != null) {
                if (response.getErrorMessage() != null) {
                    return new ResponseEntity<ListAllSurveysResponse>(response, HttpStatus.BAD_GATEWAY);
                }
                return new ResponseEntity<ListAllSurveysResponse>(response, HttpStatus.OK);
            }
        } catch (SurveySystemConnectorException e) {
            emptyResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity<ListAllSurveysResponse>(emptyResponse, HttpStatus.BAD_GATEWAY);
        }
        emptyResponse.setErrorMessage("Could not find any surveys");
        return new ResponseEntity<ListAllSurveysResponse>(emptyResponse, HttpStatus.OK);
    }

    /**
     * Lists all questions for a given survey identifier.
     * @param sid The survey identifier.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The list of all questions for the given survey identifier.
     */
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<QuestionsResponse> listQuestions(
            @RequestParam(value = "sid", required = true) String sid, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        log.debug("Starting /questions endpoint with parameter sid={}", sid);
        final QuestionsResponse emptyResponse = new QuestionsResponse();
        try {
            final QuestionsResponse response = surveyConnector.listQuestions(sid);
            if (response != null) {
                if (response.getErrorMessage() != null) {
                    return new ResponseEntity<QuestionsResponse>(response, HttpStatus.BAD_GATEWAY);
                }
                return new ResponseEntity<QuestionsResponse>(response, HttpStatus.OK);
            }
        } catch (SurveySystemConnectorException e) {
            emptyResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity<QuestionsResponse>(emptyResponse, HttpStatus.BAD_GATEWAY);
        }
        emptyResponse.setErrorMessage("Could not find any questions");
        return new ResponseEntity<QuestionsResponse>(emptyResponse, HttpStatus.OK);
    }

    /**
     * Lists all answers for a given survey identifier.
     * @param sid The survey identifier.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The list of all answers for the given survey identifier.
     */
    @RequestMapping(value = "/answers", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<AnswersResponse> listAnswers(
            @RequestParam(value = "sid", required = true) String sid, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        log.debug("Starting /answers endpoint with parameter sid={}", sid);
        final AnswersResponse emptyResponse = new AnswersResponse();
        try {
            final AnswersResponse response = surveyConnector.listAnswers(sid);
            if (response != null) {
                if (response.getErrorMessage() != null) {
                    return new ResponseEntity<AnswersResponse>(response, HttpStatus.BAD_GATEWAY);
                }
                return new ResponseEntity<AnswersResponse>(response, HttpStatus.OK);
            }
        } catch (SurveySystemConnectorException e) {
            emptyResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity<AnswersResponse>(emptyResponse, HttpStatus.BAD_GATEWAY);
        }
        emptyResponse.setErrorMessage("Could not find any answers");
        return new ResponseEntity<AnswersResponse>(emptyResponse, HttpStatus.OK);
    }

    /**
     * Lists all users.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return All the users.
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ListUsersResponse> listUsers(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        log.debug("Starting /users endpoint");
        final ListUsersResponse smResponse = userDbConnector.listUsers();
        if (smResponse.getErrorMessage() != null) {
            return new ResponseEntity<ListUsersResponse>(smResponse, HttpStatus.BAD_GATEWAY);
        }
        final ListUsersResponse surveyResponse = surveyConnector.listUsers();
        if (surveyResponse.getErrorMessage() != null) {
            smResponse.setErrorMessage(surveyResponse.getErrorMessage());
            return new ResponseEntity<ListUsersResponse>(smResponse, HttpStatus.BAD_GATEWAY);
        }
        final ListUsersResponse response = new ListUsersResponse();
        final List<UserDetails> users = smResponse.getUsers();
        response.setUsers(combineDetails(users, surveyResponse.getUsers()));
        return new ResponseEntity<ListUsersResponse>(response, HttpStatus.OK);
    }

    /**
     * Lists all roles.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return All the roles.
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ListRolesResponse> listRoles(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        log.debug("Starting /roles endpoint");
        final ListRolesResponse response = userDbConnector.listRoles();
        if (response.getErrorMessage() != null) {
            return new ResponseEntity<ListRolesResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<ListRolesResponse>(response, HttpStatus.OK);
    }

    /**
     * Get user details for the given principal identifier.
     * @param principalId The principal identifier for the user.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The user details.
     */
    @RequestMapping(value = "/users/{principalId:.+}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<UserResponse> getUser(@PathVariable String principalId,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /user endpoint with principalId={}", principalId);
        final UserDetails details = userDbConnector.getUserDetails(principalId);
        final UserResponse response = new UserResponse();
        if (details == null) {
            log.info("Could not find user with principalId {}", principalId);
            response.setErrorMessage("User not found");
            return new ResponseEntity<UserResponse>(response, HttpStatus.NOT_FOUND);
        }
        final ListUsersResponse surveyResponse = surveyConnector.listUsers();
        if (surveyResponse.getErrorMessage() != null) {
            response.setErrorMessage(surveyResponse.getErrorMessage());
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        final List<UserDetails> users = new ArrayList<>();
        users.add(details);
        response.setUser(combineDetails(users, surveyResponse.getUsers(), false).get(0));
        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    /**
     * Lists all the details for the given role.
     * @param roleName The role name.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return All the details for the role.
     */
    @RequestMapping(value = "/roles/{roleName:.+}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<RoleResponse> getRole(@PathVariable String roleName,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /roles endpoint with roleName={}", roleName);
        final RoleDetails details = userDbConnector.getRoleDetails(roleName);
        final RoleResponse response = new RoleResponse();
        if (details == null) {
            log.info("Could not find role with name {}", roleName);
            response.setErrorMessage("Role not found");
            return new ResponseEntity<RoleResponse>(response, HttpStatus.NOT_FOUND);
        }
        response.setRole(details);
        return new ResponseEntity<RoleResponse>(response, HttpStatus.OK);
    }

    /**
     * Create a new user. Error returned if the user already exists.
     * @param principalId The principal identifier for the user.
     * @param body The details for the user.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The detaila for the created user.
     */
    @RequestMapping(headers = {
            "content-type=application/json" }, value = "/users/{principalId:.+}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<UserResponse> insertUser(@PathVariable String principalId,
            @RequestBody UserDetails body, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /users POST endpoint with principalId={}", principalId);
        final UserResponse response = new UserResponse();
        final UserDetails details = body;
        if (!principalId.equals(details.getPrincipalId())) {
            log.error("Given principalId {} doesn't match with the on in user details {}", principalId,
                    details.getPrincipalId());
            response.setErrorMessage("Given principal ID doesn't match with the one in user details");
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
        }
        final UserDetails existing = userDbConnector.getUserDetails(principalId);
        if (existing != null) {
            log.info("User already exists with principalId {}", principalId);
            response.setErrorMessage("User already exists");
            response.setUser(existing);
            return new ResponseEntity<UserResponse>(response, HttpStatus.CONFLICT);
        }
        final List<UserDetails> users = new ArrayList<>();
        users.add(details);
        try {
            response.setUser(details);
            userDbConnector.updateUserDetails(details);
        } catch (SurveySystemConnectorException e) {
            response.setErrorMessage("Could not add the user to the database");
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        final ListUsersResponse surveyResponse = surveyConnector.listUsers();
        if (surveyResponse.getErrorMessage() != null) {
            response.setErrorMessage(surveyResponse.getErrorMessage());
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        response.setUser(combineDetails(users, surveyResponse.getUsers(), false).get(0));
        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    /**
     * Updates an existing user.
     * @param principalId The principal identifier for the user.
     * @param body The details for the user.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The updated details for the user.
     */
    @RequestMapping(headers = {
            "content-type=application/json" }, value = "/users/{principalId:.+}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<UserResponse> updateUser(@PathVariable String principalId,
            @RequestBody UserDetails body, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /users PUT endpoint with principalId={}", principalId);
        final UserResponse response = new UserResponse();
        final UserDetails details = body;
        if (!principalId.equals(details.getPrincipalId())) {
            log.error("Given principalId {} doesn't match with the on in user details {}", principalId,
                    details.getPrincipalId());
            response.setErrorMessage("Given principal ID doesn't match with the one in user details");
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
        }
        final UserDetails existing = userDbConnector.getUserDetails(principalId);
        if (existing == null) {
            log.info("User doesn't exist with principalId {}", principalId);
            response.setErrorMessage("User does not exist");
            response.setUser(existing);
            return new ResponseEntity<UserResponse>(response, HttpStatus.NOT_FOUND);
        }
        final List<UserDetails> users = new ArrayList<>();
        users.add(details);
        try {
            userDbConnector.updateUserDetails(details);
        } catch (SurveySystemConnectorException e) {
            response.setErrorMessage("Could not update the user in the database");
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        final ListUsersResponse surveyResponse = surveyConnector.listUsers();
        if (surveyResponse.getErrorMessage() != null) {
            response.setErrorMessage(surveyResponse.getErrorMessage());
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        response.setUser(combineDetails(users, surveyResponse.getUsers(), false).get(0));
        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    /**
     * Updates an existing survey.
     * @param sid The survey identifier.
     * @param body The details for the survey.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The updated details for the survey.
     */
    @RequestMapping(headers = {
            "content-type=application/json" }, value = "/surveys/{sid}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<SurveyResponse> updateSurvey(@PathVariable String sid,
            @RequestBody SurveyDetails body, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /surveys PUT endpoint with sid={}", sid);
        final SurveyResponse response = new SurveyResponse();
        final SurveyDetails details = body;
        if (!sid.equals(details.getSid())) {
            log.error("Given sid {} doesn't match with the one in survey details {}", sid, details.getSid());
            response.setErrorMessage("Given sid doesn't match with the one in survey details");
            return new ResponseEntity<SurveyResponse>(response, HttpStatus.BAD_REQUEST);
        }
        final ListAllSurveysResponse surveyResponse;
        try {
            surveyConnector.updateSurveyDetails(details);
            surveyResponse = surveyConnector.listSurveys();
        } catch (SurveySystemConnectorException e) {
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<SurveyResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        if (surveyResponse.getErrorMessage() != null) {
            response.setErrorMessage(surveyResponse.getErrorMessage());
            return new ResponseEntity<SurveyResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        for (SurveyDetails survey : surveyResponse.getSurveys()) {
            if (sid.equals(survey.getSid())) {
                response.setSurvey(survey);
                return new ResponseEntity<SurveyResponse>(response, HttpStatus.OK);
            }
        }
        response.setErrorMessage("Could not fetch the status of the survey after update");
        return new ResponseEntity<SurveyResponse>(response, HttpStatus.BAD_GATEWAY);
    }

    
    /**
     * Creates a new role. An error is returned if the role already exists.
     * @param roleName The role name.
     * @param body The details for the role.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The details for the created role.
     */
    @RequestMapping(headers = {
            "content-type=application/json" }, value = "/roles/{roleName:.+}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<RoleResponse> insertRole(@PathVariable String roleName,
            @RequestBody RoleDetails body, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /roles POST endpoint with roleName={}", roleName);
        final RoleResponse response = new RoleResponse();
        final RoleDetails details = body;
        if (!roleName.equals(details.getName())) {
            log.error("Given roleName {} doesn't match with the on in role details {}", roleName, details.getName());
            response.setErrorMessage("Given role name doesn't match with the one in role details");
            return new ResponseEntity<RoleResponse>(response, HttpStatus.BAD_REQUEST);
        }
        final RoleDetails existing = userDbConnector.getRoleDetails(roleName);
        if (existing != null) {
            log.info("Role already exists with name {}", roleName);
            response.setErrorMessage("Role already exists");
            response.setRole(existing);
            return new ResponseEntity<RoleResponse>(response, HttpStatus.CONFLICT);
        }
        try {
            response.setRole(details);
            userDbConnector.updateRoleDetails(details);
        } catch (SurveySystemConnectorException e) {
            response.setErrorMessage("Could not add the role to the database");
            return new ResponseEntity<RoleResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<RoleResponse>(response, HttpStatus.OK);
    }

    /**
     * Updates an existing role.
     * @param roleName The role name.
     * @param body The details for the role.
     * @param httpRequest The HTTP servlet request.
     * @param httpResponse The HTTP servlet response.
     * @return The details for the updated role.
     */
    @RequestMapping(headers = {
            "content-type=application/json" }, value = "/roles/{roleName:.+}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<RoleResponse> updateRole(@PathVariable String roleName,
            @RequestBody RoleDetails body, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("Starting /roles PUT endpoint with roleName={}", roleName);
        final RoleResponse response = new RoleResponse();
        final RoleDetails details = body;
        if (!roleName.equals(details.getName())) {
            log.error("Given roleName {} doesn't match with the on in role details {}", roleName, details.getName());
            response.setErrorMessage("Given role name doesn't match with the one in role details");
            return new ResponseEntity<RoleResponse>(response, HttpStatus.BAD_REQUEST);
        }
        final RoleDetails existing = userDbConnector.getRoleDetails(roleName);
        if (existing == null) {
            log.info("Role does not exist with name {}", roleName);
            response.setErrorMessage("Role does not exist");
            return new ResponseEntity<RoleResponse>(response, HttpStatus.NOT_FOUND);
        }
        try {
            response.setRole(details);
            userDbConnector.updateRoleDetails(details);
        } catch (SurveySystemConnectorException e) {
            response.setErrorMessage("Could not update the role in the database");
            return new ResponseEntity<RoleResponse>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<RoleResponse>(response, HttpStatus.OK);
    }

    /**
     * Combines details from the Survey Manager and survey system user details.
     * @param smUsers The Survey Manager user details.
     * @param surveyUsers The survey system user details.
     * @return The combined list of user details.
     */
    protected List<UserDetails> combineDetails(final List<UserDetails> smUsers, final List<UserDetails> surveyUsers) {
        return combineDetails(smUsers, surveyUsers, true);
    }

    /**
     * Combines detaila from the Survey Manager and survey system user details.
     * @param smUsers The survey Manager user details.
     * @param surveyUsers The survey system user details.
     * @param includeSurveyOnly Include also the ones that only exists in survey system.
     * @return The combined list of user details.
     */
    protected List<UserDetails> combineDetails(final List<UserDetails> smUsers, final List<UserDetails> surveyUsers,
            boolean includeSurveyOnly) {
        final List<UserDetails> users = new ArrayList<>();
        // first fulfill the roles from survey system for the users who exists
        // in both systems
        for (final UserDetails details : smUsers) {
            for (final UserDetails surveyDetails : surveyUsers) {
                if (details.getPrincipalId().equals(surveyDetails.getSurveyPrincipalId())
                        || surveyDetails.getSurveyPrincipalId().equals(details.getSurveyPrincipalId())) {
                    final Map<String, String> surveyAttributes = surveyDetails.getAttributes();
                    for (final String key : surveyAttributes.keySet()) {
                        log.trace("Adding a value for key {}", key);
                        details.getAttributes().put(key, surveyAttributes.get(key));
                    }
                    final Set<String> surveyRoles = surveyDetails.getRoles();
                    for (final String role : surveyRoles) {
                        log.trace("Adding a role {}", role);
                        details.getRoles().add(role);
                    }
                    details.setSurveyPrincipalId(surveyDetails.getSurveyPrincipalId());
                }
            }
            users.add(details);
        }
        if (includeSurveyOnly) {
            // then fulfill the survey users who didn't exist in the
            // surveymanager db
            for (final UserDetails details : surveyUsers) {
                boolean found = false;
                for (final UserDetails smDetails : smUsers) {
                    if (details.getSurveyPrincipalId().equals(smDetails.getPrincipalId())) {
                        found = true;
                    }
                }
                if (!found) {
                    log.debug("User {} only found in the survey user database", details.getSurveyPrincipalId());
                    users.add(details);
                } else {
                    log.trace("User {} already found in both systems", details.getPrincipalId());
                }
            }
        }
        return users;
    }
}
