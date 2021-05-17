package com.vpn.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RateUsFragment extends Fragment {
    RatingBar rbFeedback;
    EditText etFeedback;
    Button btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_us, container, false);

        rbFeedback = view.findViewById(R.id.rbFeedback);

        etFeedback = view.findViewById(R.id.etFeedback);

        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float rating = rbFeedback.getRating();

                Toast.makeText(getContext(), "Thank You For " + rating + " Rating.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), DrawerActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}
