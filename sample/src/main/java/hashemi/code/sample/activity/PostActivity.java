package hashemi.code.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import appafzar.dataservice.web.model.BaseModel;
import hashemi.code.sample.App;
import hashemi.code.sample.R;
import hashemi.code.sample.databinding.ActivityPostBinding;
import hashemi.code.sample.model.Post;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostActivity extends PostBaseActivity {
    private ActivityPostBinding binding;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onConnectionStatusChanged(boolean isConnected) {
        BaseModel.isConnected = isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int id = intent.getIntExtra(Post.FIELD_ID, 0);
        if (id == 0) finish();
        post = App.realm.where(Post.class).equalTo(Post.FIELD_ID, id).findFirst();
        if (post == null) finish();
        binding.setPost(post);
    }

    //region Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, PostEditActivity.class);
            intent.putExtra(Post.FIELD_ID, post.getId());
            startActivity(intent);
        }
        return true;
    }
    //endregion

}