package com.example.fermentationmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

public class BasicInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView BasicsText;
    private TextView SGlinkText;
    private TextView TemplinkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        setupUI();

    }



    private void setupUI() {
        toolbar = findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("The Basics of Brewing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        BasicsText = findViewById(R.id.BasicsText);
        SGlinkText = findViewById(R.id.SGlinkText);
        TemplinkText = findViewById(R.id.TemplinkText);

        //BasicsText.setMovementMethod(new ScrollingMovementMethod());

        BasicsText.setText(" Many homebrewers operate under the false assumption that they are " +
                "measuring the sugar content of their beer when they measure specific gravity. " +
                "While this is very close to the truth, it is not 100% accurate.\n" +
                "\n" +
                " The Specific Gravity of a liquid is that liquid’s density compared  to water." +
                "For example, a liquid with a Specific Gravity of 1.030 is  1.03 times denser than " +
                "water.\n" +
                "\n" +
                " In the case of your beer, there are several factors which can  contribute to the " +
                "density of your liquid before fermentation and  of your beer when it is finished " +
                "fermenting. There are sugars,  unfermentable starches, minerals, and other things " +
                "that can all  contribute to the density of the liquid.\n" +
                "\n" +
                " For more information on the appropriate Specific Gravity and Final  Gravity of " +
                "your beer, click the following link:\n "

        );

        SGlinkText.setText("Ranges of SG and FG by Style");

        SGlinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.brewersfriend" +
                        ".com/2017/05/07/beer-styles-original-gravity-and-final-gravity-chart-2017-update/"));
                startActivity(browserIntent);
            }
        });


        TemplinkText.setText("Click here for Information on Controlling the Temperature of your Brew");

        TemplinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://byo.com/" +
                        "article/controlling-fermentation-temperature-techniques/" +
                        "#:~:text=Normal%20ale%20fermentation%20temperatures%20range,5.5%20to%208." +
                        "3%20degrees%20Celsius)."));
                startActivity(browserIntent);
            }
        });

    }



}