/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.Config;
import cz.abclinuxu.datoveschranky.common.DataBoxException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.ws.BindingProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author xrosecky
 */
public class ClientCertAuthentication extends Authentication {

    protected File certFile;
    protected String certPassword;

    public ClientCertAuthentication(Config config, File certFile, String certPassword) {
        super(config);
        this.certFile = certFile;
        this.certPassword = certPassword;
    }

    @Override
    protected void configureServiceOverride(Map<String, Object> requestContext, String servicePostfix) {
    }

    @Override
    protected void configureService(Map<String, Object> requestContext, String servicePostfix) {
        requestContext.put(SSL_SOCKET_FACTORY, createSSLSocketFactory());
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.getServiceURLClientCert() + servicePostfix);
        configureServiceOverride(requestContext, servicePostfix);
    }

    private SSLSocketFactory createSSLSocketFactory() throws DataBoxException {
        try {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream keyInput = new FileInputStream(certFile);
            keyStore.load(keyInput, certPassword.toCharArray());
            keyInput.close();
            keyManagerFactory.init(keyStore, certPassword.toCharArray());
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            return context.getSocketFactory();
        } catch (Exception ex) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new DataBoxException("Can't create SSLSocketFactory.", ex);
            }
        }
    }
}
