package com.example.scrubmyride.booking;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

import java.util.ArrayList;

public class CleanerChoiceFragment extends Fragment {

    String cleanersString= "";
    JSONArray cleanersJSONArray;
    String selectedDateStart, selectedDateEnd;
    Bundle bundleReceived, bundleSend;
    Spinner CleanerListS;
    ArrayList<Cleaner> cleaners = new ArrayList<Cleaner>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_booking_cleaner_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = this.getContext();

        bundleReceived = getArguments();
        selectedDateStart = bundleReceived.getString("selectedDateStart");
        selectedDateEnd = bundleReceived.getString("selectedDateEnd");
        CleanerListS = view.findViewById(R.id.s_cleanersList);

        Log.d("selectedDateStart", selectedDateStart);
        Log.d("selectedDateEnd", selectedDateEnd);

        this.getCleaners(context);
        final NavController navController = Navigation.findNavController(view);

        Button btn_next = view.findViewById((R.id.btn_bookingCleanerChoice_next));
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend.putString("selectedDateStart", bundleReceived.getString("selectedDateStart"));
                bundleSend.putString("selectedDateEnd", bundleReceived.getString("selectedDateEnd"));
                bundleSend.putString("address", bundleReceived.getString("address"));
                bundleSend.putInt("washTypeID", bundleReceived.getInt("washTypeID"));
                navController.navigate(R.id.action_Booking_CleanerChoice_to_Booking_Payment, bundleSend);
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_Booking_CleanerChoice_to_HomePage);
            }
        });

        CleanerListS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               // AddressET.setText(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    public void getCleaners(Context context) {
        String type = "getCleaners";
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!"-1".equals((String) output)) {
                           cleanersString = output.toString();
                            try {
                                cleanersJSONArray = new JSONArray(cleanersString);
                                String[] cleanersStringArray=new String[cleanersJSONArray.length()];
                                String[] cleanerStringDisplay = new String[cleanersJSONArray.length()];

                                for(int i=0; i<cleanersJSONArray.length(); i++) {
                                    cleanersStringArray[i] = cleanersJSONArray.optString(i);

                                    cleanersStringArray[i] = cleanersStringArray[i].substring(2, cleanersStringArray[i].length()-1);
                                    int userID = Integer.parseInt(cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\"")));
                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length()-1);
                                    String firstName = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));
                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length()-1);
                                    String lastName = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));
                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length()-1);
                                    String startDate = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));
                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length()-1);
                                    String endDate = cleanersStringArray[i];
                                    Cleaner newCleaner = new Cleaner(userID, firstName, lastName, startDate, endDate);
                                    cleaners.add(newCleaner);
                                    cleanerStringDisplay[i] = firstName + " " + lastName;
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cleanerStringDisplay);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                arrayAdapter.notifyDataSetChanged();
                                CleanerListS.setAdapter(arrayAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        backgroundWorker.execute(type, selectedDateStart, selectedDateEnd);
    }

    class Cleaner {
        private int userID;
        private String firstName;
        private String lastName;
        private String startDate;
        private String endDate;

        public Cleaner(int userID, String firstName, String lastName, String startDate, String endDate) {
        }


        public int getUserID() {
            return userID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }


        public String toString() {
            return String.format("userID:%d,firstName:%s,lastName:%s,startDate:%s,endDate:%s", userID, firstName, lastName, startDate, endDate);
        }
    }
}
