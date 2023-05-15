import com.example.bodima.testvalidations.GroceryInputValidation
import org.junit.Assert.assertEquals
import org.junit.Test

class GroceryInputValidationTest {
    @Test
    fun `valid input type testing`() {
        val title = "Nectar"
        val subtitle = "Family Pack"
        val discount = "5"
        val price = "340"
        val tpNO = "0776454123"
        val locate = "Kandy"
        val about = "Full packed pepci bundle"
        val category = "Beverages"
        val groceryimg = "iVBORw0KGgoAAAANSUhEUgAAA8AAAAUACAIAAAAHjKxCAAAAA3NCSVQICAjb4U"

        val res = GroceryInputValidation.validateGroceryInput(title, subtitle, discount, price, tpNO, locate, about, category, groceryimg)

        assertEquals(true, res)
    }

    @Test
    fun `invalid input type testing`() {
        val title = "Nectar"
        val subtitle = "Family Pack"
        val discount = "5"
        val price = "340"
        val tpNO = "0776454123000"
        val locate = "Kandy"
        val about = "Full packed pepci bundle"
        val category = "Beverages"
        val groceryimg = "iVBORw0KGgoAAAANSUhEUgAAA8AAAAUACAIAAAAHjKxCAAAAA3NCSVQICAjb4U"

        val res = GroceryInputValidation.validateGroceryInput(title, subtitle, discount, price, tpNO, locate, about, category, groceryimg)

        assertEquals(false, res)
    }

    @Test
    fun `valid update type testing`() {
        val title = "Nectar"
        val subtitle = "Family Pack"
        val discount = "5"
        val price = "340"
        val tpNO = "0776454123"
        val locate = "Kandy"
        val about = "Full packed pepci bundle"
        val category = "Beverages"
        val groceryimg = "iVBORw0KGgoAAAANSUhEUgAAA8AAAAUACAIAAAAHjKxCAAAAA3NCSVQICAjb4U"

        val res = GroceryInputValidation.validateGroceryUpdate(title, subtitle, discount, price, tpNO, locate, about, category, groceryimg)

        assertEquals(true, res)
    }


    @Test
    fun `invalid update type testing`() {
        val title = "Nectar"
        val subtitle = "Family Pack"
        val discount = "5"
        val price = "340"
        val tpNO = "07764541230000"
        val locate = "Kandy"
        val about = "Full packed pepci bundle"
        val category = "Beverages"
        val groceryimg = "iVBORw0KGgoAAAANSUhEUgAAA8AAAAUACAIAAAAHjKxCAAAAA3NCSVQICAjb4U"

        val res = GroceryInputValidation.validateGroceryUpdate(title, subtitle, discount, price, tpNO, locate, about, category, groceryimg)

        assertEquals(false, res)
    }


    @Test
    fun validateDiscount() {

        val amount = 2
        val price = 700
        val discount = 3
        val res = GroceryInputValidation.validateDiscount(amount, price,discount)
        assertEquals(1358,res)
    }



}
