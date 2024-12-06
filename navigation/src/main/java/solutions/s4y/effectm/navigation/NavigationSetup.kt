package solutions.s4y.effectm.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavigationSetup {
    fun setupBottomNavWithNavController(
        activity: AppCompatActivity,
        navController: NavController
    ) {
        val topLevelDestinations = setOf(
            R.id.navigation_flight_tickets, R.id.navigation_hotels//, R.id.navigation_notifications
        )
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        val navView: BottomNavigationView = activity.findViewById(R.id.bottom_navigation)

        activity.setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}