package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.entities.Message;
import cz.abclinuxu.datoveschranky.common.entities.MessageEnvelope;
import cz.abclinuxu.datoveschranky.common.entities.MessageType;
import cz.abclinuxu.datoveschranky.common.impl.DataBoxException;
import cz.abclinuxu.datoveschranky.common.interfaces.AttachmentStorer;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxDownloadService;
import cz.abclinuxu.datoveschranky.ws.dm.DmOperationsPortType;
import cz.abclinuxu.datoveschranky.ws.dm.TReturnedMessage;
import cz.abclinuxu.datoveschranky.ws.dm.TReturnedMessage;
import cz.abclinuxu.datoveschranky.ws.dm.TStatus;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.ws.Holder;

/**
 *
 * @author xrosecky
 */
public class DataBoxDownloadServiceImpl implements DataBoxDownloadService {

    private DmOperationsPortType dmOp = null;
    private MessageValidator validator = null;

    public DataBoxDownloadServiceImpl(DmOperationsPortType dmOpService, MessageValidator validate) {
        this.dmOp = dmOpService;
        this.validator = validate;
    }

    public Message downloadMessage(MessageEnvelope envelope, AttachmentStorer storer) {
        if (envelope.getType() != MessageType.RECEIVED) {
            throw new DataBoxException("Mohu stahnout pouze prijatou zpravu.");
        }
        Holder<TStatus> status = new Holder<TStatus>();
        Holder<TReturnedMessage> hMessage = new Holder<TReturnedMessage>();
        dmOp.messageDownload(envelope.getMessageID(), hMessage, status);
        ErrorHandling.throwIfError("Nemohu stahnout prijatou zpravu.", status.value);
        TReturnedMessage message = hMessage.value;
        return validator.buildMessage(envelope, message, storer);
    }

    public void downloadSignedMessage(MessageEnvelope env, OutputStream os) {
        String id = env.getMessageID();
        Holder<byte[]> messageAsPKCS7 = new Holder<byte[]>();
        Holder<TStatus> status = new Holder<TStatus>();
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
        } catch (IOException ioe) {
            throw new DataBoxException("Chyba pri zapisu do vystupniho proudu.", ioe);
        }
    }
}
