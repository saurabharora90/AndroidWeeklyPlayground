package com.example.androidweeklyplayground

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.widget.FrameLayout
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidweeklyplayground.notification.createNotification
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel4)
class ScreenshotNotificationTest {
    val context: Context = ApplicationProvider.getApplicationContext()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            RoborazziRule.CaptureType.AllImage
        )
    )

    @Test
    fun `Generate and Test Notification Screenshot`() {
        context.createNotification(
            "Custom Notification",
            "Checkout this custom notification layout that we'll screenshot test",
            R.drawable.scenery
        )

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val statusBarNotifications = notificationManager.activeNotifications
        Assert.assertEquals(statusBarNotifications.size, 1)
        val postedNotification = statusBarNotifications[0]

        val builder = Notification.Builder.recoverBuilder(context, postedNotification.notification)
        val remoteViewToTest = builder.callPrivateFunc("createBigContentView") as RemoteViews

        // This cannot be used as it is the custom bigContentView
        // that we passed instead of the finally inflated layout,
        // i.e. without OS decorations such as actions, icons, etc.)

        // val remoteViewToTest = postedNotification.notification.bigContentView

        // We are using Compose test rule since this notification "view" is not rendered in an activity.
        // We wrap our remote view in a AndroidView composable and set it as the content of the compose test rule.
        composeTestRule.setContent {
            AndroidView(factory = {
                remoteViewToTest.apply(
                    context, FrameLayout(context)
                )
            })
        }
    }
}

inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? =
    T::class
        .declaredMemberFunctions
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.call(this, *args)

inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? =
    T::class
        .memberProperties
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.get(this) as? R