package com.example.scrubmyride.booking;

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

public class PaymentFragment extends Fragment {

    Bundle bundleReceived, bundleSend;
    int customerID, washTypeID, cleanerID;
    String address, dateTimeStart, carReg;
    TextView cleanerIDTV, customerIDTV, selectedDateTimeTV, washTypeIDTV, customerAddressTV, carRegTV;
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundleReceived = getArguments();
        dateTimeStart = bundleReceived.getString("dateTimeStart");
        address = bundleReceived.getString("address");
        carReg = bundleReceived.getString("carReg");
        customerID = bundleReceived.getInt("customerID");
        cleanerID = bundleReceived.getInt("cleanerID");
        washTypeID = bundleReceived.getInt("washTypeID");

        cleanerIDTV.setText(Integer.toString(cleanerID));
        customerIDTV.setText(Integer.toString(customerID));
        carRegTV.setText(carReg);
        selectedDateTimeTV.setText(dateTimeStart);
        washTypeIDTV.setText(Integer.toString(washTypeID));
        customerAddressTV.setText(address);

        double price = 10;
        double serviceFee = price * 0.01;

        final NavController navController = Navigation.findNavController(view);

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_Booking_Payment_to_HomePage);
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
                                    Log.d("test", "success");
                                }
                            }
                        });
                backgroundWorker.execute(type, customerID, cleanerID, dateTimeStart, price, washTypeID, address, " ", carReg, serviceFee);
            }
        });
    }
}
