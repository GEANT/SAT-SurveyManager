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

package org.geant.sat.api.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import org.geant.sat.api.EntityImporter;
import org.geant.sat.api.HttpClientBuilder;
import org.geant.sat.api.dto.AssessorDetails;
import org.geant.sat.api.dto.EntityDetails;
import org.geant.sat.api.dto.ListEntitiesResponse;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.ext.saml2mdui.Description;
import org.opensaml.saml.ext.saml2mdui.UIInfo;
import org.opensaml.saml.metadata.resolver.impl.HTTPMetadataResolver;
import org.opensaml.saml.saml2.metadata.ContactPerson;
import org.opensaml.saml.saml2.metadata.EmailAddress;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.Extensions;
import org.opensaml.saml.saml2.metadata.GivenName;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml.saml2.metadata.SSODescriptor;
import org.opensaml.saml.saml2.metadata.SurName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.resolver.ResolverException;
import net.shibboleth.utilities.java.support.xml.ParserPool;

/**
 * SAML metadata implementation for {@link EntityImporter}. The importInput field is expected to be URL for a
 * SAML metadata feed.
 */
public class Saml2EntityImporter implements EntityImporter {
    
    /** The default language to parsed from the UI info description. */
    public static final String DEFAULT_UI_INFO_LANGUAGE = "en";
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(Saml2EntityImporter.class);
    
    /** The Open SAML parser pool. */
    private ParserPool parserPool;
    
    /** The identifier for the entity importer. */
    private String id;
    
    /** The description for the input field. */
    private String inputDescription;
    
    /**
     * Constructor.
     */
    public Saml2EntityImporter() {
        log.trace("Initializing the Open SAML library");
        try {
            InitializationService.initialize();
        } catch (InitializationException e) {
            log.error("Could not initialize Open SAML" ,e);
        }
    }
    
    /**
     * Set the Open SAML parser pool.
     * @param pool What to set.
     */
    public void setParserPool(final ParserPool pool) {
        parserPool = pool;
    }
    
    /** {@inheritDoc} */
    public ListEntitiesResponse preview(final String id, final Object importInput, final String creator) {
        final ListEntitiesResponse response = new ListEntitiesResponse();
        final String metadataUrl = (String)importInput;
        final HTTPMetadataResolver resolver;
        try {
            resolver = new HTTPMetadataResolver(HttpClientBuilder.buildClient(), metadataUrl);
            resolver.setId("satMetadataResolver");
            resolver.setParserPool(parserPool);
            resolver.initialize();
        } catch (ComponentInitializationException | KeyManagementException | NoSuchAlgorithmException 
                | KeyStoreException | ResolverException e) {
            log.error("Could not initialize the metadata resolver", e);
            response.setErrorMessage("Could not contact the remote endpoint: " + e.getMessage());
            return response;
        }
        final Iterator<EntityDescriptor> iterator = resolver.iterator();
        if (iterator == null || !iterator.hasNext()) {
            log.error("Could not parse any entities from the remote endpoint {}", metadataUrl);
            response.setErrorMessage("Could not parse any entities from the remote endpoint");
            return response;
        }
        while (iterator.hasNext()) {
            final EntityDescriptor descriptor = iterator.next();
            final EntityDetails entity = new EntityDetails();
            entity.setCreator(creator);
            entity.setName(descriptor.getEntityID());
            final List<XMLObject> children = descriptor.getOrderedChildren();
            final IDPSSODescriptor idpDescriptor = descriptor.getIDPSSODescriptor(SAMLConstants.SAML20P_NS);
            final String feedDescription = "Imported from " + id;
            if (idpDescriptor != null) {
                log.trace("Found IDP descriptor");
                entity.setDescription(getUIDescription(idpDescriptor, feedDescription));
            }
            final SPSSODescriptor spDescriptor = descriptor.getSPSSODescriptor(SAMLConstants.SAML20P_NS);
            if (spDescriptor != null) {
                log.trace("Found SP descriptor");
                entity.setDescription(getUIDescription(spDescriptor, feedDescription));
            }
            if (entity.getDescription() == null) {
                entity.setDescription(feedDescription);
            }
            for (final XMLObject child : children) {
                if (child != null) {
                    if (child.getElementQName().equals(ContactPerson.DEFAULT_ELEMENT_NAME)) {
                        final AssessorDetails assessor = new AssessorDetails();
                        final ContactPerson contactPerson = (ContactPerson) child;
                        final GivenName givenName = contactPerson.getGivenName();
                        final String description = (givenName != null && givenName.getName() != null) ? 
                                givenName.getName() : "";
                        final SurName surName = contactPerson.getSurName();
                        assessor.setDescription(description.concat((surName != null && surName.getName() != null) ? " "
                                + surName.getName() : ""));
                        final List<EmailAddress> emails = contactPerson.getEmailAddresses();
                        if (emails != null) {
                            for (final EmailAddress email : emails) {
                                String address = email.getAddress();
                                if (address != null) {
                                    if (address.startsWith("mailto:")) {
                                        address = address.substring(7);
                                    }
                                    log.debug("Email address to be added: {}", address);
                                    assessor.setType("email");
                                    assessor.setValue(address);
                                    entity.getAssessors().add(assessor);
                                } else {
                                    log.debug("Skipped empty email address");
                                }
                            }
                        }
                    } else {
                        log.debug("Skipping {}", child.getElementQName());
                    }
                }
            }
            response.getEntities().add(entity);
            log.trace("Added {}", entity);
        }
        resolver.destroy();
        return response;
    }

