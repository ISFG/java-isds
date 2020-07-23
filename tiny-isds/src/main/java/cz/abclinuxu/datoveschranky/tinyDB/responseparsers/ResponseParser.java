package cz.abclinuxu.datoveschranky.tinyDB.responseparsers;

import cz.abclinuxu.datoveschranky.tinyDB.holders.OutputHolder;
import org.xml.sax.Attributes;

/**
 * Rozhraní, přijde refaktorovat
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public interface ResponseParser {

    public OutputHolder startElement(String elName, Attributes attributes);

    public void endElement(String elName, OutputHolder handle);

    public void done();
}
