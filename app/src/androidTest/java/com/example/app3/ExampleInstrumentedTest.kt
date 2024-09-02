package com.example.app3

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app3.auth.LeftMenu
import com.example.app3.auth.LoginScreen
import com.example.app3.auth.MainScreen
import com.example.app3.auth.MyListViewModel
import com.example.app3.auth.SignUpScreen
import com.google.common.base.Verify.verify
import io.mockk.MockK

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val photoUrl = "https://example.com/photo.jpg"
    private val details = Details("1", "Mock", "Description", "2012", "End", "Rai", "Image", "9", listOf(), null, listOf())

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testHomePagePhone() {
        rule.setContent {
            MainScreen(navController, isTablet = false)
        }

        // Everything is displayed
        rule.onNodeWithText("SERIES\nTIME").assertIsDisplayed()
        rule.onNodeWithText("Never miss your stories").assertIsDisplayed()
        rule.onNodeWithContentDescription("Arrow").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testHomePageTablet() {
        rule.setContent {
            MainScreen(navController, isTablet = true)
        }

        // Everything is displayed
        rule.onNodeWithText("SERIES\nTIME").assertIsDisplayed()
        rule.onNodeWithText("Never miss your stories").assertIsDisplayed()
        rule.onNodeWithContentDescription("Arrow").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testLoginPage() {
        rule.setContent {
            SignUpScreen(navController, vm =  viewModel<FbViewModel>(), currUser = LocalContext.current.getSharedPreferences("id", 1))
        }

        // Everything is displayed
        rule.onNodeWithText("Name").assertIsDisplayed()
        rule.onNodeWithText("Email").assertIsDisplayed()
        rule.onNodeWithText("Password").assertIsDisplayed()
        rule.onNodeWithText("Login").assertIsDisplayed()
        rule.onNodeWithText("E-mail").assertIsDisplayed()
        rule.onAllNodesWithText("Sign Up").assertCountEquals(2)
        rule.onNodeWithContentDescription("Image").assertIsDisplayed().assertHasClickAction().performClick()
        rule.onNodeWithText("Camera").assertIsDisplayed()
        rule.onNodeWithText("Gallery").assertIsDisplayed()
    }
    /*


    @Test
    fun testNavigationToHomePage() {
        rule.setContent {
            LeftMenu(
                currentPage = DestinationScreen.Home,
                navController = navController,
                photoUrl,
                listVM = listVM
            )
        }

        rule.onNodeWithText("Homepage").performClick()
        rule.runOnIdle {
            assert(navController.currentDestination?.route == DestinationScreen.Home.route)
        }
    }*/
}