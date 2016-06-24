package com.example.pbeekman.newyorktimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pbeekman.newyorktimessearch.Article;
import com.example.pbeekman.newyorktimessearch.R;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));
        String url = article.getUrl();
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_menu, menu);
        MenuItem item = menu.findItem(R.id.share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
        Intent.createChooser(shareIntent, "Share this article");
        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    public void onBack(MenuItem item) {
        this.finish();
    }
}
