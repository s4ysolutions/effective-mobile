package solutions.s4y.effectivem.flight_tickets.screens.search

import TicketsOffersRecyclerViewAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentSearchBinding
import solutions.s4y.effectm.domain.models.TicketOffer

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val ticketsOffersRecyclerViewAdapter: TicketsOffersRecyclerViewAdapter =
        TicketsOffersRecyclerViewAdapter(emptyArray())
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.directFlights.apply {
            adapter = ticketsOffersRecyclerViewAdapter
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        searchViewModel.offers.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showLoading()
            } else {
                showOffers(it)
            }
        }
        return root
    }

    private fun showOffers(offers: Array<TicketOffer>) {
        binding.loading.visibility = View.GONE
        binding.directFlights.visibility = View.VISIBLE
        binding.directFlights.post {
            ticketsOffersRecyclerViewAdapter.offers = offers
        }
        Log.d(TAG, "showOffers")
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.directFlights.visibility = View.GONE
        Log.d(TAG, "showLoading")
    }

    companion object {
        const val TAG = "SearchFragment"
    }
}