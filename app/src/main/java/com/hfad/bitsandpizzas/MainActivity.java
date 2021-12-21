package com.hfad.bitsandpizzas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {

    private String[] titles;
    private ShareActionProvider shareActionProvider;
    // This will be placed programatically for adding toglle button
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public DrawerLayout drawerLayout;
    public ListView drawerList;
    private int currentPosition = 0;

    //Implementation of DrawerLaout onClick Items
    private class DrawerItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class DrawerListener  implements DrawerLayout.DrawerListener{

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            // Action to onDrawerSlider
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            // Action to onDrawerOpened
            Log.d("DrawerLayout","Open");
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            // Action to onDrawerClosed
            Log.d("DrawerLayout","Closed");
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            // Action to onDrawerStateChanged
        }
    }

    //Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    public void selectItem(int position){
        Log.d("DrawerLayout","Item Clicked"+position);
        // Code to run when item gets Clicked
        Fragment fragment;
        switch (position){
            case 1:
                fragment = new PizzaFragment();
                break;
            case 2:
                fragment = new PastaFragment();
                break;
            case 3:
                fragment = new StoresFragment();
                break;
            default:
                fragment = new TopFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment,"visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
        // Set the action bar title
        setActionBarTitle(position);
        drawerLayout.closeDrawer(drawerList);
        currentPosition = position;
    }
    private void setActionBarTitle(int position){
        String title;
        Log.d(this.toString(),"Position "+position+" "+Arrays.toString(titles));
        if (position == 0){
            title = getResources().getString(R.string.app_name);
        }else{
            title = titles[position];
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get Titltes for DrawerLayout
        titles = getResources().getStringArray(R.array.titles);
        if (savedInstanceState != null){
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        }

        setContentView(R.layout.activity_main);
        // Get ListView of the Drawer Layout
        drawerList = findViewById(R.id.drawer);
        // Array Adapter to populate the ListView of Drawer Layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,titles);
        drawerList.setAdapter(adapter);
        // Attaching onItemClickListener to Drawer Layout
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerLayout = findViewById(R.id.drawer_layout);
        // Add Toggle Button to Drawer Layout in Action Bar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.nav_open,R.string.nav_close);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawerLayout.addDrawerListener(new DrawerListener());

        /*
         Adding functionality for the back button
         Whenever back button will be clicked the current fragment reference will be added to backsack
         For that we need to add onBackStackChangedListener and add give instructions for whatever
         has to be done
         */
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("visible_fragment");
                if (fragment instanceof TopFragment){
                    currentPosition = 0;
                }
                if (fragment instanceof PizzaFragment){
                    currentPosition = 1;
                }
                if (fragment instanceof PastaFragment){
                    currentPosition = 2;
                }
                if (fragment instanceof StoresFragment){
                    currentPosition = 3;
                }
                setActionBarTitle(currentPosition);
                drawerList.setItemChecked(currentPosition,true);
            }
        });


    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setIntent("This is example text");
        return super.onCreateOptionsMenu(menu);
    }

    // override the onOptionsItemSelected() function to implement
    // the item click listener callback to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_create_order:
                Intent intent = new Intent(this,OrderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setIntent(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",currentPosition);
    }
}