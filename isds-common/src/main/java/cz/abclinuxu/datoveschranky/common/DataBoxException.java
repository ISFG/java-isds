package cz.abclinuxu.datoveschranky.common;

/**
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class DataBoxException extends RuntimeException {

    private static final long serialVersionUID = 3L;

    private Status status = null;

    public DataBoxException(String mess) {
        super(mess);
    }


    public DataBoxException(String mess, Exception cause) {
        super(mess, cause);
    }

    public DataBoxException(String mess, Status st) {
        super(mess);
        this.status = st;
    }

    @Override
    public String toString() {
        if (status == null) {
            return super.toString();
        } else {
            return super.toString() + " " + status.getStatusCode() + ":" + status.getStatusMesssage();
        }
    }
}
