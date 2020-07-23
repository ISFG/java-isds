package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.DataBoxException;
import cz.abclinuxu.datoveschranky.common.entities.Hash;
import cz.abclinuxu.datoveschranky.common.entities.TimeStamp;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.jcajce.JcaX509CertSelectorConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.AlgorithmNameFinder;
import org.bouncycastle.operator.DefaultAlgorithmNameFinder;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.TimeStampTokenInfo;

import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

/**
 * Pomocná třída pro validaci časového razítka a podpisu zprávy. Jen prototyp!
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class Validator {

    static {
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
    }

    private final AlgorithmNameFinder algorithmNameFinder = new DefaultAlgorithmNameFinder();
    private Logger logger = Logger.getLogger(Validator.class.getCanonicalName());

    public Validator() {
    }

    public TimeStamp readTimeStamp(byte[] timeStamp) {
        try {
            CMSSignedData data = new CMSSignedData(timeStamp);
            TimeStampToken tst = new TimeStampToken(data);
            TimeStampTokenInfo tsti = tst.getTimeStampInfo();
            X509Certificate cert = findCertificate(tst.getSID());
            String algo = algorithmNameFinder.getAlgorithmName(tsti.getHashAlgorithm());
            byte[] hash = tsti.getMessageImprintDigest();
            return new TimeStamp(new Hash(algo, hash), cert, tsti.getGenTime());
        } catch (CMSException ex) {
            throw new DataBoxException("Chyba pri cteni casoveho razitka.", ex);
        } catch (TSPException ioe) {
            throw new DataBoxException("Chyba pri cteni casoveho razitka.", ioe);
        } catch (IOException ioe) {
            throw new DataBoxException("IO chyba pri cteni casoveho razitka.", ioe);
        }
    }

    /**
     * Vrátí obsah po odstranění PKCS7 obálky.
     */
    public byte[] readPKCS7(byte[] signedBytes) throws DataBoxException {
        try {
            CMSSignedData data = new CMSSignedData(signedBytes);
            CMSProcessable signedContent = data.getSignedContent();
            return (byte[]) signedContent.getContent();
        } catch (Exception ex) {
            throw new DataBoxException("Nemohu otevrit PKCS#7 obalku.", ex);
        }
    }

    private X509Certificate findCertificate(SignerId signer) {
        // according to bouncycastle 1.56 release migration guide
        // To convert from SignerIds and RecipientIds use the JcaX509CertSelectorConverter class.
        // To convert from X509CertSelectors use the JcaX509SelectorConverter class.
        JcaX509CertSelectorConverter converter = new JcaX509CertSelectorConverter();
        logger.debug("Hledam certifikat pro " + signer);
        return converter.getCertSelector(signer).getCertificate();
    }

}
