package solutions.s4y.effectivem.hotels.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is hotels Fragment"
    }
    val text: LiveData<String> = _text
}