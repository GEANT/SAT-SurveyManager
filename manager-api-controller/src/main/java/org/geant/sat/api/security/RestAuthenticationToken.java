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
package org.geant.sat.api.security;

import java.util.Collection;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * A simple randomly generated authentication token.
 */
@SuppressWarnings("serial")
public class RestAuthenticationToken implements Authentication {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(RestAuthenticationToken.class);

    /** The name for the token holder. */
    private String name;

    /** The shared-secret token. */
    private String token;

    /**
     * Constructor.
     * @param holder The name of the token holder.
     */
    public RestAuthenticationToken(final String holder) {
        this.name = holder;
        this.token = RandomStringUtils.randomAlphabetic(12);
        log.debug("Created random token {}", token);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        log.debug("getName()");
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.debug("getAuthorities()");
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object getCredentials() {
        log.debug("getCredentials()");
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDetails() {
        log.debug("getDetails() returns {}", token);
        return token;
    }

    /** {@inheritDoc} */
    @Override
    public Object getPrincipal() {
        log.debug("getPrincipal()");
        return new java.security.Principal() {

            /** {@inheritDoc} */
            @Override
            public String getName() {
                return name;
            }

        };
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAuthenticated() {
        log.debug("isAuthenticated()");
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        log.debug("setAuthenticated()");
        // no op
    }

}
