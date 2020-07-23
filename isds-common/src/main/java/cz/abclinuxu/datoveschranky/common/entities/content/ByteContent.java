package cz.abclinuxu.datoveschranky.common.entities.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Obsah přílohy uložený v bytovém poli v paměti.
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class ByteContent implements Content {

    byte[] content;

    public ByteContent(byte[] cont) {
        this.content = cont;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    public byte[] getBytes() {
        return content;
    }

    public long estimatedSize() {
        return content.length;
    }
}
