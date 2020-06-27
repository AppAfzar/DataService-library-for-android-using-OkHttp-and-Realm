package hashemi.code.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hashemi.code.sample.activity.PostActivity;
import hashemi.code.sample.databinding.PostItemBinding;
import hashemi.code.sample.model.Post;


public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ItemViewHolder> {

    private OnItemLongClickListener mItemLongClickListener;
    private OnItemClickListener mItemClickListener;
    private List<Post> postList;
    private Context context;
//    private DateUtil dateUtil;


    public PostListAdapter(Context context) {
        this.context = context;
        postList = new ArrayList<>();
    }

    public void setData(List<Post> mPostList) {
        this.postList.clear();
        notifyDataSetChanged();
        if (mPostList != null) {
            this.postList.addAll(mPostList);
            notifyDataSetChanged();
        }
    }

    public void addData(List<Post> mPostList) {
        if (this.postList == null) {
            this.postList = new ArrayList<>(mPostList);
        } else
            this.postList.addAll(mPostList);
        notifyDataSetChanged();
    }


    /**
     * Inflates the correct layout according to the View Type.
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(PostItemBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Post post = postList.get(position);
        if (post == null) return;
        holder.binding.setPost(post);
        holder.itemView.setOnClickListener(v ->
                {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Post.FIELD_ID, post.getId());
                    context.startActivity(intent);
                }
        );
    }


    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    interface OnItemLongClickListener {
        void onPostItemLongClick(Post post, int position);
    }

    interface OnItemClickListener {
        void onPostItemClick(Post post);
    }

    //region ViewHolders

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private PostItemBinding binding;

        ItemViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    //endregion

}




