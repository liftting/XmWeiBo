package wm.xmwei.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.core.database.datatask.DbUserBingTask;
import wm.xmwei.ui.activity.BaseActivity;

/**
 * this is show user bing info activity
 */
public class UserBingActivity extends BaseActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<UserBingDomain>> {

    private Button mBtnUserBing;
    private List<UserBingDomain> mBingDataList;

    private ListView mBingList;
    private UserBingAdapter mBingDataAdapter;

    private final int LOADER_ID = 0;
    private final int ADD_ACCOUNT_REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_user_bing_main);

        mBtnUserBing = (Button) findViewById(R.id.btn_bing_user);
        mBtnUserBing.setOnClickListener(this);
        mBingList = (ListView) findViewById(R.id.v_user_bing_list);

        mBingDataList = new ArrayList<UserBingDomain>();
        mBingDataAdapter = new UserBingAdapter();
        mBingList.setAdapter(mBingDataAdapter);

        this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(UserBingActivity.this, XmSoLoginActivity.class);
        startActivityForResult(intent, ADD_ACCOUNT_REQUEST_CODE);
    }

    @Override
    public Loader<List<UserBingDomain>> onCreateLoader(int id, Bundle args) {

        return new UserBingDataLoader(UserBingActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<UserBingDomain>> loader, List<UserBingDomain> data) {
        mBingDataList = data;
        mBingDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<UserBingDomain>> loader) {
        mBingDataList = new ArrayList<UserBingDomain>();
        mBingDataAdapter.notifyDataSetChanged();
    }

    //数据load
    private static class UserBingDataLoader extends AsyncTaskLoader<List<UserBingDomain>> {

        public UserBingDataLoader(Context context) {
            super(context);
        }

        @Override
        public List<UserBingDomain> loadInBackground() {
            return DbUserBingTask.getUserBingList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ACCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            refresh();
        }
    }

    private void refresh() {
        getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
    }


    private class UserBingAdapter extends BaseAdapter {
        private int defaultBG;

        public UserBingAdapter() {
            defaultBG = getResources().getColor(R.color.transparent);
        }

        @Override
        public int getCount() {
            return mBingDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mBingDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.valueOf(mBingDataList.get(i).getUid());
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null || view.getTag() == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View mView = layoutInflater
                        .inflate(R.layout.layer_user_bing_list_item, viewGroup, false);
                holder = new ViewHolder();
                holder.root = mView.findViewById(R.id.listview_root);
                holder.name = (TextView) mView.findViewById(R.id.account_name);
                holder.avatar = (ImageView) mView.findViewById(R.id.imageView_avatar);
                holder.tokenInvalid = (TextView) mView.findViewById(R.id.token_expired);
                view = mView;
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.root.setBackgroundColor(defaultBG);

            if (mBingDataList.get(i).getInfo() != null) {
                holder.name.setText(mBingDataList.get(i).getInfo().getScreen_name());
            } else {
                holder.name.setText(mBingDataList.get(i).getUsernick());
            }

            if (!TextUtils.isEmpty(mBingDataList.get(i).getAvatar_url())) {
//                getBitmapDownloader()
//                        .downloadAvatar(holder.avatar, accountList.get(i).getInfo(), false);
            }

//            holder.tokenInvalid.setVisibility(!Utility.isTokenValid(accountList.get(i)) ? View.VISIBLE : View.GONE);
            return view;
        }
    }

    class ViewHolder {
        View root;
        TextView name;
        ImageView avatar;
        TextView tokenInvalid;
    }


}
