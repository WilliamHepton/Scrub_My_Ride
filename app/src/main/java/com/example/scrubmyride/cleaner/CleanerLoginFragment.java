package com.example.scrubmyride.cleaner;

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

public class CleanerLoginFragment extends Fragment {

    EditText EmailET, PasswordET;
    int userID;
    Bundle bundleSend;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_login, container, false);
        EmailET = view.findViewById((R.id.input_email));
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
            String email = EmailET.getText().toString();
            String password = PasswordET.getText().toString();
            String type = "login";
            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                    new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            Log.d("output", output.toString());
                            if (!"-1".equals((String) output)) {
                                userID = Integer.parseInt(output.toString());
                                bundleSend = new Bundle();
                                bundleSend.putInt("userID", userID);
                                navController.navigate(R.id.action_Cleaner_Login_to_Cleaner_Page, bundleSend);
                            } else {
                                CharSequence text = "Wrong email or password";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(getContext(), text, duration);
                                toast.show();
                            }
                        }
            });
            backgroundWorker.execute(type, email, password);

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
