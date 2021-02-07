package com.example.scrubmyride;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.sql.Date;
import java.util.HashMap;

public class BackgroundWorker extends AsyncTask<Object, Void, String> {

    AlertDialog alertDialog;
    Context context;

    public AsyncResponse delegate = null; //Call back interface

    public BackgroundWorker(Context ctx, AsyncResponse asyncResponse) {
        context = ctx;
        delegate = asyncResponse; //Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(Object... params) {
        String type = params[0].toString();
        String login_url = "http://w-hepton.com.wwi6776.odns.fr/login.php";
        String register_url = "http://w-hepton.com.wwi6776.odns.fr/register.php";
        String schedule_url = "http://w-hepton.com.wwi6776.odns.fr/schedule.php";
        String getCleaners_url = "http://w-hepton.com.wwi6776.odns.fr/getcleaners.php";

        if (type.equals("login")) {
            try {
                String phone = params[1].toString();
                String password = params[2].toString();
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals(("register"))) {

            try {
                String firstName = params[1].toString();
                String lastName = params[2].toString();
                String email = params[3].toString();
                String phoneNumber = params[4].toString();
                String address = "none";
                String postCode = params[5].toString();
                String password = params[6].toString();
                String isCleaner = params[7].toString();
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8")
                        + "&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8")
                        + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&" + URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8")
                        + "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8")
                        + "&" + URLEncoder.encode("postCode", "UTF-8") + "=" + URLEncoder.encode(postCode, "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
                        + "&" + URLEncoder.encode("isCleaner", "UTF-8") + "=" + URLEncoder.encode(isCleaner, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals(("schedule"))) {

            try {
                String userID = params[1].toString();
                HashMap<String, String> startTimesList = (HashMap<String, String>) params[2];
                HashMap<String, String> endTimesList = (HashMap<String, String>) params[3];

                String result = "";
                for (int i = 1; i <= startTimesList.size(); i++) {
                    String startTime = startTimesList.get(i + "");
                    Log.d("i=", i + "");
                    Log.d("size", startTime + "");
                    if (startTime != "-1") {
                        String[] startTimeParts = startTime.split(":");
                        Calendar startTimeDate = Calendar.getInstance();
                        startTimeDate.set(Calendar.DAY_OF_WEEK, i);
                        startTimeDate.set(Calendar.HOUR, Integer.parseInt(startTimeParts[0]));
                        startTimeDate.set(Calendar.MINUTE, Integer.parseInt(startTimeParts[1]));
                        startTimeDate.set(Calendar.SECOND, 0);
                        startTimeDate.set(Calendar.MILLISECOND, 0);

                        String endTime = endTimesList.get(i + "");
                        Calendar endTimeDate = Calendar.getInstance();
                        if (endTime != "-1") {
                            String[] endTimeParts = startTime.split(":");
                            endTimeDate.set(Calendar.DAY_OF_WEEK, i);
                            endTimeDate.set(Calendar.HOUR, Integer.parseInt(endTimeParts[0]));
                            endTimeDate.set(Calendar.MINUTE, Integer.parseInt(endTimeParts[1]));
                        } else {
                            endTimeDate = startTimeDate;
                            endTimeDate.set(Calendar.HOUR, 0);
                            endTimeDate.set(Calendar.MINUTE, 0);
                        }
                        endTimeDate.set(Calendar.SECOND, 0);
                        endTimeDate.set(Calendar.MILLISECOND, 0);

                        String startDateFormated = (new java.sql.Date(startTimeDate.getTimeInMillis())).toString() + " " + startTime + ":00";
                        String endDateFormated = (new java.sql.Date(endTimeDate.getTimeInMillis())).toString() + " " + endTime + ":00";


                        Log.d("testDate", startDateFormated);
                        Log.d("endTimeDate", endDateFormated);

                        URL url = new URL(schedule_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String post_data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8")
                                + "&" + URLEncoder.encode("startDateFormated", "UTF-8") + "=" + URLEncoder.encode(startDateFormated, "UTF-8")
                                + "&" + URLEncoder.encode("endDateFormated", "UTF-8") + "=" + URLEncoder.encode(endDateFormated, "UTF-8");
                        bufferedWriter.write(post_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                        //String result = "";
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                    }
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals(("getCleaners"))) {

            try {
                String selectedDateStart = params[1].toString();
                String selectedDateEnd = params[2].toString();
                URL url = new URL(getCleaners_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("selectedDateStart", "UTF-8") + "=" + URLEncoder.encode(selectedDateStart, "UTF-8")
                        + "&" + URLEncoder.encode("selectedDateEnd", "UTF-8") + "=" + URLEncoder.encode(selectedDateEnd, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}