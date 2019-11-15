package com.example.weitblickapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.TabStopSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListAdapter;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListFragment;
import com.example.weitblickapp_android.ui.profil.ProfilFragment;
import com.example.weitblickapp_android.ui.project.ProjectListAdapter;
import com.example.weitblickapp_android.ui.project.ProjectListFragment;
import com.example.weitblickapp_android.ui.stats.StatsFragment;
import com.example.weitblickapp_android.ui.tabs.TabsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        mAppBarConfiguration = new AppBarConfiguration.Builder(

                R.id.nav_tabs, R.id.nav_project, R.id.nav_imprint,
                R.id.nav_stats, R.id.nav_location, R.id.nav_blog, R.id.nav_faq)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);
        }

        // Menu-Navigation-Item onclick- Function
        public void startMapsActivity(MenuItem item){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profil) {
            ProfilFragment fragment = new ProfilFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
