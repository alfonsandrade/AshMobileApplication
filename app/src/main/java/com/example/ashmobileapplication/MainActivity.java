package com.example.ashmobileapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the NavController from the NavHostFragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Setup ActionBar with NavController (if using a Toolbar)
        // Uncomment the next line if you have a Toolbar configured as the ActionBar
        // NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // This method is called when the up button is pressed. Just return the NavController's navigateUp method.
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }
}
