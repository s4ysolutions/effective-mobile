package solutions.s4y.effectivem.hotels

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import solutions.s4y.effectivem.hotels.databinding.ActivityHotelsBinding
import solutions.s4y.effectivem.profile.ProfileActivity

class HotelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val tickets =
            navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_flight_tickets)
        tickets.setOnMenuItemClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("effectivem://tickets"))
            startActivity(intent)
            // launch hotels activity
            /*
            val intent = android.content.Intent(this, FlightTicketsActivity::class.java)
            startActivity(intent)
            finish()
             */
            true
        }
        val hotels = navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_hotels)
        hotels.setOnMenuItemClickListener(null)
        val profile = navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_profile)
        profile.setOnMenuItemClickListener {
            // launch profile activity
            val intent = android.content.Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        navView.selectedItemId = solutions.s4y.effectivem.views.R.id.navigation_hotels

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_hotels) as NavHostFragment
        val navController = navHostFragment.navController

        // val navController = findNavController(R.id.nav_host_fragment_activity_hotels)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
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