package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.Config;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxAccessService;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxDownloadService;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxMessagesService;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxSearchService;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxServices;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxUploadService;
import cz.abclinuxu.datoveschranky.ws.ServiceBuilder;
import cz.abclinuxu.datoveschranky.ws.db.DataBoxSearchPortType;
import cz.abclinuxu.datoveschranky.ws.dm.DmInfoPortType;
import cz.abclinuxu.datoveschranky.ws.dm.DmOperationsPortType;

import java.io.File;

/**
 * @author xrosecky
 */
public class DataBoxManager implements DataBoxServices {

    protected Authentication auth;
    protected Config config;
    protected DataBoxMessagesService dataBoxMessagesService = null;
    protected DataBoxDownloadService dataBoxDownloadService = null;
    protected DataBoxUploadService dataBoxUploadService = null;
    protected DataBoxSearchServiceImpl dataBoxFindingService = null;
    protected MessageValidator messageValidator = null;

    public DataBoxManager(Config conf, Authentication auth) {
        this.auth = auth;
        config = conf;
        messageValidator = new MessageValidator(config);
    }

    public static DataBoxManager login(Config config, String userName, String password) throws Exception {
        Authentication auth = BasicAuthentication.login(config, userName, password);
        DataBoxManager manager = new DataBoxManager(config, auth);
        return manager;
    }

    public static DataBoxManager login(Config config, File clientCert, String password) throws Exception {
        return null;
    }

    @Override
    public DataBoxDownloadService getDataBoxDownloadService() {
        if (dataBoxDownloadService == null) {
            DmOperationsPortType dataMessageOperationsService = auth.createService(
                ServiceBuilder.createDmOperationsWebService(),
                DmOperationsPortType.class, "dz");
            dataBoxDownloadService = new DataBoxDownloadServiceImpl(dataMessageOperationsService, messageValidator);
        }
        return dataBoxDownloadService;
    }

    @Override
    public DataBoxMessagesService getDataBoxMessagesService() {
        if (dataBoxMessagesService == null) {
            DmInfoPortType dataMessageInfo = auth.createService(
                ServiceBuilder.createDmInfoWebService(),
                DmInfoPortType.class, "dx");
            dataBoxMessagesService = new DataBoxMessagesServiceImpl(dataMessageInfo);
        }
        return dataBoxMessagesService;
    }

    @Override
    public DataBoxUploadService getDataBoxUploadService() {
        if (dataBoxUploadService == null) {
            DmOperationsPortType dataMessageOperationsService = auth.createService(
                ServiceBuilder.createDmOperationsWebService(),
                DmOperationsPortType.class, "dz");
            dataBoxUploadService = new DataBoxUploadServiceImpl(dataMessageOperationsService);
        }
        return dataBoxUploadService;
    }

    @Override
    public DataBoxSearchService getDataBoxSearchService() {
        if (dataBoxFindingService == null) {
            DataBoxSearchPortType searchService = auth.createService(
                ServiceBuilder.createDataBoxSearch(),
                DataBoxSearchPortType.class, "df");
            dataBoxFindingService = new DataBoxSearchServiceImpl(searchService);
        }
        return dataBoxFindingService;
    }

    @Override
    public DataBoxAccessService getDataBoxAccessService() {
        throw new UnsupportedOperationException("Operace getDataBoxAccessService neni " +
            "touto knihovnou podporovana.");
    }

}
