package no.floatinggoat.smartnotifications

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListener : NotificationListenerService() {
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbnmeh: StatusBarNotification?) {
        if(activeNotifications != null && activeNotifications.isNotEmpty()){
            for (sbn in activeNotifications){
                if(!sbn.isClearable) continue

                try {
                    val pm = applicationContext.packageManager
                    val ai = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val aiFlag = PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                        pm.getApplicationInfo(sbn.packageName, aiFlag)
                    } else {
                        val aiFlag = PackageManager.GET_META_DATA
                        pm.getApplicationInfo(sbn.packageName, aiFlag)
                    }

                    val intent = Intent("no.floatinggoat.notificationtesting")
                    intent.putExtra("Notification Text", sbn.notification.extras.getString("android.title") + ": " + sbn.notification.extras.getString("android.text"))
                    intent.putExtra("Notification Name", pm.getApplicationLabel(ai))
                    sendBroadcast(intent)
                }catch (_: NameNotFoundException){}
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}