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

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * A HTTP client builder tool.
 * 
 * TODO: Configurable TLS options. Currently trusts all servers.
 */
public class HttpClientBuilder {
    
    /**
     * Constructor hidden.
     */
    private HttpClientBuilder() {
        // no op
    }

    /**
     * Builds a new closeable {@link HttpClient}.
     * @return A new closeable {@link HttpClient}.
     * @throws NoSuchAlgorithmException The algorithm is not supported by the platform.
     * @throws KeyStoreException The trust store cannot be loaded.
     * @throws KeyManagementException The trust store cannot be loaded.
     */
    public static CloseableHttpClient buildClient()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(createTrustAll()).build();
        return httpclient;

    }

    /**
     * Builds a new SSL/TLS configuration trusting all the servers. Not to be used in production systems.
     * @return A new SSL/TLS configuration trusting all the servers.
     * @throws NoSuchAlgorithmException The algorithm is not supported by the platform.
     * @throws KeyStoreException The trust store cannot be loaded.
     * @throws KeyManagementException The trust store cannot be loaded.
     */
    protected static SSLConnectionSocketFactory createTrustAll()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        });

        SSLConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(builder.build(),
                NoopHostnameVerifier.INSTANCE);

        return sslSF;
    }
}
