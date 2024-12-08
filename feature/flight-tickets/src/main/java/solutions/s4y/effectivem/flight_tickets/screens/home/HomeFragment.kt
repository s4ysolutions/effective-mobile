package solutions.s4y.effectivem.flight_tickets.screens.home

import OffersPagerAdapter
import OffersRecyclerViewAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentHomeBinding
import solutions.s4y.effectm.domain.models.Offer

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    // TODO: There can be only one
    private val useRecyclerViewAdapter = 0
    private val offersRecyclerViewAdapter: OffersRecyclerViewAdapter =
        OffersRecyclerViewAdapter(emptyArray())
    private val offersPagerAdapter: OffersPagerAdapter = OffersPagerAdapter(emptyArray())
    private val offersPager2Adapter: OffersPager2Adapter = OffersPager2Adapter(emptyArray())

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.cardSearch.search.setOnClickListener {
            val searchDialog = SearchDialogFragment()
            searchDialog.show(parentFragmentManager, "search")
        }

        // TODO: There can be only one
        when (useRecyclerViewAdapter) {
            0 -> {
                binding.offersPager.visibility = View.GONE
                binding.offersPager2.visibility = View.GONE
                binding.offers.apply {
                    visibility = View.VISIBLE
                    adapter = offersRecyclerViewAdapter
                    clipToPadding = false
                    clipChildren = false
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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


        homeViewModel.offers.observe(viewLifecycleOwner) {
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

    companion object {
        private const val TAG = "HomeFragment"
    }
}