package com.example.heroes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HeroesListActivity extends AppCompatActivity {

    private ListView list;
    List<Hero> heroesList;
    HeroAdapter heroAdapter;

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

        heroAdapter = new HeroAdapter(heroesList);
        list.setAdapter(heroAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_heroeslist_sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_heroeslist_sort_by_rank:
                SortByRank();
                Log.d(TAG, "SortByRank: SortByRank");
                heroAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_heroeslist_sort_by_name:
                SortByName();
                Log.d(TAG, "SortByName: SortByName");
                heroAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SortByRank() {
        // 1. extract the list from the adapter -> heroadapter.heroeslist
        Collections.sort(heroesList, new Comparator<Hero>() {
            @Override
            public int compare(Hero hero, Hero t1) {
                // negative number if thing comes before t1
                // 0 if thing and t1 are the same
                // positive number if thing comes after t1
                return hero.getRank() - t1.getRank();
            }
        });
        // the data in the adapter has changed, but it isn't aware
        // call the method notifyDataSetChanged on the adapter.

        Toast.makeText(this, "Sort by rank clicked", Toast.LENGTH_SHORT);
    }

    private void SortByName() {
        // 1. extract the list from the adapter -> heroadapter.heroeslist
        Collections.sort(heroesList, new Comparator<Hero>() {
            @Override
            public int compare(Hero hero, Hero t1) {
                 return hero.getName().toLowerCase()
                        .compareTo(t1.getName().toLowerCase());
            }
        });
        // the data in the adapter has changed, but it isn't aware
        // call the method notifyDataSetChanged on the adapter.

        Toast.makeText(this, "Sort by name clicked", Toast.LENGTH_SHORT);
    }

    private void wireWidgets() {
        list = findViewById(R.id.listView_main_list);
    }

    private void setListeners() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent targetIntent = new Intent(HeroesListActivity.this, HeroDetailActivity.class);
                targetIntent.putExtra(EXTRA_HERO, heroesList.get(position));
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

//    Collections.sort(heroList, new Comparator() {
//        // use the autocomplete when typing new Comparator() { ... }
//        // overrides compare method
//    }

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