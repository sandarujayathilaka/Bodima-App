import com.example.bodima.fragments.CalculatorFragment
import org.junit.Test
import org.junit.Assert

class CalculatorFragmentTest {
    private val calculatorFragment = CalculatorFragment()

    @Test
    fun test() {
        calculatorFragment.tdays.setText("5")
        calculatorFragment.traFee.setText("10.0")
        val result = calculatorFragment.totalTransportCost()

        Assert.assertEquals(50,result )
    }




}
