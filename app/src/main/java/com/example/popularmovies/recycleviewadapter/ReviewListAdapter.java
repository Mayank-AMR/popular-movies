package com.example.popularmovies.recycleviewadapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Review;

import java.util.List;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 10-05-2020 05:27 PM
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {
    List<Review> reviewList;

    public ReviewListAdapter(List<Review> rviewList) {
        this.reviewList = rviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review rv = reviewList.get(position);
        holder.author.setText(rv.getAuthor());
        holder.content.setText(rv.getContent());
        holder.v.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rv.getUrl()));
            v.getContext().startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        View v;
        TextView content;
        TextView author;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            content = v.findViewById(R.id.content_tv);
            author = v.findViewById(R.id.author_tv);
        }
    }
}
