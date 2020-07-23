package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.Config;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Autentizace
 */
public abstract class Authentication {
    // copied from mvn:com.sun.xml.ws/jaxws-rt JAXWSProperties
    public static final String SSL_SOCKET_FACTORY = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";

    protected final Config config;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected Authentication(Config config) {
        this.config = config;
    }

    public <T> T createService(Service serviceBuilder, Class<T> serviceClass, String servicePostfix) {
        T service = serviceBuilder.getPort(serviceClass);
        configureService(((BindingProvider) service).getRequestContext(), servicePostfix);
        return service;
    }

    protected void configureService(Map<String, Object> requestContext, String servicePostfix) {
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.getServiceURL() + servicePostfix);
        configureServiceOverride(requestContext, servicePostfix);
    }

    protected abstract void configureServiceOverride(Map<String, Object> requestContext, String servicePostfix);
}
