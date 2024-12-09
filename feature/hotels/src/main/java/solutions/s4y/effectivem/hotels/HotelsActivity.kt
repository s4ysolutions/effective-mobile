package solutions.s4y.effectivem.hotels

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import solutions.s4y.effectivem.hotels.databinding.ActivityHotelsBinding
import solutions.s4y.effectivem.views.BaseActivity

class HotelsActivity : BaseActivity() {

    private lateinit var binding: ActivityHotelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        setupBottomNavigationView(navView, solutions.s4y.effectivem.views.R.id.navigation_hotels)

        // val navController = findNavController(R.id.nav_host_fragment_activity_hotels)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_hotels) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.flight_navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
         */
    }
}