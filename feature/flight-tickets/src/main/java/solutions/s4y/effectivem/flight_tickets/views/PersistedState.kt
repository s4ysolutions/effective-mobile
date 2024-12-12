package solutions.s4y.effectivem.flight_tickets.views

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class PersistedState<T>(val key: Preferences.Key<T>, val defval: T) {
    // TODO: IRL it is enough to have string key name
    //       but this is a demo of ADT usage
    object CityDestination : PersistedState<String>(
        stringPreferencesKey("dest_city"), ""
    )

    object CountryDestination : PersistedState<String>(
        stringPreferencesKey("dest_country"), ""
    )

    fun asStateFlow(context: Context, coroutineScope: CoroutineScope): StateFlow<T> =
        context.ticketsDataSource.data.map { it[key] ?: defval }
            .distinctUntilChanged()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(1000),
                initialValue = defval
            )

    suspend fun set(context: Context, value: T) {
        context.ticketsDataSource.edit { settings ->
            if (value != settings[key]) {
                settings[key] = value
            }
        }
    }

    companion object {
        private val Context.ticketsDataSource: DataStore<Preferences> by preferencesDataStore(name = "tickets")
    }
}

// allow by syntax for property delegation
class PersistedStateFlowDelegate<T>(
    private val state: PersistedState<T>,
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : ReadWriteProperty<Any?, StateFlow<T>> {
    private val flow by lazy {
        state.asStateFlow(context, coroutineScope)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): StateFlow<T> = flow

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: StateFlow<T>) {
        val newValue = value.value
        coroutineScope.launch {
            state.set(context, newValue)
        }
    }
}


fun <T> PersistedState<T>.asStateFlow(
    context: Context,
    coroutineScope: CoroutineScope
): ReadOnlyProperty<Any?, StateFlow<T>> {
    return PersistedStateFlowDelegate(this, context, coroutineScope)
}
/* TODO?
mutableStateFlow variant: I do not like it, because of 2 reasons:
 - overcomplicated
 - there's a use case we want only update property
   without subscribing to its changes

    fun asStateFlow(context: Context, coroutineScope: CoroutineScope): MutableStateFlow<T> {
        val dataStore = context.ticketsDataSource
        val initialValue = runBlocking {
            dataStore.data.map { it[key] ?: defval }.last()
        }

        val flow = InterceptableStateFlow(initialValue, dataStore, coroutineScope)

        coroutineScope.launch {
            dataStore.data
                .map { it[key] ?: defval }
                .distinctUntilChanged()
                .collect { flow.updateFromDataStore(it) }
        }

        return flow

    }


    @OptIn(ExperimentalForInheritanceCoroutinesApi::class)
    inner class InterceptableStateFlow(
        initialValue: T,
        private val dataStore: DataStore<Preferences>,
        private val coroutineScope: CoroutineScope
    ) : MutableStateFlow<T> by MutableStateFlow(initialValue) {

        // Override the value setter to sync with DataStore
        override var value: T
            get() = MutableStateFlow<T>::value.get(this)
            set(newValue) {
                MutableStateFlow<T>::value.set(this, newValue)

                // Update DataStore whenever the value changes
                coroutineScope.launch {
                    dataStore.edit { preferences ->
                        preferences[key] = newValue
                    }
                }
            }

        fun updateFromDataStore(newValue: T) {
            MutableStateFlow<T>::value.set(this, newValue)
        }
    }

 */