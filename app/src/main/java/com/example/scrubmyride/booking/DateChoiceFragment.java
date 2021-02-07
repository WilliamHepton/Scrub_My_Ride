package com.example.scrubmyride.booking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.scrubmyride.R;

import java.util.Calendar;

public class DateChoiceFragment extends Fragment {

    CalendarView calendarView;
    Button btn_next, btn_close;
    String selectedDateStart, selectedDateEnd;
    Bundle bundleReceived, bundleSend;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_booking_date, container, false);

        calendarView =  view.findViewById((R.id.cv_availableDates));
        btn_next = view.findViewById((R.id.btn_bookingDate_next));
        btn_close = view.findViewById((R.id.btn_close));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundleReceived = getArguments();

        final NavController navController = Navigation.findNavController(view);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend.putString("selectedDateStart", selectedDateStart);
                bundleSend.putString("selectedDateEnd", selectedDateEnd);
                bundleSend.putString("address", bundleReceived.getString("address"));
                bundleSend.putInt("washTypeID", bundleReceived.getInt("washTypeID"));

                navController.navigate(R.id.action_Booking_Date_to_Booking_CleanerChoice, bundleSend);
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_Booking_Date_to_HomePage);
            }
        });

        calendarView.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int realMonth = month +1;
                int nextDay = dayOfMonth+1;
                selectedDateStart = year + "-" + realMonth + "-" + dayOfMonth + " 00:00:00";
                selectedDateEnd = year + "-" + realMonth + "-" + nextDay + " 00:00:00";
            }
        });
    }
}
