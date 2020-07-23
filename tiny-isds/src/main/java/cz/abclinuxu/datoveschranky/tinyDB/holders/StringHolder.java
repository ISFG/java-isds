package cz.abclinuxu.datoveschranky.tinyDB.holders;

/**
 * Zapisuje výsledek čtení do StringBuilderu, výsledek vrátí jako String.
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class StringHolder implements OutputHolder<String> {

    StringBuilder result = new StringBuilder();

    public void write(char[] array, int start, int length) {
        result.append(array, start, length);
    }

    public String getResult() {
        return result.toString();
    }

    @Override
    public String toString() {
        return result.toString();
    }

}
