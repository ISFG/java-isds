package cz.abclinuxu.datoveschranky;

import cz.abclinuxu.datoveschranky.common.ByteArrayAttachmentStorer;
import cz.abclinuxu.datoveschranky.common.entities.Attachment;
import cz.abclinuxu.datoveschranky.common.entities.DataBox;
import cz.abclinuxu.datoveschranky.common.entities.DeliveryInfo;
import cz.abclinuxu.datoveschranky.common.entities.Message;
import cz.abclinuxu.datoveschranky.common.entities.MessageEnvelope;
import cz.abclinuxu.datoveschranky.common.entities.MessageState;
import cz.abclinuxu.datoveschranky.common.entities.MessageStateChange;
import cz.abclinuxu.datoveschranky.common.entities.MessageType;
import cz.abclinuxu.datoveschranky.common.entities.content.ByteContent;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxServices;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxUploadService;
import cz.abclinuxu.datoveschranky.impl.MessageValidator;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author xrosecky
 */
public class MessageUploadAndDownloadTest {

    // private static DataBoxServices services = null;
    private static final TestHelper helper = new TestHelper();
    private static final Date begin = Date.from(LocalDate.now().minusDays(100).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final Date end = Date.from(LocalDate.now().atTime(23, 59, 59).toInstant(ZoneOffset.UTC));

    @Test
    public void testSendMessageAsOVM() throws Exception {
        DataBoxServices services = helper.connectAsOVM();
        String recipientID = helper.getProperties().getProperty("fo.id");
        testSendMessage(services, recipientID);
    }

    @Test
    public void testSendMessageAsFO() throws Exception {
        DataBoxServices services = helper.connectAsFO();
        String recipientID = helper.getProperties().getProperty("ovm.id");
        testSendMessage(services, recipientID);
    }

    @Test
    public void testGetListOfSentMessages() throws Exception {
        DataBoxServices services = helper.connectAsFO();
        testGetListOfSentMessages(services);
    }

    @Test
    public void testGetListOfReceivedtMessages() throws Exception {
        DataBoxServices services = helper.connectAsFO();
        testGetListOfReceivedMessages(services);
    }

    @Test
    public void testSignedDeliveryInfo() throws Exception {
        DataBoxServices services = helper.connectAsFO();
        List<MessageEnvelope> messages = services.getDataBoxMessagesService().getListOfSentMessages(begin, end, null, 0,
            5);
        for (MessageEnvelope env : messages) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            services.getDataBoxMessagesService().getSignedDeliveryInfo(env, bos);
            MessageValidator validator = new MessageValidator();
            DeliveryInfo delivery = validator.createDeliveryInfo(bos.toByteArray());
            assertNotNull(delivery.getHash());
        }
    }

    @Test
    public void testIntegrityOfSentMessages() throws Exception {
        DataBoxServices services = helper.connectAsFO();
        testIntegrityOfSentMessages(services);
    }

    @Test
    public void testIntegrityOfReceivedMessages() throws Exception {
        DataBoxServices services = helper.connectAsFO();
        testIntegrityOfReceivedMessages(services);
    }

    private void testSendMessage(DataBoxServices services, String recipientID) throws Exception {
        DataBoxUploadService uploadService = services.getDataBoxUploadService();
        MessageEnvelope env = new MessageEnvelope();
        env.setRecipient(new DataBox(recipientID));
        env.setAnnotation("Óda_na_příliš_žluťoučkého_koně");
        List<Attachment> attachments = new ArrayList<>();
        // prvni priloha
        String prolog = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Attachment attach1 = new Attachment();
        attach1.setDescription("ahoj.xml");
        attach1.setMetaType("main");
        // attach1.setMimeType("text/xml");
        attach1.setContents(new ByteContent((prolog + "<text>Vanoce jsou svatky klidu</text>").getBytes("UTF-8")));
        attachments.add(attach1);
        // druha priloha
        Attachment attach2 = new Attachment();
        attach2.setDescription("Óda_na_příliš_žluťoučkého_koně.xml");
        attach2.setMetaType("enclosure");
        // attach2.setMimeType("text/xml");
        attach2.setContents(new ByteContent((prolog + "<text>Příliš žluťoučký kůň úpěl ďábelské ódy.</text>").getBytes("UTF-8")));
        attachments.add(attach2);
        // a ted ji poslem
        Message message = new Message(env, null, null, attachments);
        uploadService.sendMessage(message);
        while (true) {
            DeliveryInfo delivery = services.getDataBoxMessagesService().getDeliveryInfo(env);
            if (delivery.getDelivered() != null) {
                System.out.println(delivery.getDelivered().getTime());
                break;
            }
            Thread.sleep(5000);
        }
        List<MessageStateChange> changes = services.getDataBoxMessagesService().GetMessageStateChanges(null, null);
        for (MessageStateChange change : changes) {
            assertNotNull(change.getEventTime());
            assertNotNull(change.getMessageId());
            assertNotNull(change.getState());
        }
    }

