package hashemi.code.sample.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hashemi.code.sample.adapter.PostListAdapter;
import hashemi.code.sample.model.Post;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostRecyclerView extends RecyclerView {
    //    private PostRealmListViewAdapter adapter;
    private PostListAdapter adapter;


    public PostRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public PostRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setHasFixedSize(true);
        addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new PostListAdapter(getContext());
//        adapter = new PostRealmListViewAdapter(getContext(),
//                App.realm.where(Post.class).sort(Post.FIELD_ID, Sort.DESCENDING).findAll());
        setAdapter(adapter);
    }

//    public void setLimitPageNo(int pageNo) {
//        adapter.setPageNo(pageNo);
//    }


//    public int getItemCount() {
//        return adapter.getItemCount();
//    }
//
//    public Post getItem(int p) {
//        return adapter.getItem(p);
//    }


    /*public void enablePagination(int limitPerPage) {
        adapter.setPagination(true);
        adapter.setPaginationPerPage(limitPerPage);
    }*/

    public void setData(List<Post> lstT) {
        adapter.setData(lstT);
    }

    public void addData(List<Post> lstT) {
        adapter.addData(lstT);
    }
}
