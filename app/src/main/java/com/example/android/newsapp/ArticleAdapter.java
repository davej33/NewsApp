package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dnj on 11/8/16.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, ArrayList<Article> articles){
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentArticle.getmTitle());

        TextView section = (TextView) listItemView.findViewById(R.id.section);
        section.setText(currentArticle.getmSection());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(currentArticle.getmDate());

        return listItemView;
    }
}

