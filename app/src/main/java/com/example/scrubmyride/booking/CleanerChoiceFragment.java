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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;
import com.example.scrubmyride.entities.Cleaner;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class CleanerChoiceFragment extends Fragment {

    String cleanersString= "";
    JSONArray cleanersJSONArray;
    String selectedDateStart, selectedDateEnd, dateTimeStart;
    Bundle bundleReceived, bundleSend;
    Spinner CleanerListS, CleanerHoursS;
    ArrayList<Cleaner> cleaners = new ArrayList<Cleaner>();
    int customerID, washTypeID;
    Cleaner selectedCleaner;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_booking_cleaner_choice, container, false);

        CleanerListS = view.findViewById(R.id.s_cleanersList);
        CleanerHoursS = view.findViewById(R.id.s_cleanerHours);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = this.getContext();
        final NavController navController = Navigation.findNavController(view);
        bundleReceived = getArguments();
        selectedDateStart = bundleReceived.getString("selectedDateStart");
        selectedDateEnd = bundleReceived.getString("selectedDateEnd");
        customerID = bundleReceived.getInt("customerID");
        washTypeID = bundleReceived.getInt("washTypeID");
        this.getCleaners(context);

        Button btn_next = view.findViewById((R.id.btn_bookingCleanerChoice_next));
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCleaner == null) {
                    CharSequence text = "Please try again";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                } else {
                    bundleSend = new Bundle();
                    bundleSend.putString("dateTimeStart", dateTimeStart);
                    bundleSend.putString("address", bundleReceived.getString("address"));
                    bundleSend.putString("carReg", bundleReceived.getString("carReg"));
                    bundleSend.putString("email", bundleReceived.getString("email"));
                    bundleSend.putString("postcode", bundleReceived.getString("postcode"));
                    bundleSend.putInt("washTypeID", washTypeID);
                    bundleSend.putInt("customerID", customerID);
                    bundleSend.putInt("cleanerID", selectedCleaner.getUserID());
                    bundleSend.putString("servicePrice", selectedCleaner.getPrice().toString());
                    bundleSend.putString("cleanerFullName", (selectedCleaner.getFirstName() + " " + selectedCleaner.getLastName()));

                    navController.navigate(R.id.action_Booking_CleanerChoice_to_Booking_Payment, bundleSend);
                }
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", customerID);
                navController.navigate(R.id.action_Booking_CleanerChoice_to_userPageFragment, bundleSend);
            }
        });

        CleanerListS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String cleanerFullName = parentView.getItemAtPosition(position).toString();
                cleanerFullName = cleanerFullName.substring(0, cleanerFullName.indexOf("-")-1);
                if (getCleanerByName(cleanerFullName)) {
                    getHours(context);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        CleanerHoursS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tempDate = LocalDateTime.parse(selectedCleaner.getStartDate(), dateFormat);
                String tempTime = parentView.getItemAtPosition(position).toString().substring(0,parentView.getItemAtPosition(position).toString().indexOf(' '));
                dateTimeStart = tempDate.getYear() + "-" + tempDate.getMonthValue() + "-" + tempDate.getDayOfMonth() + " " + tempTime + ":00";
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

                                    // put cleaner json output into an array of cleaners
                                    cleanersStringArray[i] = cleanersStringArray[i].substring(2, cleanersStringArray[i].length()-1);

                                    int userID = Integer.parseInt(cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\"")));

                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length());
                                    String firstName = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));

                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length());
                                    String lastName = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));

                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length());
                                    String startDate = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));

                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length());
                                    String endDate = cleanersStringArray[i].substring(0, cleanersStringArray[i].indexOf("\""));

                                    cleanersStringArray[i] = cleanersStringArray[i].substring(cleanersStringArray[i].indexOf("\"")+3, cleanersStringArray[i].length()-1);
                                    Double price = Double.parseDouble(cleanersStringArray[i]);

                                    Cleaner newCleaner = new Cleaner(userID, firstName, lastName, "", "", "", "", true, startDate, endDate, price);
                                    cleaners.add(newCleaner);
                                    cleanerStringDisplay[i] = firstName + " " + lastName + " - " + price.toString() + "£";
                                }
                                selectedCleaner = cleaners.get(0);
                                //dateTimeStart = cleaners.get(0).getStartDate();
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
        backgroundWorker.execute(type, selectedDateStart, selectedDateEnd, washTypeID);
    }

    public void getHours(Context context) {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(selectedCleaner.getStartDate(), dateFormat);
        LocalDateTime endTime = LocalDateTime.parse(selectedCleaner.getEndDate(), dateFormat);

        int startHour = startTime.getHour();
        int startMinute = startTime.getMinute();
        int endHour = endTime.getHour();
        int endMinute = startTime.getMinute();
        int loops = (endHour - startHour)*4;

        if((endMinute - startMinute) > 0) {
            loops += (endMinute - startMinute)/15;
        }
        if(loops > 0) {
            String[] cleanerStringDisplay = new String[loops];
            String displayTimeStart;
            String displayTimeEnd;
            int displayHour = startHour;
            int currentMinutes = startMinute;
            for(int i = 0; i < loops; i++){
                displayTimeStart = displayHour + ":" + currentMinutes;

                if(currentMinutes < 10){
                    displayTimeStart = displayHour + ":0" + currentMinutes;
                }

                if(currentMinutes >= 45){
                    displayTimeEnd = displayHour + 1 + ":" + (currentMinutes+15 - 60);
                    if (((currentMinutes+15) - 60) < 10) {
                        displayTimeEnd = displayHour + ":0" + (currentMinutes+15 - 60);
                    }
                } else {
                    displayTimeEnd = displayHour + ":" + (currentMinutes+15);
                }
                cleanerStringDisplay[i] = displayTimeStart + " to " + displayTimeEnd;
                currentMinutes += 15;
                if (currentMinutes >= 60) {
                    displayHour++;
                    currentMinutes = currentMinutes - 60;
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cleanerStringDisplay);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            arrayAdapter.notifyDataSetChanged();
            CleanerHoursS.setAdapter(arrayAdapter);
        }
    }

    public boolean getCleanerByName(String fullName) {
        if(cleaners.size() > 0) {
            Iterator<Cleaner> iter = cleaners.iterator();
            while (iter.hasNext()) {
                Cleaner c = iter.next();
                if (fullName.equals(c.getFirstName() + " " + c.getLastName())) {
                    selectedCleaner = c;
                    return true;
                }
            }
        }
        return false;
    }
}
