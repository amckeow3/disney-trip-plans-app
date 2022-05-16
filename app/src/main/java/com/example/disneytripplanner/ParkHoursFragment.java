package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.disneytripplanner.databinding.FragmentParkHoursBinding;
import com.example.disneytripplanner.models.ParkHours;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ParkHoursFragment extends Fragment {
    private static final String TAG = "park hours fragment";
    ParkHoursFragmentListener mListener;
    FragmentParkHoursBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<ParkHours> epcotHours = new ArrayList<>();
    ArrayList<ParkHours> dakHours = new ArrayList<>();
    ArrayList<ParkHours> mkHours = new ArrayList<>();
    ArrayList<ParkHours> hollywoodStudiosHours = new ArrayList<>();
    ArrayList<ParkHours> allParkHours = new ArrayList<>();
    ArrayList<ParkHours> matchingParkHours = new ArrayList<>();
    String todaysDate;
    String dateSelected;
    Date dateToMatch;
    Date epcotOpeningTime;
    Date epcotClosingTime;
    Date dakOpeningTime;
    Date dakClosingTime;
    Date mkOpeningTime;
    Date mkClosingTime;
    Date hsOpeningTime;
    Date hsClosingTime;

    final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ssZ");
    SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");

    void getEpcotHours() {
        String slug = "WaltDisneyWorldEpcot";
        Request request = new Request.Builder()
                .url("https://api.themeparks.wiki/preview/parks/" + slug + "/calendar")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    epcotHours.clear();

                    try {
                        JSONArray jsonArray = new JSONArray(body);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hoursJsonObject = jsonArray.getJSONObject(i);
                            ParkHours parkHours = new ParkHours();
                            parkHours.setDate(hoursJsonObject.getString("date"));
                            parkHours.setOpeningTime(hoursJsonObject.getString("openingTime"));
                            parkHours.setClosingTime(hoursJsonObject.getString("closingTime"));
                            parkHours.setPark("Epcot");
                            epcotHours.add(parkHours);
                        }
                        getEpcotHoursForDate(epcotHours);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Error getting EPCOT PARK HOURS data --- " + response.body());
                }
            }
        });

    }

    void getDAKHours() {
        String slug = "WaltDisneyWorldAnimalKingdom";

        Request request = new Request.Builder()
                .url("https://api.themeparks.wiki/preview/parks/" + slug + "/calendar")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    dakHours.clear();

                    try {
                        JSONArray jsonArray = new JSONArray(body);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hoursJsonObject = jsonArray.getJSONObject(i);
                            ParkHours parkHours = new ParkHours();
                            parkHours.setDate(hoursJsonObject.getString("date"));
                            parkHours.setOpeningTime(hoursJsonObject.getString("openingTime"));
                            parkHours.setClosingTime(hoursJsonObject.getString("closingTime"));
                            parkHours.setPark("Animal Kingdom");
                            dakHours.add(parkHours);
                        }
                        getDAKHoursForDate(dakHours);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Error getting ANIMAL KINGDOM PARK HOURS data --- " + response.body());
                }
            }
        });

    }

    void getMKHours() {
        String slug = "WaltDisneyWorldMagicKingdom";

        Request request = new Request.Builder()
                .url("https://api.themeparks.wiki/preview/parks/" + slug + "/calendar")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    mkHours.clear();

                    try {
                        JSONArray jsonArray = new JSONArray(body);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hoursJsonObject = jsonArray.getJSONObject(i);
                            ParkHours parkHours = new ParkHours();
                            parkHours.setDate(hoursJsonObject.getString("date"));
                            parkHours.setOpeningTime(hoursJsonObject.getString("openingTime"));
                            parkHours.setClosingTime(hoursJsonObject.getString("closingTime"));
                            parkHours.setPark("Magic Kingdom");
                            mkHours.add(parkHours);
                        }
                        getMKHoursForDate(mkHours);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Error getting MAGIC KINGDOM PARK HOURS data --- " + response.body());
                }
            }
        });
    }

    void getHollywoodStudiosHours() {
        String slug = "WaltDisneyWorldHollywoodStudios";

        Request request = new Request.Builder()
                .url("https://api.themeparks.wiki/preview/parks/" + slug + "/calendar")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    hollywoodStudiosHours.clear();

                    try {
                        JSONArray jsonArray = new JSONArray(body);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hoursJsonObject = jsonArray.getJSONObject(i);
                            ParkHours parkHours = new ParkHours();
                            parkHours.setDate(hoursJsonObject.getString("date"));
                            parkHours.setOpeningTime(hoursJsonObject.getString("openingTime"));
                            parkHours.setClosingTime(hoursJsonObject.getString("closingTime"));
                            parkHours.setPark("Hollywood Studios");
                            hollywoodStudiosHours.add(parkHours);
                        }
                        getHollywoodStudiosHoursForDate(hollywoodStudiosHours);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Error getting HOLLYWOOD STUDIOS PARK HOURS data --- " + response.body());
                }
            }
        });
    }

    public void getHoursForAllParks() {
        getEpcotHours();
        getDAKHours();
        getMKHours();
        getHollywoodStudiosHours();
    }

    public ParkHoursFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParkHoursBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    void setupUI() {
        Calendar calendar = Calendar.getInstance();

        Date todaysDate = calendar.getTime();
        String todaysDateformatted = sdf.format(todaysDate);
        setTodaysDate(todaysDateformatted);

        getHoursForAllParks();
    }

    void setTodaysDate(String date) {
        todaysDate = date;
    }

    /*
    void setupUI() {
        getTodaysDate();
        binding.calendarViewParkHours.setMinDate(new Date().getTime());

        binding.calendarViewParkHours.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String sDayOfWeek = dayOfWeekToString(dayOfWeek);
                String sDate = sdf.format(calendar.getTime());
                String dateString = String.format("%s, %s", sDayOfWeek, sDate);
                Log.d(TAG, "formatted date string: " + sDate);
                getHoursForAllParks();
                setDateSelected(sDate);
                binding.textViewSelectedDate.setText(dateString);
            }
        });
        
        binding.buttonBackFromParkHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToHomePage();
            }
        });
    }



    void getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, month, dayOfMonth);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String sDayOfWeek = dayOfWeekToString(dayOfWeek);
        String sDate = sdf.format(calendar.getTime());
        String dateString = String.format("%s, %s", sDayOfWeek, sDate);
        getHoursForAllParks();
        setDateSelected(sDate);
        binding.textViewSelectedDate.setText(dateString);
    }

    public void setDateSelected(String sDate) {
        dateSelected = sDate;
    }
    
    String dayOfWeekToString(int dayOfWeek) {
        String dayOfWeekString;
        switch(dayOfWeek) {
            case 1: {
                dayOfWeekString = "Sunday";
                break;
            } case 2: {
                dayOfWeekString = "Monday";
                break;
            } case 3: {
                dayOfWeekString = "Tuesday";
                break;
            } case 4: {
                dayOfWeekString = "Wednesday";
                break;
            } case 5: {
                dayOfWeekString = "Thursday";
                break;
            } case 6: {
                dayOfWeekString = "Friday";
                break;
            } case 7: {
                dayOfWeekString = "Saturday";
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
        return dayOfWeekString;
    }

     */

    void getEpcotHoursForDate(ArrayList<ParkHours> epcot) {
       for (int i = 0; i < epcot.size(); i++) {
           ParkHours epcotHours = epcot.get(i);
           String sDate = epcotHours.getDate();
           try {
               DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
               Date parsedDate = inputDateFormat.parse(sDate);
               String formattedInputDate = sdf.format(parsedDate);
               if (formattedInputDate.equals(todaysDate)) {
                   Log.d(TAG, "epcotHoursForSelectedDay: " + parsedDate);
                   epcotOpeningTime = dateTimeFormatter.parse(epcotHours.getOpeningTime());
                   epcotClosingTime = dateTimeFormatter.parse(epcotHours.getClosingTime());
                   String formattedOpening = formatTime.format(epcotOpeningTime);
                   String formattedClosing = formatTime.format(epcotClosingTime);
                   Log.d(TAG, "Epcot: " + formattedOpening + " to " + formattedClosing);
                   if (getActivity() != null) {
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               binding.textViewEpcotParkHours.setText(formattedOpening + " to " + formattedClosing);
                           }
                       });
                   }
               }
           } catch (ParseException e) {
               e.printStackTrace();
           }
       }

    }

    void getDAKHoursForDate(ArrayList<ParkHours> dak) {
        for (int i = 0; i < dak.size(); i++) {
            ParkHours dakHours = dak.get(i);
            String sDate = dakHours.getDate();
            try {
                DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = inputDateFormat.parse(sDate);
                String formattedInputDate = sdf.format(parsedDate);
                if (formattedInputDate.equals(todaysDate)) {
                    Log.d(TAG, "dakHoursForSelectedDay: " + parsedDate);
                    dakOpeningTime = dateTimeFormatter.parse(dakHours.getOpeningTime());
                    dakClosingTime = dateTimeFormatter.parse(dakHours.getClosingTime());
                    String formattedOpening = formatTime.format(dakOpeningTime);
                    String formattedClosing = formatTime.format(dakClosingTime);
                    Log.d(TAG, "DAK: " + formattedOpening + " to " + formattedClosing);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewDAKParkHours.setText(formattedOpening + " to " + formattedClosing);
                            }
                        });
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    void getMKHoursForDate(ArrayList<ParkHours> mk) {
        for (int i = 0; i < mk.size(); i++) {
            ParkHours mkHours = mk.get(i);
            String sDate = mkHours.getDate();
            try {
                DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = inputDateFormat.parse(sDate);
                String formattedInputDate = sdf.format(parsedDate);
                if (formattedInputDate.equals(todaysDate)) {
                    Log.d(TAG, "mkHoursForSelectedDay: " + parsedDate);
                    mkOpeningTime = dateTimeFormatter.parse(mkHours.getOpeningTime());
                    mkClosingTime = dateTimeFormatter.parse(mkHours.getClosingTime());
                    String formattedOpening = formatTime.format(mkOpeningTime);
                    String formattedClosing = formatTime.format(mkClosingTime);
                    Log.d(TAG, "MK: " +  formattedOpening + " to " + formattedClosing);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewMKParkHours.setText(formattedOpening + " to " + formattedClosing);
                            }
                        });
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    void getHollywoodStudiosHoursForDate(ArrayList<ParkHours> hollywoodStudios) {
        for (int i = 0; i < hollywoodStudios.size(); i++) {
            ParkHours hsHours = hollywoodStudios.get(i);
            String sDate = hsHours.getDate();
            try {
                DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = inputDateFormat.parse(sDate);
                String formattedInputDate = sdf.format(parsedDate);
                if (formattedInputDate.equals(todaysDate)) {
                    Log.d(TAG, "hollywoodHoursForSelectedDay: " + parsedDate);
                    hsOpeningTime = dateTimeFormatter.parse(hsHours.getOpeningTime());
                    hsClosingTime = dateTimeFormatter.parse(hsHours.getClosingTime());
                    String formattedOpening = formatTime.format(hsOpeningTime);
                    String formattedClosing = formatTime.format(hsClosingTime);
                    Log.d(TAG, "Hollywood: " + formattedOpening + " to " + formattedClosing);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewStudiosParkHours.setText(formattedOpening + " to " + formattedClosing);
                            }
                        });
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ParkHoursFragmentListener) context;
    }

    interface ParkHoursFragmentListener {
        void goToHomePage();
    }
}