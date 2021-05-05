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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import com.example.scrubmyride.AsyncResponse;
import com.example.scrubmyride.BackgroundWorker;
import com.example.scrubmyride.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.navigation.Navigation.findNavController;

public class CleanerWorkFragment extends Fragment {

    TableLayout dateTable;
    EditText monStartTime, tueStartTime,  wedStartTime,  thuStartTime,  friStartTime,  satStartTime,  sunStartTime,
            monEndTime, tueEndTime,  wedEndTime,  thuEndTime,  friEndTime,  satEndTime,  sunEndTime;
    Bundle bundleReceived, bundleSend;
    int userID;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_cleaner_work, container, false);
        dateTable = view.findViewById(R.id.table_dates);
        monStartTime = view.findViewById(R.id.et_monStart);
        tueStartTime = view.findViewById(R.id.et_tueStart);
        wedStartTime = view.findViewById(R.id.et_wedStart);
        thuStartTime = view.findViewById(R.id.et_thuStart);
        friStartTime = view.findViewById(R.id.et_friStart);
        satStartTime = view.findViewById(R.id.et_satStart);
        sunStartTime = view.findViewById(R.id.et_sunStart);

        monEndTime = view.findViewById(R.id.et_monEnd);
        tueEndTime = view.findViewById(R.id.et_tueEnd);
        wedEndTime = view.findViewById(R.id.et_wedEnd);
        thuEndTime = view.findViewById(R.id.et_thuEnd);
        friEndTime = view.findViewById(R.id.et_friEnd);
        satEndTime = view.findViewById(R.id.et_satEnd);
        sunEndTime = view.findViewById(R.id.et_sunEnd);

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

        monStartTime.setShowSoftInputOnFocus(false);
        tueStartTime.setShowSoftInputOnFocus(false);
        wedStartTime.setShowSoftInputOnFocus(false);
        thuStartTime.setShowSoftInputOnFocus(false);
        friStartTime.setShowSoftInputOnFocus(false);
        satStartTime.setShowSoftInputOnFocus(false);
        sunStartTime.setShowSoftInputOnFocus(false);

        monEndTime.setShowSoftInputOnFocus(false);
        tueEndTime.setShowSoftInputOnFocus(false);
        wedEndTime.setShowSoftInputOnFocus(false);
        thuEndTime.setShowSoftInputOnFocus(false);
        friEndTime.setShowSoftInputOnFocus(false);
        satEndTime.setShowSoftInputOnFocus(false);
        sunEndTime.setShowSoftInputOnFocus(false);

        Button btn_save = view.findViewById((R.id.btn_save));
        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = "schedule";
                HashMap<String, String> startTimesList = new HashMap<String, String>();
                HashMap<String, String> endTimesList = new HashMap<String, String>();
                startTimesList.put("2", monStartTime.getText().toString());
                startTimesList.put("3", tueStartTime.getText().toString());
                startTimesList.put("4", wedStartTime.getText().toString());
                startTimesList.put("5", thuStartTime.getText().toString());
                startTimesList.put("6", friStartTime.getText().toString());
                startTimesList.put("7", satStartTime.getText().toString());
                startTimesList.put("1", sunStartTime.getText().toString());
                endTimesList.put("2", monEndTime.getText().toString());
                endTimesList.put("3", tueEndTime.getText().toString());
                endTimesList.put("4", wedEndTime.getText().toString());
                endTimesList.put("5", thuEndTime.getText().toString());
                endTimesList.put("6", friEndTime.getText().toString());
                endTimesList.put("7", satEndTime.getText().toString());
                endTimesList.put("1", sunEndTime.getText().toString());

                startTimesList.forEach((k,v)-> {
                    if(v.length() == 0) {
                        startTimesList.put(k, "-1");
                    }
                });

                endTimesList.forEach((k,v)-> {
                    if(v.length() == 0) {
                        endTimesList.put(k, "-1");
                    }
                });
                BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity(),
                        new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!"-1".equals((String) output)) {
                                    bundleSend = new Bundle();
                                    bundleSend.putInt("userID", bundleReceived.getInt("userID"));
                                    navController.navigate(R.id.action_Cleaner_Work_to_Cleaner_Page, bundleSend);
                                }
                            }
                        });
                backgroundWorker.execute(type, userID, startTimesList, endTimesList);
            }
        });

        Button btn_close = view.findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleSend = new Bundle();
                bundleSend.putInt("userID", bundleReceived.getInt("userID"));
                navController.navigate(R.id.action_Cleaner_Work_to_Cleaner_Page, bundleSend);
            }
        });

        monStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                monStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                monStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                monStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                monStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        monStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            monStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            monStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            monStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            monStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tueStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                tueStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                tueStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                tueStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                tueStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        tueStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            tueStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            tueStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            tueStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            tueStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        wedStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                wedStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                wedStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                wedStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                wedStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        wedStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            wedStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            wedStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            wedStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            wedStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        thuStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                thuStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                thuStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                thuStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                thuStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        thuStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            thuStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            thuStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            thuStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            thuStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        friStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                friStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                friStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                friStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                friStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        friStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            friStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            friStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            friStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            friStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        satStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                satStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                satStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                satStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                satStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        satStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            satStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            satStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            satStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            satStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        sunStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                sunStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                sunStartTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                sunStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                sunStartTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        sunStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            sunStartTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            sunStartTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            sunStartTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            sunStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        monEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                monEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                monEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                monEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                monEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        monEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            monEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            monEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            monEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            monEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tueEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                tueEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                tueEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                tueEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                tueEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        tueEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            tueEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            tueEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            tueEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            tueEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        wedEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                wedEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                wedEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                wedEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                wedEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        wedEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            wedEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            wedEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            wedEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            wedEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        thuEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                thuEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                thuEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                thuEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                thuEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        thuEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            thuEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            thuEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            thuEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            thuEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        friEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                friEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                friEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                friEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                friEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        friEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            friEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            friEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            friEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            friEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        satEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                satEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                satEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                satEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                satEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        satEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            satEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            satEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            satEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            satEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        sunEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedMinute < 10 && selectedHour < 10){
                                sunEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else if(selectedMinute < 10){
                                sunEndTime.setText(selectedHour + ":0" + selectedMinute);
                            } else if(selectedHour < 10){
                                sunEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                sunEndTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        sunEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10 && selectedHour < 10){
                            sunEndTime.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if(selectedMinute < 10){
                            sunEndTime.setText(selectedHour + ":0" + selectedMinute);
                        } else if(selectedHour < 10){
                            sunEndTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else {
                            sunEndTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

}
