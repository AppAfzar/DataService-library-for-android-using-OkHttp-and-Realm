package hashemi.code.sample.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import appafzar.dataservice.web.model.BaseModel;
import hashemi.code.sample.App;
import hashemi.code.sample.R;
import hashemi.code.sample.databinding.ActivityPostListBinding;
import hashemi.code.sample.helper.InfiniteScrollProvider;
import hashemi.code.sample.model.Post;
import hashemi.code.sample.presenter.PostPresenter;
import io.realm.Realm;

import static appafzar.dataservice.helper.Tools.timeNow;


/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostListActivity extends PostBaseActivity {
    public static final String TAG = "PostListActivity";
    public static final boolean INFO = false;
    public static final boolean ERROR = true;
    private ActivityPostListBinding binding;
    private PostPresenter presenter;
    private int pageNo;
    private boolean hasMoreData = false;

    //region Activity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.realm = Realm.getDefaultInstance();
        binding = ActivityPostListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new PostPresenter(this, this);
        binding.recyclerView.enablePagination(15);
        binding.recyclerView.setLimitPageNo(Post.PEGINATION_PER_PAGE);

        new InfiniteScrollProvider().attach(binding.recyclerView, () -> {
            if (hasMoreData) {
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.presentAllPaginate(pageNo + 1, Post.PEGINATION_PER_PAGE);
                showLog(INFO, "Request start -> {domain}/api/v1/post/read_all?page=" + (pageNo + 1)
                        + "&per_page=" + Post.PEGINATION_PER_PAGE);
            }
        });
    }

    @Override
    protected void onConnectionStatusChanged(boolean isConnected) {
        BaseModel.isConnected = isConnected;
        showLog(!isConnected, isConnected ? "Internet is connected." : "Connection failed.");
        if (isConnected) {
            showLog(INFO, "Data loaded from Realm (local database).");
            presenter.presentAllPaginate(1, Post.PEGINATION_PER_PAGE);
            showLog(INFO, "Request start -> {domain}/api/v1/post/read_all?page=1&per_page=" + Post.PEGINATION_PER_PAGE);
        }
    }

//endregion

    //region Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, PostEditActivity.class));
        }
        return true;
    }
    //endregion

    //region Data Service Interface
    @Override
    public void onStartRequest() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataListResponse(List<Post> lstT, int pageNo, int pageCount) {
        this.pageNo = pageNo;
        this.hasMoreData = pageNo < pageCount;
        binding.recyclerView.setLimitPageNo(pageNo);
        showLog(INFO, String.format("Response successful.\nAdded page %s from %s. Local data updated.", pageNo, pageCount));
    }

    @Override
    public void onConnectionError(Exception e) {
        showLog(ERROR, "No internet connection.");
    }

    @Override
    public void onTimeOut(Exception e) {
        showLog(ERROR, "Time out connection!");
    }

    @Override
    public void onFailure(Exception e) {
        showLog(ERROR, "Server connection error");
    }

    @Override
    public void onFinishRequest() {
        binding.progressBar.setVisibility(View.GONE);
        showLog(INFO, "Request finished.");
    }

    //endregion

    private void showLog(boolean isError, String text) {
        Log.i(TAG, text);
        TextView textView = new TextView(this);
        textView.setTextColor(Color.parseColor(isError ? "#FF6130" : "#F2F2F2"));
        textView.setText(String.format("%s: %s", timeNow(), text));
        binding.logcat.addView(textView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.logcatScrollView.scrollToDescendant(textView);
        } else
            binding.logcatScrollView.scrollTo(0, binding.logcatScrollView.getBottom());
    }

}