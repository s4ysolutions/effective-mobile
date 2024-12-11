## Architecture 

### Data layer

[Data layer](https://github.com/s4ysolutions/effective-mobile/tree/main/data/tickets) is Android
library module that implements [TicketsProvider interface](https://github.com/s4ysolutions/effective-mobile/blob/main/domain/src/main/java/solutions/s4y/effectm/domain/dependencies/TicketsProvider.kt) consumed
by domain layer with the [Retrofit](class RetrofitProvider @Inject constructor(@ApplicationContext context: Context) : TicketsProvider {
) client emulating
the [http requests](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RestClient.kt#L16C1-L24C54)
by [reading the json](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RestClient.kt#L40C1-L55C45) files from the assets folder.

Exposes the implementation of the TicketsProvider interface as a [Hilt](https://github.com/s4ysolutions/effective-mobile/blob/main/data/tickets/src/main/java/solutions/s4y/effectm/provider/TicketsProviderModule.kt) object.

Also it implements the [EntityId interface](https://github.com/s4ysolutions/effective-mobile/blob/main/data/tickets/src/main/java/solutions/s4y/effectm/provider/RemoteEntityId.kt) of the domain layer (see next section).

### Domain Layer

Domain layer is [pure kotlin library](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/domain/build.gradle.kts#L3C1-L3C23)
(no Android dependencies) module with the main purpose to
provide [TicketsService](https://github.com/s4ysolutions/effective-mobile/blob/main/domain/src/main/java/solutions/s4y/effectm/domain/TicketsService.kt) for the rest of the application.

#### Dependencies

Domain layer defines [TicketsProvider interface](https://github.com/s4ysolutions/effective-mobile/blob/main/domain/src/main/java/solutions/s4y/effectm/domain/dependencies/TicketsProvider.kt)
to be [implemented](https://github.com/s4ysolutions/effective-mobile/blob/main/data/tickets/src/main/java/solutions/s4y/effectm/provider/RetrofitProvider.kt)
by the data layer. This way
the domain layer is decoupled from the data layer, can consume the data from different sources
and can be tested independently.

For demo purposes this interface assumes [RxJava API](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/domain/src/main/java/solutions/s4y/effectm/domain/dependencies/TicketsProvider.kt#L9C24-L9C44)
(i.e. data layer can be implemented with non-kotlin libraries).

#### Models
Domain layer abstracts the [domain models](https://github.com/s4ysolutions/effective-mobile/tree/main/domain/src/main/java/solutions/s4y/effectm/domain/models)
from the data layer presentation. The data layer is
responsible to [convert](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/json/JsonOffer.kt#L14C1-L28C10)
data from the external sources to the domain models.

Also domain layer introduces the [ModelId](https://github.com/s4ysolutions/effective-mobile/blob/main/domain/src/main/java/solutions/s4y/effectm/domain/models/ModelId.kt)
interface to do not depend on the actual [equality
implementation](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RemoteEntityId.kt#L5C1-L5C51).
(At least the ID can be either a String or an Int depending on the database).

While domain layer models does not follow active record pattern they have some additional properties
to be used in the presentation layer. For example Money class has [formatted](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/domain/src/main/java/solutions/s4y/effectm/domain/models/Money.kt#L15C1-L18C34)
property to be [used in the UI](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/search/TicketsOffersRecyclerViewAdapter.kt#L78C1-L78C57).

The service mostly does nothing with the data from the data layer, but [converts](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/domain/src/main/java/solutions/s4y/effectm/domain/TicketsService.kt#L8C54-L8C78)
RxJava API to Flow API to be used by Android consumers.

The service is provided with [Singleton pattern](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/domain/src/main/java/solutions/s4y/effectm/domain/TicketsService.kt#L15C1-L19C10)
for demo purposes.

### UI & presentation layers

UI constructed according to features modules pattern and uses LiveData as a reactive sources of data.

According to the mockup the app have few independent components: tickers, hotels, profile, etc and
following this design the app is divided into [features modules](https://github.com/s4ysolutions/effective-mobile/tree/main/feature): 
 - :feature:fligt-tickets
 - :feature:hotels
 - :feature:profile

Each of them has one [activity](https://github.com/s4ysolutions/effective-mobile/blob/main/feature/profile/src/main/java/solutions/s4y/effectivem/profile/ProfileActivity.kt)
and at least one [fragment](https://github.com/s4ysolutions/effective-mobile/blob/main/feature/profile/src/main/java/solutions/s4y/effectivem/profile/screens/home/HomeFragment.kt)
to implement the UI of the module.

As a consequence of the features modules pattern the [:app](https://github.com/s4ysolutions/effective-mobile/tree/main/app)
module is very thin and contains only Manifest file to [define](https://github.com/s4ysolutions/effective-mobile/blob/d61adde3936a6dede590bf8865f3435aba230430/app/src/main/AndroidManifest.xml#L6C1-L6C39) required by Hilt [custom application class](https://github.com/s4ysolutions/effective-mobile/blob/main/app/src/main/java/solutions/s4y/effectivem/TheApplication.kt).

Also there's a supplementary module [:shared](https://github.com/s4ysolutions/effective-mobile/tree/main/shared) that contains the common resources and utility methods.

### Navigation

Navigation is implemented with 2 approaches:
 - Navigation component for the main navigation within the feature
 - Deep links for the navigation between the features

Navigation graph currently [implemented only for the flight tickets](https://github.com/s4ysolutions/effective-mobile/blob/main/feature/flight-tickets/src/main/res/navigation/flight_navigation.xml)
feature and it's nothing but a couple of fragment destination with the actions available for them and one dialog destination.

The inter-module navigation is a bit more complex and depends both on deep links and bottom navigation
bar as UI element.

Since all the features share the same bottom navigation bar the need to depend on :shared module
with the [menu definition](https://github.com/s4ysolutions/effective-mobile/blob/main/shared/src/main/res/menu/bottom_nav_menu.xml)
and BaseActivity class to have a [method](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/java/solutions/s4y/effectivem/views/BaseActivity.kt#L10C1-L10C94)
to setup the navigation bar state.

## Theming

The app uses [Material Design theme](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/res/values/themes.xml#L3C1-L3C70)
as a starting point and extends it in order to reflect the
design in [semantic and visual way](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/res/values/themes.xml#L45C1-L52C72).

The main idea is to make all styling by [assigning the attributes](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/res/values/themes.xml#L4C1-L76C79)
in the themes.xml file and [reference](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/res/layout/card_search.xml#L61C21-L61C62)
them in the layout files with `?attr/` syntax. The values to be assigned to the attributes are
defined in the [colors.xml](https://github.com/s4ysolutions/effective-mobile/blob/main/shared/src/main/res/values/colors.xml),
[dimens.xml](https://github.com/s4ysolutions/effective-mobile/blob/main/shared/src/main/res/values/dimens.xml),
[styles.xml](https://github.com/s4ysolutions/effective-mobile/blob/main/shared/src/main/res/values/styles.xml) etc.


## Technical details

 - Hilt requires to [subclass the Application](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/app/src/main/java/solutions/s4y/effectivem/TheApplication.kt#L5C1-L7C2) class and annotate it with @HiltAndroidApp
 - Retrofit client [add interceptor](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RestClient.kt#L32C1-L33C43)
   to [read the json files from the assets](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RestClient.kt#L42C1-L55C45)
    folder and uses [Gson](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RestClient.kt#L70C25-L70C76)
    and [RxJava](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RestClient.kt#L70C25-L70C76)
   libraries to parse the json files and expose the endpoints as Singles. The also add
   some delays to [simulate the network latency](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/data/tickets/src/main/java/solutions/s4y/effectm/provider/RetrofitProvider.kt#L21C11-L21C67).
 - Besides ModelId the domain layer introduces the [ImageValue](https://github.com/s4ysolutions/effective-mobile/blob/main/domain/src/main/java/solutions/s4y/effectm/domain/models/ImageValue.kt) model (ADT) to abstract the source of the
    image and the image itself (current implementation assumes the image is defined by the ID, but 
    real word service assume URLs or such). Also there's a NoImage object to represent the absence
   of image in unambiguous way. From the side of UI the [loadImageValueToImageView](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/OffersRecyclerViewAdapter.kt#L78C1-L96C10) method converts
   this [platform-agnostic value to the platform-dependent](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/OffersRecyclerViewAdapter.kt#L78C1-L96C10) image.
 - [Money.formatted](https://github.com/s4ysolutions/effective-mobile/blob/main/domain/src/main/java/solutions/s4y/effectm/domain/models/Money.kt) property is implemented as an extension property to the Money class in order keep
   the model class clean from the functionality.
 - TicketsService.getSingleton method uses [recursion pattern](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/domain/src/main/java/solutions/s4y/effectm/domain/TicketsService.kt#L18C12-L18C41) for esthetic reasons: this way it is
   possible to avoid both !! operator and introducing the intermediate variable.
 - In order to make the navigation between the features possible each of the (main) activity in the
   feature modules are inherited from the [BaseActivity](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/FlightTicketsActivity.kt#L10C1-L10C47) class and
   [calls](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/FlightTicketsActivity.kt#L22C9-L22C106)
   the [setupBottomNavigationBar](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/java/solutions/s4y/effectivem/views/BaseActivity.kt#L10C14-L10C92)
   from the onCreate method. This way the navigation bar [highlight](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/java/solutions/s4y/effectivem/views/BaseActivity.kt#L14C13-L14C68)
   the current feature and [assigns
   the deep link](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/shared/src/main/java/solutions/s4y/effectivem/views/BaseActivity.kt#L17C1-L21C38)s to the navigation items.
 - Tickets feature shares the search filter among few fragments and [uses DataStore](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/state/FilterService.kt#L17C1-L17C94)
   component under the hood
   while [getting access](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/state/FilterService.kt#L28C1-L32C45)
   to it through FilterService layer with Flow API interface.
 - ViewModels designed for each fragment [inject the TicketsService and FilterService](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeViewModel.kt#L15C1-L19C4)
   with Hilt and
   [wrap](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeViewModel.kt#L22C1-L25C6)
 - services' Flow API with LiveData to be consumed by the UI. This approach assumes that UI
   developers don’t get their hands dirty with non-Android-specific libraries.
 - City and country destinations fields require to be updated by the dataStore changes and store
   the values back there. The [implementation is straightforward](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeFragment.kt#L121C1-L144C10) with liveData observers and [text
    watchers](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeFragment.kt#L209C1-L263C6)
    but currently the code is duplicated in the 3 fragments. In a real world application 
    it is a subject to be refactored.
 - Observing the dataStore makes the the adding listener to the text field changes put to [onResume](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeFragment.kt#L198C1-L207C6)
   method instead of onCreateView. This is necessary to skip onViewStateRestored lifecycle event.
 - The design mockup and the description contradicts each other in terms of scrolling/swiping. The
   current implementation assumes the scrolling behavior with RecyclerView, but Pager/Pager2 approaches
   are prepared and turned off with kind of [feature flag](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeFragment.kt#L53C1-L53C40).
 - Search screen has a not so-simple ui element with the fancy-formatted date. It's implemented with
   [Spannabe](https://github.com/s4ysolutions/effective-mobile/blob/5ed6d48459ce884692fb5c4251f8c2af8c4ba904/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/search/SearchFragment.kt#L198C1-L205C14) text object and Calendar API.
 - View models are [instantiated](https://github.com/s4ysolutions/effective-mobile/blob/d61adde3936a6dede590bf8865f3435aba230430/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeFragment.kt#L30C1-L30C57) with Hilt
   and receive the [dependencies](https://github.com/s4ysolutions/effective-mobile/blob/d61adde3936a6dede590bf8865f3435aba230430/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeViewModel.kt#L15C1-L18C45)
   as injected constructor parameters, while TicketService is received by [Singleton](https://github.com/s4ysolutions/effective-mobile/blob/d61adde3936a6dede590bf8865f3435aba230430/feature/flight-tickets/src/main/java/solutions/s4y/effectivem/flight_tickets/screens/home/HomeViewModel.kt#L33C3-L34C1).
 - Activities currently have redundant DeepLink definitions in the manifest files: they are left
   there for the future use.