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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Authentication provider validating the incoming username/password token.
 */
@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(RestAuthenticationProvider.class);
    
    /** The user database. */
    private Map<String, String> userDatabase = new HashMap<String, String>();
    
    /**
     * Set the user database.
     * @param users What to set.
     */
    public void setUserDatabase(final Map<String, String> users) {
        userDatabase = Constraint.isNotNull(users, "The user database cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Starting authentication of: {}", authentication.toString());
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            final String username = (String) token.getPrincipal();
            final String password = (String) token.getCredentials();
            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                final RestAuthenticationToken restToken = new RestAuthenticationToken(authentication.getName());
                SecurityContextHolder.getContext().setAuthentication(restToken);
                return SecurityContextHolder.getContext().getAuthentication();                
            }
            throw new BadCredentialsException("The user " + username + " cannot be authenticated: invalid username/password");
        } else {
            log.warn("Unsupported type of authentication token : {}", authentication.getClass());
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean supports(Class<?> authentication) {
        log.trace("Supports {}", authentication.getCanonicalName());
        if (authentication == UsernamePasswordAuthenticationToken.class) {
            return true;
        }
        log.debug("Does not support {}", authentication.getCanonicalName());
        return false;
    }

}
