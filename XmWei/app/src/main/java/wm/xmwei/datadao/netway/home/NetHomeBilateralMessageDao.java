package wm.xmwei.datadao.netway.home;

import wm.xmwei.util.URLHelper;

/**
 *
 * this is bilateral message dao
 *
 */
public class NetHomeBilateralMessageDao extends NetHomeDefaultMessageDao {

    public NetHomeBilateralMessageDao(String access_token) {
        super(access_token);
    }

    @Override
    protected String getUrl() {
        return URLHelper.BILATERAL_TIMELINE;
    }

}
