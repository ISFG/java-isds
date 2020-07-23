package cz.abclinuxu.datoveschranky.common.entities;

/**
 * Typy zpráv (přijatá, odeslaná, zpráva k odeslání)
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public enum MessageType {

    SENT, // odeslaná zpráva
    RECEIVED, // přijatá zpráva
    CREATED // zpráva k odeslání

}
