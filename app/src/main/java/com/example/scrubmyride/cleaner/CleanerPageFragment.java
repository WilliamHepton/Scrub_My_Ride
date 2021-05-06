package com.example.scrubmyride.cleaner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
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
import com.example.scrubmyride.entities.Cleaner;
import com.example.scrubmyride.entities.Customer;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CleanerPageFragment extends Fragment {

    CalendarView WorkCalendarCV;
    String pickedDate, email;
    int userID;
    Bundle bundleReceived, bundleSend;
    Cleaner cleaner;
    TextView cleanerName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_page, container, false);
        WorkCalendarCV = view.findViewById((R.id.cv_workCalendar));
        cleanerName = view.findViewById((R.id.txt_username));
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

        Button btn_signOut= view.findViewById((R.id.btn_logout));
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_Cleaner_Page_to_Cleaner_Login);
            }
        });

        Button btn_scheduleWork = view.findViewById((R.id.btn_scheduleWork));
        btn_scheduleWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", userID);
                navController.navigate(R.id.action_Cleaner_Page_to_Cleaner_Work, bundleSend);
            }
        });

        Button btn_setWashType = view.findViewById((R.id.btn_setWashType));
        btn_setWashType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", userID);
                navController.navigate(R.id.action_Cleaner_Page_to_cleanerWashTypesFragment, bundleSend);
            }
        });

        Button btn_editDetails = view.findViewById((R.id.btn_editDetails));
        btn_editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", userID);
                navController.navigate(R.id.action_Cleaner_Page_to_Cleaner_Details, bundleSend);
            }
        });

        WorkCalendarCV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                pickedDate = year + "-" + month  + "-" + dayOfMonth;
                showPopup(view);
            }
        });

    }

    public void showPopup(View view) {


        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                this.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.workdaymenu_popup, null);

        //test = popupView.findViewById((R.id.tv_test));
        //test.setText(userID+"");

        // create the popup window
        int width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics())); // LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics())); //LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup windo
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


    }

    public void getSchedule(Context context) {
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
        backgroundWorker.execute(type, userID);
    }

    public void getUser(Context context) {
        String type = "getUser";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                            String cleanerString = output.toString();
                            Gson gson=new Gson();
                            cleaner = gson.fromJson(cleanerString, Cleaner.class);
                            cleanerName.setText("Welcome " + cleaner.getFirstName() + " " + cleaner.getLastName());
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
                            String cleanerString = output.toString();
                            Gson gson=new Gson();
                            cleaner = gson.fromJson(cleanerString, Cleaner.class);
                            userID = cleaner.getUserID();
                            cleanerName.setText("Welcome " + cleaner.getFirstName() + " " + cleaner.getLastName());
                        }
                    }
                });
        backgroundWorker.execute(type, email);
    }
}
