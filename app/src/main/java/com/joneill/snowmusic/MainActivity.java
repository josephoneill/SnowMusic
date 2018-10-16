package com.joneill.snowmusic;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.joneill.snowmusic.activities.SettingsActivity;
import com.joneill.snowmusic.activities.ThemesActivity;
import com.joneill.snowmusic.adapters.LibraryPagerAdapter;
import com.joneill.snowmusic.chromecast.MusicChromecastHelper;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.data.SongsUserInfo;
import com.joneill.snowmusic.fragments.ArtistFragment;
import com.joneill.snowmusic.helper.SavedInformationPrefs;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.helper.SongHelper;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.service.MusicConnection;
import com.joneill.snowmusic.service.MusicService;
import com.joneill.snowmusic.song.Queue;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.views.SlidingPanelLayout;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.ThemedFragmentActivity;

import java.lang.reflect.Type;
import java.util.List;


public class MainActivity extends ThemedFragmentActivity implements View.OnClickListener {
    private static final int PERMISSION_READ_CODE = 55;
    private static final int PERMISSION_WRITE_CODE = 56;
    private LibraryPagerAdapter mLibraryPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout libraryTabs;
    public ThemedToolbar toolbar;
    public DrawerLayout drawerLayout;
    private SystemBarTintHelper tintHelper;
    private SongPlayerPanel songPanel;
    private MaterialMenuIconToolbar materialMenu;
    private boolean isNavOpen;
    public MusicChromecastHelper musicChromecastHelper;
    private SlidingPanelLayout panelView;
    private SavedInformationPrefs savedInformationPrefs;
    private int currTabIndex;
    private MusicConnection.OnMusicServicePreparedCallback onMusicServicePreparedCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startup();
        requestPermissions();
        if (savedInstanceState != null) {
            Log.e("not null", "joneill saved instance isn't null!");
        }
    }

    private void startup() {
        savedInformationPrefs = new SavedInformationPrefs(this);
        Settings.getInstance().loadSettings(this);
        Settings.getInstance().createGenericSettings(this);
        musicChromecastHelper = new MusicChromecastHelper(this);
        musicChromecastHelper.init();
        isNavOpen = false;
        toolbar = (ThemedToolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("My Library");
        toolbar.setTitleTextColor(Color.WHITE);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        tintHelper = new SystemBarTintHelper(this);
        ThemeManager.getInstance().configureStatusBar(this, tintHelper, 0f);

        songPanel = new SongPlayerPanel(this, viewGroup, tintHelper, findViewById(R.id.sliding_layout), true);

        setToolbarMenu();
        setupDrawerLayout();
    }

    private void init() {
        initBasics();
        setSupportActionBar(toolbar);
        setupTabLayout();
        loadPreviousState();
    }

    private void loadSettings() {
        libraryTabs.setBackgroundColor((int) Settings.getInstance().getSettingsData().get(Settings.INT_SINGLE_TOOLBAR_COLOR));
    }

    private void loadPreviousState() {
        int tabIndex = (int) savedInformationPrefs.getData().get(SavedInformationPrefs.INT_CURR_TAB_INDEX);
        mViewPager.setCurrentItem(tabIndex);
    }

    public void requestPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_READ_CODE);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length == 0) {
            //android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            init();
        }
    }

    private void initBasics() {
        onMusicServicePreparedCallback = new MusicConnection.OnMusicServicePreparedCallback() {
            @Override
            public void onMusicServicePrepared(MusicService musicService) {
                String queueJson = savedInformationPrefs.getSharedPreferences().getString(SavedInformationPrefs.OBJECT_CURR_QUEUE, "{0}");
                int currSongIndex = savedInformationPrefs.getSharedPreferences().getInt(SavedInformationPrefs.INT_CURR_QUEUE_SONG_INDEX, 0);
                Type listType = new TypeToken<List<Long>>() {}.getType();
                List<Long> queueSongIds = null;
                Queue queue = null;
                try {
                    queueSongIds = new Gson().fromJson(queueJson, listType);
                    queue = Queue.buildQueue(queueSongIds, currSongIndex);
                } catch (IllegalStateException e) {

                } catch (JsonSyntaxException e) {

                }

                if(queue != null && queueSongIds.size() > 0) {
                    int songPos = (int) savedInformationPrefs.getData().get(SavedInformationPrefs.INT_CURR_SONG_POS);
                    SongDataHolder.getInstance().getSongPanel().loadPreviousState(queue, songPos);
                }
            }
        };

        SongsUserInfo.getInstance().loadSongsUserInfo(this);

        SongHelper.getInstance().init(this, onMusicServicePreparedCallback);

        loadData();

        initPanel();

        Log.e("IS running", "Is MusicService running? snowmusic joneill " + isMyServiceRunning(MusicService.class));
        SongHelper.getInstance().setReceiverRunning(isMyServiceRunning(MusicService.class));
        SongHelper.getInstance().setSongPlayerPanel(songPanel);
        SongHelper.getInstance().startServices(this);
    }

    private void initPanel() {
        panelView = (SlidingPanelLayout) findViewById(R.id.dragView);
        panelView.setShouldCloseOC((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SONG_PANEL_CLOSE_OC));
        panelView.setShouldExpandOC((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SONG_PANEL_EXPAND_OC));
        panelView.setSongPanel(songPanel);
        panelView.startGestureDetector();
    }

    private void setToolbarMenu() {
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_themes:
                        i = new Intent(MainActivity.this, ThemesActivity.class);
                        startActivity(i);
                        return true;

                    case R.id.action_settings:
                        i = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Nav", Toast.LENGTH_LONG).show();
                MaterialMenuDrawable.IconState newState = null;
               /* if(isNavOpen) {
                    newState = MaterialMenuDrawable.IconState.BURGER;
                } else {
                    newState = MaterialMenuDrawable.IconState.ARROW;
                }

                isNavOpen = !isNavOpen;*/
                drawerLayout.openDrawer(Gravity.LEFT);
                materialMenu.animatePressedState(newState);
            }
        });
    }

    private void setupDrawerLayout() {
        materialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN) {
            @Override
            public int getToolbarViewId() {
                return R.id.main_toolbar;
            }
        };
        materialMenu.setNeverDrawTouch(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                materialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isNavOpen ? 2 - slideOffset : slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isNavOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isNavOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (isNavOpen) {
                        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    } else {
                        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    }
                }
            }
        });
        drawerLayout.setStatusBarBackgroundColor(Color.argb(0, 0, 0, 0));
    }

    private void setupTabLayout() {
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mLibraryPagerAdapter =
                new LibraryPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mLibraryPagerAdapter);

        libraryTabs = (TabLayout) findViewById(R.id.library_tabs);
        libraryTabs.setupWithViewPager(mViewPager);
        libraryTabs.setSelectedTabIndicatorColor(Color.WHITE);
        libraryTabs.setTabTextColors(Color.WHITE, Color.WHITE);
        libraryTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                mLibraryPagerAdapter.getFragment(position).colorToolbar();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int oldPos = mViewPager.getCurrentItem();
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //positionOffset is a float between 0-1
                if(positionOffset < 1.0f) {
                    int color1 = mLibraryPagerAdapter.getFragment(position).getToolbarColor();
                    int color2 = 0;
                    //Moving left
                    if(position < oldPos) {
                        color2 = mLibraryPagerAdapter.getFragment(position - 1).getToolbarColor();
                    } else {
                        color2 = mLibraryPagerAdapter.getFragment(position + 1).getToolbarColor();
                    }
                    int color = Utils.interpolateColor(color1, color2, positionOffset);
                    colorToolbar(color);
                    currTabIndex = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                currTabIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadData() {
        SongDataHolder.getInstance().setActivity(this);
        SongDataHolder.getInstance().setSongPanel(songPanel);
        SongDataHolder.getInstance().init();
        SongDataHolder.getInstance().loadSongInfo();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void colorToolbar(int color) {
        toolbar.setBackgroundColor(color);
        libraryTabs.setBackgroundColor(color);

        /***
         * if(Utils.useWhiteFont(toolbarColor)) {
         toolbar.setTitleTextColor(Color.WHITE);
         tabs.setTabTextColors(Color.WHITE, Color.WHITE);
         } else {
         toolbar.setTitleTextColor(Color.BLACK);
         tabs.setTabTextColors(Color.BLACK, Color.BLACK);
         }

         */
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds songs to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        musicChromecastHelper.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_themes) {
            Intent i = new Intent(MainActivity.this, ThemesActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        musicChromecastHelper.onPause();
        SongHelper.getInstance().onPause(this);
        saveData();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SongHelper.getInstance().onResume(this);
        musicChromecastHelper.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SongHelper.getInstance().onStop(this);
    }

    @Override
    protected void onDestroy() {
        musicChromecastHelper.onDestroy();
        SongDataHolder.getInstance().onDestroy();
        songPanel.onDestroy();
        SongHelper.getInstance().onDestroy(this);
        MusicService musicService = SongHelper.getInstance().getMusicService();
       /* if(!musicService.isPlaying()) {
            musicService.stopForeground(false);
            musicService.stopSelf();
            //musicService.onDestroy();
        }
        musicService.setListeners(musicService.contentView); */
        super.onDestroy();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        materialMenu.syncState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        materialMenu.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        ArtistFragment replaceFragment = new ArtistFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, replaceFragment)
                .addToBackStack(null);
        ft.commit();
    }

    private void saveData() {
        savedInformationPrefs.getData().put(SavedInformationPrefs.INT_CURR_TAB_INDEX, currTabIndex);
        Gson gson = new Gson();
        if(SongDataHolder.getInstance().getSongPanel() != null
                && SongDataHolder.getInstance().getSongPanel().getMusicService() != null) {
            Queue queue = SongDataHolder.getInstance().getSongPanel().getMusicService().getQueue();
            if (queue != null) {
                String queueJson = gson.toJson(queue.getSongIdList());
                savedInformationPrefs.getData().put(SavedInformationPrefs.OBJECT_CURR_QUEUE, queueJson);
                int songIndex = queue.getCurrSongIndex();
                savedInformationPrefs.getData().put(SavedInformationPrefs.INT_CURR_QUEUE_SONG_INDEX, songIndex);
            }

            int position = SongDataHolder.getInstance().getSongPanel().getMusicService().getIntPosition();
            savedInformationPrefs.getData().put(SavedInformationPrefs.INT_CURR_SONG_POS, position);
        }
        savedInformationPrefs.saveData();
    }
}