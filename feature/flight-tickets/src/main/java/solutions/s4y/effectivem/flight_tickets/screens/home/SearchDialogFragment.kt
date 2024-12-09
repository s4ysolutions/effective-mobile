package solutions.s4y.effectivem.flight_tickets.screens.home

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectivem.flight_tickets.input.cyrillicFilter
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class SearchDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val destCityUpdating = AtomicBoolean(false)
    private val destCountryUpdating = AtomicBoolean(false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet =
                (dialogInterface as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                // Set height to screen height
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams

                bottomSheet.findViewById<TextInputEditText>(R.id.dest_city)?.let { destCity ->
                    destCity.filters = arrayOf(cyrillicFilter)
                    destCity.addTextChangedListener(cityWatcher)
                    viewModel.destCityLiveData.observe(viewLifecycleOwner) { text ->
                        Log.d(TAG, "destCity.observe $text")
                        if (text != destCity.text.toString()) {
                            Log.d(TAG, "destCity.observe setText $text")
                            destCity.setText(text)
                        }
                    }
                }

                bottomSheet.findViewById<TextInputEditText>(R.id.dest_country)?.let { destCountry ->
                    with(destCountry) {
                        filters = arrayOf(cyrillicFilter)
                        addTextChangedListener(countryWatcher)
                    }
                    viewModel.destCountryLiveData.observe(viewLifecycleOwner) { text ->
                        Log.d(TAG, "destCountry.observe $text")
                        if (text != destCountry.text.toString()) {
                            Log.d(TAG, "destCountry.observe setText $text")
                            destCountry.setText(text)
                        }
                    }
                    bottomSheet.findViewById<View>(R.id.clear)?.setOnClickListener {
                        destCountry.setText("")
                    }
                    bottomSheet.findViewById<TextInputEditText>(R.id.dest_city)?.let { destCity ->
                        bottomSheet.findViewById<View>(R.id.exchange)?.setOnClickListener {
                            val swap = destCountry.text.toString().orEmpty()
                            destCountry.setText(destCity.text.toString().orEmpty())
                            destCity.setText(swap)
                        }
                    }
                }
            }
    }

    private val countryWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d(TAG, "beforeTextChanged countryWatcher $s")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d(TAG, "onTextChanged countryWatcher $s")
        }

        override fun afterTextChanged(s: Editable?) {
            Log.d(TAG, "afterTextChanged countryWatcher $s")
            if (s.isNullOrEmpty()) {
                return
            }
            val country = s.toString().lowercase()
            if (country.length > 7 || country == "турция" || country == "россия" || country == "таиланд") {
                findNavController().navigate(R.id.action_flight_navigation_search_dialog_to_flight_navigation_search)
            }
            if (destCountryUpdating.getAndSet(true)) {
                Log.d(TAG, "afterTextChanged countryWatcher already updating")
                return
            }
            lifecycleScope.launch {
                Log.d(TAG, "afterTextChanged countryWatcher $s")
                viewModel.setDestCountry(s.toString())
            }.invokeOnCompletion {
                Log.d(TAG, "afterTextChanged countryWatcher invokeOnCompletion")
                destCountryUpdating.set(false)
            }
        }
    }

    private val cityWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d(TAG, "beforeTextChanged cityWatcher $s")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d(TAG, "onTextChanged cityWatcher $s")
        }

        override fun afterTextChanged(s: Editable?) {
            Log.d(TAG, "afterTextChanged cityWatcher $s")
            if (destCityUpdating.getAndSet(true)) {
                Log.d(TAG, "afterTextChanged cityWatcher already updating")
                return
            }
            lifecycleScope.launch {
                Log.d(TAG, "afterTextChanged cityWatcher $s")
                viewModel.setDestCity(s.toString())
            }.invokeOnCompletion {
                Log.d(TAG, "afterTextChanged cityWatcher invokeOnCompletion")
                destCityUpdating.set(false)
            }
        }
    }

    companion object {
        private val TAG = SearchDialogFragment::class.java.simpleName
    }
}