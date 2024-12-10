## Architecture 

### Data layer

Data layer is Android library module that implements TicketsProvider interface consumed
by domain layer with the Retrofit client emulating
the http requests by reading the json files from the assets folder.

Exposes the implementation of the TicketsProvider interface as a Hilt singleton object.

Also it implements the EntityId interface of the domain layer (see next domain layer section).

### Domain Layer

Domain layer is pure kotlin library (no Android dependencies) module with the main purpose to
provide TicketsService for the rest of the application.

#### Dependencies

Domain layer defines TicketsProvider interface to be implemented by the data layer. This way
the domain layer is decoupled from the data layer, can consume the data from different sources
and can be tested independently.

For demo purposes this interface assumes RxJava API (i.e. data layer can be implemented with 
non-kotlin libraries).

#### Models
Domain layer abstracts the domain models from the data layer presentation. The data layer is
responsible to convert data from the external sources to the domain models.

Also domain layer introduces the ModelId interface to do not depend on the actual equality
implementation. (At least the ID can be either a String or an Int depending on the database).

While domain layer models does not follow active recodr pattern they have some additional properties
to be used in the presentation layer. For example Money class has formatted property to be used
in the UI.

The service mostly does nothing with the data from the data layer, but converts RxJava API to
Flow API to be used by Android consumers.

The service is provided with Singleton pattern for demo purposes.

### UI & presentation layers

UI constructed according to features modules pattern and uses LiveData as a reactive sources of data
(for demo purposes once again).

According to the mockup the app have few independent components: tickers, hotels, profile, etc and
following this design the app is divided into features modules: 
 - :feature:fligt-tickets
 - :feature:hotels
 - :feature:profile

Each of them has one activity and at least one fragment to implement the UI of the module.

As a consequence of the features modules pattern the :app module is very thin and contains only
Manifest file to define the activities with the corresponding deep links (The latter are needed
to switch between features without declaring them as dependency on each other).

Also there's a supplementary module :shared that contains the common resources and utility methods.

### Navigation

Navigation is implemented with 2 approaches:
 - Navigation component for the main navigation within the feature
 - Deep links for the navigation between the features

Navigation graph currently implemented only for the flight tickets feature and it's nothing
but a couple of fragment destination with the actions available for them and one dialog destination.

The inter-module navigation is a bit more complex and depends both on deep links and bottom navigation
bar as UI element.

Since all the features share the same bottom navigation bar the need to depend on :shared module
with the menu definition and BaseActivity class to have a method to setup the navigation bar state.

## Theming

The app uses Material Design theme as a starting point and extends it in order to reflect the
design in semantic and visual way.

The main idea is to make all styling by assigning the attributes in the themes.xml file and reference
them in the layout files with `?attr/` syntax. The values to be assigned to the attributes are
defined in the colors.xml, dimens.xml, styles.xml etc.


## Technical details

 - Hilt requires to subclass the Application class and annotate it with @HiltAndroidApp
 - Retrofit client add interceptor to read the json files from the assets folder and uses Gson and
    RxJava libraries to parse the json files and expose the endpoints as Singles. The also add
   some delays to simulate the network latency.
 - Besides ModelId the domain layer introduces the ImageValue model (ADT) to abstract the source of the
    image and the image itself (current implementation assumes the image is defined by the ID, but 
    real word service assume URLs or such). Also there's a NoImage object to represent the absence
   of image in unambiguous way. From the side of UI the loadImageValueToImageView method converts
   this platform-agnostic value to the platform-dependent image.
 - Money.formatted property is implemented as an extension property to the Money class in order keep
   the model class clean from the functionality.
 - TicketsService.getSingleton method uses recursion patter for esthetic reasons: this way it is
   possible to avoid both !! operator and introducing the intermediate variable.
 - In order to make the navigation between the features possible each of the (main) activity in the
   feature modules are inherited from the BaseActivity class and calls the setupBottomNavigationBar
   from the onCreate method. This way the navigation bar highlight the current feature and assigns
   the deep links to the navigation items.
 - Tickets feature shares the filter among few fragments and uses DataStore component under the hood
   while getting access to it through FilterService layer with Flow API interface.
 - ViewModels designed for each fragment inject the TicketsService and FilterService with Hilt and
   wrap services' Flow API with LiveData to be consumed by the UI. This approach assumes that UI
   developers don’t get their hands dirty with non-Android-specific libraries.
 - City and country destinations fields require to be updated by the dataStore changes and store
   the values back there. The implementation is straightforward with liveData observers and text
    watchers but currently the code is duplicated in the 3 fragments. In a real world application 
    it is a subject to be refactored.
 - Observing the dataStore makes the the adding listener to the text field changes put to onRestore
   method instead of onCreateView. This is necessary to skip onViewStateRestored lifecycle event.
 - The design mockup and the description contradicts each other in terms of scrolling/swiping. The
   current implementation assumes the scrolling behavior with RecyclerView, but Pager/Pager2 approaches
   are prepared and turned off with kind of feature flag.
 - Search screen has a not so-simple ui element with the fancy-formatted date. It's implemented with
   Spannabe text object and Calendar API.