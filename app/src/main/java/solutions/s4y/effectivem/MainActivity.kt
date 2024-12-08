package solutions.s4y.effectivem

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import solutions.s4y.effectivem.R
import solutions.s4y.effectivem.flight_tickets.FlightTicketsActivity
import solutions.s4y.effectivem.hotels.HotelsActivity
import solutions.s4y.effectivem.profile.ProfileActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        val intent = Intent(
            this, when (uri?.host) {
                "tickets" -> FlightTicketsActivity::class.java
                "hotels" -> HotelsActivity::class.java
                "profile" -> ProfileActivity::class.java
                else -> FlightTicketsActivity::class.java
            }
        )
        startActivity(intent)
        finish()
    }
}