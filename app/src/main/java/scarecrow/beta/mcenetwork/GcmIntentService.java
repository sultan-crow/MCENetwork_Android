package scarecrow.beta.mcenetwork;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import library.UniqueFunctions;

public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotificationForPost("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotificationForPost("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                for (int i = 0; i < 3; i++) {
                    Log.i(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }

                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                String tag = extras.getString("tag");
                String content = extras.getString("content");

                Log.d("hello_tag", content);

                if(tag.equals("post"))
                    sendNotificationForPost(content);
                else if(tag.equals("message")) {
                    String sender = extras.getString("sender");
                    String date = extras.getString("date");
                    String time = extras.getString("time");
                    String name = extras.getString("name");

                    String formatted_time = new UniqueFunctions().getFormattedDateTime(date, time);
                    sendNotificationForMessage(sender, content, name);
                    updateActivity(getApplicationContext(), content, sender, formatted_time);
                }

                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    static void updateActivity(Context context, String message,
                               String sender, String time) {

        Intent intent = new Intent("unique_name");

        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        intent.putExtra("sender", sender);
        intent.putExtra("time", time);

        //send broadcast
        context.sendBroadcast(intent);
    }

    private void sendNotificationForMessage(String sender, String msg, String name) {
        Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //To be changed, pending

        Intent details = new Intent(getApplicationContext(), ChatActivity.class);
        details.putExtra("sender", sender);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                details, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = { 0, 100, 200, 300 };

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setVibrate(vibrate)
                .setAutoCancel(true)
                .setSound(alarmSound);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }


    private void sendNotificationForPost(String msg) {
        Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //To be changed, pending

        Intent details = new Intent(getApplicationContext(), DashboardActivity.class);
        details.putExtra("subject", msg);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                details, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = { 0, 100, 200, 300 };

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("MCE Network")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setVibrate(vibrate)
                .setAutoCancel(true)
                .setSound(alarmSound);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }
}
