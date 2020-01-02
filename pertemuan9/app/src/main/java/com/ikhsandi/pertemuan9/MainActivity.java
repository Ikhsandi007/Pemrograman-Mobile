package com.ikhsandi.pertemuan9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private NotificationManager nManager;

    private static final String NOTIF_GUIDE_URL = "https://www.blogsetyaaji.com/2017/09/membuat-aplikasi-android-notifikasi.html";
    private static final String ACT_UPDATE_NOTIF = "com.example.allam.notification.ACT_UPDATE_NOTIF";
    private static final String ACT_CANCEL_NOTIF = "com.example.allam.notification.ACT_CANCEL_NOTIF";

    private NotifReceiver nReceiver = new NotifReceiver();

    TextView notif1, hasil;
    Button cancel, update;
    ImageView send;
    EditText edt_pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasil = findViewById(R.id.txtHasil);
        notif1 = findViewById(R.id.txtNotif);
        cancel = findViewById(R.id.btnCancel);
        send = findViewById(R.id.btnSend);
        update = findViewById(R.id.btnUpdate);
        edt_pesan = findViewById(R.id.edtText);

        notif1.setEnabled(true);
        cancel.setEnabled(true);
        update.setEnabled(false);
        send.setEnabled(true);

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ACT_UPDATE_NOTIF);
        iFilter.addAction(ACT_CANCEL_NOTIF);
        registerReceiver(nReceiver, iFilter);

        notif1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotif();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotif();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotif();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean edt = edt_pesan
                        .getText()
                        .toString()
                        .startsWith(" ");
                if (edt_pesan
                        .getText()
                        .toString()
                        .matches("")) {
                    Toast.makeText(MainActivity.this,
                            "Harus Di isi",
                            Toast.LENGTH_SHORT).show();
                } else if (edt) {
                    Toast.makeText(MainActivity.this,
                            "Tdk Boleh Diawali spasi",
                            Toast.LENGTH_SHORT).show();
                } else {
                    sendNewNotif();
                    edt_pesan.setText("");
                }
            }
        });

        nManager = (NotificationManager)
                getApplicationContext()
                        .getSystemService(NOTIFICATION_SERVICE);
    }


    private void sendNewNotif(){
        String edt = edt_pesan.getText().toString();

        Intent notif = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notif,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent iCancel = new Intent(ACT_CANCEL_NOTIF);
        PendingIntent pCancel = PendingIntent.getBroadcast(this,
                0,
                iCancel,PendingIntent.FLAG_ONE_SHOT);
        Intent iUpdate = new Intent(ACT_UPDATE_NOTIF);
        PendingIntent pUpdate = PendingIntent.getBroadcast(this,
                0,iUpdate,PendingIntent.FLAG_ONE_SHOT);
        Intent iLearn = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIF_GUIDE_URL));
        PendingIntent pLearn = PendingIntent.getActivity(this,
                0,iLearn,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this,"CHANEl_ID_01");
        nBuilder.setContentTitle("Pesan ini dari EditText")
                .setContentText(edt)
                .setSmallIcon(R.drawable.ic_adb_green_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_adb_green_24dp,
                    "Update",pUpdate)
                .addAction(R.drawable.ic_adb_green_24dp,
                        "Learn More",pLearn)
                .setDeleteIntent(pCancel)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel = new NotificationChannel("CHANEL_ID_01",
                    "Sandi",NotificationManager.IMPORTANCE_DEFAULT);
            nManager.createNotificationChannel(chanel);
            nBuilder.setChannelId("CHANEL_ID_01");
        }

        nManager.notify(0,nBuilder.build());

        notif1.setEnabled(false);
        cancel.setEnabled(true);
        update.setEnabled(true);
        send.setEnabled(true);
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(nReceiver);
        super.onDestroy();
    }

    private void sendNotif() {
        Intent notifIntent = new Intent(this, MainActivity.class);
        PendingIntent nPending = PendingIntent.getActivity(this, 0,
                notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(ACT_CANCEL_NOTIF);
        PendingIntent cPending = PendingIntent.getBroadcast(this, 0,
                cancelIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent learnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIF_GUIDE_URL));
        PendingIntent lPending = PendingIntent.getActivity(this, 0,
                learnIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent uIntent = new Intent(ACT_UPDATE_NOTIF);
        PendingIntent uPending = PendingIntent.getBroadcast(this, 0,
                uIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(getApplicationContext(),
                        "CHANEL_ID_01");
        nBuilder.setContentTitle("Notification")
                .setContentText("Isi Notifikasi")
                .setSmallIcon(R.drawable.ic_adb_green_24dp)
                .setContentIntent(nPending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_adb_green_24dp,
                        "Update", uPending)
                .addAction(R.drawable.ic_adb_green_24dp,
                        "Learn More", lPending)
                .setDeleteIntent(cPending)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "nama";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel("CHANER_ID_01", "Sandi", importance);
            nManager.createNotificationChannel(channel);
            nBuilder.setChannelId("CHANEL_ID_01");
        }

        nManager.notify(0, nBuilder.build());

        notif1.setEnabled(false);
        cancel.setEnabled(true);
        update.setEnabled(true);
        send.setEnabled(true);
    }

    private void updateNotif(){
        Bitmap bImage = BitmapFactory.decodeResource(
                getResources(),R.drawable.ic_adb_green_24dp);

        Intent iNotif = new Intent(this,MainActivity.class);
        PendingIntent nPending = PendingIntent.getActivity(this,
                0,iNotif,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent iCancel = new Intent(ACT_CANCEL_NOTIF);
        PendingIntent cPending = PendingIntent.getBroadcast(
                this,0,iCancel,PendingIntent.FLAG_ONE_SHOT);

        Intent learnIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(NOTIF_GUIDE_URL));
        PendingIntent lPending = PendingIntent.getActivity(this,0,learnIntent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder nBuild = new NotificationCompat.Builder(getApplicationContext(),
                "CHANEL_ID_01");
        nBuild.setContentTitle("Notification")
                .setContentText("Isi Notifikasi")
                .setSmallIcon(R.drawable.ic_adb_green_24dp)
                .setContentIntent(nPending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_adb_green_24dp,
                        "Learn More", lPending)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bImage).setBigContentTitle("Telah Terupdate"))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("CHANER_ID_01", "Sandi",
            NotificationManager.IMPORTANCE_DEFAULT);
            nManager.createNotificationChannel(channel);
            nBuild.setChannelId("CHANEL_ID_01");
        }

        Notification mNotif = nBuild.build();
        nManager.notify(0, mNotif);

        notif1.setEnabled(false);
        cancel.setEnabled(true);
        update.setEnabled(false);
        send.setEnabled(true);

    }

    private void cancelNotif(){
        nManager.cancel(0);

        notif1.setEnabled(true);
        cancel.setEnabled(false);
        update.setEnabled(false);
        send.setEnabled(true);
    }
    private class NotifReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            switch (act){
                case ACT_CANCEL_NOTIF :
                    cancelNotif();
                    break;
                case ACT_UPDATE_NOTIF:
                    updateNotif();
                    break;
            }
        }
    }
}
