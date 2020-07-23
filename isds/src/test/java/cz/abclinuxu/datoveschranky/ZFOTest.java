package cz.abclinuxu.datoveschranky;

import cz.abclinuxu.datoveschranky.common.ByteArrayAttachmentStorer;
import cz.abclinuxu.datoveschranky.common.Config;
import cz.abclinuxu.datoveschranky.common.DataBoxEnvironment;
import cz.abclinuxu.datoveschranky.common.entities.Message;
import cz.abclinuxu.datoveschranky.common.entities.content.FileContent;
import cz.abclinuxu.datoveschranky.common.interfaces.AttachmentStorer;
import cz.abclinuxu.datoveschranky.impl.MessageValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by jludvice on 6.10.16.
 */
public class ZFOTest {

    public static final String ZFO_PATH = "/DDZ_5033479.zfo";
    public static final String ATT_CONTENT = "Červeňoučký kůň.\n";
    public static final String ATT_ENCODING = "utf-8";

    private File zfo;
    private MessageValidator validator;

    @Before
    public void initFile() throws URISyntaxException {
        zfo = new File(ZFOTest.class.getResource(ZFO_PATH).toURI());
        validator = new MessageValidator(new Config(DataBoxEnvironment.TEST));
    }

    @Test
    public void signedZFOtoMessage() throws IOException, URISyntaxException {
        System.out.println("Path to test zfo: " + zfo);
        FileContent fc = new FileContent(zfo);
        AttachmentStorer ats = new ByteArrayAttachmentStorer();
        Message m = validator.validateAndCreateMessage(fc, ats);

        System.out.println("Extracted message from zfo file: " + m);
        assertEquals("Message must have correct subject", "Test Message OVM -_ FO Fri Sep 30 17:45:42 CEST 2016",
            m.getEnvelope().getAnnotation());
        assertEquals("Message must have 2 attachments", 1, m.getAttachments().size());

        InputStream is = null;
        try {
            is = m.getAttachments().get(0).getContent().getInputStream();
            // hack around input stream to string with Scanner
            Scanner s = new Scanner(is, ATT_ENCODING).useDelimiter("\\A");
            String content = s.hasNext() ? s.next() : null;
            System.out.println(content);
            assertEquals("Attachment content must be: " + ATT_CONTENT, ATT_CONTENT, content);
        } catch (Exception e) {
            fail("Failed to read content of attachment: " + e.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
