package mobiledev.unb.ca.sharedpreferencesdemo.persistence

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {
    @Mock
    var mockSharedPreferences: SharedPreferences? = null

    @Mock
    var mockEditor: SharedPreferences.Editor? = null

    /**
     * Method that is run prior to every test
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this) // Initialize mocks
    }

    @Test
    fun testSharedPreferencesHelper_WhenSaveIntValueIsSuccessful() {
        // Setup the SUT
        val expected = Random.nextInt(1000)
        createMockSharedPreferenceManager(expectedValue = expected)

        // Save a result
        SharedPreferencesManager.saveIntValue(TEST_KEY, expected)
        Mockito.verify(mockEditor)?.putInt(TEST_KEY, expected)
        Mockito.verify(mockEditor)?.apply()

        // Retrieve the saved result
        val actual = SharedPreferencesManager.getIntValue(TEST_KEY, 0)
        MatcherAssert.assertThat("Verify int value has been persisted and read correctly",
            actual,
            CoreMatchers.`is`(CoreMatchers.equalTo(expected)))
    }

    @Test
    fun testSharedPreferencesHelper_WhenKeyNotFound() {
        // Setup the SUT
        val expected = Random.nextInt(1000)
        createDefaultValueSharedPreferencesManager(defaultValue = expected)

        // Retrieve the saved result
        val actual = SharedPreferencesManager.getIntValue(TEST_KEY, expected)
        MatcherAssert.assertThat("Verify default value is returned",
            actual,
            CoreMatchers.`is`(CoreMatchers.equalTo(expected)))
    }

    @Test
    fun testSharedPreferencesHelper_WhenSaveIntValueIsNotSuccessfulAndThrowsException() {
        // Setup the SUT
        val expected = Random.nextInt(1000)
        createBrokenMockSharedPreferenceManager()

        Assert.assertThrows(IllegalArgumentException::class.java, { SharedPreferencesManager.saveIntValue(TEST_KEY, expected) })
    }

    /**
     * Create a mocked SharedPreferences
     */
    private fun createMockSharedPreferenceManager(expectedValue: Int) {
        // Mocked SharedPreferences object edit
        Mockito.`when`(mockSharedPreferences?.edit())
            .thenReturn(mockEditor)

        // Mocked SharedPreferences object putInt
        Mockito.`when`<SharedPreferences.Editor?>(
            mockEditor?.putInt(
                Mockito.anyString(),
                Mockito.anyInt()
            )
        ).thenReturn(mockEditor)

        // Mocked SharedPreferences object getInt
        Mockito. `when`(mockSharedPreferences?.getInt(
            Mockito.anyString(),
            Mockito.anyInt())
        ).thenReturn(expectedValue)

        SharedPreferencesManager.init(mockSharedPreferences!!)
    }

    /**
     * Create a mocked SharedPreferences object which returns the default value
     */
    private fun createDefaultValueSharedPreferencesManager(defaultValue: Int) {
        // Configure the mock to return the default value as the key does not exist
        Mockito. `when`(mockSharedPreferences?.getInt(
            Mockito.anyString(),
            Mockito.anyInt())
        ).thenReturn(defaultValue)

        SharedPreferencesManager.init(mockSharedPreferences!!)
    }

    /**
     * Create a mocked SharedPreferences object that fails when writing
     */
    private fun createBrokenMockSharedPreferenceManager() {
        // Mocked SharedPreferences object edit
        Mockito.`when`(mockSharedPreferences?.edit())
            .thenReturn(mockEditor)

        // Mocked SharedPreferences object putInt
        Mockito.`when`<SharedPreferences.Editor?>(
            mockEditor?.putInt(
                Mockito.anyString(),
                Mockito.anyInt()
            )
        ).thenThrow(IllegalArgumentException())

        SharedPreferencesManager.init(mockSharedPreferences!!)
    }

    companion object {
        private const val TEST_KEY = "TEST_KEY"
    }
}