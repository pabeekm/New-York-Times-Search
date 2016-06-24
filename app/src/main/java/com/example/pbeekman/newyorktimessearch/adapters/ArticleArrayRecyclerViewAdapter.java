package com.example.pbeekman.newyorktimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pbeekman.newyorktimessearch.Article;
import com.example.pbeekman.newyorktimessearch.R;
import com.example.pbeekman.newyorktimessearch.activities.ArticleActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbeekman on 6/22/16.
 */
public class ArticleArrayRecyclerViewAdapter extends RecyclerView.Adapter<ArticleArrayRecyclerViewAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView thumbnail;
        @BindView(R.id.ivVideo) ImageView video;
        @BindView(R.id.tvTitle) TextView title;
        @BindView(R.id.tvDate) TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition(); // gets item position
                    Intent i = new Intent(context, ArticleActivity.class);
                    i.putExtra("article", Parcels.wrap(articles.get(position)));
                    context.startActivity(i);
                }
            });
        }
    }


    public ArticleArrayRecyclerViewAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    public void clearAll() {
        articles = new ArrayList<>();
    }
    public void addAll(List<Article> l) {
        articles.addAll(l);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Article article = articles.get(position);
        viewHolder.thumbnail.setImageResource(0);
        viewHolder.video.setImageResource(0);
        viewHolder.title.setText(article.getHeadline());
        viewHolder.date.setText(article.getDate());
        viewHolder.date.setVisibility(View.GONE);
        String thumbnail = article.getThumbnail();
        if(!TextUtils.isEmpty(thumbnail)) {
            Glide.with(getContext()).load(thumbnail).into(viewHolder.thumbnail);
        } else {
            Glide.with(getContext()).load(R.drawable.logo).centerCrop().into(viewHolder.thumbnail);
        }
        try {
            if (article.getUrl().contains("video/movies")) {
                Glide.with(getContext()).load(R.drawable.video2).centerCrop().into(viewHolder.video);
            }
        }catch (NullPointerException e) {}
    }

    public int getItemCount() {
        return articles.size();
    }
}