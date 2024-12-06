package solutions.s4y.effectivem.flight_tickets.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import solutions.s4y.effectivem.flight_tickets.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* val buttonSearch: ImageView = binding.buttonSearch
        buttonSearch.setOnClickListener {
            val searchDialog = SearchDialogFragment()
            searchDialog.show(parentFragmentManager, "search")
        }
         */
        /*
      homeViewModel.text.observe(viewLifecycleOwner) {
        textView.text = it
      }
         */
        homeViewModel.offers.observe(viewLifecycleOwner) {
            println(it.joinToString("\n"))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}