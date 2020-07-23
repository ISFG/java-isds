package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.Config;

import javax.xml.ws.BindingProvider;
import java.util.Map;

/**
 * Autentizace
 */
public class BasicAuthentication extends Authentication {

    protected String userName;
    protected String password;

    public BasicAuthentication(Config config, String userName, String password) {
        super(config);
        this.userName = userName;
        this.password = password;
    }

    /**
     * Realizuje přihlášení do datové schránky pod daným uživatelským jménem
     * a heslem a při úspěšném přihlášení vrátí příslušnou instanci ISDSManageru
     * poskytující služby k této schránce.
     *
     * @param userName jméno uživatele
     * @param password heslo uživatele
     */
    public static Authentication login(Config config, String userName, String password) {
        Authentication auth = new BasicAuthentication(config, userName, password);
        return auth;
    }

    @Override
    protected void configureServiceOverride(Map<String, Object> requestContext, String servicePostfix) {
        requestContext.put(BindingProvider.USERNAME_PROPERTY, userName);
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
    }

}
