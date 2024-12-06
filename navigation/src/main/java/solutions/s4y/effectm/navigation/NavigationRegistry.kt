package solutions.s4y.effectm.navigation
/*
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater

object NavigationRegistry {
    interface NavDestinationFactory {
        fun createDestination(navController: NavController): NavDestination
    }

    private val dynamicDestinations = mutableListOf<NavDestinationFactory>()

    fun registerDestination(factory: NavDestinationFactory) {
        dynamicDestinations.add(factory)
    }

    fun createNavGraph(navController: NavController, inflater: NavInflater): NavGraph {
        val navGraph = inflater.inflate(R.navigation.base_nav_graph)
        dynamicDestinations.forEach { factory ->
            val destination = factory.createDestination(navController)
            navGraph.addDestination(destination)
        }
        return navGraph
    }
}
*/