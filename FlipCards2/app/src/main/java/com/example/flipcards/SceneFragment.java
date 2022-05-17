package com.example.flipcards;

import static com.example.flipcards.LanguageCards.CARD_ENGLISH;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import org.w3c.dom.Text;

public class SceneFragment extends Fragment {
    ObjectAnimator objectAnimator;
    CardView card;

    public SceneFragment (){
        super(R.layout.fragment_scene);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            ImageView cardImageView = view.findViewById(R.id.cardImage);
            TextView cardTextView = view.findViewById(R.id.cardText);
            String title = bundle.getString(CARD_ENGLISH);
            cardTextView.setText(title);
            int id = getResources().getIdentifier(title, "drawable", getContext().getPackageName());
            cardImageView.setImageResource(id);
        }

        card = view.findViewById(R.id.cardFront);
        float currentRotation = card.getRotationY();
        objectAnimator = ObjectAnimator.ofFloat(card, "rotationY", (currentRotation-180)%360);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectAnimator.setDuration(2000);
                objectAnimator.start();
            }
        });
    }

}
