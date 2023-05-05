package com.example.bodima.activities

import org.junit.Assert.*
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.example.bodima.R
import com.example.bodima.databinding.ActivityRegistrationBinding
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(JUnit4::class)
class RegistrationTest {
    @Rule @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var registrationActivity: Registration
    private lateinit var binding: ActivityRegistrationBinding

    @Before
    fun setUp() {
        registrationActivity = Robolectric.buildActivity(Registration::class.java).create().get()
        binding = ActivityRegistrationBinding.bind(registrationActivity.findViewById(R.id.regi))
    }

    @Test
    fun `successful registration`() {
        // Arrange
        binding.emailEt.setText("example@gmail.com")
        binding.passET.setText("123456")
        binding.confirmPassEt.setText("123456")

        // Act
        binding.button.performClick()

        // Assert
        val expectedIntent = Intent(registrationActivity, Login::class.java)
        val actualIntent = Shadows.shadowOf(registrationActivity).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }

    @Test
    fun `password does not match`() {
        // Arrange
        binding.emailEt.setText("example@gmail.com")
        binding.passET.setText("123456")
        binding.confirmPassEt.setText("654321")

        // Act
        binding.button.performClick()

        // Assert
        val toast = ShadowToast.getLatestToast()
        assertEquals("Password is not matching", ShadowToast.getTextOfLatestToast())

    }

    @Test
    fun `empty email field`() {
        // Arrange
        binding.emailEt.setText("")
        binding.passET.setText("123456")
        binding.confirmPassEt.setText("123456")

        // Act
        binding.button.performClick()

        // Assert
        val toast = ShadowToast.getLatestToast()
        assertEquals("Empty Fields Are not Allowed !!", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun `empty password field`() {
        // Arrange
        binding.emailEt.setText("example@gmail.com")
        binding.passET.setText("")
        binding.confirmPassEt.setText("123456")

        // Act
        binding.button.performClick()

        // Assert
        val toast = ShadowToast.getLatestToast()
        assertEquals("Empty Fields Are not Allowed !!", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun `empty confirm password field`() {
        // Arrange
        binding.emailEt.setText("example@gmail.com")
        binding.passET.setText("123456")
        binding.confirmPassEt.setText("")

        // Act
        binding.button.performClick()

        // Assert
        val toast = ShadowToast.getLatestToast()
        assertEquals("Empty Fields Are not Allowed !!", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun testRegistrationWithFirebaseError() {
        val email = "example@gmail.com"
        val pass = "123456"
        val confirmPass = "123456"
        val firebaseAuth = mock(FirebaseAuth::class.java)
        `when`(firebaseAuth.createUserWithEmailAndPassword(email, pass)).thenThrow(IllegalStateException("Firebase registration error"))

        registrationActivity.setFirebaseAuth(firebaseAuth)

        val button = registrationActivity.findViewById<Button>(R.id.button)
        button.performClick()

        val toast = ShadowToast.getLatestToast()
        assertEquals("Firebase registration error", ShadowToast.getTextOfLatestToast())
    }

}
