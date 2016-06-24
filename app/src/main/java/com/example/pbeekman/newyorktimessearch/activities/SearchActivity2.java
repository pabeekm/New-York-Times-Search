package com.example.pbeekman.newyorktimessearch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.pbeekman.newyorktimessearch.Article;
import com.example.pbeekman.newyorktimessearch.R;
import com.example.pbeekman.newyorktimessearch.SpacesItemDecoration;
import com.example.pbeekman.newyorktimessearch.adapters.ArticleArrayRecyclerViewAdapter;
import com.example.pbeekman.newyorktimessearch.fragments.SettingsDialogFragment;
import com.example.pbeekman.newyorktimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity2 extends AppCompatActivity {
    String query;
    Boolean topStories;
    SearchView searchView;
    @BindView(R.id.rvArticles) RecyclerView rvResults;
    ArrayList<Article> articles;
    ArticleArrayRecyclerViewAdapter adapter;

    SettingsDialogFragment settingsDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        ButterKnife.bind(this);
        setupViews();
        loadTopStories();
        settingsDialogFragment = SettingsDialogFragment.newInstance("Settings");
    }

    public void loadTopStories(MenuItem item) {
        loadTopStories();
    }
    public void loadTopStories() {
        topStories = true;
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/topstories/v2/home.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "e256f110ace149e6ab7a05c3f2b15c73");
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONArray("results");
                    adapter.clearAll();
                    adapter.addAll(Article.fromJSONArray(articleJsonResults, true));
                    adapter.notifyDataSetChanged();
                    rvResults.scrollToPosition(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadPage(final int page, String query) {
        topStories = false;
        if (page == 0) {
            adapter.clearAll();
            adapter.notifyDataSetChanged();
        }
        this.query = query;
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        try{
            if (!settingsDialogFragment.getNewsDeskValues().equals("")) {
                String newsDeskParamValue =
                    String.format("news_desk:(%s)", settingsDialogFragment.getNewsDeskValues());
                params.put("fq", newsDeskParamValue);
            }
            if (!settingsDialogFragment.getDate().equals("")) {
                params.put("begin_date", settingsDialogFragment.getDate());
            }
            if (!settingsDialogFragment.getSortOrder().equals("None")) {
                params.put("sort", settingsDialogFragment.getSortOrder().toLowerCase());
            }

        } catch (NullPointerException e) {}
        params.put("api-key", "e256f110ace149e6ab7a05c3f2b15c73");
        params.put("page", page);
        params.put("q", query);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults, false));
                    adapter.notifyDataSetChanged();
                    if (page == 0)
                        rvResults.scrollToPosition(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setupViews() {
        articles = new ArrayList<>();
        adapter = new ArticleArrayRecyclerViewAdapter(this, articles);
        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.addItemDecoration(new SpacesItemDecoration(10));
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!topStories)
                    loadPage(page, query);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadPage(0, query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void showSettingsDialog(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        settingsDialogFragment.show(fm, "fragment_settings");
    }

}
