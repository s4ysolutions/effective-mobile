package solutions.s4y.effectivem.flight_tickets

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.databinding.ActivityFlightTicketsBinding
import solutions.s4y.effectivem.hotels.HotelsActivity
import solutions.s4y.effectivem.profile.ProfileActivity

@AndroidEntryPoint
class FlightTicketsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlightTicketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlightTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val tickets =
            navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_flight_tickets)
        tickets.setOnMenuItemClickListener(null)
        val hotels = navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_hotels)
        hotels.setOnMenuItemClickListener {
            // launch hotels activity
            val intent = android.content.Intent(this, HotelsActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        val profile = navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_profile)
        profile.setOnMenuItemClickListener {
            // launch profile activity
            val intent = android.content.Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        navView.selectedItemId = solutions.s4y.effectivem.views.R.id.navigation_flight_tickets

        // This is required by FragmentContainerView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_flight_tickets) as NavHostFragment
        val navController = navHostFragment.navController
        // val navController = findNavController(R.id.nav_host_fragment_activity_flight_tickets)
        /*
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
         */
        //navView.setupWithNavController(navController)
    }
}