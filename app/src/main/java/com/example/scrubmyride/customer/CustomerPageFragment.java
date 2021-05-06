package com.example.scrubmyride.customer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

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
import com.example.scrubmyride.entities.InvoiceCustomer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerPageFragment extends Fragment {

    int userID;
    String email, carReg, postcode;
    Bundle bundleReceived, bundleSend;
    Customer customer;
    TextView customerName;
    ArrayList<InvoiceCustomer> customerInvoices = new ArrayList<>();
    CalendarView calendar;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_customer_page, container, false);

        customerName = view.findViewById(R.id.txt_username);
        calendar = view.findViewById(R.id.calendarView);

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
        } else if (bundleReceived.getString("email") != "0") {
            email = bundleReceived.getString("email");
            this.getUserByEmail(getContext());
        }
        if (bundleReceived.getString("carReg") != "0" & bundleReceived.getString("carReg") != null) {
            carReg = bundleReceived.getString("carReg");
            this.setUserCarReg(getContext());
        }

        this.getCustomerInvoices();

        Button btn_booking = view.findViewById((R.id.btn_booking));
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("customerID", userID);
                bundleSend.putString("email", email);
                bundleSend.putString("postcode", postcode);
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

        calendar.
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
                            postcode = customer.getPostcode();
                            email = customer.getEmail();
                            customerName.setText("Welcome " + customer.getFirstName() + " " + customer.getLastName());
                        }
                    }
                });
        backgroundWorker.execute(type, userID);
    }

    public void getUserByEmail(Context context) {
        String type = "getUserByEmail";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            String customerString = output.toString();
                            Gson gson=new Gson();
                            customer = gson.fromJson(customerString, Customer.class);
                            userID = customer.getUserID();
                            postcode = customer.getPostcode();
                            customerName.setText("Welcome " + customer.getFirstName() + " " + customer.getLastName());
                        }
                    }
                });
        backgroundWorker.execute(type, email);
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

    public void getCustomerInvoices(){
        String type = "getCustomerInvoices";

        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {

                        if (!"-1".equals((String) output)) {
                            String invoicesString = output.toString();
                            //Convert SQL output to an array of InvoiceCustomer objects
                            JSONArray invoicesJSONArray = null;
                            try {
                                invoicesJSONArray = new JSONArray(invoicesString);
                                Gson gson=new Gson();

                               for(int i=0; i<invoicesJSONArray.length(); i++) {
                                    String stringInvoice = invoicesJSONArray.getJSONArray(i).toString();
                                    String[] invoiceStringArray = stringInvoice.replaceAll("[\\[\\]\"]", "").split(",");
                                    customerInvoices.add(new InvoiceCustomer(Integer.parseInt(invoiceStringArray[0]), invoiceStringArray[1], Float.parseFloat(invoiceStringArray[2]), Float.parseFloat(invoiceStringArray[3]), invoiceStringArray[4], invoiceStringArray[5], invoiceStringArray[6], invoiceStringArray[7], invoiceStringArray[8]));
                               }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        backgroundWorker.execute(type, userID);
    }
}
