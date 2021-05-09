package com.example.scrubmyride.booking;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrubmyride.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AddressChoiceFragment extends Fragment {


    EditText PostCodeET, AddressET;
    Spinner AddressListS;
    Bundle bundleReceived, bundleSend;
    int washTypeID, customerID;
    String carReg, email, postcode;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_booking_address, container, false);
        PostCodeET = view.findViewById(R.id.input_postCode);
        AddressET = view.findViewById(R.id.input_address);
        AddressListS = view.findViewById(R.id.s_addressList);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = this.getContext();

        final NavController navController = Navigation.findNavController(view);
        final String GEOCODING_RESOURCE = "https://geocode.search.hereapi.com/v1/geocode";
        final String API_KEY = "AIzaSyD80l21RxuZZaE2OsJVIpknj_Q9ha4AU4s";

        final Locale locale = new Locale("en", "GB");
        final Geocoder geocoder = new Geocoder(context, locale);

        final List<String> addresses = new ArrayList<String>();
        final ArrayAdapter<String> adapter;

        bundleReceived = getArguments();
        washTypeID = bundleReceived.getInt("washTypeID");
        customerID = bundleReceived.getInt("customerID");
        carReg = bundleReceived.getString("carReg");
        email = bundleReceived.getString("email");
        postcode = bundleReceived.getString("postcode");

        if (postcode.length() > 1) {
            PostCodeET.setText(postcode);

            this.getAddressesFromPostCode(addresses, geocoder);
        }

        Button btn_next = view.findViewById((R.id.btn_bookingAddress_next));
        Button btn_close = view.findViewById((R.id.btn_close));

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AddressET.getText().toString().trim().length() == 0) {
                    AddressET.setError("Please enter your address");
                } else {
                    bundleSend = new Bundle();
                    bundleSend.putString("address", AddressET.getText().toString());
                    bundleSend.putInt("washTypeID", washTypeID);
                    bundleSend.putInt("customerID", customerID);
                    bundleSend.putString("carReg", carReg);
                    bundleSend.putString("email", email);
                    bundleSend.putString("postcode", postcode);
                    navController.navigate(R.id.action_Booking_Address_to_Booking_Date, bundleSend);
                }
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", customerID);
                navController.navigate(R.id.action_Booking_Address_to_userPageFragment, bundleSend);
            }
        });

        PostCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count >= 5 && count <= 8) {
                    addresses.clear();
                    List<Address> location = null;
                    List<Address> geocoderAddresses = null;
                    double latitude = 0;
                    double longitude = 0;
                    try {
                        location = geocoder.getFromLocationName(charSequence.toString(), 1);
                        if (location.size() > 0) {
                            latitude = location.get(0).getLatitude();
                            longitude = location.get(0).getLongitude();
                            geocoderAddresses = geocoder.getFromLocation(latitude, longitude, 300);
                            String addressLine = "";
                            String addressNumbers;

                            int firstSpaceIndex;
                            int firstCommaIndex;
                            for (Address var : geocoderAddresses) {
                                addressLine = var.getAddressLine(0);

                                firstSpaceIndex = addressLine.indexOf(' ');
                                firstCommaIndex = addressLine.indexOf(',');
                                if (firstCommaIndex < firstSpaceIndex) {
                                    firstSpaceIndex = firstCommaIndex;
                                }
                                addressNumbers = addressLine.substring(0, firstSpaceIndex);
                                if (addressNumbers.matches("(\\d+)(-)(\\d+)")) {
                                    int dashIndex = addressNumbers.indexOf('-');
                                    int minNumber = Integer.parseInt(addressNumbers.substring(0, dashIndex));
                                    int maxnumber = Integer.parseInt(addressNumbers.substring(dashIndex + 1));
                                    if (minNumber > maxnumber) {
                                        int temp = minNumber;
                                        minNumber = maxnumber;
                                        maxnumber = temp;
                                    }
                                    while (minNumber < maxnumber) {
                                        String newAddressLine = minNumber + addressLine.substring(firstSpaceIndex);
                                        if (!addresses.contains(newAddressLine)) {
                                            addresses.add(newAddressLine);
                                        }
                                        minNumber++;
                                    }
                                } else if (!addresses.contains(addressLine)) {
                                    addresses.add(addressLine);
                                }
                            }
                        }
                    } catch (
                            IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] addressesArray = addresses.toArray(new String[0]);
                Arrays.sort(addressesArray, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return extractInt(o1) - extractInt(o2);
                    }

                    int extractInt(String s) {
                        String num = s.replaceAll("\\D", "");
                        // return 0 if no digits found
                        return num.isEmpty() ? 0 : Integer.parseInt(num);
                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, addressesArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.notifyDataSetChanged();
                AddressListS.setAdapter(adapter);

            }
        });

        AddressListS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                AddressET.setText(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    public void getAddressesFromPostCode(List<String> addresses , Geocoder geocoder){
        addresses.clear();
        List<Address> location = null;
        List<Address> geocoderAddresses = null;
        double latitude = 0;
        double longitude = 0;
        try {
            location = geocoder.getFromLocationName(postcode, 1);
            if (location.size() > 0) {
                latitude = location.get(0).getLatitude();
                longitude = location.get(0).getLongitude();
                geocoderAddresses = geocoder.getFromLocation(latitude, longitude, 300);
                String addressLine = "";
                String addressNumbers;

                int firstSpaceIndex;
                int firstCommaIndex;
                for (Address var : geocoderAddresses) {
                    addressLine = var.getAddressLine(0);

                    firstSpaceIndex = addressLine.indexOf(' ');
                    firstCommaIndex = addressLine.indexOf(',');
                    if (firstCommaIndex < firstSpaceIndex) {
                        firstSpaceIndex = firstCommaIndex;
                    }
                    addressNumbers = addressLine.substring(0, firstSpaceIndex);
                    if (addressNumbers.matches("(\\d+)(-)(\\d+)")) {
                        int dashIndex = addressNumbers.indexOf('-');
                        int minNumber = Integer.parseInt(addressNumbers.substring(0, dashIndex));
                        int maxnumber = Integer.parseInt(addressNumbers.substring(dashIndex + 1));
                        if (minNumber > maxnumber) {
                            int temp = minNumber;
                            minNumber = maxnumber;
                            maxnumber = temp;
                        }
                        while (minNumber < maxnumber) {
                            String newAddressLine = minNumber + addressLine.substring(firstSpaceIndex);
                            if (!addresses.contains(newAddressLine)) {
                                addresses.add(newAddressLine);
                            }
                            minNumber++;
                        }
                    } else if (!addresses.contains(addressLine)) {
                        addresses.add(addressLine);
                    }
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        String[] addressesArray = addresses.toArray(new String[0]);
        Arrays.sort(addressesArray, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, addressesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        AddressListS.setAdapter(adapter);
    }
}
