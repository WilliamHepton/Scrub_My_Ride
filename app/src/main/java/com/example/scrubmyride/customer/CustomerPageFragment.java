package com.example.scrubmyride.customer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.Timer;
import java.util.TimerTask;

public class CustomerPageFragment extends Fragment {

    int userID;
    String phoneNumber, carReg;
    Bundle bundleReceived, bundleSend;
    Customer customer;
    TextView customerName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_customer_page, container, false);

        customerName = view.findViewById((R.id.txt_username));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        bundleReceived = getArguments();
        if (bundleReceived.getInt("userID") != 0) {
            userID = bundleReceived.getInt("userID");
            this.getUser(getContext());
        } else if (bundleReceived.getString("phoneNumber") != "0") {
            phoneNumber = bundleReceived.getString("phoneNumber");
            this.getUserByPhone(getContext());
        }
        if (bundleReceived.getString("carReg") != "0") {
            carReg = bundleReceived.getString("carReg");
            this.setUserCarReg(getContext());
        }

        Button btn_booking = view.findViewById((R.id.btn_booking));
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("customerID", userID);
                navController.navigate(R.id.action_userPageFragment_to_Booking_WashChoice, bundleSend);
            }
        });

        Button btn_signOut= view.findViewById((R.id.btn_logout));
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_userPageFragment_to_userLoginFragment);
            }
        });

        Button btn_editDetails = view.findViewById((R.id.btn_editDetails));
        btn_editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_userPageFragment_to_userDetailsFragment);
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
                            String customerString = output.toString();
                            Gson gson=new Gson();
                            customer = gson.fromJson(customerString, Customer.class);
                            customerName.setText("Welcome " + customer.getFirstName() + " " + customer.getLastName());
                        }
                    }
                });
        backgroundWorker.execute(type, userID);
    }

    public void getUserByPhone(Context context) {
        String type = "getUserByPhone";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            String customerString = output.toString();
                            Gson gson=new Gson();
                            customer = gson.fromJson(customerString, Customer.class);
                            userID = customer.getUserID();
                            customerName.setText("Welcome " + customer.getFirstName() + " " + customer.getLastName());
                        }
                    }
                });
        backgroundWorker.execute(type, phoneNumber);
    }

    public void setUserCarReg(Context context) {
        String type = "setUserCarReg";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            Log.d("customer insert", output.toString());
                        }
                    }
                });
        new Timer().schedule(
                new TimerTask(){

                    @Override
                    public void run(){

                        backgroundWorker.execute(type, userID, carReg);
                    }

                }, 2000);

    }
}
