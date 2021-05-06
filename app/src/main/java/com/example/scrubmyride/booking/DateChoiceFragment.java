package com.example.scrubmyride.booking;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateChoiceFragment extends Fragment {

    DatePicker availableDatesDP;
    Button btn_next, btn_close;
    String selectedDateStart, selectedDateEnd;
    Bundle bundleReceived, bundleSend;
    int customerID;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_booking_date, container, false);

        availableDatesDP =  view.findViewById(R.id.dp_availableDates);
        btn_next = view.findViewById(R.id.btn_bookingDate_next);
        btn_close = view.findViewById(R.id.btn_close);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundleReceived = getArguments();
        customerID = bundleReceived.getInt("customerID");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        selectedDateStart = LocalDateTime.now().format(dateFormat);
        selectedDateEnd = LocalDateTime.now().plusDays(1).format(dateFormat);

        final NavController navController = Navigation.findNavController(view);
        final Context context = getContext();

        this.getSchedules(context);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putString("selectedDateStart", selectedDateStart);
                bundleSend.putString("selectedDateEnd", selectedDateEnd);
                bundleSend.putString("address", bundleReceived.getString("address"));
                bundleSend.putString("carReg", bundleReceived.getString("carReg"));
                bundleSend.putString("email", bundleReceived.getString("email"));
                bundleSend.putString("postcode", bundleReceived.getString("postcode"));
                bundleSend.putInt("washTypeID", bundleReceived.getInt("washTypeID"));
                bundleSend.putInt("customerID", customerID);

                navController.navigate(R.id.action_Booking_Date_to_Booking_CleanerChoice, bundleSend);
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", customerID);
                navController.navigate(R.id.action_Booking_Date_to_userPageFragment, bundleSend);
            }
        });


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        availableDatesDP.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                int realMonth = month +1;
                int nextDay = dayOfMonth+1;
                selectedDateStart = year + "-" + realMonth + "-" + dayOfMonth + " 00:00:00";
                selectedDateEnd = year + "-" + realMonth + "-" + nextDay + " 00:00:00";
            }
        });

        availableDatesDP.setMinDate(System.currentTimeMillis() - 1000);
    }

    public void getSchedules(Context context) {
        String type = "getSchedules";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            String scheduleString = output.toString();
                            try {
                                JSONArray scheduleJSON = new JSONArray(scheduleString);
                                String[] cleanersStringArray=new String[scheduleJSON.length()];
                                for(int i=0; i<scheduleJSON.length(); i++) {
                                    cleanersStringArray[i] = scheduleJSON.optString(i);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        backgroundWorker.execute(type);
    }
}
