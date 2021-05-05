package com.example.scrubmyride.cleaner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static androidx.navigation.Navigation.findNavController;

public class CleanerWashTypesFragment extends Fragment {

    EditText outsideWashPrice, outsideInsideWashPrice,  insideWashPrice;
    Bundle bundleReceived, bundleSend;
    int userID;
    ArrayList<Double> prices = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_wash_types, container, false);

        outsideWashPrice = view.findViewById(R.id.et_outsideWash_price);
        outsideInsideWashPrice = view.findViewById(R.id.et_outsideInsideWash_price);
        insideWashPrice = view.findViewById(R.id.et_insideWash_price);

        return view;
    }

    @SuppressLint({"ResourceType", "NewApi"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundleReceived = getArguments();
        userID = bundleReceived.getInt("userID");

        final NavController navController = findNavController(view);
        final Context context = this.getContext();

        Button btn_save = view.findViewById((R.id.btn_save));
        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = "cleaners_washType";

                prices.add(Double.parseDouble(outsideWashPrice.getText().toString()));
                prices.add(Double.parseDouble(outsideInsideWashPrice.getText().toString()));
                prices.add(Double.parseDouble(insideWashPrice.getText().toString()));

                BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                        new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!"-1".equals((String) output)) {
                                    bundleSend = new Bundle();
                                    bundleSend.putInt("userID", bundleReceived.getInt("userID"));
                                    navController.navigate(R.id.action_cleanerWashTypesFragment_to_Cleaner_Page, bundleSend);
                                }
                            }
                        });
                backgroundWorker.execute(type, userID, prices);
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", bundleReceived.getInt("userID"));
                navController.navigate(R.id.action_cleanerWashTypesFragment_to_Cleaner_Page, bundleSend);
            }
        });


    }



}
