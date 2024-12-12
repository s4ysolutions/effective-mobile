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
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectivem.flight_tickets.inputfilter.cyrillicFilter
import solutions.s4y.effectivem.flight_tickets.views.PersistedInputEditText

@AndroidEntryPoint
class SearchDialogFragment : BottomSheetDialogFragment() {
    private var destinationCountryInput: PersistedInputEditText? = null
    private val isSetByPersistedState: Boolean
        get() =
            destinationCountryInput?.isSetByPersistedState?.get() == true

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
                }

                bottomSheet.findViewById<TextInputEditText>(R.id.dest_country)?.let { destCountry ->
                    assert(destCountry is PersistedInputEditText)
                    destinationCountryInput = destCountry as PersistedInputEditText
                    with(destCountry) {
                        filters = arrayOf(cyrillicFilter)
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

    override fun onResume() {
        super.onResume()
        destinationCountryInput?.addTextChangedListener(countryWatcher)
    }

    override fun onPause() {
        super.onPause()
        destinationCountryInput?.removeTextChangedListener(countryWatcher)
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
            if (isSetByPersistedState) {
                return
            }
            val country = s.toString().lowercase()
            if (country.length > 7 || country == "турция" || country == "россия" || country == "таиланд") {
                findNavController().navigate(R.id.action_flight_navigation_search_dialog_to_flight_navigation_search)
            }
        }
    }

    companion object {
        private val TAG = SearchDialogFragment::class.java.simpleName
    }
}