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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.geant.sat.api.dto.AnswerDetails;
import org.geant.sat.api.dto.AnswersResponse;
import org.geant.sat.api.dto.ListUsersResponse;
import org.geant.sat.api.dto.QuestionDetails;
import org.geant.sat.api.dto.QuestionsResponse;
import org.geant.sat.api.dto.SurveyDetails;
import org.geant.sat.api.dto.SurveyTokenDetails;
import org.geant.sat.api.dto.ListAllSurveysResponse;
import org.geant.sat.api.dto.ListSurveyTokensResponse;
import org.geant.sat.api.dto.UserDetails;
import org.geant.sat.api.dto.lime.LimeStatusResponse;
import org.geant.sat.api.dto.lime.LimeStatusResponse.Status;
import org.geant.sat.api.dto.lime.ListQuestionsResponse;
import org.geant.sat.api.dto.lime.ListSurveysResponse;
import org.geant.sat.api.dto.lime.ParticipantOverview;
import org.geant.sat.api.dto.lime.ListLimeUsersResponse;
import org.geant.sat.api.dto.lime.ListParticipantsResponse;
import org.geant.sat.api.dto.lime.AbstractLimeSurveyResponse;
import org.geant.sat.api.dto.lime.AddParticipantsResponse;
import org.geant.sat.api.dto.lime.LimePermission;
import org.geant.sat.api.dto.lime.LimeQuestionDetails;
import org.geant.sat.api.dto.lime.StringResultResponse;
import org.geant.sat.api.dto.lime.SurveyOverview;
import org.geant.sat.api.dto.lime.SurveyPropertiesResponse;
import org.joda.time.DateTime;
import org.geant.sat.api.dto.lime.LimeUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * A {@link SurveySystemConnector} for using Limesurvey as the survey system.
 */
public class LimeSurveyConnector implements SurveySystemConnector {

    /** The prefix for all the attributes stored in the Limesurvey database. */
    public static final String ATTRIBUTE_NAME_PREFIX = "lime_";

    /** The attribute name for full name in the Limesurvey database. */
    public static final String ATTRIBUTE_NAME_FULL_NAME = ATTRIBUTE_NAME_PREFIX + "fullName";

    /** The attribute name for email address in the Limesurvey database. */
    public static final String ATTRIBUTE_NAME_EMAIL = ATTRIBUTE_NAME_PREFIX + "email";

    /** The attribute name for creation time in the Limesurvey database. */
    public static final String ATTRIBUTE_NAME_CREATED = ATTRIBUTE_NAME_PREFIX + "created";

    /** The attribute name for last modified time in the Limesurvey database. */
    public static final String ATTRIBUTE_NAME_MODIFIED = ATTRIBUTE_NAME_PREFIX + "modified";

    /** The attribute name for language in the Limesurvey database. */
    public static final String ATTRIBUTE_NAME_LANGUAGE = ATTRIBUTE_NAME_PREFIX + "language";
    
    /** The first name injected aside of the token. */
    public static final String PARTICIPANT_FIRST_NAME = "Managed by SAT";

    /** The last name injected aside of the token. */
    public static final String PARTICIPANT_LAST_NAME = "Managed by SAT";

    /** The email address injected aside of the token. */
    public static final String PARTICIPANT_EMAIL = "noreply@invalid.org";
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(LimeSurveyConnector.class);

    /** The username used for authenticating to Limesurvey API. */
    private String apiUser;

    /** The password used for authenticating to Limesurvey API. */
    private String apiPassword;

    /** The endpoint URL to Limesurvey API. */
    private String apiEndpoint;

    /** The session key (obtained after successful authentication). */
    private String sessionKey;
    
    /** The maximum amount of entries returned by list_participants. */
    private int maxParticipants = 1000;

    /**
     * Constructor.
     */
    public LimeSurveyConnector() {
        sessionKey = null;
    }

    /**
     * Get the username used for authenticating to Limesurvey API.
     * 
     * @return The username used for authenticating to Limesurvey API.
     */
    public String getApiUser() {
        return apiUser;
    }

