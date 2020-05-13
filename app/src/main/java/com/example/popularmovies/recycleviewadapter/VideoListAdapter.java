package com.example.popularmovies.recycleviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.R2;
import com.example.popularmovies.model.Videos;
import com.example.popularmovies.network.ApiHandler;

import java.util.List;

import butterknife.BindView;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 10-05-2020 05:27 PM
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private List<Videos> mVideoList;

    public VideoListAdapter(List<Videos> mVideoList) {
        this.mVideoList = mVideoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final Videos video = mVideoList.get(position);
        holder.title.setText(video.getName());
        holder.type.setText(video.getType());
        holder.videoItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiHandler.launchVideoPlayer(v.getContext(),video.getKey());
            }
        });
    }
    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "VideoViewHolder";
        /*@BindView(R2.id.tv_type) */TextView type;
        /*@BindView(R2.id.tv_title)*/ TextView title;
        View videoItemView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoItemView = itemView;
            type  = videoItemView.findViewById(R.id.tv_type);
            title = videoItemView.findViewById(R.id.tv_title);
        }
    }
}
