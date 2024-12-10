package solutions.s4y.effectivem.profile

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    }
}