    /**
     * Set the username used for authenticating to Limesurvey API.
     * 
     * @param newApiUser
     *            What to set.
     */
    public void setApiUser(String newApiUser) {
        this.apiUser = newApiUser;
    }

    /**
     * Get the password used for authenticating to Limesurvey API.
     * 
     * @return The password used for authenticating to Limesurvey API.
     */
    public String getApiPassword() {
        return apiPassword;
    }

    /**
     * Set the password used for authenticating to Limesurvey API.
     * 
     * @param newApiPassword
     *            What to set.
     */
    public void setApiPassword(String newApiPassword) {
        this.apiPassword = newApiPassword;
    }

    /**
     * Get the endpoint URL to Limesurvey API.
     * 
     * @return The endpoint URL to Limesurvey API.
     */
    public String getApiEndpoint() {
        return apiEndpoint;
    }

    /**
     * Set the endpoint URL to Limesurvey API.
     * 
     * @param newApiEndpoint
     *            What to set.
     */
    public void setApiEndpoint(String newApiEndpoint) {
        this.apiEndpoint = newApiEndpoint;
    }
    
    /**
     * Set the maximum amount of entries returned by list_participants.
     * 
     * @param participants What to set.
     */
    public void setMaxParticipants(int participants) {
        maxParticipants = participants;
    }

