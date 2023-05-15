import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isSystemAlertWindow
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.bodima.R
import com.example.bodima.fragments.GroceryAddFragment
import com.example.bodima.fragments.HouseAddFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class GroceryAddFragmentTest {

    private lateinit var scenario: FragmentScenario<GroceryAddFragment>

    @Before
    fun setup() {
        val fragmentArgs = Bundle().apply {
            // Add any arguments that the fragment needs
        }
        val scenario = launchFragmentInContainer<GroceryAddFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_Bodima
        )
        scenario.moveToState(Lifecycle.State.STARTED)
    }


    @Test
    fun testAllFieldToast() {

        Espresso.closeSoftKeyboard()

        Thread.sleep(1000); // Wait for 1 second before checking toast message
        onView(withText("Please fill in all fields"))
            .inRoot(isSystemAlertWindow())
            .check(matches(isDisplayed()));

    }


    @Test
    fun testmobileInvalidToast() {

        Espresso.closeSoftKeyboard()

        Thread.sleep(1000); // Wait for 1 second before checking toast message
        onView(withText("Please enter a valid phone number with 10 digits"))
            .inRoot(isSystemAlertWindow())
            .check(matches(isDisplayed()));

    }


}