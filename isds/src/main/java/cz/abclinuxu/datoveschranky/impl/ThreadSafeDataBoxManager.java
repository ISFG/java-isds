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

/**
 * Thread safe implementation of DataBoxServices.
 * <p>
 * Methods of this object are thread safe, returned instances are not.
 *
 * @author xrosecky
 */
public class ThreadSafeDataBoxManager implements DataBoxServices {

    protected Authentication auth = null;
    protected Config config = null;

    public ThreadSafeDataBoxManager(Config conf, Authentication auth) {
        this.auth = auth;
        config = conf;
    }

    @Override
    public DataBoxDownloadService getDataBoxDownloadService() {
        MessageValidator messageValidator = new MessageValidator(config);
        DmOperationsPortType dataMessageOperationsService = auth.createService(
            ServiceBuilder.createDmOperationsWebService(),
            DmOperationsPortType.class, "dz");
        return new DataBoxDownloadServiceImpl(dataMessageOperationsService, messageValidator);
    }

    @Override
    public DataBoxMessagesService getDataBoxMessagesService() {
        DmInfoPortType dataMessageInfo = auth.createService(
            ServiceBuilder.createDmInfoWebService(),
            DmInfoPortType.class, "dx");
        return new DataBoxMessagesServiceImpl(dataMessageInfo);
    }

    @Override
    public DataBoxUploadService getDataBoxUploadService() {
        DmOperationsPortType dataMessageOperationsService = auth.createService(
            ServiceBuilder.createDmOperationsWebService(),
            DmOperationsPortType.class, "dz");
        return new DataBoxUploadServiceImpl(dataMessageOperationsService);
    }

    @Override
    public DataBoxSearchService getDataBoxSearchService() {
        DataBoxSearchPortType searchService = auth.createService(
            ServiceBuilder.createDataBoxManipulation(),
            DataBoxSearchPortType.class, "dfs");
        return new DataBoxSearchServiceImpl(searchService);
    }

    @Override
    public DataBoxAccessService getDataBoxAccessService() {
        throw new UnsupportedOperationException("Operace getDataBoxAccessService neni "
            + "touto knihovnou podporovana.");
    }
}
