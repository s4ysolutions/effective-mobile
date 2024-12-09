package solutions.s4y.effectivem.profile

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import solutions.s4y.effectivem.profile.databinding.ActivityProfileBinding
import solutions.s4y.effectivem.views.BaseActivity

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        setupBottomNavigationView(navView, solutions.s4y.effectivem.views.R.id.navigation_profile)

        // val navController = findNavController(R.id.nav_host_fragment_activity_profile)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
         */
    }
}