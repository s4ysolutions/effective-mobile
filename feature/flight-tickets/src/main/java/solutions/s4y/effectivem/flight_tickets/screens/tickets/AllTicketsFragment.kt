package solutions.s4y.effectivem.flight_tickets.screens.tickets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentAllTicketsBinding
import solutions.s4y.effectivem.flight_tickets.screens.search.SearchFragment.Companion.TAG
import solutions.s4y.effectm.domain.models.Ticket

@AndroidEntryPoint
class AllTicketsFragment : Fragment() {
    private lateinit var binding: FragmentAllTicketsBinding
    private val ticketsRecyclerViewAdapter: TicketsRecyclerViewAdapter =
        TicketsRecyclerViewAdapter(emptyArray())

    private val viewModel: AllTicketsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllTicketsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tickets.apply {
            adapter = ticketsRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.ticketsLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showLoading()
            } else {
                showTickets(it)
            }
        }

        viewModel.route.observe(viewLifecycleOwner) {
            binding.route.text = it
        }

        viewModel.filter.observe(viewLifecycleOwner) {
            binding.filter.text = it
        }
        return root
    }

    private fun showTickets(tickets: Array<Ticket>) {
        binding.loading.visibility = View.GONE
        binding.tickets.visibility = View.VISIBLE
        binding.tickets.post {
            ticketsRecyclerViewAdapter.tickets = tickets
        }
        Log.d(TAG, "showTickets")
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.tickets.visibility = View.GONE
        Log.d(TAG, "showLoading")
    }

    companion object {
        fun newInstance() = AllTicketsFragment()
    }
}