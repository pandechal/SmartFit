package com.example.smartfit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment displaying a list of workouts fetched from the Gemini API.
 */
public class WorkoutFragment extends Fragment {

    private static final String TAG = "WorkoutFragment";
    private WorkoutAdapter workoutAdapter;
    private RecyclerView recyclerView;
    private EditText searchWorkout;

    // --- Data Model for Gemini's Structured JSON Response ---
    // This MUST match the JSON schema defined in the previous React example.
    public static class Workout {
        @SerializedName("id")
        public int id;
        @SerializedName("category")
        public String category;
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
        @SerializedName("metrics")
        public String metrics;
        @SerializedName("bodyPart")
        public String bodyPart; // Used for filtering (e.g., "Upper Body", "Lower Body", "All")
    }

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        // 1. Initialize RecyclerView
        recyclerView = view.findViewById(R.id.workoutRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        workoutAdapter = new WorkoutAdapter(new ArrayList<>());
        recyclerView.setAdapter(workoutAdapter);

        // 2. Initialize Search Bar and Filters
        searchWorkout = view.findViewById(R.id.searchWorkout);
        setupFilterButtons(view);

        // 3. Kick off data fetching
        fetchWorkoutsFromGemini();

        return view;
    }

    private void setupFilterButtons(View view) {
        // Assuming the filter IDs from the XML
        TextView categoryAll = view.findViewById(R.id.categoryAll);
        TextView categoryUpper = view.findViewById(R.id.categoryUpper);
        TextView categoryLower = view.findViewById(R.id.categoryLower);

        categoryAll.setOnClickListener(v -> filterWorkouts("All"));
        categoryUpper.setOnClickListener(v -> filterWorkouts("Upper Body"));
        categoryLower.setOnClickListener(v -> filterWorkouts("Lower Body"));

        // You would also implement a TextWatcher on searchWorkout to filter on input change.
    }

    private void filterWorkouts(String bodyPart) {
        // This method would typically interact with a ViewModel to apply a filter
        // and refresh the RecyclerView data.
        Toast.makeText(getContext(), "Filtering by: " + bodyPart, Toast.LENGTH_SHORT).show();
        // Example: workoutAdapter.filter(bodyPart, searchWorkout.getText().toString());
    }


    /**
     * Placeholder for the asynchronous network call to the Gemini API.
     * This uses Gson for parsing the structured JSON response.
     * NOTE: This is a synchronous placeholder. In a real app, use Retrofit/OkHttp
     * in an AsyncTask, Coroutine, or ViewModel.
     */
    private void fetchWorkoutsFromGemini() {
        Log.d(TAG, "Starting Gemini API call...");

        // --- MOCK API RESPONSE (for demonstration) ---
        // In a real app, this String would be the JSON response body from the API.
        // The structured generation ensures the response is consistent and predictable.
        String mockJsonResponse = "[{\"id\":1,\"category\":\"High Intensity\",\"title\":\"Full Body HIIT\",\"description\":\"Burn calories fast with this intense full-body workout.\",\"metrics\":\"450 cal \\u2022 12 exercises\",\"bodyPart\":\"All\"},{\"id\":2,\"category\":\"Strength\",\"title\":\"Upper Body Power\",\"description\":\"Build strength in arms, chest, and back.\",\"metrics\":\"320 cal \\u2022 10 exercises\",\"bodyPart\":\"Upper Body\"},{\"id\":3,\"category\":\"Flexibility\",\"title\":\"Morning Yoga Flow\",\"description\":\"A gentle routine to improve mobility and reduce stiffness.\",\"metrics\":\"150 cal \\u2022 8 poses\",\"bodyPart\":\"All\"},{\"id\":4,\"category\":\"Endurance\",\"title\":\"Leg Day Blitz\",\"description\":\"Targeted exercises for powerful and toned lower body muscles.\",\"metrics\":\"400 cal \\u2022 9 exercises\",\"bodyPart\":\"Lower Body\"},{\"id\":5,\"category\":\"Core Focus\",\"title\":\"Abdominal Strength Challenge\",\"description\":\"A short, intense routine focused entirely on core stability.\",\"metrics\":\"180 cal \\u2022 6 exercises\",\"bodyPart\":\"Core\"}]";

        try {
            // Use Gson to parse the JSON string into a List of Workout objects
            Gson gson = new Gson();
            Type workoutListType = new TypeToken<ArrayList<Workout>>(){}.getType();
            List<Workout> workouts = gson.fromJson(mockJsonResponse, workoutListType);

            // Update the RecyclerView with the data
            workoutAdapter.updateWorkouts(workouts);
            Log.d(TAG, "Successfully parsed and displayed " + workouts.size() + " workouts.");

        } catch (Exception e) {
            Log.e(TAG, "Error parsing Gemini response or updating UI: " + e.getMessage());
            // Show user-friendly error message
            if (getContext() != null) {
                Toast.makeText(getContext(), "Failed to load workouts.", Toast.LENGTH_LONG).show();
            }
        }
    }
}