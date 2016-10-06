package cz.abclinuxu.datoveschranky.common.entities;

import java.io.Serializable;

import cz.abclinuxu.datoveschranky.common.entities.content.Content;
import cz.abclinuxu.datoveschranky.common.impl.MimeTypes;

/**
 * Reprezentuje jednu přílohu zprávy.
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class Attachment implements Serializable {

    private static final long serialVersionUID = 2L;

    private String description = null;
    private String metaType = null;
    private String mimeType = null;
    private Content content = null;

    public Attachment() {
    }

    public Attachment(String descr, Content cont) {
        if (descr == null) {
            throw new NullPointerException(descr);
        }
        this.description = descr;
        this.content = cont;
        setAutoMimeType(descr);
    }

    /**
     * Vrátí obsah přílohy.
     */
    public Content getContent() {
        return content;
    }

    public void setContents(Content cont) {
        this.content = cont;
    }

    /**
     * Vrátí popis přílohy.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Nastaví popis přílohy
     *
     * @param newDescr popis zprávy, povinný, maximální délka 256 znaků.
     */
    public void setDescription(String newDescr) {
        Validator.assertNotNull(newDescr);
        this.description = newDescr;
        setAutoMimeType(newDescr);
    }

    /**
     * Vrátí druh písemnosti (meta type), v ISDS jsou povolené main, enclosure,
     * signature a meta.
     */
    public String getMetaType() {
        return metaType;
    }

    /**
     * Nastaví druh písemnosti (meta type), v ISDS jsou povolené main, enclosure,
     * signature a meta.
     */
    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    /**
     * Vrátí MIME typ zprávy.
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    private void setAutoMimeType(String desc) {
        String newMimeType = MimeTypes.fileNameToMimeType(desc);
        if (newMimeType != null) {
            this.setMimeType(newMimeType);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Attachment{");
        sb.append("description='").append(description).append('\'');
        sb.append(", metaType='").append(metaType).append('\'');
        sb.append(", mimeType='").append(mimeType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
