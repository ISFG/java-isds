package cz.abclinuxu.datoveschranky.common.interfaces;

import cz.abclinuxu.datoveschranky.common.entities.OwnerInfo;
import cz.abclinuxu.datoveschranky.common.entities.UserInfo;

import java.util.GregorianCalendar;

public interface DataBoxAccessService {

    OwnerInfo GetOwnerInfoFromLogin();

    UserInfo GetUserInfoFromLogin();

    /**
     * Vraci datum expirace hesla
     *
     * @return
     */
    GregorianCalendar GetPasswordInfo();
}
