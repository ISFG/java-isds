package cz.abclinuxu.datoveschranky;

import cz.abclinuxu.datoveschranky.common.entities.Address;
import cz.abclinuxu.datoveschranky.common.entities.DataBoxType;
import cz.abclinuxu.datoveschranky.common.entities.DataBoxWithDetails;
import cz.abclinuxu.datoveschranky.common.entities.SearchResult;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxServices;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * @author xrosecky
 */
public class SearchTest {

    private static DataBoxServices services = null;

    private static TestHelper helper = new TestHelper();

    @BeforeClass
    public static void setUpClass() throws Exception {
        services = helper.connectAsFO();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void search() {
        List<DataBoxWithDetails> boxes1 = services.getDataBoxSearchService().findOVMsByName("min");
        Assert.assertTrue("Search must return more than 1 entry. Found: " + boxes1.size(), boxes1.size() > 1);

        for (int i = 0; i < Math.min(10, boxes1.size()); i++) {
            DataBoxWithDetails db = boxes1.get(i);
            Assert.assertNotNull("City shouldn't be null. " + db, db.getAddressDetails().getCity());
            Assert.assertNotNull("Address shouldn't be null. " + db, db.getAddressDetails().getStreet());
            Assert.assertNotNull("Zip Code shouldn't be null. " + db, db.getAddressDetails().getZipCode());
        }
        List<DataBoxWithDetails> boxes2 = services.getDataBoxSearchService().findOVMsByName("Ministerstvo nepravdy a lasky");
        Assert.assertTrue("Search result for non existent entry should be empty", boxes2.isEmpty());
    }

    @Test
    public void searchExtended() {
        // podle jmena
        DataBoxWithDetails db1 = new DataBoxWithDetails();
        db1.setIdentity("min");
        db1.setDataBoxType(DataBoxType.OVM);
        dump(services.getDataBoxSearchService().find(db1));
        // podle jmena a mesta
        DataBoxWithDetails db2 = new DataBoxWithDetails();
        db2.setDataBoxType(DataBoxType.OVM_EXEKUT);
        db2.setAddressDetails(new Address());
        db2.getAddressDetails().setCity("Praha");
        db2.setIdentity("SMS exekutor");
        dump(services.getDataBoxSearchService().find(db2));
        // podle jmena a mesta
        DataBoxWithDetails db3 = new DataBoxWithDetails();
        db3.setDataBoxType(DataBoxType.OVM_EXEKUT);
        db3.setAddressDetails(new Address());
        db3.getAddressDetails().setCity("Brno");
        db3.setIdentity("SMS exekutor");
        dump(services.getDataBoxSearchService().find(db3));
        // test vyhledani PO
        DataBoxWithDetails db4 = new DataBoxWithDetails();
        db4.setAddressDetails(new Address());
        db4.setDataBoxType(DataBoxType.PO);
        //db4.getAddressDetails().setCity("Praha");
        db4.setIdentity("aaa");
        dump(services.getDataBoxSearchService().find(db4));
        // test hledani podle IC
        DataBoxWithDetails db5 = new DataBoxWithDetails();
        db5.setIC("31342183 ");
        db5.setDataBoxType(DataBoxType.OVM);
        dump(services.getDataBoxSearchService().find(db5));
    }

    private void dump(List<DataBoxWithDetails> dbs) {
        System.out.println("========================================");
        for (DataBoxWithDetails db : dbs) {
            System.out.println(db);
        }
        System.out.println("========================================");
    }

    private void dump(SearchResult result) {
        dump(result.getResult());
    }
}
