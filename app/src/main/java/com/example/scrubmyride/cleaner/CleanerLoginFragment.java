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

public class CleanerLoginFragment extends Fragment {

    EditText PhoneNumberET, PasswordET;
    String userID;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_login, container, false);
        PhoneNumberET = view.findViewById((R.id.input_phone));
        PasswordET = view.findViewById((R.id.input_password));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        Button btn_login = view.findViewById((R.id.btn_login));
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String phone = PhoneNumberET.getText().toString();
            String password = PasswordET.getText().toString();
            String type = "login";
            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                    new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!"-1".equals((String) output)) {
                                userID = output.toString();
                                Bundle bundle = new Bundle();
                                bundle.putString("userID", userID);
                                navController.navigate(R.id.action_Cleaner_Login_to_Cleaner_Page, bundle);
                            }
                        }
            });
            backgroundWorker.execute(type, phone, password);

            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_Cleaner_Login_to_homeLoginFragment);
            }
        });
    }
}
