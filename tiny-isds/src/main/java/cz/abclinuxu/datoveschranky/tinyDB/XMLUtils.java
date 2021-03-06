package cz.abclinuxu.datoveschranky.tinyDB;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * These methods were put here from ISDSCommon.
 * ISDS cannot contain them, since some platforms (Android) do not support XMLGregorianCalendar
 *
 * @author b00lean
 */
public class XMLUtils {

    public static XMLGregorianCalendar toXmlDate(Date date) {
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException dtce) {
            throw new UnsupportedOperationException("Nemohu prevest "
                + "GregorianCalendar na XMLGregorianCalendar", dtce);
        }
    }

    public static GregorianCalendar toGregorianCalendar(String date) {
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
            return xmlDate.toGregorianCalendar();
        } catch (DatatypeConfigurationException dtce) {
            throw new UnsupportedOperationException("Nemohu prevest "
                + "GregorianCalendar na XMLGregorianCalendar", dtce);
        }
    }
}
