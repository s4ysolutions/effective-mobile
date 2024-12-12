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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentSearchBinding
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

        binding.date.text = currentDateFormatDDMonthDoW(binding.date)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.github.setOnClickListener {
            val url = "https://github.com/s4ysolutions/effective-mobile"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.directFlights.apply {
            adapter = ticketsOffersRecyclerViewAdapter
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        with(binding.destCity) {
            filters = arrayOf(solutions.s4y.effectivem.flight_tickets.inputfilter.cyrillicFilter)
        }

        with(binding.destCountry) {
            filters = arrayOf(solutions.s4y.effectivem.flight_tickets.inputfilter.cyrillicFilter)
        }

        viewModel.offers.observe(viewLifecycleOwner) {
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

        fun currentDateFormatDDMonthDoW(view: View): Spannable  {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val locale = Locale("ru")
            // get short name of the month
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale)?.toString() ?: ""
            // get short name of the day of the week
            val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale) ?: ""
            // format "dat, month, grayed out day of the week""
            val formated = "$day $month,$dayOfWeek"
            val color = MaterialColors.getColor(view, com.google.android.material.R.attr.colorOnSurface)
            return SpannableString(formated).apply {
                setSpan(
                    ForegroundColorSpan(color),
                    day.length + 1 + month.length+1,
                    formated.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

    }
}