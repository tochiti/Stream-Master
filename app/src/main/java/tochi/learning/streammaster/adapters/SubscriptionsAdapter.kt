import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import tochi.learning.streammaster.fragments.ActiveSubscriptions
import tochi.learning.streammaster.fragments.Categories

class SubscriptionsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ActiveSubscriptions() // Replace with your ActiveSubscriptionsFragment
            1 -> Categories() // Replace with your CategoriesFragment
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 2 // Number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Active Subscriptions"
            1 -> "Categories"
            else -> null
        }
    }
}
