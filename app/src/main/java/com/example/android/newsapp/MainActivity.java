package com.example.android.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // global EditText obj
    public static EditText search_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set EditText view to obj
        search_input = (EditText) findViewById(R.id.search_input);

        // set Button view and onClickListener to obj
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_input.getText();
                Intent results = new Intent(MainActivity.this, ArticleList.class);
                startActivity(results);
            }
        });

    }
}
