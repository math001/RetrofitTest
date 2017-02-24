package com.example.math.newsever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import bean.ROConsult;
import bean.TaoBao;
import json.Jackson;
import subscribers.SubscriberOnNextListener;

public class MainActivity extends AppCompatActivity {
    private SubscriberOnNextListener getHomeDataOnNext;
    private Button button;
    private TextView dataContent;
    HomeRequestor homeRequestor;
    SecurityRequestor securityRequestor;
    public static Object lock=new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.getHome);
        dataContent= (TextView) findViewById(R.id.dataContent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeRequestor.getData(getHomeDataOnNext);
            }
        });
        getHomeDataOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                TaoBao taoBao=(TaoBao)o;
                dataContent.setText(Jackson.toJson(taoBao));
                Toast.makeText(MainActivity.this, "aaaaaaaaaaaaaaaa", Toast.LENGTH_LONG).show();
            }
        };

        homeRequestor = new HomeRequestor(this);
        securityRequestor=new SecurityRequestor(this);
    }
}
