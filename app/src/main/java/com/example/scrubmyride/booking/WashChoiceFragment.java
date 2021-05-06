package com.example.scrubmyride.booking;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;
import com.example.scrubmyride.entities.Customer;
import com.google.gson.Gson;

public class WashChoiceFragment extends Fragment {

    int washTypeID, customerID;
    String carReg = "";
    String email = "";
    String postcode = "";
    Bundle bundleReceived, bundleSend;
    EditText carRegET;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_booking_wash_choice, container, false);

        carRegET = view.findViewById(R.id.et_carReg);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundleReceived = getArguments();

        if (bundleReceived.getInt("customerID") != 0) {
            customerID = bundleReceived.getInt("customerID");
            this.getUserCarReg(getContext());
        }

        if (bundleReceived.getString("email") != "0") {
            email = bundleReceived.getString("email");
        }

        if (bundleReceived.getString("postcode") != "0") {
            postcode = bundleReceived.getString("postcode");
        }


        String regexCarReg = "(^[A-Z]{2}[0-9]{2}\\s?[A-Z]{3}$)|(^[A-Z][0-9]{1,3}[A-Z]{3}$)|(^[A-Z]{3}[0-9]{1,3}[A-Z]$)|(^[0-9]{1,4}[A-Z]{1,2}$)|(^[0-9]{1,3}[A-Z]{1,3}$)|(^[A-Z]{1,2}[0-9]{1,4}$)|(^[A-Z]{1,3}[0-9]{1,3}$)|(^[A-Z]{1,3}[0-9]{1,4}$)|(^[0-9]{3}[DX]{1}[0-9]{3}$)";

        final NavController navController = Navigation.findNavController(view);

        Button btn_outsideWash = view.findViewById((R.id.btn_outsideWash));
        btn_outsideWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carReg == "") {
                    carRegET.setError("Please enter a valid UK car registration number");
                } else {
                    bundleSend = new Bundle();
                    washTypeID = 1;
                    bundleSend.putInt("washTypeID", washTypeID);
                    bundleSend.putInt("customerID", customerID);
                    bundleSend.putString("carReg", carReg);
                    bundleSend.putString("postcode", postcode);
                    bundleSend.putString("email", email);
                    navController.navigate(R.id.action_Booking_WashChoice_to_Booking_Address, bundleSend);
                }

            }
        });

        Button btn_outInWash = view.findViewById((R.id.btn_outInWash));
        btn_outInWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carReg == "") {
                    carRegET.setError("Please enter a valid UK car registration number");
                } else {
                    bundleSend = new Bundle();
                    washTypeID = 2;
                    bundleSend.putInt("washTypeID", washTypeID);
                    bundleSend.putInt("customerID", customerID);
                    bundleSend.putString("carReg", carReg);
                    bundleSend.putString("postcode", postcode);
                    bundleSend.putString("email", email);
                    navController.navigate(R.id.action_Booking_WashChoice_to_Booking_Address, bundleSend);
                }
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", customerID);
                navController.navigate(R.id.action_Booking_WashChoice_to_userPageFragment, bundleSend);
            }
        });

        carRegET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String regCapitalised = editable.toString().toUpperCase();
                if (regCapitalised.matches(regexCarReg)) {
                    carReg = editable.toString();
                }
            }
        });
    }

    public void getUserCarReg(Context context) {
        String type = "getUserCarReg";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            Log.d("testwc", output.toString());
                            String carRegString = output.toString();
                            carRegET.setText(carRegString);
                            carReg = carRegString;
                        }
                    }
                });
        backgroundWorker.execute(type, customerID);
    }
}