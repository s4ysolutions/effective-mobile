package solutions.s4y.effectm.provider

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import solutions.s4y.effectm.domain.models.ImageValue

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RetrofitProviderTest {
    @Test
    fun queryOffersShouldProvideModels() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val provider = RetrofitProvider(context)
        // Act
        val testObserver = provider.queryOffers().test()
        testObserver.await()
        // Assert
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        val offers = testObserver.values().first()
        offers.isNotEmpty()
        assert(offers[0].id is RemoteEntityId)
        assertEquals(1, (offers[0].id as RemoteEntityId).value)
        assertEquals(5000, offers[0].price.i)
        assertEquals(0, offers[0].price.f)
        assertEquals("RUB", offers[0].price.c)
        assertEquals("Die Antwoord", offers[0].title)
        assertEquals("Будапешт", offers[0].town)
        assertEquals(ImageValue.ImageValueById::class.java, offers[0].image.javaClass)
        assertEquals(1, (offers[0].image as ImageValue.ImageValueById).id)
        assert(offers[1].id is RemoteEntityId)
        assertEquals(2, (offers[1].id as RemoteEntityId).value)
        assertEquals(1999, offers[1].price.i)
        assertEquals(0, offers[1].price.f)
        assertEquals("RUB", offers[1].price.c)
        assertEquals("Socrat&Lera", offers[1].title)
        assertEquals("Санкт-Петербург", offers[1].town)
        assertEquals(ImageValue.ImageValueById::class.java, offers[1].image.javaClass)
        assertEquals(2, (offers[1].image as ImageValue.ImageValueById).id)
        assert(offers[2].id is RemoteEntityId)
        assertEquals(3, (offers[2].id as RemoteEntityId).value)
        assertEquals(2390, offers[2].price.i)
        assertEquals(0, offers[2].price.f)
        assertEquals("RUB", offers[2].price.c)
        assertEquals("Лампабикт", offers[2].title)
        assertEquals("Москва", offers[2].town)
        assertEquals(3, offers.size)
        assertEquals(ImageValue.ImageValueById::class.java, offers[2].image.javaClass)
        assertEquals(3, (offers[2].image as ImageValue.ImageValueById).id)
    }

    @Test
    fun queryTicketsShouldProvideModels() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val provider = RetrofitProvider(context)
        // Act
        val testObserver = provider.queryTickets().test()
        testObserver.await()
        // Assert
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        val tickets = testObserver.values().first()
        tickets.isNotEmpty()
        assert(tickets[0].id is RemoteEntityId)
        assertEquals(100, (tickets[0].id as RemoteEntityId).value)
        assertEquals("Самый удобный", tickets[0].badge)
        assertEquals(1999, tickets[0].price.i)
        assertEquals(0, tickets[0].price.f)
        assertEquals("RUB", tickets[0].price.c)
        assertEquals("На сайте Купибилет", tickets[0].providerName)
        assertEquals("Якутия", tickets[0].company)
        assertEquals("Москва", tickets[0].departure.town)
        assertEquals("2024-02-23T03:15:00", tickets[0].departure.date)
        assertEquals("VKO", tickets[0].departure.airport)
        assertEquals("Сочи", tickets[0].arrival.town)
        assertEquals("2024-02-23T07:10:00", tickets[0].arrival.date)
        assertEquals("AER", tickets[0].arrival.airport)
        assertFalse(tickets[0].hasTransfer)
        assertFalse(tickets[0].hasVisaTransfer)
        assertFalse(tickets[0].luggage.hasLuggage)
        assertEquals(1082, tickets[0].luggage.price.i)
        assertEquals(0, tickets[0].luggage.price.f)
        assertEquals("RUB", tickets[0].luggage.price.c)
        assertTrue(tickets[0].handLuggage.hasHandLuggage)
        assertEquals("55x20x40", tickets[0].handLuggage.size)
        assertFalse(tickets[0].isReturnable)
        assertTrue(tickets[0].isExchangable)
        assert(tickets[6].id is RemoteEntityId)
        assertEquals(106, (tickets[6].id as RemoteEntityId).value)
        assertEquals(9500, tickets[6].price.i)
        assertEquals(0, tickets[6].price.f)
        assertEquals("RUB", tickets[6].price.c)
        assertEquals("На сайте Aeroflot", tickets[6].providerName)
        assertEquals("Аэрофлот", tickets[6].company)
        assertEquals("Москва", tickets[6].departure.town)
        assertEquals("2024-02-23T21:00:00", tickets[6].departure.date)
        assertEquals("VKO", tickets[6].departure.airport)
        assertEquals("Сочи", tickets[6].arrival.town)
        assertEquals("2024-02-24T00:20:00", tickets[6].arrival.date)
        assertEquals("AER", tickets[6].arrival.airport)
        assertFalse(tickets[6].hasTransfer)
        assertFalse(tickets[6].hasVisaTransfer)
        assertFalse(tickets[6].luggage.hasLuggage)
        assertEquals(5999, tickets[6].luggage.price.i)
        assertEquals(0, tickets[6].luggage.price.f)
        assertEquals("RUB", tickets[6].luggage.price.c)
        assertFalse(tickets[6].handLuggage.hasHandLuggage)
        assertFalse(tickets[6].isReturnable)
        assertFalse(tickets[6].isExchangable)
        assertEquals(7, tickets.size)
    }

    @Test
    fun queryTickersOffersShouldProvideModels() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val provider = RetrofitProvider(context)
        // Act
        val testObserver = provider.queryTicketsOffers().test()
        testObserver.await()
        // Assert
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        val offers = testObserver.values().first()
        offers.isNotEmpty()
        assert(offers[0].id is RemoteEntityId)
        assertEquals(1, (offers[0].id as RemoteEntityId).value)
        assertEquals("Уральские авиалинии", offers[0].title)
        assertArrayEquals(arrayOf("07:00", "09:10", "10:00", "11:30", "14:15", "19:10", "21:00", "23:30"), offers[0].timeRange)
        assertEquals(3999, offers[0].price.i)
        assertEquals(0, offers[0].price.f)
        assertEquals("RUB", offers[0].price.c)
        assert(offers[1].id is RemoteEntityId)
        assertEquals(10, (offers[1].id as RemoteEntityId).value)
        assertEquals("Победа", offers[1].title)
        assertArrayEquals(arrayOf("09:10", "21:00"), offers[1].timeRange)
        assertEquals(4999, offers[1].price.i)
        assertEquals(0, offers[1].price.f)
        assertEquals("RUB", offers[1].price.c)
        assert(offers[2].id is RemoteEntityId)
        assertEquals(30, (offers[2].id as RemoteEntityId).value)
        assertEquals("NordStar", offers[2].title)
        assertArrayEquals(arrayOf("07:00"), offers[2].timeRange)
        assertEquals(2390, offers[2].price.i)
        assertEquals(0, offers[2].price.f)
        assertEquals("RUB", offers[2].price.c)
        assertEquals(3, offers.size)
    }
}