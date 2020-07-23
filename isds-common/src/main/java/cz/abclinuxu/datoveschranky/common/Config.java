package cz.abclinuxu.datoveschranky.common;

import java.io.Serializable;
import java.security.KeyStore;

/**
 * Konfigurace připojení k ISDS.
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class Config implements Serializable {

    /**
     * URL testovacího provozu
     */
    @Deprecated
    public static final String TEST_URL = "ws1.czebox.cz"; // was ws1.czebox.cz
    /**
     * URL produkčního prostředí
     */
    @Deprecated
    public static final String PRODUCTION_URL = "ws1.mojedatovaschranka.cz"; // was ws1.mojedatovaschranka.cz
    private static final long serialVersionUID = 3L;
    private final DataBoxEnvironment dataBoxEnvironment;

    /**
     * Vytvoří konfiguraci s daným URL a s KeyStore načteným z resources.
     * Konstruktor je určen pro testovací účely, pro realné nasazení použijte
     * vlastní keyStore.
     *
     * @param servURL URL služby (TEST_URL či PRODUCTION_URL)
     * @see Config#(String, KeyStore) konstruktor Config.
     */
    @Deprecated
    public Config(String servURL) {
        if (servURL.equals(TEST_URL)) {
            dataBoxEnvironment = DataBoxEnvironment.TEST;
        } else if (servURL.equals(PRODUCTION_URL)) {
            dataBoxEnvironment = DataBoxEnvironment.PRODUCTION;
        } else {
            throw new IllegalArgumentException("servURL");
        }
    }

    public Config(DataBoxEnvironment dbe) {
        dataBoxEnvironment = dbe;
    }

    /**
     * Vytvoří konfiguraci s daným URL a příslušným klíči
     *
     * @param servURL URL služby (TEST_URL či PRODUCTION_URL)
     * @param keys    instance třídy KeyStore, která obsahuje certifikáty
     *                nutné pro přihlášení do ISDS, certifikáty, kterými je podepsána obálka
     *                zprávy a certifikáty časových razítek.
     */
    @Deprecated
    public Config(String servURL, KeyStore keys) {
        if (servURL.equals(TEST_URL)) {
            dataBoxEnvironment = DataBoxEnvironment.TEST;
        } else if (servURL.equals(PRODUCTION_URL)) {
            dataBoxEnvironment = DataBoxEnvironment.PRODUCTION;
        } else {
            throw new IllegalArgumentException("servURL");
        }
    }

    public String getServiceURL() {
        // return "https://" + url + "/cert/DS/"; // was "/DS/"
        return "https://" + dataBoxEnvironment.basicURL() + "/DS/";
    }

    public String getServiceURLClientCert() {
        return "https://" + dataBoxEnvironment.clientCertURL() + "/cert/DS/";
    }

    @Deprecated
    public String getLoginScope() {
        return "login." + dataBoxEnvironment.basicURL();
    }

}
