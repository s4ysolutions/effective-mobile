package solutions.s4y.effectivem.flight_tickets.state

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import solutions.s4y.effectivem.flight_tickets.R
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dsFilter: DataStore<Preferences> by preferencesDataStore(name = "filter")

@Singleton
class FilterService @Inject constructor(@ApplicationContext private val context: Context) {
    init {
        runBlocking {
            setDestCity(context.getString(R.string.dest_city_default))
            setDestCountry("")
        }
    }

    val destCity: Flow<String> = context.dsFilter.data.map {
        it[DEST_CITY_KEY] ?: context.getString(R.string.dest_city_default)
    }

    suspend fun setDestCity(value: String) {
        context.dsFilter.edit { settings ->
            if (value != settings[DEST_CITY_KEY]) {
                settings[DEST_CITY_KEY] = value
            }
        }
    }


    suspend fun setDestCountry(value: String) {
        context.dsFilter.edit { settings ->
            if (value != settings[DEST_COUNTRY_KEY]) {
                settings[DEST_COUNTRY_KEY] = value
            }
        }
    }

    val destCountry = context.dsFilter.data.map { it[DEST_COUNTRY_KEY] ?: "" }

    companion object {
        private val DEST_CITY_KEY = stringPreferencesKey("dest_city")
        private val DEST_COUNTRY_KEY = stringPreferencesKey("dest_country")
    }
}