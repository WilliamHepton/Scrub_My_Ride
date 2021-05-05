package com.example.scrubmyride;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomePageFragment extends Fragment {

    Bundle bundleSend = new Bundle();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_home_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        Button btn_booking = view.findViewById((R.id.btn_booking));
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend.putInt("customerID", 0);
                navController.navigate(R.id.action_HomePage_to_Booking_WashChoice, bundleSend);
            }
        });

        Button btn_login = view.findViewById((R.id.btn_login));
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_HomePage_to_homeLoginFragment);
            }
        });

        Button btn_signUp = view.findViewById((R.id.btn_signupPage));
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_HomePage_to_homeSignupFragment);
            }
        });
    }
}