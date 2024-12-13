package solutions.s4y.effectivem.flight_tickets.screens.search

import TicketsOffersRecyclerViewAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentSearchBinding
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectivem.flight_tickets.views.PersistedState
import solutions.s4y.effectm.domain.models.TicketOffer
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
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

        // binding.date.text = currentDateFormatDDMonthDoW(binding.date)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        /*
                binding.github.setOnClickListener {
                    val url = "https://github.com/s4ysolutions/effective-mobile"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
         */

        binding.directFlights.apply {
            adapter = ticketsOffersRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        with(binding.destCity) {
            filters = arrayOf(solutions.s4y.effectivem.flight_tickets.inputfilter.cyrillicFilter)
        }

        with(binding.destCountry) {
            filters = arrayOf(solutions.s4y.effectivem.flight_tickets.inputfilter.cyrillicFilter)
        }

        binding.exchange.setOnClickListener {
            val departureCity = binding.destCity.text.toString()
            val destinationCity = binding.destCountry.text.toString()
            binding.destCity.setText(destinationCity)
            binding.destCountry.setText(departureCity)
        }

        binding.clear.setOnClickListener {
            binding.destCity.setText("")
            binding.destCountry.setText("")
        }

        viewModel.offers.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showLoading()
            } else {
                showOffers(it)
            }
        }

        PersistedState.DepartureDate.asStateFlow(requireContext(), lifecycle.coroutineScope)
            .asLiveData().observe(viewLifecycleOwner) {
                binding.date.text = it.formatDDMonthDoW(binding.date)
            }

        binding.date.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Дата вылета")
                .build()
            datePicker.addOnPositiveButtonClickListener {
                lifecycle.coroutineScope.launch {
                    PersistedState.DepartureDate.set(requireContext(), it)
                }
            }
            datePicker.show(childFragmentManager, "date_picker")
        }

        PersistedState.ReturnDate.asStateFlow(requireContext(), lifecycle.coroutineScope)
            .asLiveData().observe(viewLifecycleOwner) {
                if (it == -1L) {
                    binding.returnDate.text = "обратно"
                    binding.returnDate.setIconResource(solutions.s4y.effectivem.views.R.drawable.plus)
                } else {
                    binding.returnDate.text = it.formatDDMonthDoW(binding.returnDate)
                    binding.returnDate.icon = null
                }
            }

        binding.returnDate.setOnClickListener() {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Дата возврата")
                .build()
            datePicker.addOnPositiveButtonClickListener {
                lifecycle.coroutineScope.launch {
                    PersistedState.ReturnDate.set(requireContext(), it)
                }
            }
            datePicker.show(childFragmentManager, "date_picker")
        }

        binding.allTickets.setOnClickListener {
            findNavController().navigate(R.id.action_flight_navigation_search_to_all_tickets)
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

        fun Long.formatDDMonthDoW(view: View): Spannable {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = this
            val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val locale = Locale("ru")
            // get short name of the month
            val month =
                calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale)?.toString() ?: ""
            // get short name of the day of the week
            val dayOfWeek =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale) ?: ""
            // format "dat, month, grayed out day of the week""
            val formated = "$day $month,$dayOfWeek"
            val color =
                MaterialColors.getColor(view, com.google.android.material.R.attr.colorOnSurface)
            return SpannableString(formated).apply {
                setSpan(
                    ForegroundColorSpan(color),
                    day.length + 1 + month.length + 1,
                    formated.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

    }
}