    private void testGetListOfSentMessages(DataBoxServices services) {
        List<MessageEnvelope> messages = services.getDataBoxMessagesService().getListOfSentMessages(begin, end, null, 0,
            0);
        Assert.assertTrue(messages.isEmpty());
        messages = services.getDataBoxMessagesService().getListOfSentMessages(begin, end, null, 0, 5);
        Assert.assertTrue(messages.size() > 0);
        for (MessageEnvelope mess : messages) {
            Assert.assertEquals(mess.getType(), MessageType.SENT);
        }
        messages = services.getDataBoxMessagesService().getListOfSentMessages(begin, end,
            EnumSet.of(MessageState.VIRUS_FOUND), 0, 5);
        Assert.assertEquals(0, messages.size());
    }

    private void testGetListOfReceivedMessages(DataBoxServices services) {
        List<MessageEnvelope> messages = services.getDataBoxMessagesService().getListOfReceivedMessages(begin, end,
            null, 0, 0);
        Assert.assertEquals("Precondition: there should be no messages", 0, messages.size());
        messages = services.getDataBoxMessagesService().getListOfReceivedMessages(begin, end, null, 0, 5);
        Assert.assertTrue("There should be at leas one message, found: " + messages.size(), messages.size() > 0);
        for (MessageEnvelope mess : messages) {
            Assert.assertEquals(mess.getType(), MessageType.RECEIVED);
        }
    }

    private void testIntegrityOfSentMessages(DataBoxServices services) throws Exception {
        List<MessageEnvelope> messages = services.getDataBoxMessagesService().getListOfSentMessages(begin, end, null, 0,
            15);
        for (MessageEnvelope mess : messages) {
            testIntegrity(services, mess);
        }
    }

    private void testIntegrityOfReceivedMessages(DataBoxServices services) throws Exception {
        List<MessageEnvelope> envelopes = services.getDataBoxMessagesService().getListOfReceivedMessages(begin, end,
            null, 0, 15);
        for (MessageEnvelope env : envelopes) {
            Message mess1 = testIntegrity(services, env);
            Message mess2 = services.getDataBoxDownloadService().downloadMessage(env, new ByteArrayAttachmentStorer());
            List<Attachment> list1 = mess1.getAttachments();
            List<Attachment> list2 = mess2.getAttachments();
            Assert.assertEquals(list1.size(), list2.size());
            for (int i = 0; i != list1.size(); i++) {
                byte[] bytes1 = ((ByteContent) list1.get(i).getContent()).getBytes();
                byte[] bytes2 = ((ByteContent) list2.get(i).getContent()).getBytes();
                Assert.assertArrayEquals(bytes1, bytes2);
            }
        }
    }

    private Message testIntegrity(DataBoxServices services, MessageEnvelope envelope) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            services.getDataBoxDownloadService().downloadSignedMessage(envelope, os);
        } finally {
            os.close();
        }
        MessageValidator validator = new MessageValidator(helper.getConfig());
        ByteContent content = new ByteContent(os.toByteArray());
        return validator.validateAndCreateMessage(content, new ByteArrayAttachmentStorer());
    }
}
