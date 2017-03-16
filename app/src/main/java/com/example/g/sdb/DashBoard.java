package com.example.g.sdb;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.VideoView;
import android.widget.MediaController;
import android.net.Uri;

import fragment.ManageFragment;
import fragment.SettingsFragment;
import fragment.help_fragment;
import fragment.homefragment;
import fragment.recent_activity;

public class DashBoard extends AppCompatActivity implements View.OnClickListener, homefragment.OnFragmentInteractionListener {



    //initialise the buttons that are going to be used
    private ImageButton btnMic, btnAlarm, btnDoorOpen, btnDoorClose;


    //initialise other components
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MediaController mediaController;
    private VideoView videoViewStream;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_RECENT_ACTIVITY = "recent activity";
    private static final String TAG_MANAGE_USERS = "user management";
    private static final String TAG_SETTINGS = "SettingsFragment";
    private static final String TAG_HELP = "help";
    private static final String TAG_LOGOUT = "log out";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        btnMic = (ImageButton) findViewById(R.id.dashboard_btn_microphone);
        btnAlarm = (ImageButton) findViewById(R.id.dashboard_btn_emergency);
        btnDoorOpen = (ImageButton) findViewById(R.id.dashboard_btn_unlock);
        videoViewStream = (VideoView) findViewById(R.id.dashboard_videoview);
        drawerLayout = (DrawerLayout) findViewById(R.id.dashboard_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_menu);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        btnAlarm.setOnClickListener(this);
        btnDoorOpen.setOnClickListener(this);
        btnMic.setOnClickListener(this);
        drawerLayout.setOnClickListener(this);


        /*
        Fragment HomeFragment = new Fragment();*/

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        ActionBar actionBar = getSupportActionBar();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();
        String s = "http://192.168.43.154:8554/";
        playStream(s);

        // initializing navigation menud
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        //String s="192.168.137.2:8554";

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawerLayout
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();


            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }



        //Closing drawerLayout on item click
        drawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                Fragment homeFragment = new homefragment();
                return homeFragment;
            case 1:
                // recent activity
                Fragment recentActivity = new recent_activity();
                return recentActivity;
            case 2:
                // manage users
                Fragment manageUsers = new ManageFragment();
                return manageUsers;
            case 3:
                // settings
                Fragment SettingsFragment = new SettingsFragment();
                return SettingsFragment;

            case 4:
                // help
                Fragment HelpFragmet = new help_fragment();
                return HelpFragmet;
            default:
                return new homefragment();
        }
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void setToolbarTitle() {

        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_recent_activity:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_RECENT_ACTIVITY;
                        break;
                    case R.id.nav_usr_management:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MANAGE_USERS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_help:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_HELP;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawerLayout closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawerLayout open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawerLayout layout
       // drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.navigation_menu, menu);
        }


        return true;
    }










    private void playStream(String src) {
        Uri UriSrc = Uri.parse(src);
        if (UriSrc == null) {
            Toast.makeText(DashBoard.this,
                    "UriSrc == null", Toast.LENGTH_LONG).show();
        } else {
            videoViewStream.setVideoURI(UriSrc);
            mediaController = new MediaController(this);
            videoViewStream.setMediaController(mediaController);
            videoViewStream.start();

            Toast.makeText(DashBoard.this,
                    "Connect: " + src,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            if (drawerLayout.isDrawerOpen(navigationView)){
                drawerLayout.closeDrawer(navigationView);
            }else{
                drawerLayout.openDrawer(navigationView);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}