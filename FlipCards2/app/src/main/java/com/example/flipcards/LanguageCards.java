package com.example.flipcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class LanguageCards extends AppCompatActivity implements OnCardDeckClickListener {
    private RecyclerView reciclerView;
    private AdaptorLanguageCards adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CardDeckItem> cardDecks;

    public static String CARD_ENGLISH = "";
    public static String CARD_FOREIGN = "";

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



}