    /** {@inheritDoc} */
    public ListAllSurveysResponse listSurveys() {
        final ListAllSurveysResponse response = new ListAllSurveysResponse();

        try {
            final ListSurveysResponse surveys = fetchSurveys();
            if (surveys != null) {
                response.setErrorMessage(surveys.getErrorMessage());
                final SurveyOverview[] overviews = surveys.getSurveys();
                final List<SurveyDetails> details = new ArrayList<>();
                final ListLimeUsersResponse usersResponse = fetchUsers();
                if (usersResponse != null) {
                    final LimeUserDetails[] users = usersResponse.getUsers();
                    for (int i = 0; i < overviews.length; i++) {
                        final SurveyDetails newDetails = new SurveyDetails();
                        if ("Y".equalsIgnoreCase(overviews[i].getActive())) {
                            if (isExpired(overviews[i].getExpires())) {
                                log.debug("The survey is active, but expired");
                                newDetails.setActive(false);
                            } else {
                                log.debug("The survey is active and not expired");
                                newDetails.setActive(true);
                            }
                        } else {
                            log.debug("The survey is not active, expiration not checked");
                            newDetails.setActive(false);
                        }
                        newDetails.setSid(overviews[i].getSid());
                        newDetails.setTitle(overviews[i].getTitle());
                        final String owner = getOwner(overviews[i].getSid());
                        newDetails.setOwner(getUsername(users, owner));
                        details.add(newDetails);
                    }
                }
                response.setSurveys(details);
            } else {
                response.setErrorMessage("Could not find any surveys from the backend");
            }
        } catch (SurveySystemConnectorException e) {
            log.error("Could not fetch surveys from Limesurvey", e);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    /** {@inheritDoc} */
    public ListSurveyTokensResponse listSurveyTokens(final String sid) {
        final ListSurveyTokensResponse response = new ListSurveyTokensResponse();
        try {
            final ListParticipantsResponse participants = fetchParticipants(sid);
            for (final ParticipantOverview participant : participants.getParticipants()) {
                final SurveyTokenDetails details = new SurveyTokenDetails();
                details.setSurveyId(sid);
                details.setCompleted("Y".equalsIgnoreCase(participant.getCompleted()));
                details.setToken(participant.getToken());
                response.getTokens().add(details);
            }
        } catch (SurveySystemConnectorException e) {
            log.error("Could not fetch questions from Limesurvey", e);
            response.setErrorMessage(e.getMessage());
        }   
        return response;
    }

    /** {@inheritDoc} */
    public QuestionsResponse listQuestions(final String sid) {
        final QuestionsResponse response = new QuestionsResponse();
        try {
            final ListQuestionsResponse questions = fetchQuestions(sid);
            if (questions != null) {
                response.setErrorMessage(questions.getErrorMessage());
                final LimeQuestionDetails[] limeDetails = questions.getQuestions();
                final List<QuestionDetails> responseDetails = new ArrayList<>();
                for (int i = 0; i < limeDetails.length; i++) {
                    final QuestionDetails newDetails = new QuestionDetails();
                    if ("Y".equalsIgnoreCase(limeDetails[i].getMandatory())) {
                        newDetails.setMandatory(true);
                    } else {
                        newDetails.setMandatory(false);
                    }
                    newDetails.setGid(limeDetails[i].getGid());
                    newDetails.setQid(limeDetails[i].getQid());
                    newDetails.setLanguage(limeDetails[i].getLanguage());
                    newDetails.setParentQid(limeDetails[i].getParentQid());
                    newDetails.setQuestion(limeDetails[i].getQuestion());
                    newDetails.setQuestionOrder(limeDetails[i].getQuestionOrder());
                    newDetails.setSid(sid);
                    newDetails.setTitle(limeDetails[i].getTitle());
                    newDetails.setType(limeDetails[i].getType());
                    responseDetails.add(newDetails);
                }
                response.setQuestions(responseDetails);
            } else {
                response.setErrorMessage("Could not find any questions from the backend");
            }
        } catch (SurveySystemConnectorException e) {
            log.error("Could not fetch questions from Limesurvey", e);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /** {@inheritDoc} */
    public AnswersResponse listAnswers(final String sid) throws SurveySystemConnectorException {
        final AnswersResponse response = new AnswersResponse();
        final String csv = fetchAnswers(sid);
        if (csv == null) {
            throw new SurveySystemConnectorException("Could not find any answers");
        }
        final StringTokenizer lines = new StringTokenizer(csv, "\n");
        final String headerLine = lines.nextToken();
        // the CSV format is "value1","value2","value3",..
        final StringTokenizer headerTokenizer = new StringTokenizer(headerLine.substring(1, headerLine.length() - 1),
                "\",\"");
        for (int i = 0; i < 9; i++) {
            final String header = headerTokenizer.nextToken();
            log.trace("Ignoring header token {}", header);
        }
        final List<String> questionTitles = new ArrayList<>();
        while (headerTokenizer.hasMoreTokens()) {
            final String header = headerTokenizer.nextToken();
            log.trace("Parsed question title {}", header);
            questionTitles.add(header);
        }
        log.debug("Parsed {} question titles", questionTitles.size());
        final List<AnswerDetails> answers = new ArrayList<>();
        while (lines.hasMoreTokens()) {
            answers.add(parseAnswerLine(lines.nextToken(), questionTitles));
        }
        response.setAnswers(answers);
        return response;
    }

    /** {@inheritDoc} */
    public ListUsersResponse listUsers() {
        final ListUsersResponse response = new ListUsersResponse();
        final List<UserDetails> users = new ArrayList<>();
        try {
            ListLimeUsersResponse limeUsers = fetchUsers();
            for (final LimeUserDetails limeDetails : limeUsers.getUsers()) {
                final UserDetails details = new UserDetails();
                details.setSurveyPrincipalId(limeDetails.getUsername());
                details.setAttributes(parseUserAttributes(limeDetails));
                details.setRoles(parseUserRoles(limeDetails));
                users.add(details);
            }
            response.setUsers(users);
        } catch (SurveySystemConnectorException e) {
            log.error("Could not fetch users from Limesurvey", e);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /** {@inheritDoc} */
    public void updateSurveyDetails(final SurveyDetails survey) throws SurveySystemConnectorException {
        final String sid = survey.getSid();
        final ListSurveysResponse response = fetchSurveys();
        for (SurveyOverview overview : response.getSurveys()) {
            if (sid.equals(overview.getSid())) {
                if ("N".equalsIgnoreCase(overview.getActive())) {
                    if (survey.getActive()) {
                        log.debug("Activating the survey {}", sid);
                        //TODO: check answer contents
                        final String contents = getContents("activate_survey", "\"" + sid + "\"", true);
                    }
                } else {
                    final DateTime expiration = getDateFromTimestamp(overview.getExpires());
                    log.debug("Parsed the following expiration date {}", expiration);
                    if (survey.getActive()) {
                        if (expiration != null && expiration.isBeforeNow()) {
                            log.debug("Removing the expiration timestamp for survey {}", sid);
                            //TODO: check answer contents
                            final String contents = getContents("set_survey_properties",
                                    "\"" + sid + "\", { \"expires\": null} ", true);
                        }
                    } else {
                        if (expiration == null || expiration.isAfterNow()) {
                            log.debug("Adding the expiration timestamp for survey {}", sid);
                            //TODO: check answer contents
                            final String contents = getContents("set_survey_properties",
                                    "\"" + sid + "\", { \"expires\": \"" + getCurrentTimestamp() + "\"} ", true);
                        }
                    }
                }
                return;
            }
        }
        throw new SurveySystemConnectorException("Survey " + sid + " not found to be updated!");
    }
    
    /**
     * Creates a new token for the given survey to the survey management system.
     * @param sid The survey identifier.
     * @return The generated token.
     * @throws SurveySystemConnectorException If the token cannot be generated.
     */
    public String generateToken(final String sid) throws SurveySystemConnectorException {
        log.debug("Adding a token for survey {}", sid);
        final AddParticipantsResponse response = getContents("add_participants", "\"" + sid + "\", [{\"firstname\":\"" 
                + PARTICIPANT_FIRST_NAME + "\",\"lastname\":\"" + PARTICIPANT_LAST_NAME + "\",\"email\":\""
                + PARTICIPANT_EMAIL + "\",\"emailstatus\":\"OK\"}]", new AddParticipantsResponse(), true);
/*        log.trace("Contents {}", contents);
        Gson gson = new Gson();
        final AddParticipantsResponse response = gson.fromJson(contents, AddParticipantsResponse.class);*/
        if (response.getErrorMessage() != null) {
            log.error("Found error from Limesurvey API: {}", response.getErrorMessage());
            throw new SurveySystemConnectorException(response.getErrorMessage());
        }
        if (response.getParticipantDetails() == null || response.getParticipantDetails().isEmpty()) {
            log.error("No participant details found from the response");
            throw new SurveySystemConnectorException("No participant details found from the response");
        }
        return response.getParticipantDetails().get(0).getToken();
    }

    /**
     * Get the current timestamp in Limesurvey format.
     * @return The current timestamp in Limesurvey format.
     */
    protected static String getCurrentTimestamp() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        final String timestamp = dateFormat.format(new Date());
        return timestamp;
    }

    /**
     * Get the given Limesurvey-formatted timestamp in {@link DateTime} format.
     * @param timestamp The Limesurvey-formatted timestamp.
     * @return The given Limesurvey-formatted timestamp in {@link DateTime} format.
     */
    protected static DateTime getDateFromTimestamp(final String timestamp) {
        if (timestamp == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        try {
            return new DateTime(dateFormat.parse(timestamp));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Checks whether the given Limesurvey-formatted timestamp is in the past or not. Empty or nulls are not.
     * @param timestamp The Limesurvey-formatted timestamp.
     * @return True if the timestamp was in the past, false otherwise.
     */
    protected static boolean isExpired(final String timestamp) {
        if (timestamp == null) {
            return false;
        }
        final DateTime dateTime = getDateFromTimestamp(timestamp);
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBeforeNow();
    }

    /**
     * Parses the selected attributes from the given details into a map of
     * attributes.
     * 
     * @param limeDetails
     *            The user details in the Limesurvey database.
     * @return The map of selected attributes and their values.
     */
    protected Map<String, String> parseUserAttributes(final LimeUserDetails limeDetails) {
        final Map<String, String> attributes = new HashMap<>();
        addIfValueNotEmpty(attributes, ATTRIBUTE_NAME_FULL_NAME, limeDetails.getFullName());
        addIfValueNotEmpty(attributes, ATTRIBUTE_NAME_EMAIL, limeDetails.getEmail());
        addIfValueNotEmpty(attributes, ATTRIBUTE_NAME_LANGUAGE, limeDetails.getLanguage());
        addIfValueNotEmpty(attributes, ATTRIBUTE_NAME_CREATED, limeDetails.getCreated());
        addIfValueNotEmpty(attributes, ATTRIBUTE_NAME_MODIFIED, limeDetails.getModified());
        return attributes;
    }

    /**
     * Adds a new attribute key and value to the given map, if the value is not
     * empty.
     * 
     * @param attributes
     *            The map of attributes.
     * @param key
     *            The key for the attribute.
     * @param value
     *            The value for the attribute.
     */
    protected void addIfValueNotEmpty(final Map<String, String> attributes, final String key, final String value) {
        if (value != null && value.length() > 0) {
            attributes.put(key, value);
        }
    }

    /**
     * Parses the limesurvey global roles into a set of roles.
     * 
     * @param limeDetails
     *            The user details in the Limesurvey database.
     * @return The set of global roles.
     */
    protected Set<String> parseUserRoles(final LimeUserDetails limeDetails) {
        final LimePermission[] permissions = limeDetails.getPermissions();
        if (permissions == null) {
            return null;
        }
        final Set<String> roles = new HashSet<>();
        for (final LimePermission permission : permissions) {
            if ("global".equalsIgnoreCase(permission.getEntity())) {
                roles.add(permission.getPermission());
                log.debug("Added global permission {} as role", permission.getPermission());
            } else {
                log.trace("Ignored non-global ({}) permission {}", permission.getEntity(), permission.getPermission());
            }
        }
        return roles;
    }

    /**
     * Parses a CSV line separated by '\",' -strings.
     * 
     * @param line
     *            The line to be parsed.
     * @param questionTitles
     *            The list of question titles.
     * @return The answer details, parsed from the given line.
     */
    protected AnswerDetails parseAnswerLine(final String line, final List<String> questionTitles) {
        log.trace("Parsing line \n{}", line);
        final AnswerDetails details = new AnswerDetails();
        final String[] lineElements = line.substring(0, line.length() - 1).split("\",");
        details.setId(lineElements[0].substring(1));
        log.trace("id = {}", details.getId());
        details.setSubmitDate(lineElements[1].substring(1));
        // skip lastpage (2)
        details.setStartLanguage(lineElements[3].substring(1));
        details.setToken(lineElements[4].substring(1));
        details.setStartDate(lineElements[5].substring(1));
        // skip datestamp (6)
        // skip ipaddrs (7)
        // skip refurl (8)
        final Map<String, String> answers = new HashMap<>();
        for (int i = 0; i < questionTitles.size(); i++) {
            log.trace("Successfully parsed value '{}' for {}", lineElements[i + 9].substring(1), questionTitles.get(i));
            answers.put(questionTitles.get(i), lineElements[i + 9].substring(1));
        }
        details.setAnswers(answers);
        return details;
    }

    /**
     * Get the username for the given user identifier.
     * 
     * @param users
     *            The list of Limesurvey users.
     * @param id
     *            The identifier for the user to be returned.
     * @return The username corresponding to the given identifier.
     */
    protected String getUsername(final LimeUserDetails[] users, final String id) {
        for (final LimeUserDetails user : users) {
            if (id.equals(user.getUid())) {
                return user.getUsername();
            }
        }
        log.warn("Could not find a matching user details for uid {}", id);
        return null;
    }

    /**
     * Parses the response from the Limesurvey API.
     * 
     * @param method
     *            The HTTP method.
     * @param params
     *            The parameters for the request.
     * @param response
     *            The response from the Limesurvey API.
     * @param retry
     *            Whether to try again if first request fails.
     * @param <T>
     *            The format of the response.
     * @return The parsed response.
     * @throws SurveySystemConnectorException
     *             If the communication or response parsing fails.
     */
    protected <T extends AbstractLimeSurveyResponse> T getContents(final String method, final String params, T response,
            boolean retry) throws SurveySystemConnectorException {
        log.trace("Starting getContents for response type {}", response.getClass());
        final String contents = getContents(method, params, true);
        final Gson gson = new Gson();
        try {
            return (T) gson.fromJson(contents, response.getClass());
        } catch (JsonSyntaxException e) {
            log.debug("Could not encode response {} from contents {}", response.getClass(), contents);
            try {
                final LimeStatusResponse statusResponse = gson.fromJson(contents, LimeStatusResponse.class);
                final Status status = statusResponse.getStatus();
                if (status != null) {
                    final String errorMessage = status.getStatus();
                    if (retry && "Invalid session key".equals(errorMessage)) {
                        log.debug("Session key was expired, trying again after authentication");
                        updateSessionKey();
                        return getContents(method, params, response, false);
                    } else {
                        log.debug("Parsed error {} from the response", errorMessage);
                        response.setErrorMessage(errorMessage);
                        return response;
                    }
                } else {
                    log.error("Could not parse status or {} from {}", response.getClass(), contents);
                }
            } catch (JsonSyntaxException jse) {
                log.error("Could not parse the contents {}", contents);
            }
        }
        return null;
    }

    /**
     * Fetches the surveys from Limesurvey.
     * 
     * @return The list of surveys.
     * @throws SurveySystemConnectorException
     *             If the communication fails.
     */
    protected ListSurveysResponse fetchSurveys() throws SurveySystemConnectorException {
        return getContents("list_surveys", null, new ListSurveysResponse(), true);
    }

    /**
     * Fetches the users from Limesurvey.
     * 
     * @return The list of users.
     * @throws SurveySystemConnectorException
     *             If the communication fails.
     */
    protected ListLimeUsersResponse fetchUsers() throws SurveySystemConnectorException {
        return getContents("list_users", null, new ListLimeUsersResponse(), true);
    }
    
    /**
     * Fetches the participants from Limesurvey for the given survey.
     * @param sid The survey identifier.
     * @return The list of participants.
     * @throws SurveySystemConnectorException If the operation fails.
     */
    protected ListParticipantsResponse fetchParticipants(final String sid) throws SurveySystemConnectorException {
        return getContents("list_participants", sid + ", 0, " + maxParticipants + ", false, [ \"completed\" ]", 
                new ListParticipantsResponse(), true);
    }

    /**
     * Fetches the answers from Limesurvey.
     * 
     * @param sid
     *            The survey identifier.
     * @return The answers in CSV.
     * @throws SurveySystemConnectorException
     *             If the communication fails.
     */
    protected String fetchAnswers(final String sid) throws SurveySystemConnectorException {
        final StringResultResponse stringResponse = getContents("export_responses", "\"" + sid + "\", \"csv\"",
                new StringResultResponse(), true);
        if (stringResponse != null && stringResponse.getStringValue() != null) {
            final String decoded = new String(Base64.decodeBase64(stringResponse.getStringValue()));
            log.trace("Decoded: {}", decoded);
            return decoded;
        }
        return null;
    }

    /**
     * Get the status -value from the given contents, if it exists.
     * 
     * @param contents
     *            The JSON contents in raw string.
     * @return The status if it exists, null otherwise.
     */
    protected String getStatusIfExists(final String contents) {
        try {
            Gson gson = new Gson();
            LimeStatusResponse statusResponse = gson.fromJson(contents, LimeStatusResponse.class);
            if (statusResponse.getStatus() != null) {
                return statusResponse.getStatus().getStatus();
            }
        } catch (JsonSyntaxException e) {
            log.warn("Could not parse status", e);
        }
        return null;
    }

    /**
     * Fetches the questions from Limesurvey.
     * 
     * @param sid
     *            The survey identifier.
     * @return The list of questions.
     * @throws SurveySystemConnectorException
     *             If the communication fails.
     */
    protected ListQuestionsResponse fetchQuestions(final String sid) throws SurveySystemConnectorException {
        final String contents = getContents("list_questions", "\"" + sid + "\"", true);
        Gson gson = new Gson();
        return gson.fromJson(contents, ListQuestionsResponse.class);
    }

    /**
     * Get the owner identifier for a survey.
     * 
     * @param sid
     *            The survey identifier.
     * @return The owner identifier.
     * @throws SurveySystemConnectorException
     *             If the communication fails.
     */
    protected String getOwner(final String sid) throws SurveySystemConnectorException {
        final String contents = getContents("get_survey_properties", "\"" + sid + "\", [\"owner_id\", \"admin\"]",
                true);
        Gson gson = new Gson();
        return gson.fromJson(contents, SurveyPropertiesResponse.class).getOverview().getOwnerId();
    }

    /**
     * Contacts Limesurvey API with the given method and parameters.
     * 
     * @param method
     *            The Limesurvey method.
     * @param params
     *            The Limesurvey parameters.
     * @param addSessionKey
     *            Whether or not to add the session key to the request.
     * @return The response contents as raw string if the status code was 200.
     * @throws SurveySystemConnectorException
     *             If the communication fails (response code not 200).
     */
    protected String getContents(final String method, final String params, boolean addSessionKey)
            throws SurveySystemConnectorException {
        if (addSessionKey && sessionKey == null) {
            log.debug("Updating session key");
            updateSessionKey();
        }
        try (final CloseableHttpClient httpClient = HttpClientBuilder.buildClient()) {
            HttpPost post = new HttpPost(apiEndpoint);
            post.setHeader("Content-type", "application/json");
            final String query;
            if (addSessionKey) {
                if (params != null) {
                    query = "{\"method\": \"" + method + "\", \"params\": [ \"" + sessionKey + "\", " + params
                            + " ], \"id\": 1}";
                } else {
                    query = "{\"method\": \"" + method + "\", \"params\": [ \"" + sessionKey + "\" ], \"id\": 1}";
                }
            } else {
                query = "{\"method\": \"" + method + "\", \"params\": [ " + params + " ], \"id\": 1}";
            }
            post.setEntity(new StringEntity(query));
            log.debug("Sending query {}", query);
            final HttpResponse response = httpClient.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            log.debug("Response code {} from {}", statusCode, method);
            if (statusCode == 200) {
                final HttpEntity entity = response.getEntity();
                final String contents = EntityUtils.toString(entity);
                log.trace("Got the following response contents from {}: {}", method, contents);
                return contents;
            }
            throw new SurveySystemConnectorException("Unexpected status code: " + statusCode);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SurveySystemConnectorException(e);
        }
    }

    /**
     * Updates the session key, i.e. authenticates to Limesurvey.
     * 
     * @throws SurveySystemConnectorException
     *             If the communication fails.
     */
    protected void updateSessionKey() throws SurveySystemConnectorException {
        final String contents = getContents("get_session_key", "\"" + apiUser + "\", \"" + apiPassword + "\"", false);
        final String newSessionKey = getSessionKey(contents);
        if (newSessionKey != null) {
            log.debug("Successfully parsed session key {}", newSessionKey);
            this.sessionKey = newSessionKey;
            return;
        }
        try {
            Gson gson = new Gson();
            final String errorMessage = gson.fromJson(contents, LimeStatusResponse.class).getStatus().getStatus();
            throw new SurveySystemConnectorException(errorMessage);
        } catch (JsonSyntaxException e) {
            log.error("Could not get session_key from Limesurvey", e);
            throw new SurveySystemConnectorException(e.getMessage(), e);
        }
    }

    /**
     * Parses the session key from the contents.
     * 
     * @param contents
     *            The JSON contents in raw string.
     * @return The session key.
     * @throws SurveySystemConnectorException
     *             If the session key cannot be parsed.
     */
    protected String getSessionKey(final String contents) throws SurveySystemConnectorException {
        Gson gson = new Gson();
        try {
            return gson.fromJson(contents, StringResultResponse.class).getStringValue();
        } catch (JsonSyntaxException e) {
            log.trace("Could not read session key from the contents", e);
            throw getSessionKeyFailed(contents);
        }
    }

    /**
     * Parses the error message if it's found from the contents.
     * 
     * @param contents
     *            The JSON contents in raw string.
     * @return The exception containing the parsed error message if it was
     *         found.
     */
    protected SurveySystemConnectorException getSessionKeyFailed(final String contents) {
        Gson gson = new Gson();
        try {
            final String errorMessage = gson.fromJson(contents, LimeStatusResponse.class).getStatus().getStatus();
            log.trace("Successfully parsed error message {}", errorMessage);
            return new SurveySystemConnectorException(errorMessage);
        } catch (JsonSyntaxException e) {
            log.trace("Could not error message from the contents {}", contents, e);
            return new SurveySystemConnectorException(e);
        }
    }
}
