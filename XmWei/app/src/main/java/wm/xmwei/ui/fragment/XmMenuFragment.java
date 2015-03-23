package wm.xmwei.ui.fragment;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.datadao.dbway.DbUserBingTask;
import wm.xmwei.ui.fragment.base.XmBaseFragment;

/**
 *
 *
 */
public class XmMenuFragment extends XmBaseFragment {

    private Layout mLeftContainer;

    public static XmMenuFragment newInstance() {
        XmMenuFragment fragment = new XmMenuFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // data load and show
        UserBingDomain bingUser = XmApplication.getInstance().getUserBingDomain();
        XmImageLoader.getInstance().loadImage(bingUser.getAvatar_url(), mLeftContainer.avatar);
        mLeftContainer.nickname.setText(bingUser.getUsernick());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containView = inflater.inflate(R.layout.layer_left_menu_contents, null);

        mLeftContainer = new Layout();

        mLeftContainer.avatar = (ImageView) containView.findViewById(R.id.avatar);
        mLeftContainer.nickname = (TextView) containView.findViewById(R.id.nickname);

        mLeftContainer.home = (LinearLayout) containView.findViewById(R.id.btn_home);
        mLeftContainer.mention = (LinearLayout) containView.findViewById(R.id.btn_mention);
        mLeftContainer.comment = (LinearLayout) containView.findViewById(R.id.btn_comment);
        mLeftContainer.search = (Button) containView.findViewById(R.id.btn_search);
        mLeftContainer.profile = (Button) containView.findViewById(R.id.btn_profile);
        mLeftContainer.setting = (Button) containView.findViewById(R.id.btn_setting);
        mLeftContainer.dm = (Button) containView.findViewById(R.id.btn_dm);
        mLeftContainer.logout = (Button) containView.findViewById(R.id.btn_logout);
        mLeftContainer.fav = (Button) containView.findViewById(R.id.btn_favourite);
        mLeftContainer.homeCount = (TextView) containView.findViewById(R.id.tv_home_count);
        mLeftContainer.mentionCount = (TextView) containView.findViewById(R.id.tv_mention_count);
        mLeftContainer.commentCount = (TextView) containView.findViewById(R.id.tv_comment_count);

        return containView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set view listener
    }

    private class Layout {
        ImageView avatar;
        TextView nickname;
        LinearLayout home;
        LinearLayout mention;
        LinearLayout comment;
        TextView homeCount;
        TextView mentionCount;
        TextView commentCount;
        Button search;
        //        Button location;
        Button dm;
        Button logout;
        Button profile;
        Button setting;
        Button fav;
    }

}
