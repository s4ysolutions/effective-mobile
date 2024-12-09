package solutions.s4y.effectivem.views

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity: AppCompatActivity() {
    open fun setupBottomNavigationView(navView: BottomNavigationView, currentMenuItem: Int) {
        val tickets =
            navView.menu.findItem(R.id.navigation_flight_tickets)
        if (currentMenuItem == R.id.navigation_flight_tickets) {
            navView.selectedItemId = R.id.navigation_flight_tickets
            tickets.setOnMenuItemClickListener(null)
        } else {
            tickets.setOnMenuItemClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.component = ComponentName("solutions.s4y.effectivem", "solutions.s4y.effectivem.flight_tickets.FlightTicketsActivity")
                intent.data = Uri.parse("effectivem://tickets")
                startActivity(intent)
                finish()
                true
            }
        }
        val hotels = navView.menu.findItem(R.id.navigation_hotels)
        if (currentMenuItem == R.id.navigation_hotels) {
            hotels.setOnMenuItemClickListener(null)
            navView.selectedItemId = R.id.navigation_hotels
        } else {
            hotels.setOnMenuItemClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.component = ComponentName("solutions.s4y.effectivem", "solutions.s4y.effectivem.hotels.HotelsActivity")
                intent.data = Uri.parse("effectivem://hotels")
                startActivity(intent)
                finish()
                true
            }
        }
        val profile = navView.menu.findItem(solutions.s4y.effectivem.views.R.id.navigation_profile)
        if (currentMenuItem == R.id.navigation_profile) {
            profile.setOnMenuItemClickListener(null)
            navView.selectedItemId = R.id.navigation_profile
        } else {
            profile.setOnMenuItemClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.component = ComponentName("solutions.s4y.effectivem", "solutions.s4y.effectivem.profile.ProfileActivity")
                intent.data = Uri.parse("effectivem://profile")
                startActivity(intent)
                finish()
                true
            }
        }
    }
}