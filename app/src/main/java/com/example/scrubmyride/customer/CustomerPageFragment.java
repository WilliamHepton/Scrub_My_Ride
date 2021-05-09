package com.example.scrubmyride.customer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
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
import com.example.scrubmyride.entities.InvoiceDisplay;
import com.google.gson.Gson;
import com.skyhope.eventcalenderlibrary.CalenderEvent;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomerPageFragment extends Fragment {

    int userID;
    String email, carReg, postcode;
    Bundle bundleReceived, bundleSend;
    Customer customer;
    TextView customerName;
    ArrayList<InvoiceDisplay> customerInvoices = new ArrayList<>();
    CalenderEvent calendar;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_customer_page, container, false);

        customerName = view.findViewById(R.id.txt_username);
        calendar = view.findViewById(R.id.calender_event);

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
                bundleSend = new Bundle();
                bundleSend.putInt("userID", userID);
                navController.navigate(R.id.action_userPageFragment_to_userDetailsFragment, bundleSend);
            }
        });

        calendar.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(DayContainerModel dayContainerModel) {
                showPopup(view, dayContainerModel.getDate());
            }
        });
    }

    public void showPopup(View view, String date) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                this.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.workdaymenu_popup, null);

        TextView clientName = popupView.findViewById((R.id.txt_popup1));
        TextView startTime = popupView.findViewById((R.id.txt_popup2));
        TextView washType = popupView.findViewById((R.id.txt_popup3));
        TextView clientEmail = popupView.findViewById((R.id.txt_popup4));
        TextView clientPhone = popupView.findViewById((R.id.txt_popup5));
        TextView servicePrice = popupView.findViewById((R.id.txt_popup6));


        InvoiceDisplay bookingOnSelectedDay = customerInvoices.stream().filter(x -> SQLDateToEventCalendarDate(x.getServiceTimeStart()).equals(date)).findFirst().orElse(null);

        if(bookingOnSelectedDay != null){
            clientName.setText("Cleaner: " + bookingOnSelectedDay.getFirstName() + " " + bookingOnSelectedDay.getLastName());
            startTime.setText("Start time: " + SQLDateToTime(bookingOnSelectedDay.getServiceTimeStart()));
            washType.setText("Wash type: " + bookingOnSelectedDay.getDescription());
            clientEmail.setText("Cleaner's email: " + bookingOnSelectedDay.getEmail());
            clientPhone.setText("Cleaner's phone: " + bookingOnSelectedDay.getPhoneNumber());
            float fullPrice = bookingOnSelectedDay.getPrice() + bookingOnSelectedDay.getServiceFee();
            servicePrice.setText("Service price including fees: £ " + fullPrice);
        } else {
            clientName.setText("You have not yet booked anything on this day.");
        }



        // create the popup window
        int width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics())); // LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics())); //LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup windo
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Close popup window
        Button btn_closePopup = popupView.findViewById((R.id.btn_close_popup));
        btn_closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
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
                            //Convert SQL output to an array of InvoiceDisplay objects
                            JSONArray invoicesJSONArray = null;
                            try {
                                invoicesJSONArray = new JSONArray(invoicesString);
                                Gson gson=new Gson();

                               for(int i=0; i<invoicesJSONArray.length(); i++) {
                                    String stringInvoice = invoicesJSONArray.getJSONArray(i).toString();
                                    String[] invoiceStringArray = stringInvoice.replaceAll("[\\[\\]\"]", "").split(",");
                                    customerInvoices.add(new InvoiceDisplay(Integer.parseInt(invoiceStringArray[0]), invoiceStringArray[1], Float.parseFloat(invoiceStringArray[2]), Float.parseFloat(invoiceStringArray[3]), invoiceStringArray[4], invoiceStringArray[5], invoiceStringArray[6], invoiceStringArray[7], invoiceStringArray[8]));
                               }

                                setCalendarEvents();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        backgroundWorker.execute(type, userID);
    }

    public void setCalendarEvents(){
        Log.d("trest", customerInvoices.size() + "");
        for(int i=0; i<customerInvoices.size(); i++) {
            Event event = new Event(DateStringToMillis(customerInvoices.get(i).getServiceTimeStart()), "CW", Color.RED);
            calendar.addEvent(event);
        }
    }

    public long DateStringToMillis(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        return timeInMilliseconds;
    }

    public String SQLDateToEventCalendarDate(String date){
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("d MMM yyyy");
        date = spf.format(newDate);
        return date;
    }

    public String SQLDateToTime(String date){
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("HH:mm");
        date = spf.format(newDate);
        return date;
    }
}
