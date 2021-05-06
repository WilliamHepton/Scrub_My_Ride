package com.example.scrubmyride.cleaner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;

public class CleanerSignupFragment extends Fragment {

    EditText FirstNameET, LastNameET, PhoneNumberET, EmailET, PostCodeET, PasswordET, PasswordConfirmET;
    Bundle bundleSend;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_signup, container, false);
        FirstNameET = view.findViewById((R.id.et_firstName));
        LastNameET = view.findViewById((R.id.et_lastName));
        PhoneNumberET = view.findViewById((R.id.et_phoneNumber));
        EmailET = view.findViewById((R.id.et_email));
        PostCodeET = view.findViewById((R.id.et_postCode));
        PasswordET = view.findViewById((R.id.et_password));
        PasswordConfirmET = view.findViewById((R.id.et_passwordConfirm));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        Button btn_signUp = view.findViewById((R.id.btn_next));
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = FirstNameET.getText().toString();
                String lastName = LastNameET.getText().toString();
                String phoneNumber = PhoneNumberET.getText().toString();
                String email = EmailET.getText().toString();
                String postCode = PostCodeET.getText().toString();
                String password = PasswordET.getText().toString();
                String passwordConfirm = PasswordConfirmET.getText().toString();
                String type = "register";

                if (password.equals(passwordConfirm)) {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                            new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!"-1".equals((String) output)) {
                                        bundleSend = new Bundle();
                                        bundleSend.putString("email", email);
                                        navController.navigate(R.id.action_Cleaner_Signup_to_Cleaner_Page, bundleSend);
                                    }
                                }
                            });
                    backgroundWorker.execute(type, firstName, lastName, email, phoneNumber, postCode, password, "1");
                }
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_Cleaner_Signup_to_homeSignupFragment);
            }
        });
    }
}
