package com.example.heroes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HeroDetailActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewDescription;
    private TextView textViewSuperpower;
    private TextView textViewRank;
    private ImageView imageViewImage;
    private Hero hero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);

        wireWidgets();

        Intent lastIntent = getIntent();
        Hero hero = lastIntent.getParcelableExtra(HeroesListActivity.EXTRA_HERO);

        textViewName.setText("Name: " + hero.getName());
        textViewDescription.setText("Description: " + hero.getDescription());
        textViewSuperpower.setText("Superpower: " + hero.getSuperpower());
        textViewRank.setText("Rank #" + hero.getRank());
//        imageViewImage.setImageResource(hero.get);
    }

    private void wireWidgets() {
        textViewName = findViewById(R.id.textView_heroDetail_name);
        textViewDescription = findViewById(R.id.textView_heroDetail_description);
        textViewSuperpower = findViewById(R.id.textView_heroDetail_superpower);
        textViewRank = findViewById(R.id.textView_heroDetail_rank);
        imageViewImage = findViewById(R.id.imageView_heroDetail_image);
    }
}