    /**
     * Gets the (preferably English) UI description from the given {@link SSODescriptor}. If no English description
     * was not found, then first description is returned.
     * @param ssoDescriptor The descriptor whose description is to be returned.
     * @param feedDescription The metadata feed description to be appended after the UI description, or simply as the
     * description if UI description was not found.
     * @return The (preferably English) UI description of the given descriptor, or empty String if none was found.
     */
    protected String getUIDescription(final SSODescriptor ssoDescriptor, final String feedDescription) {
        if (ssoDescriptor != null) {
            final Extensions extensions = ssoDescriptor.getExtensions();
            log.trace("Extensions {}", extensions);
            if (extensions != null) {
                for (final XMLObject child : extensions.getOrderedChildren()) {
                    if (child != null && child.getElementQName().equals(UIInfo.DEFAULT_ELEMENT_NAME)) {
                        log.debug("Found UI!");
                        List<Description> descriptions = ((UIInfo) child).getDescriptions();
                        if (descriptions != null && !descriptions.isEmpty()) {
                            for (final Description description : descriptions) {
                                if (description.getXMLLang() != null 
                                        && description.getXMLLang().equals(DEFAULT_UI_INFO_LANGUAGE)) {
                                    log.debug("Found English description {}", description.getValue());
                                    return description.getValue() + " (" + feedDescription + ")";
                                }
                            }
                            log.debug("Did not find English description, returning {}", descriptions.get(0).getValue());
                            return descriptions.get(0).getValue() + " (" + feedDescription + ")";
                        } else {
                            log.debug("List of descriptions is empty");
                        }
                    } else {
                        log.debug("Skipping {}", child.getElementQName());
                    }
                }
            }
        }
        log.debug("Could not find description");
        return feedDescription;
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return id;
    }
    
    /**
     * Set the identifier for the entity importer.
     * @param newId What to set.
     */
    public void setId(final String newId) {
        id = newId;
    }

    /** {@inheritDoc} */
    @Override
    public String getInputDescription() {
        return inputDescription;
    }
    
    /**
     * Set the description for the input field.
     * @param description What to set.
     */
    public void setInputDescription(final String description) {
        inputDescription = description;
    }
}
