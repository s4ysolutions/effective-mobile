package solutions.s4y.effectivem.hotels

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    }
}