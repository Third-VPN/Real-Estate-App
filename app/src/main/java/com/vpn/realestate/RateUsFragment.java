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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpn.realestate.ApiManager.JSONField;
import com.vpn.realestate.ApiManager.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RateUsFragment extends Fragment {
    RatingBar rbFeedback;
    EditText etFeedback;
    Button btnSubmit;

    float rating;
    String feedback;

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

                rating = rbFeedback.getRating();
                feedback = etFeedback.getText().toString();

                if (rating != 0 && !feedback.equals("")) {

                    sendFeedback();

                } else {
                    Toast.makeText(getContext(), "Please rate first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void sendFeedback() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.FEEDBACK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONFeedback(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put(JSONField.RATING, String.valueOf(rating));
                params.put(JSONField.FEEDBACK, feedback);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONFeedback(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);

                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), DrawerActivity.class);
                startActivity(intent);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
