package com.example.flipcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    public static String CARD_ENGLISH = "";
    public static String CARD_FOREIGN = "";

    @Override
    public void onCardDeckClick(CardDeckItem cardDeckItem) {
        DatabaseReference dao = FirebaseDatabase.getInstance().getReference();;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_cards);

        ArrayList<CardDeckItem> cardDecks = new ArrayList<>();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cards_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}