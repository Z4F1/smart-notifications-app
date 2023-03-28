package no.floatinggoat.smartnotifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import no.floatinggoat.smartnotifications.ui.SimpleButton
import no.floatinggoat.smartnotifications.ui.theme.SmartHomeControllerTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val CHANNEL_ID = "TEST"
    }

    private var lastNotifications: String = ""
    private val notificationText = mutableStateOf("You will see active notifications here:")

    private val receiver : NotificationReceiver = NotificationReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainContent() }

        if(!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)){
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        createNotificationChannel()

        val intentFilter = IntentFilter()
        intentFilter.addAction("no.floatinggoat.notificationtesting")
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun createNotificationChannel(){
        val name = getString(R.string.app_name)
        val descriptionText = "App notification tester and retriever"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val nm : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    private fun createNotification(){
        lastNotifications = ""

        Log.i("Main", "Creating test notification")

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("A TEST")
            .setContentText("This is just a test notification")

        with(NotificationManagerCompat.from(this)){
            notify(10002, notification.build())
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainContent(){
        SmartHomeControllerTheme(content = {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(modifier = Modifier.padding(32.dp, 64.dp, 32.dp, 16.dp)){
                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Smart Home Notifier",
                            style = MaterialTheme.typography.h5,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "by Pelle Pastoor",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.size(32.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "This is just a app for updating notifications. Test the app by pressing the button bellow and it will tell you here:"
                        )
                        Spacer(modifier = Modifier.size(32.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = notificationText.value
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        SimpleButton(
                            text = "Create test notification",
                            action = { createNotification() },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        })
    }

    fun updateText(text: String){
        lastNotifications += "\n - $text"
        notificationText.value = "You will see active notifications here: $lastNotifications"
    }

    class NotificationReceiver(
        private val mainActivity: MainActivity
    ): BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null && intent.extras != null){
                val text = intent.extras!!.getString("Notification Text").toString()
                val name = intent.extras!!.getString("Notification Name").toString()
                Log.i("Received", name)

                mainActivity.updateText("$name - $text")
            }
        }

    }
}

