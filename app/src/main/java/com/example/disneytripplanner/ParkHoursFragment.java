package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.disneytripplanner.databinding.FragmentParkHoursBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
    ParkHoursFragment.ParkHoursFragmentListener mListener;
    FragmentParkHoursBinding binding;
    private final OkHttpClient client = new OkHttpClient();

    ArrayList<ParkHours> epcotHours = new ArrayList<>();
    ArrayList<ParkHours> dakHours = new ArrayList<>();
    ArrayList<ParkHours> mkHours = new ArrayList<>();
    ArrayList<ParkHours> hollywoodStudiosHours = new ArrayList<>();

    String todaysDate;
    String dateSelected;
    SimpleDateFormat dateToTextFormatter = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ssZ");

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
                            epcotHours.add(parkHours);
                        }
                        Log.d(TAG, "EPCOT PARK HOURS ==================>>>>>> " + epcotHours.toString());
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
                            dakHours.add(parkHours);
                        }
                        Log.d(TAG, "ANIMAL KINGDOM HOURS ==================>>>>>> " + dakHours.toString());
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
                            mkHours.add(parkHours);
                        }
                        Log.d(TAG, "MAGIC KINGDOM HOURS ==================>>>>>> " + mkHours.toString());
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
                            hollywoodStudiosHours.add(parkHours);
                        }
                        Log.d(TAG, "HOLLYWOOD STUDIOS HOURS ==================>>>>>> " + hollywoodStudiosHours.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Error getting HOLLYWOOD STUDIOS PARK HOURS data --- " + response.body());
                }
            }
        });
    }

    void getTodaysDate() {
        Date date = new Date();
        String formattedDate = dateFormatter.format(date);
        todaysDate = formattedDate;
        Log.d(TAG, " TODAYS DATE: " + formattedDate);
    }

    public void setDateSelected(String date) {
        dateSelected = date;
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

        setupUI();

        return binding.getRoot();
    }

    void setupUI() {
        //getTodaysDate();
        getEpcotHours();
        getDAKHours();
        getMKHours();
        getHollywoodStudiosHours();

        binding.calendarViewParkHours.setMinDate(new Date().getTime());

        Calendar rightNow = Calendar.getInstance();

        binding.calendarViewParkHours.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String sDayOfWeek = dayOfWeekToString(dayOfWeek);
                String sDate = sdf.format(calendar.getTime());
                String dateString = String.format("%s, %s", sDayOfWeek, sDate);
                //String dateStr = dateToTextFormatter.format(calendar);
                Log.d(TAG, "formatted date string: " + sDate);
                setDateSelected(dateString);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ParkHoursFragment.ParkHoursFragmentListener) context;
    }

    interface ParkHoursFragmentListener {
        void goToHomePage();
    }
}