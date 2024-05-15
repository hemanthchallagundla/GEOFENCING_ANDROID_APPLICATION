package com.example.login;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.view.MenuItem;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.firebase.auth.FirebaseAuth;
import com.example.login.databinding.ActivityHomePageBinding;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String userEmail = getIntent().getStringExtra("email");
        setSupportActionBar(binding.appBarHomePage.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_setg, R.id.nav_updateg)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        updateNavHeader(userEmail);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_setg) {
                    navigateToSetGeofenceFragment();
                    return true;
                }
                else if(itemId == R.id.nav_home){
                    navigateToHomeFragment();
                    return true;
                } else if (itemId == R.id.nav_updateg){
                    navigateToUpdateGeofence();

                } else if (itemId == R.id.nav_logout) {
                    logout();
                    return true;
                }
                return false;
            }
        });
    }

    private void navigateToUpdateGeofence() {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_home_page);
        navController.navigate(R.id.nav_updateg);
    }

    private void navigateToHomeFragment() {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_home_page);
        navController.navigate(R.id.nav_home);
    }

    private void navigateToSetGeofenceFragment() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        navController.navigate(R.id.nav_setg); // Navigate directly to the Set Geofence fragment
    }

    private void updateNavHeader(String userEmail) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0); // Get the first header view
        TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);
        textViewEmail.setText(userEmail);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    private void logout() {
        // Sign out the user (Firebase Authentication)
        FirebaseAuth.getInstance().signOut();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity to prevent the user from going back to HomePageActivity
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
