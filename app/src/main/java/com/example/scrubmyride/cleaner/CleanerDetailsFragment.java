package com.example.scrubmyride.cleaner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;
import com.example.scrubmyride.entities.Cleaner;
import com.example.scrubmyride.entities.Customer;
import com.google.gson.Gson;

public class CleanerDetailsFragment extends Fragment {

    int userID;
    Bundle bundleReceived, bundleSend;
    String email, phoneNumber, address, postcode;
    EditText emailET, phoneNumberET, addressET, postcodeET, passwordET, passwordConfirmET;
    Cleaner cleaner;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_details, container, false);

        emailET = view.findViewById(R.id.et_email);
        phoneNumberET = view.findViewById(R.id.et_phoneNumber);
        addressET = view.findViewById(R.id.et_address);
        postcodeET = view.findViewById(R.id.et_postcode);
        passwordET = view.findViewById(R.id.et_password);
        passwordConfirmET = view.findViewById(R.id.et_passwordConfirm);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        bundleReceived = getArguments();
        userID = bundleReceived.getInt("userID");

        this.getUser(getContext());

        Button btn_save = view.findViewById((R.id.btn_save));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = "0";
                if (emailET.getText().toString().length() == 0) {
                    CharSequence text = "You must enter an email address";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                } else if (passwordET.getText().toString().length() > 0 && passwordConfirmET.getText().toString().length() > 0) {
                    if (!(passwordET.getText().toString().equals(passwordConfirmET.getText().toString()))) {
                        CharSequence text = "Your passwords do not match";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(getContext(), text, duration);
                        toast.show();
                    }
                } else {
                    if (passwordET.getText().toString().length() > 0) {
                        newPassword = passwordET.getText().toString();
                    }
                    String type = "updateCleaner";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                            new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    Log.d("test", output.toString());
                                    if (!"-1".equals((String) output)) {
                                        CharSequence text = "Changes have been saved";
                                        int duration = Toast.LENGTH_LONG;
                                        Toast toast = Toast.makeText(getContext(), text, duration);
                                        toast.show();
                                        bundleSend = new Bundle();
                                        bundleSend.putInt("userID", userID);
                                        navController.navigate(R.id.action_Cleaner_Details_to_Cleaner_Page, bundleSend);
                                    }
                                }
                            });
                    backgroundWorker.execute(type, userID, emailET.getText().toString(), phoneNumberET.getText().toString(), addressET.getText().toString(), postcodeET.getText().toString(), newPassword);
                }
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", userID);
                navController.navigate(R.id.action_Cleaner_Details_to_Cleaner_Page, bundleSend);
            }
        });
    }

    public void getUser(Context context) {
        String type = "getUser";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            String cleanerString = output.toString();
                            Gson gson=new Gson();
                            cleaner = gson.fromJson(cleanerString, Cleaner.class);
                            postcode = cleaner.getPostcode();
                            phoneNumber = cleaner.getPhoneNumber();
                            address = cleaner.getAddress();
                            email = cleaner.getEmail();
                            emailET.setText(email);
                            phoneNumberET.setText(phoneNumber);
                            addressET.setText(address);
                            postcodeET.setText(postcode);
                        }
                    }
                });
        backgroundWorker.execute(type, userID);
    }
}
