package solutions.s4y.effectivem.flight_tickets.views

import android.content.Context
import android.content.ContextWrapper
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import solutions.s4y.effectivem.flight_tickets.R
import java.util.concurrent.atomic.AtomicBoolean

class PersistedInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {
    private val persistedState: PersistedState<String>
    private var isRestoring: Boolean = false

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            if (isRestoring) {
                return
            }
            val newValue = charSequence?.toString() ?: ""
            lifecycleOwner?.let { lifecycleOwner ->
                if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    lifecycleOwner.lifecycleScope.launch {
                        persistedState.set(context, newValue)
                    }
                }
            }
        }

        override fun afterTextChanged(editable: Editable?) {
        }
    }

    private val _isSetByPersistedState = AtomicBoolean(false)
    val isSetByPersistedState: AtomicBoolean
        get() = _isSetByPersistedState

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PersistedInputEditText,
            0, 0
        ).apply {
            try {
                val persistKey = getInt(R.styleable.PersistedInputEditText_persistKey, -1)
                persistedState = when (persistKey) {
                    // TODO: IRL it is enough to have string key name
                    //       but this is a demo of ADT usage
                    1 -> PersistedState.CityDeparture
                    2 -> PersistedState.CityDestination
                    else -> throw IllegalArgumentException("Unknown persist key $persistKey")
                }
                // Example: Observe the persisted state in the activity's lifecycle
            } finally {
                recycle()
            }
        }

        addTextChangedListener(textWatcher)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleOwner?.let { lifecycleOwner ->
            lifecycleOwner.lifecycleScope.launch {
                persistedState.asStateFlow(context, lifecycleOwner.lifecycleScope)
                    .collect { newValue ->
                        if (text.toString() != newValue) {
                            removeTextChangedListener(textWatcher)
                            _isSetByPersistedState.set(true)
                            setText(newValue)
                            _isSetByPersistedState.set(false)
                            addTextChangedListener(textWatcher)
                        }
                    }
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        isRestoring = true
        super.onRestoreInstanceState(state)
        isRestoring = false
    }

    private val lifecycleOwner: LifecycleOwner? by lazy {
        var context = this.context
        while (context is ContextWrapper) {
            if (context is LifecycleOwner) {
                return@lazy context
            }
            context = context.baseContext
        }
        null;
    }

}