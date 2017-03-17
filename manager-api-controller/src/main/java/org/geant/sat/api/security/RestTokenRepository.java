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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Simple token repository, to be replaced.
 */
public class RestTokenRepository implements SecurityContextRepository {

    /** The header name for fetching the token from the request. */
    public static final String API_TOKEN_HEADER = "X-SAT-API-Token";

    /** Cache manager for the stored tokens. */
    private static final CacheManager CACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder()
            .withCache(API_TOKEN_HEADER, CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(String.class, SecurityContext.class, ResourcePoolsBuilder.heap(100))
                    .build())
            .build(true);

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(RestTokenRepository.class);

    /** {@inheritDoc} */
    @Override
    public boolean containsContext(final HttpServletRequest servletRequest) {
        log.debug("containsContext(), returning {}", getToken(servletRequest) != null);
        return getToken(servletRequest) != null;
    }

    /** {@inheritDoc} */
    @Override
    public SecurityContext loadContext(final HttpRequestResponseHolder holder) {
        if (containsContext(holder.getRequest())) {
            log.debug("Token exists, returning existing context.");
            return getToken(holder.getRequest());
        } else {
            log.debug("Token does not exist, creating empty context.");
            return SecurityContextHolder.createEmptyContext();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void saveContext(final SecurityContext securityContext, final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse) {
        final Authentication authentication = securityContext.getAuthentication();
        if (authentication instanceof RestAuthenticationToken) {
            final String token = (String) ((RestAuthenticationToken) authentication).getDetails();
            getCache().put(token, securityContext);
            log.debug("Stored to the cache with token {}", token);
            servletResponse.addHeader(API_TOKEN_HEADER, token);
        }
    }

    /**
     * Get the token from the servlet request.
     * @param servletRequest The HTTP servlet request.
     * @return The token parsed from the request, null if it doesn't exist.
     */
    protected SecurityContext getToken(final HttpServletRequest servletRequest) {
        final String token = servletRequest.getHeader(API_TOKEN_HEADER);
        if (token != null) {
            SecurityContext element = getCache().get(token);
            return element;
        }
        return null;
    }

    /**
     * Get the cache containing the tokens.
     * @return The cache containing the tokens.
     */
    protected static Cache<String, SecurityContext> getCache() {
        return CACHE_MANAGER.getCache(API_TOKEN_HEADER, String.class, SecurityContext.class);
    }
}