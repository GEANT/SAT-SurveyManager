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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.geant.sat.api.dto.AbstractConnectorResponse;
import org.geant.sat.api.dto.AnswersResponse;
import org.geant.sat.api.dto.AssessorResponse;
import org.geant.sat.api.dto.EntityResponse;
import org.geant.sat.api.dto.ListRolesResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.QuestionsResponse;
import org.geant.sat.api.dto.RoleDetails;
import org.geant.sat.api.dto.RoleResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.api.dto.SurveyResponse;
import org.geant.sat.api.dto.ListAllSurveysResponse;
import org.geant.sat.api.dto.ListAssessorsResponse;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.geant.sat.api.dto.UserDetails;
import org.geant.sat.api.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * This class represents a simple client wrapper to the Survey Manager API.
 */
public class SatApiClient {

    /** The header name carrying the API token. */
    public static final String API_TOKEN_HEADER = "X-SAT-API-Token";

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SatApiClient.class);

    /** The base URL for the Survey Manager API. */
    private final String apiBaseUrl;

    /** The username used for authenticating to Survey Manger API. */
    private String username;

    /** The password used for authentication to Survey Manager API. */
    private String password;

    /** The token obtained after successful authentication. */
    private String token;

    /**
     * Constructor.
     * @param baseUrl The base URL for the Survey Manager API.
     */
    public SatApiClient(final String baseUrl) {
        apiBaseUrl = baseUrl;
        token = null;
    }

    /**
     * Set the username used for authenticating to Survey Manger API.
     * @param name What to set.
     */
    public void setUsername(final String name) {
        username = name;
    }

    /**
     * Set the password used for authenticating to Survey Manger API.
     * @param pwd What to set.
     */
    public void setPassword(final String pwd) {
        password = pwd;
    }

    /**
     * Get all the surveys from the Survey Manager API.
     * @return All the surveys from the Survey Manager API.
     */
    public ListAllSurveysResponse getSurveys() {
        final String url = apiBaseUrl + "/surveys";
        return getResponseWithGet(url, ListAllSurveysResponse.class);
    }

    /**
     * Get all questions for one survey from the Survey Manager API.
     * @param sid The survey identifier.
     * @return All the questions for one survey from the Survey Manager API.
     */
    public QuestionsResponse getQuestions(final String sid) {
        final String url = apiBaseUrl + "/questions?sid=" + sid;
        return getResponseWithGet(url, QuestionsResponse.class);
    }

    /**
     * Get all the answers for one survey from the Survey Manager API.
     * @param sid The survey identifier.
     * @return All the answers for one survey from the Survey Manager API.
     */
    public AnswersResponse getAnswers(final String sid) {
        final String url = apiBaseUrl + "/answers?sid=" + sid;
        return getResponseWithGet(url, AnswersResponse.class);
    }

    /**
     * Get all the users from the Survey Manager API.
     * @return All the users from the Survey Manager API.
     */
    public ListUsersResponse getUsers() {
        final String url = apiBaseUrl + "/users";
        return getResponseWithGet(url, ListUsersResponse.class);
    }

    /**
     * Get all the roles from the Survey Manager API.
     * @return All the roles from the Survey Manager API.
     */
    public ListRolesResponse getRoles() {
        final String url = apiBaseUrl + "/roles";
        return getResponseWithGet(url, ListRolesResponse.class);
    }

    /**
     * Get all the entities from the Survey Manager API.
     * @return All the entities from the Survey Manager API.
     */
    public ListEntitiesResponse getEntities() {
        final String url = apiBaseUrl + "/entities";
        return getResponseWithGet(url, ListEntitiesResponse.class);
    }

    /**
     * Get all the assessors from the Survey Manager API.
     * @return All the assessors from the Survey Manager API.
     */
    public ListAssessorsResponse getAssessors() {
        final String url = apiBaseUrl + "/assessorss";
        return getResponseWithGet(url, ListAssessorsResponse.class);
    }

    /**
     * Creates a new entity to the Survey Manager database.
     * @param name The name of the entity.
     * @param description The description of the entity.
     * @param creator The creator of the entity.
     * @return The details for the entity.
     */
    public EntityResponse createEntity(final String name, final String description, final String creator) {
        final String url = apiBaseUrl + "/entities";
        final HttpPost method = new HttpPost(url);
        final List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("name", name));
        postParameters.add(new BasicNameValuePair("description", description));
        postParameters.add(new BasicNameValuePair("creator", creator));
        try {
            method.setEntity(new UrlEncodedFormEntity(postParameters));
        } catch (UnsupportedEncodingException e) {
            log.error("Could not encode the parameters to the request", e);
            return null;
        }
        method.addHeader("content-type", "application/x-www-form-urlencoded");
        return getResponse(method, EntityResponse.class, true);
    }
    
    /**
     * Creates a new assessor to the Survey Manager database.
     * @param type The type of the assessor.
     * @param value The value of the assessor (corresponding to the type).
     * @param description The description for the assessor.
     * @return The details for the assessor.
     */
    public AssessorResponse createAssessor(final String type, final String value, final String description) {
        final String url = apiBaseUrl + "/assessors";
        final HttpPost method = new HttpPost(url);
        final List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("type", type));
        postParameters.add(new BasicNameValuePair("value", value));
        postParameters.add(new BasicNameValuePair("description", description));
        try {
            method.setEntity(new UrlEncodedFormEntity(postParameters));
        } catch (UnsupportedEncodingException e) {
            log.error("Could not encode the parameters to the request", e);
            return null;
        }
        method.addHeader("content-type", "application/x-www-form-urlencoded");
        return getResponse(method, AssessorResponse.class, true);
    }
    
    /**
     * Get details for one user from the Survey Manager API.
     * @param principalId The principal identifier.
     * @return The details for one user from the Survey Manager API.
     */
    public UserResponse getUser(final String principalId) {
        final String url = apiBaseUrl + "/users/" + principalId;
        return getResponseWithGet(url, UserResponse.class);
    }

    /**
     * Get details for one role from the Survey Manager API.
     * @param name The role name.
     * @return The details for one role from the Survey Manager API.
     */
    public RoleResponse getRole(final String name) {
        final String url = apiBaseUrl + "/roles/" + name;
        return getResponseWithGet(url, RoleResponse.class);
    }

    /**
     * Create a new user to the Survey Manager API.
     * @param details The details for the new user.
     * @return The details for the created user.
     */
    public UserResponse createUser(final UserDetails details) {
        log.trace("Creating a user {}", details.getPrincipalId());
        final String url = apiBaseUrl + "/users/" + details.getPrincipalId();
        final Gson gson = new Gson();
        final String encoded = gson.toJson(details);
        return getResponseWithPost(url, encoded, UserResponse.class, true);
    }

    /**
     * Create a new role to the Survey Manager API.
     * @param details The details for the new role.
     * @return The details for the created role.
     */
    public RoleResponse createRole(final RoleDetails details) {
        final String url = apiBaseUrl + "/roles/" + details.getName();
        final Gson gson = new Gson();
        final String encoded = gson.toJson(details);
        return getResponseWithPost(url, encoded, RoleResponse.class, true);
    }

    /**
     * Update existing user in the Survey Manager API.
     * @param details The details for the user to be updated.
     * @return The details for the updated user.
     */
    public UserResponse updateUser(final UserDetails details) {
        log.trace("Updating a user {}", details.getPrincipalId());
        final String url = apiBaseUrl + "/users/" + details.getPrincipalId();
        final Gson gson = new Gson();
        final String encoded = gson.toJson(details);
        return getResponseWithPut(url, encoded, UserResponse.class, true);
    }

    /**
     * Update existing role in the Survey Manager API.
     * @param details The details for the role to be updated.
     * @return The details for the updated role.
     */
    public RoleResponse updateRole(final RoleDetails details) {
        final String url = apiBaseUrl + "/roles/" + details.getName();
        final Gson gson = new Gson();
        final String encoded = gson.toJson(details);
        return getResponseWithPut(url, encoded, RoleResponse.class, true);
    }
    
    /**
     * Update existing survey in the Survey Manager API.
     * @param details The details for the survey to be updated.
     * @return The details for the updated survey.
     */
    public SurveyResponse updateSurvey(final SurveyDetails details) {
    	final String url = apiBaseUrl + "/surveys/" + details.getSid();
    	final Gson gson = new Gson();
    	final String encoded = gson.toJson(details);
    	return getResponseWithPut(url, encoded, SurveyResponse.class, true);
    }

    /**
     * Get the response from the Survey Manager API with GET-method.
     * @param url The URL to be called.
     * @param clazz The class for the expected response.
     * @param retry Whether or not to try again if fails.
     * @param <T> The expected response type.
     * @return The response from the Survey Manager API.
     */
    protected <T extends AbstractConnectorResponse> T getResponseWithGet(final String url, Class<T> clazz,
            boolean retry) {
        final HttpGet method = new HttpGet(url);
        return getResponse(method, clazz, retry);
    }

    /**
     * Get the response from the Survey Manager API with POST-method.
     * @param url The URL to be called.
     * @param payload The payload to be sent to the Survey Manager API.
     * @param clazz The class for the expected response.
     * @param retry Whether or not to try again if fails.
     * @param <T> The expected response type.
     * @return The response from the Survey Manager API.
     */
    protected <T extends AbstractConnectorResponse> T getResponseWithPost(final String url, final String payload,
            final Class<T> clazz, boolean retry) {
        log.trace("Building a POST call for URL {} with payload {}", url, payload);
        final HttpPost method = new HttpPost(url);
        method.setEntity(new StringEntity(payload, "UTF-8"));
        method.addHeader("content-type", "application/json");
        return getResponse(method, clazz, retry);
    }

    /**
     * Get the response from the Survey Manager API with PUT-method.
     * @param url The URL to be called.
     * @param payload The payload to be sent to the Survey Manager API.
     * @param clazz The class for the expected response.
     * @param retry Whether or not to try again if fails.
     * @param <T> The expected response type.
     * @return The response from the Survey Manager API.
     */
    protected <T extends AbstractConnectorResponse> T getResponseWithPut(final String url, final String payload,
            final Class<T> clazz, boolean retry) {
        log.trace("Building a PUT call for URL {} with payload {}", url, payload);
        final HttpPut method = new HttpPut(url);
        method.setEntity(new StringEntity(payload, "UTF-8"));
        method.addHeader("content-type", "application/json");
        return getResponse(method, clazz, retry);
    }

    /**
     * Get the response from the Survey Manager API with given HTTP-method.
     * @param method The method used for the request.
     * @param clazz The class for the expected response.
     * @param retry Whether or not to try again if fails.
     * @param <T> The expected response type.
     * @return The response from the Survey Manager API.
     */
    protected <T extends AbstractConnectorResponse> T getResponse(final HttpUriRequest method, Class<T> clazz,
            boolean retry) {
        log.trace("Starting the method {} call for {}", method.getMethod(), clazz);
        if (token == null) {
            if (!authenticate()) {
                log.debug("Could not authenticate");
                return null;
            }
        }
        try (final CloseableHttpClient httpClient = HttpClientBuilder.buildClient()) {
            method.addHeader(API_TOKEN_HEADER, token);
            final HttpResponse response = httpClient.execute(method);
            final int statusCode = response.getStatusLine().getStatusCode();
            log.debug("Response code {} from {}", statusCode, method.getURI());
            if (statusCode >= HttpStatus.SC_OK && statusCode < 300) {
                final HttpEntity entity = response.getEntity();
                final String contents = EntityUtils.toString(entity);
                log.trace("Got the following response contents from {}: {}", method.getURI(), contents);
                Gson gson = new Gson();
                return gson.fromJson(contents, clazz);
            }
            if (statusCode == 401 && retry) {
                log.debug("Unauthorized status code, trying to authenticate");
                if (authenticate()) {
                    log.debug("Authentication successful, retrying the API call");
                    return getResponse(method, clazz, false);
                }
                log.warn("Authentication fails with username {}", username);
            }
            log.error("Unexpected response code from the API: {}", statusCode);
            log.debug("The contents {}", EntityUtils.toString(response.getEntity()));
            return buildNewInstance(clazz, "Unexpected response code from the API: " + statusCode);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Error during the API call", e);
            return buildNewInstance(clazz, "Internal server error during the API call");
        }

    }

    /**
     * Builds a new instance of given class with the given error message.
     * @param clazz The class to contain the error message.
     * @param errorMessage The error message to be set to the class.
     * @param <T> The expected response type.
     * @return A new instance of the given class with the given error message.
     */
    protected <T extends AbstractConnectorResponse> T buildNewInstance(final Class<T> clazz,
            final String errorMessage) {
        try {
            final T instance = clazz.newInstance();
            instance.setErrorMessage(errorMessage);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Could not build a new instance of {}", clazz, e);
        }
        return null;
    }

    /**
     * Authenticates the user to the Survey Manager API. The method uses the username and password for
     * authentication, and updates the token class variable after successful authentication.
     * @return True if authentication succeeds, false otherwise.
     */
    protected boolean authenticate() {
        if (username == null || password == null) {
            log.error("Username and password must be configured for authentication");
            return false;
        }
        try (final CloseableHttpClient httpClient = HttpClientBuilder.buildClient()) {
            final String url = apiBaseUrl + "/login";
            final HttpPost method = new HttpPost(url);
            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("username", username));
            parameters.add(new BasicNameValuePair("password", password));
            method.setEntity(new UrlEncodedFormEntity(parameters));
            final HttpResponse response = httpClient.execute(method);
            final int statusCode = response.getStatusLine().getStatusCode();
            log.debug("Response code {} from ", statusCode, url);
            if (statusCode == 200) {
                final HttpEntity entity = response.getEntity();
                final String contents = EntityUtils.toString(entity);
                log.trace("Got the following response contents from {}: {}", url, contents);
                final Header header = response.getFirstHeader(API_TOKEN_HEADER);
                if (header == null || header.getValue() == null) {
                    log.error("Header {} not found", API_TOKEN_HEADER);
                    return false;
                }
                token = header.getValue();
                log.info("Token successfully updated: {}", token);
                return true;
            } else {
                log.error("Unexpected response code from the API: {}", statusCode);
            }
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Error during the API call", e);
        }
        return false;
    }

    /**
     * Get the response from the Survey Manager API with GET-method without retrying if fails.
     * @param url The URL to be called.
     * @param clazz The class for the expected response.
     * @param <T> The expected response type.
     * @return The response from the Survey Manager API.
     */
    protected <T extends AbstractConnectorResponse> T getResponseWithGet(final String url, Class<T> clazz) {
        return getResponseWithGet(url, clazz, true);
    }
}
