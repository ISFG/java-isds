package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.DataBoxException;
import cz.abclinuxu.datoveschranky.common.entities.Message;
import cz.abclinuxu.datoveschranky.common.entities.MessageEnvelope;
import cz.abclinuxu.datoveschranky.common.entities.MessageType;
import cz.abclinuxu.datoveschranky.common.interfaces.AttachmentStorer;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxDownloadService;
import cz.abclinuxu.datoveschranky.ws.dm.DmOperationsPortType;
import cz.abclinuxu.datoveschranky.ws.dm.TReturnedMessage;
import cz.abclinuxu.datoveschranky.ws.dm.TStatus;
import org.apache.log4j.Logger;

import javax.xml.ws.Holder;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author xrosecky
 */
public class DataBoxDownloadServiceImpl implements DataBoxDownloadService {

    static Logger logger = Logger.getLogger(DataBoxDownloadServiceImpl.class);
    private DmOperationsPortType dmOp = null;
    private MessageValidator validator = null;

    public DataBoxDownloadServiceImpl(DmOperationsPortType dmOpService, MessageValidator validate) {
        dmOp = dmOpService;
        validator = validate;
    }

    /*
     * Stahne prijatou zpravu. Pro odeslane zpravy se muzi pouzit downloadSignedMessage - omezeni na strane ISDS.
     */
    @Override
    public Message downloadMessage(MessageEnvelope envelope, AttachmentStorer storer) {
        logger.info(String.format("downloadMessage: id:%s", envelope.getMessageID()));
        if (envelope.getType() != MessageType.RECEIVED) {
            throw new DataBoxException("Mohu stahnout pouze prijatou zpravu.");
        }
        Holder<TStatus> status = new Holder<>();
        Holder<TReturnedMessage> hMessage = new Holder<>();
        dmOp.messageDownload(envelope.getMessageID(), hMessage, status);
        ErrorHandling.throwIfError("Nemohu stahnout prijatou zpravu.", status.value);
        logger.info(String.format("downloadMessage successfull"));
        TReturnedMessage message = hMessage.value;
        return validator.buildMessage(envelope, message, storer);
    }

    @Override
    public void downloadSignedMessage(MessageEnvelope env, OutputStream os) {
        logger.info(String.format("downloadSignedMessage: id:%s", env.getMessageID()));
        String id = env.getMessageID();
        Holder<byte[]> messageAsPKCS7 = new Holder<>();
        Holder<TStatus> status = new Holder<>();
        switch (env.getType()) {
            case RECEIVED:
                dmOp.signedMessageDownload(id, messageAsPKCS7, status);
                break;
            case SENT:
                dmOp.signedSentMessageDownload(id, messageAsPKCS7, status);
                break;
            default:
                throw new DataBoxException("Neodeslanou zpravu nelze stahnout");
        }
        ErrorHandling.throwIfError("Nemohu stahnout podepsanou zpravu.", status.value);
        try {
            os.write(messageAsPKCS7.value);
            os.flush();
            logger.info(String.format("downloadSignedMessage successfull"));
        } catch (IOException ioe) {
            throw new DataBoxException("Chyba pri zapisu do vystupniho proudu.", ioe);
        }
    }

}
