package product.warehouse.com.warehouse

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import product.warehouse.com.warehouse.model.UserModel
import product.warehouse.com.warehouse.remote.ApiClient
import product.warehouse.com.warehouse.remote.ApiInterface
import product.warehouse.com.warehouse.view.MainActivity
import retrofit2.Response
import java.io.IOException







/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    internal lateinit var response: Response<UserModel>
    private lateinit var mainActivity :MainActivity


    @get:Rule
    public val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
       mainActivity = mActivityRule.activity
    }


    @Test
    fun getUserId() {
        // Context of the app under test.
        val apiEndpoints = ApiClient.getRetrofitInstance(mainActivity).create(ApiInterface::class.java)

        val call = apiEndpoints.getUserID()

        try {
            //Magic is here at .execute() instead of .enqueue()
            val response = call.subscribe()
            val authResponse =  response.dispose()

            assertTrue(response.isDisposed)

        } catch (e: IOException) {
            e.printStackTrace()
        }


    }
}
