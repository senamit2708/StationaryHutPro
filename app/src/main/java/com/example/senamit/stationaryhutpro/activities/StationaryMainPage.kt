package com.example.senamit.stationaryhutpro.activities

//import android.support.v4.widget.DrawerLayout
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.support.v7.widget.Toolbar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout


import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.senamit.stationaryhutpro.R


class StationaryMainPage : AppCompatActivity() {

    private var drawerLayout: DrawerLayout?= null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_stationary_main_page)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
//        supportActionBar?.run { setDisplayHomeAsUpEnabled(true)
//        setHomeButtonEnabled(true)}

            val host: NavHostFragment =
                    supportFragmentManager
                            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

            val navController = host.navController

            setupActionBar(navController)
            Toast.makeText(this, "sorry", Toast.LENGTH_SHORT).show();

        }
        private fun setupActionBar(navController: NavController) {
            drawerLayout = findViewById(R.id.drawer_layout)
          setupActionBarWithNavController(this, navController, drawerLayout)
        }

        override fun onSupportNavigateUp(): Boolean {
            return NavigationUI.navigateUp(drawerLayout,
                    Navigation.findNavController(this, R.id.my_nav_host_fragment))
        }



    }
