package solutions.s4y.effectivem.flight_tickets.screens.search

import TicketsOffersRecyclerViewAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentSearchBinding
import solutions.s4y.effectm.domain.models.TicketOffer
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private val ticketsOffersRecyclerViewAdapter: TicketsOffersRecyclerViewAdapter =
        TicketsOffersRecyclerViewAdapter(emptyArray())
    private lateinit var binding: FragmentSearchBinding
    private val destCityUpdating = AtomicBoolean(false)
    private val destCountryUpdating = AtomicBoolean(false)

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
            isEnabled = false
            filters = arrayOf(solutions.s4y.effectivem.flight_tickets.input.cyrillicFilter)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (destCityUpdating.getAndSet(true)) {
                        Log.d(TAG, "afterTextChanged destCity already updating")
                        return
                    }
                    lifecycleScope.launch {
                        Log.d(TAG, "afterTextChanged destCity $s")
                        viewModel.setDestCity(s.toString())
                    }.invokeOnCompletion {
                        Log.d(TAG, "afterTextChanged destCity invokeOnCompletion")
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

        with(binding.destCountry) {
            isEnabled = false
            filters = arrayOf(solutions.s4y.effectivem.flight_tickets.input.cyrillicFilter)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (destCountryUpdating.getAndSet(true)) {
                        Log.d(TAG, "afterTextChanged destCountry already updating")
                        return
                    }
                    lifecycleScope.launch {
                        Log.d(TAG, "afterTextChanged destCountry $s")
                        viewModel.setDestCountry(s.toString())
                    }.invokeOnCompletion {
                        Log.d(TAG, "afterTextChanged destCountry invokeOnCompletion")
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
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                }
            }
            setOnClickListener {
            }
        }

        viewModel.destCityLiveData.observe(viewLifecycleOwner) {
            with(binding.destCity) {
                Log.d(TAG, "destCity.observe $it")
                isEnabled = true
                if (text.toString() != it) {
                    setText(it)
                }
            }
        }

        viewModel.destCountryLiveData.observe(viewLifecycleOwner) {
            with(binding.destCountry) {
                Log.d(TAG, "destCountry.observe $it")
                // currently redundant, but just to do not forget to enable it
                isEnabled = true
                if (text.toString() != it) {
                    setText(it)
                }
            }
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