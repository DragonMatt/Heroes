package com.example.heroes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class HeroesListActivity extends AppCompatActivity {

    private ListView list;
    List<Hero> heroesList;

    public static final String EXTRA_HERO = "hero";

    public static final String TAG = HeroesListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_list);

        wireWidgets();
        setListeners();

        InputStream XmlFileInputStream = getResources().openRawResource(R.raw.heroes); // getting XML
        String jsonString = readTextFile(XmlFileInputStream);

        Gson gson = new Gson();
        Hero[] heroes = gson.fromJson(jsonString, Hero[].class);
        heroesList = Arrays.asList(heroes);
        Log.d(TAG, "onCreate: " + heroesList.toString());

        HeroAdapter heroAdapter = new HeroAdapter(heroesList);
        list.setAdapter(heroAdapter);
    }

    private void wireWidgets() {
        list = findViewById(R.id.listView_main_list);
    }

    private void setListeners() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int hero = list.getCheckedItemPosition();
                Intent targetIntent = new Intent(HeroesListActivity.this, HeroDetailActivity.class);
                targetIntent.putExtra(EXTRA_HERO, heroesList.get(hero + 1));
                startActivity(targetIntent);
                finish();
            }
        });
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    private class HeroAdapter extends ArrayAdapter<Hero> {
        // make an instance variable to keep track of the hero list
        private List<Hero> heroesList;

        public HeroAdapter(List<Hero> heroesList) {
            // since we're in the HeroListActivity class, we already have the context
            // we're hardcoding in a particular layout, so we don't need to put it in
            // the constructor either
            // we'll send a placeholder resource to the superclass of -1
            super(HeroesListActivity.this, -1, heroesList);
            this.heroesList = heroesList;
        }

        // The goal of the adapter is to link your list to the listview
        // and tell the listview where each aspect of the list item goes
        // so we override a method called getView


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 1. inflate a layout
            LayoutInflater inflater = getLayoutInflater();

            // check if convertview is null; if so, replace it
            if(convertView == null) {
                // R.layout.item_hero is a custom layout we make that represents
                // what a single item would look like in our listview
                convertView = inflater.inflate(R.layout.item_hero, parent, false);
            }

            // 2. wire widgets and link the hero to those widgets
            // instead of calling findViewById at the activity class level,
            // we're calling it from the inflated layout to find THOSE widgets
            TextView textViewName = convertView.findViewById(R.id.textView_heroItem_name);
            TextView textViewRank = convertView.findViewById(R.id.textView_heroItem_rank);
            TextView textViewDescription = convertView.findViewById(R.id.textView_heroItem_description);
            // do this for as many widgets as you need

            // to get the hero that you need out of the list
            Hero hero = heroesList.get(position);
            // set the values for each widget, use the position parameter variable
            textViewName.setText(String.valueOf(hero.getName()));
            textViewDescription.setText(String.valueOf(hero.getDescription()));
            textViewRank.setText(String.valueOf(hero.getRank()));
            // and set the values for widgets


            // 3. return inflated view
            return convertView;
        }
    }
}