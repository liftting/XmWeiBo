package wm.xmwei.util;


import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.bean.UserDomain;

/**
 * this is show domain text info
 */
public class DomainTextUtil {

    public static String toString(UserBingDomain account) {
        return account.getUsernick();
    }

    public static String toString(UserDomain bean) {
        return "user id=" + bean.getId()
                + "," + "name=" + bean.getScreen_name();
    }

}
