package cz.abclinuxu.datoveschranky.common.interfaces;

import cz.abclinuxu.datoveschranky.common.entities.Message;

/**
 * Služba zodpovědná za odesílání zpráv.
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public interface DataBoxUploadService {

    /**
     * Odešle zprávu. V případě jakékoliv chyby vyhodí vyjímku.
     *
     * @param mess zpráva k odeslání
     */
    public void sendMessage(Message mess);

}
