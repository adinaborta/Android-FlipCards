package com.example.flipcards;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import java.util.List;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import com.example.flipcards.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
//         share function
        Button button = (Button) findViewById(R.id.sharebutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText searchText = findViewById(R.id.sharetext);
                shareText(searchText.getText().toString());
                //testingShowAlertDialogButtonClicked(v, searchText.getText().toString());
            }
        });
        createNotificationChannel();
        // local notif function
        Button button2 = (Button) findViewById(R.id.notifbutton);
        button2.setOnClickListener(new View.OnClickListener() {
            private static final String CHANNEL_ID = "cardNotifyChannel";

            public void onClick(View v) {
                EditText notifText = findViewById(R.id.notiftext);
                sendNotification(v, notifText.getText().toString());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel("fmi.gr3xy.cards", "cards", importance);

        channel.setDescription("my description");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(
                new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void sendNotification(View view, String card) {

        int notificationID = 101;
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("Filter", card);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        final Icon icon = Icon.createWithResource(MainActivity.this,
                android.R.drawable.ic_dialog_info);

        Notification.Action action =
                new Notification.Action.Builder(icon, "Open", pendingIntent)
                        .build();

        String channelID = "fmi.gr3xy.cards";

        Notification notification =
                new Notification.Builder(MainActivity.this,
                        channelID)
                        .setContentTitle("New Message")
                        .setContentText("Open card: " + card)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setChannelId(channelID)
                        .setContentIntent(pendingIntent)
                        .setActions(action)
                        .build();

        notificationManager.notify(notificationID, notification);
    }

    private void shareText(String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    private void testingShowAlertDialogButtonClicked(View view, String message) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My box title");
        builder.setMessage(message);

        // add a button
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}