package com.example.flipcards;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageCards extends AppCompatActivity implements OnCardDeckClickListener {
    private RecyclerView reciclerView;
    private AdaptorLanguageCards adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CardDeckItem> cardDecks;
    private NotificationManager notificationManager;

    public static String CARD_ENGLISH = "";
    public static String CARD_FOREIGN = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_cards);

        cardDecks = new ArrayList<>();

        cardDecks.add(new CardDeckItem("Insects", "", R.drawable.butterfly));
        cardDecks.add(new CardDeckItem("Animals", "", R.drawable.dog));
        cardDecks.add(new CardDeckItem("Nature", "", R.drawable.mountain));
        cardDecks.add(new CardDeckItem("Instruments", "", R.drawable.guitar));
        cardDecks.add(new CardDeckItem("Vehicles", "", R.drawable.car));
        cardDecks.add(new CardDeckItem("Sweets", "", R.drawable.icecream));

        reciclerView = findViewById(R.id.cardDecks);
        reciclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);

        adapter = new AdaptorLanguageCards(cardDecks, this);

        reciclerView.setLayoutManager(layoutManager);
        reciclerView.setAdapter(adapter);

        // search function
        EditText searchText = findViewById(R.id.searchtext);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        // share function
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

        // local notification ?
        Intent intent = getIntent();
        String action = intent.getAction();
        String filterStr = intent.getStringExtra("Filter");
        // started with deeplink?
        if (filterStr != null) {
            searchText.setText(filterStr);
        }
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
        Intent resultIntent = new Intent(this, LanguageCards.class);
        resultIntent.putExtra("Filter", card);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        final Icon icon = Icon.createWithResource(LanguageCards.this,
                android.R.drawable.ic_dialog_info);

        Notification.Action action =
                new Notification.Action.Builder(icon, "Open", pendingIntent)
                        .build();

        String channelID = "fmi.gr3xy.cards";

        Notification notification =
                new Notification.Builder(LanguageCards.this,
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



    private void filter(String text) {
        ArrayList<CardDeckItem> filterList = new ArrayList<>();
        for (CardDeckItem item: cardDecks) {
            if (item.getCardDeckTitle().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        adapter.filterList(filterList);
    }

    @Override
    public void onCardDeckClick(CardDeckItem cardDeckItem) {
        DatabaseReference dao = FirebaseDatabase.getInstance().getReference();
        dao.child(cardDeckItem.getCardDeckTitle().toLowerCase(Locale.ROOT))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Bundle bundle = new Bundle();
                if (!task.isSuccessful()) {
                    bundle.putString(CARD_ENGLISH, "not found");
                }
                else {
                    String value = "";
                    for(DataSnapshot data :  task.getResult().getChildren()) {
                        value = data.child("english").getValue(String.class);
                    }
                    bundle.putString(CARD_ENGLISH, value);
                }
                SceneFragment sceneFragment = new SceneFragment();
                sceneFragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragmentContainerView, sceneFragment, null)
                        .addToBackStack(null)
                        .commit();


            }
        });

    }

// used for testing
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