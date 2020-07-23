package cz.abclinuxu.datoveschranky.ws;

import cz.abclinuxu.datoveschranky.ws.db.DataBoxManipulation;
import cz.abclinuxu.datoveschranky.ws.db.DataBoxSearch;
import cz.abclinuxu.datoveschranky.ws.dm.DmInfoWebService;
import cz.abclinuxu.datoveschranky.ws.dm.DmOperationsWebService;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * @author xrosecky
 */
public class ServiceBuilder {

    public static final String ISDS_CZECHPOINT_CZ_V_20 = "http://isds.czechpoint.cz/v20";

    public static DmOperationsWebService createDmOperationsWebService() {
        QName qName = new QName(ISDS_CZECHPOINT_CZ_V_20, "dmOperationsWebService");
        URL url = ServiceBuilder.class.getResource("/wsdl/dm_operations.wsdl");
        return new DmOperationsWebService(url, qName);
    }

    public static DmInfoWebService createDmInfoWebService() {
        QName qName = new QName(ISDS_CZECHPOINT_CZ_V_20, "dmInfoWebService");
        URL url = ServiceBuilder.class.getResource("/wsdl/dm_info.wsdl");
        return new DmInfoWebService(url, qName);
    }

    public static DataBoxManipulation createDataBoxManipulation() {
        QName qName = new QName(ISDS_CZECHPOINT_CZ_V_20, "DataBoxManipulation");
        URL url = ServiceBuilder.class.getResource("/wsdl/db_manipulations.wsdl");
        return new DataBoxManipulation(url, qName);
    }

    public static DataBoxSearch createDataBoxSearch() {
        QName qName = new QName(ISDS_CZECHPOINT_CZ_V_20, "DataBoxSearch");
        URL url = ServiceBuilder.class.getResource("/wsdl/db_search.wsdl");
        return new DataBoxSearch(url, qName);
    }

}
