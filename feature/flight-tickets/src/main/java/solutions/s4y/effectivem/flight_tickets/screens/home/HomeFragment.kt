package solutions.s4y.effectivem.flight_tickets.screens.home

import OffersPagerAdapter
import OffersRecyclerViewAdapter
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentHomeBinding
import solutions.s4y.effectivem.flight_tickets.input.cyrillicFilter
import solutions.s4y.effectm.domain.models.Offer
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    // TODO: There can be only one
    private val useRecyclerViewAdapter = 0
    private val offersRecyclerViewAdapter: OffersRecyclerViewAdapter =
        OffersRecyclerViewAdapter(emptyArray())
    private val offersPagerAdapter: OffersPagerAdapter = OffersPagerAdapter(emptyArray())
    private val offersPager2Adapter: OffersPager2Adapter = OffersPager2Adapter(emptyArray())

    private lateinit var binding: FragmentHomeBinding
    private val destCityUpdating = AtomicBoolean(false)
    private val destCountryUpdating = AtomicBoolean(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TODO: There can be only one
        when (useRecyclerViewAdapter) {
            0 -> {
                binding.offersPager.visibility = View.GONE
                binding.offersPager2.visibility = View.GONE
                binding.offers.apply {
                    addItemDecoration(DividerItemDecoration(resources.getDimension(R.dimen.offers_interval).toInt()));
                    visibility = View.VISIBLE
                    adapter = offersRecyclerViewAdapter
                    clipToPadding = false
                    clipChildren = false
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }

            1 -> {
                binding.offers.visibility = View.GONE
                binding.offersPager2.visibility = View.GONE
                binding.offersPager.apply {
                    visibility = View.VISIBLE
                    adapter = offersPagerAdapter
                    offscreenPageLimit = 3
                }
            }

            else -> {
                binding.offers.visibility = View.GONE
                binding.offersPager.visibility = View.GONE
                with(binding.offersPager2) {
                    visibility = View.VISIBLE
                    adapter = offersPager2Adapter
                    offscreenPageLimit = 3
                    with(getChildAt(0) as RecyclerView) {
                        clipToPadding = false
                        clipChildren = false
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        }

        with(binding.cardSearch.search) {
            setOnClickListener {
                findNavController().navigate(R.id.action_flight_navigation_home_to_flight_navigation_search_dialog)
            }
        }

        with(binding.cardSearch.destCity) {
            filters = arrayOf(cyrillicFilter)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (destCityUpdating.getAndSet(true)) {
                        Log.d(TAG, "afterTextChanged already updating")
                        return
                    }
                    lifecycleScope.launch {
                        Log.d(TAG, "afterTextChanged $s")
                        viewModel.setDestCity(s.toString())
                    }.invokeOnCompletion {
                        Log.d(TAG, "afterTextChanged invokeOnCompletion")
                        destCityUpdating.set(false)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }

        with(binding.cardSearch.destCountry) {
            filters = arrayOf(cyrillicFilter)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (destCountryUpdating.getAndSet(true)) {
                        Log.d(TAG, "afterTextChanged already updating")
                        return
                    }
                    lifecycleScope.launch {
                        Log.d(TAG, "afterTextChanged $s")
                        viewModel.setDestCountry(s.toString())
                    }.invokeOnCompletion {
                        Log.d(TAG, "afterTextChanged invokeOnCompletion")
                        destCountryUpdating.set(false)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    findNavController().navigate(R.id.action_flight_navigation_home_to_flight_navigation_search_dialog)
                }
            }
            setOnClickListener {
                findNavController().navigate(R.id.action_flight_navigation_home_to_flight_navigation_search_dialog)
            }
        }

        viewModel.destCityLiveData.observe(viewLifecycleOwner) {
            with(binding.cardSearch.destCity) {
                Log.d(TAG, "destCity.observe $it")
                isEnabled = true
                if (text.toString() != it) {
                    setText(it)
                }
            }
        }

        viewModel.destCountryLiveData.observe(viewLifecycleOwner) {
            with(binding.cardSearch.destCountry) {
                Log.d(TAG, "destCountry.observe $it")
                // currently redundant, but just to do not forget to enable it
                isEnabled = true
                if (text.toString() != it) {
                    setText(it)
                }
            }
        }

        viewModel.offersLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showLoading()
            } else {
                showOffers(it)
            }
        }


        return root
    }

    private fun showOffers(offers: Array<Offer>) {
        binding.loading.visibility = View.GONE
        binding.offers.visibility = View.VISIBLE
        binding.offers.post {
            when {
                useRecyclerViewAdapter == 0 -> {
                    offersRecyclerViewAdapter.offers = offers
                }

                useRecyclerViewAdapter == 1 -> {
                    offersPagerAdapter.offers = offers
                }

                else -> {
                    offersPagerAdapter.offers = offers
                }
            }
        }
        Log.d(TAG, "showOffers")
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.offers.visibility = View.GONE
        Log.d(TAG, "showLoading")
    }

    private class DividerItemDecoration(val spacing: Int) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = spacing
        }

    }


    companion object {
        private const val TAG = "HomeFragment"
    }
}