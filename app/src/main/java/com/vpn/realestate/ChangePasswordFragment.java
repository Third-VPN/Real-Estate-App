package com.vpn.realestate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static android.content.Context.MODE_PRIVATE;

public class ChangePasswordFragment extends Fragment {
    EditText etOldPassword, etNewPassword, etNewConPassword;
    Button btnDone;

    public static final String PROFILE = "profile";
    public static final String ID_KEY = "user_id";

    String user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        etOldPassword = view.findViewById(R.id.etOldPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etNewConPassword = view.findViewById(R.id.etNewConPassword);

        btnDone = view.findViewById(R.id.btnDone);

        SharedPreferences preferences = getActivity().getSharedPreferences(PROFILE, MODE_PRIVATE);
        user_id = preferences.getString(ID_KEY, "");

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etOldPassword.getText().toString().trim().equals("") &&
                !etNewPassword.getText().toString().trim().equals("") &&
                etNewConPassword.getText().toString().trim().equals(etNewPassword.getText().toString().trim())) {

                    changePasswordRequest();

                }
            }
        });

        return view;
    }

    private void changePasswordRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PASSWORD_CHANGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSONChangePassword(response);
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
                params.put(JSONField.USER_ID, user_id);
                params.put(JSONField.USER_PASSWORD, etOldPassword.getText().toString().trim());
                params.put(JSONField.NEW_PASSWORD, etNewPassword.getText().toString().trim());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONChangePassword(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);

            if (success == 1) {

                String msg = jsonObject.optString(JSONField.MSG);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

//                getChildFragmentManager().beginTransaction().replace(R.id.fragContainer,
//                        new SearchPropertyFragment()).commit();

                Intent intent = new Intent(getContext(), DrawerActivity.class);
                startActivity(intent);

            } else {
                String msg = jsonObject.optString(JSONField.MSG);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
