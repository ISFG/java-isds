package cz.abclinuxu.datoveschranky.common.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by jludvice on 21.9.16.
 */
public class EqualsHashCodeTest {

    public static MessageEnvelope generateEnv(String id) {
        MessageType type = MessageType.CREATED;
        DataBox sender = new DataBox("123", DataBoxType.FO, "name", "address");
        DataBox recipient = new DataBox("456", DataBoxType.FO, "name", "address");
        String msgId = "asldkfj03294";
        String annotation = null;
        return new MessageEnvelope(type, sender, recipient, msgId, annotation);
    }

    @Test
    public void equalEnvelopeIds() {
        MessageEnvelope env1 = generateEnv("aabbcc");
        MessageEnvelope env2 = generateEnv("aabbcc");

        assertEquals("Envelopes created with same attributes must be equal", env1, env2);
        assertEquals("Envelopes created with same attributes must have equal hashcode", env1.hashCode(), env2.hashCode());
    }

    @Test
    public void nonEqualEnvelopes() {
        MessageEnvelope env1 = new MessageEnvelope();
        MessageEnvelope env2 = generateEnv("abc");

        assertNotEquals("Envelopes with different id can't be equal", env1, env2);
        assertNotEquals("Envelopes with different id can't have equal hashcode", env1.hashCode(), env2.hashCode());
    }

    @Test
    public void equalMessage() {
        MessageEnvelope e = generateEnv("abc");
        Message m = new Message(e, null);
        Message m2 = new Message(e, null);

        assertEquals("Messages with same envelope must be equal", m, m2);
    }
}
