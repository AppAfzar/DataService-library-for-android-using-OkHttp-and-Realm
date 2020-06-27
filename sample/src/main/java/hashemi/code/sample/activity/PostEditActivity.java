package hashemi.code.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import appafzar.dataservice.web.model.BaseModel;
import hashemi.code.sample.App;
import hashemi.code.sample.databinding.ActivityPostEditBinding;
import hashemi.code.sample.model.Post;
import hashemi.code.sample.presenter.PostPresenter;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostEditActivity extends PostBaseActivity {
    private ActivityPostEditBinding binding;
    private PostPresenter presenter;
    private Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        int id = intent.getIntExtra(Post.FIELD_ID, 0);
        post = App.realm.where(Post.class).equalTo(Post.FIELD_ID, id).findFirst();
        if (post == null) post = new Post();
        binding.setPost(post);
        presenter = new PostPresenter(this, this);
        binding.btnSave.setOnClickListener(v -> {
            save();
        });
    }

    @Override
    protected void onConnectionStatusChanged(boolean isConnected) {
        BaseModel.isConnected = isConnected;
    }

    private void save() {
        String title = String.valueOf(binding.title.getText()).trim();
        String description = String.valueOf(binding.description.getText()).trim();
        if (title.length() == 0) {
            Toast.makeText(this, "Title is Requerd.", Toast.LENGTH_SHORT).show();
            return;
        }
        Post p = new Post();
        p.setId(post.getId());
        p.setTitle(title);
        p.setDescription(description);
        if (p.getId() == 0)
            presenter.create(p);
        else
            presenter.update(p);
    }

    @Override
    public void onDataResponse(Post post) {
        Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show();
        finish();
    }

}