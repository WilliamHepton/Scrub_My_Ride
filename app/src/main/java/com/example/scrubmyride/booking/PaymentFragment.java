package com.example.scrubmyride.booking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Timer;
import java.util.TimerTask;

public class PaymentFragment extends Fragment {

    Bundle bundleReceived, bundleSend;
    int customerID, washTypeID, cleanerID;
    String address, dateTimeStart, carReg, email, postcode;
    TextView cleanerIDTV, customerIDTV, selectedDateTimeTV, washTypeIDTV, customerAddressTV, carRegTV;
    EditText emailET;
    Button payButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_booking_payment, container, false);

        cleanerIDTV = view.findViewById(R.id.txt_cleanerID);
        customerIDTV = view.findViewById(R.id.txt_customerID);
        selectedDateTimeTV = view.findViewById(R.id.txt_selectedDateTime);
        washTypeIDTV = view.findViewById(R.id.txt_washTypeID);
        customerAddressTV = view.findViewById(R.id.txt_customerAddress);
        carRegTV = view.findViewById(R.id.txt_carReg);
        payButton = view.findViewById(R.id.btn_bookingPayment_pay);
        emailET = view.findViewById(R.id.et_email);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundleReceived = getArguments();
        dateTimeStart = bundleReceived.getString("dateTimeStart");
        address = bundleReceived.getString("address");
        carReg = bundleReceived.getString("carReg");
        email = bundleReceived.getString("email");
        postcode = bundleReceived.getString("postcode");
        customerID = bundleReceived.getInt("customerID");
        Log.d("test1", customerID + "");
        cleanerID = bundleReceived.getInt("cleanerID");
        washTypeID = bundleReceived.getInt("washTypeID");

        cleanerIDTV.setText(Integer.toString(cleanerID));
        customerIDTV.setText(Integer.toString(customerID));
        carRegTV.setText(carReg);
        selectedDateTimeTV.setText(dateTimeStart);
        washTypeIDTV.setText(Integer.toString(washTypeID));
        customerAddressTV.setText(address);

        if (email.length() > 1) {
            emailET.setText(email);
        }

        double price = 10;
        double serviceFee = price * 0.01;

        final NavController navController = Navigation.findNavController(view);

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", customerID);
                navController.navigate(R.id.action_Booking_Payment_to_userPageFragment, bundleSend);
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "invoice";
                BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                        new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!"-1".equals((String) output)) {
                                    CharSequence text = "You have successfully booked a car wash!";
                                    int duration = Toast.LENGTH_LONG;
                                    Toast toast = Toast.makeText(getContext(), text, duration);
                                    toast.show();
                                    CharSequence text2 = "Your new booking can be seen in your calendar";
                                    int duration2 = Toast.LENGTH_LONG;
                                    Toast toast2 = Toast.makeText(getContext(), text2, duration2);
                                    toast2.show();
                                    new Timer().schedule(
                                            new TimerTask(){
                                                @Override
                                                public void run(){
                                                    Log.d("test2", customerID + "");
                                                    bundleSend = new Bundle();
                                                    bundleSend.putInt("userID", customerID);
                                                    navController.navigate(R.id.action_Booking_Payment_to_userPageFragment, bundleSend);
                                                }

                                            }, 2000);
                                }
                            }
                        });
                backgroundWorker.execute(type, customerID, cleanerID, dateTimeStart, price, washTypeID, address, postcode, carReg, serviceFee);
            }
        });
    }
    
}
