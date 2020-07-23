package cz.abclinuxu.datoveschranky.tinyDB.responseparsers;

import cz.abclinuxu.datoveschranky.common.Utils;
import cz.abclinuxu.datoveschranky.tinyDB.holders.OutputHolder;
import cz.abclinuxu.datoveschranky.tinyDB.holders.OutputStreamHolder;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.xml.sax.Attributes;

import java.io.Closeable;
import java.io.OutputStream;

/**
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class DownloadSignedReceivedMessage extends AbstractResponseParser {

    private OutputStream output;

    public DownloadSignedReceivedMessage(OutputStream os) {
        this.output = os;
    }

    @Override
    public OutputHolder startElementImpl(String elName, Attributes attributes) {
        if ("dmSignature".equals(elName)) {
            Base64OutputStream bos = new Base64OutputStream(output, false, 0, null);
            OutputHolder input = new OutputStreamHolder(bos);
            return input;

        }
        return null;
    }

    @Override
    public void endElementImpl(String elName, OutputHolder handle) {
        if (handle instanceof Closeable) {
            Utils.close((Closeable) handle);
        }
    }
}
