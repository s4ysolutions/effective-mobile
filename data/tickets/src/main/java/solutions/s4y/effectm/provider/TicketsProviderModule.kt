package solutions.s4y.effectm.provider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import solutions.s4y.effectm.domain.dependencies.TicketsProvider

@Module
@InstallIn(SingletonComponent::class)
object TicketsProviderModule {
    @Provides
    fun provideTicketsProvider(@ApplicationContext context: Context): TicketsProvider {
        return RetrofitProvider(context)  // Now context is injected properly
    }
}