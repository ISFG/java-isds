package cz.abclinuxu.datoveschranky;

import cz.abclinuxu.datoveschranky.common.ByteArrayAttachmentStorer;
import cz.abclinuxu.datoveschranky.common.entities.Message;
import cz.abclinuxu.datoveschranky.common.entities.MessageEnvelope;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxDownloadService;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxMessagesService;
import junit.framework.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xrosecky
 */
public class DownloadAllMessagesTest {

    private static TestHelper helper = new TestHelper();
    private static int MAX = 10000;
    private static int BELOW_LIMIT = 2;
    private static int BELOW_LIMIT_COUNT = 3;

    @Test
    public void downloadAllMessages() throws Exception {
		int limits[] = new int[]{3, 4, 5, MAX};
		for (int limit : limits) {
			downloadAllMessages(limit);
		}
	}

    public void downloadAllMessages(int limit) throws Exception {
		Set<String> seen = new HashSet<>();
		Date begin = Date.from(LocalDate.now().minusDays(100).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(LocalDate.now().atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
		DataBoxMessagesService messageService = helper.connectAsFO().getDataBoxMessagesService();
		DataBoxDownloadService downloadService = helper.connectAsFO().getDataBoxDownloadService();
		int offset = 1;
		int count = 0;
		while (true) {
			List<MessageEnvelope> messages = messageService.getListOfReceivedMessages(begin, end, null, offset, limit);
			if (messages.isEmpty() || count > BELOW_LIMIT_COUNT) {
				break;
			}
			if (messages.size() < BELOW_LIMIT) {
				count++;
			}
			offset += messages.size();
			for (MessageEnvelope envelope : messages) {
				String id = envelope.getMessageID();
				Assert.assertFalse(seen.contains(id));
				seen.add(id);
				if (envelope.getState().canBeDownloaded()) {
					Message mess = downloadService.downloadMessage(envelope, new ByteArrayAttachmentStorer());
				} else {
					System.out.println("Skipping message with state:" + envelope.getState().toString());
				}
			}
		}
		List<MessageEnvelope> messages = messageService.getListOfReceivedMessages(begin, end, null, 0, MAX);
		Assert.assertEquals(seen.size(), messages.size());
		System.out.println(seen.size());
	}

}
