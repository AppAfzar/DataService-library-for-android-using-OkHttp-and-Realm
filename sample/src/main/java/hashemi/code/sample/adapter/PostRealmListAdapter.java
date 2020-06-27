/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hashemi.code.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import appafzar.dataservice.adapter.RealmRecyclerViewAdapter;
import hashemi.code.sample.activity.PostActivity;
import hashemi.code.sample.databinding.PostItemBinding;
import hashemi.code.sample.model.Post;
import io.realm.OrderedRealmCollection;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostRealmListAdapter extends RealmRecyclerViewAdapter<Post, PostRealmListAdapter.ItemViewHolder> {

    private Context context;

    public PostRealmListAdapter(Context context, OrderedRealmCollection<Post> data) {
        super(data, true);
        this.context = context;
        // Only set this if the model class has a primary key that is also a integer or long.
        // In that case, {@code getItemId(int)} must also be overridden to return the key.
        setHasStableIds(false);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(PostItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Post post = getItem(position);
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
        int count = super.getItemCount();
        int totalPageItem = pageNo * limitPerPage;
        return (pagination && totalPageItem < count) ? totalPageItem : count;
    }

    @Override
    public void setPaginationPerPage(int limitPerPage) {
        this.limitPerPage = limitPerPage;
        notifyDataSetChanged();
    }

    @Override
    public void setPagination(boolean pagination) {
        this.pagination = pagination;
        notifyDataSetChanged();
    }

    @Override
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private PostItemBinding binding;

        ItemViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
