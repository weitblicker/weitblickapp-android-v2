package com.example.weitblickapp_android.ui.news;

import android.app.ListFragment;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsListFragment extends ListFragment {
    ArrayList<NewsViewModel> news = new ArrayList<NewsViewModel>();


    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadNews();
    }

    private void loadNews(){

        //TODO put this to options
        final String url = "https://new.weitblicker.org/rest/news/";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        // check whether an update is necessary or not, maybe merge
                        // update news -> clear existing list of news
                        news.clear();

                        try {
                            System.out.println(response.toString());
                            JSONArray articllist = response.getJSONArray("news-list");
                            try {
                                for (int i = 0; i < articllist.length(); i++) {
                                    JSONObject container = articllist.getJSONObject(i);
                                    JSONObject article = container.getJSONObject("news-article");
                                    String title = article.getString("article-title");
                                    String text = article.getString("description");


                                    //Creates News-object and assigns values to it
                                    NewsViewModel newsArticle = new NewsViewModel();
                                    newsArticle.setTitle(title);
                                    newsArticle.setText(text);


                                    news.add(newsArticle);
                                }
                            } catch (JSONException e){
                                // TODO log this error
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            // TODO log this error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        error.printStackTrace();
                    }
                });
    }
}
