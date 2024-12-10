package solutions.s4y.effectivem.flight_tickets

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.databinding.ActivityFlightTicketsBinding
import solutions.s4y.effectivem.views.BaseActivity

@AndroidEntryPoint
class FlightTicketsActivity : BaseActivity() {

    private lateinit var binding: ActivityFlightTicketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlightTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView
        setupBottomNavigationView(navView, solutions.s4y.effectivem.views.R.id.navigation_flight_tickets)

    }
}