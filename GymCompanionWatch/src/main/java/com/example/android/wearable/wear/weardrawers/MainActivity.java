/*
Copyright 2016 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.example.android.wearable.wear.weardrawers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wear.ambient.AmbientModeSupport;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Demonstrates use of Navigation and Action Drawers on Wear.
 */
public class MainActivity extends FragmentActivity implements
        AmbientModeSupport.AmbientCallbackProvider,
        MenuItem.OnMenuItemClickListener,
        WearableNavigationDrawerView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";

    private WearableNavigationDrawerView mWearableNavigationDrawer;
    private WearableActionDrawerView mWearableActionDrawer;

    private ArrayList<Exercise> mExercisesList;
    private int mSelectedPlanet;

    private PlanetFragment mPlanetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        // Enables Ambient mode.
        AmbientModeSupport.attach(this);

        mExercisesList = initializeExercisesList();
        mSelectedPlanet = 0;

        // Initialize content to first planet.
        mPlanetFragment = new PlanetFragment();
        Bundle args = new Bundle();

        int imageId = getResources().getIdentifier(mExercisesList.get(mSelectedPlanet).getImage(),
                "drawable", getPackageName());


        args.putInt(PlanetFragment.ARG_PLANET_IMAGE_ID, imageId);
        mPlanetFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mPlanetFragment).commit();


        // Top Navigation Drawer
        mWearableNavigationDrawer =
                (WearableNavigationDrawerView) findViewById(R.id.top_navigation_drawer);
        mWearableNavigationDrawer.setAdapter(new NavigationAdapter(this));
        // Peeks navigation drawer on the top.
        mWearableNavigationDrawer.getController().peekDrawer();
        mWearableNavigationDrawer.addOnItemSelectedListener(this);

        // Bottom Action Drawer
        mWearableActionDrawer =
                (WearableActionDrawerView) findViewById(R.id.bottom_action_drawer);
        // Peeks action drawer on the bottom.
        mWearableActionDrawer.getController().peekDrawer();
        mWearableActionDrawer.setOnMenuItemClickListener(this);

        /* Action Drawer Tip: If you only have a single action for your Action Drawer, you can use a
         * (custom) View to peek on top of the content by calling
         * mWearableActionDrawer.setPeekContent(View). Make sure you set a click listener to handle
         * a user clicking on your View.
         */
    }

    private ArrayList<Exercise> initializeExercisesList() {
        ArrayList<Exercise> exercisesList = new ArrayList<Exercise>();

        //String currentWorkoutStyle = "push";
        String[] exerciseArrayNames = getResources().getStringArray(R.array.workout_exercises_push);
        //if (currentWorkoutStyle=="push") {
        //    exerciseArrayNames = getResources().getStringArray(R.array.workout_exercises_push);
        //}

        for (int i = 0; i < exerciseArrayNames.length; i++) {
            String exercise = exerciseArrayNames[i];
            int exerciseResourceId =
                    getResources().getIdentifier(exercise, "array", getPackageName());
            String[] exerciseInformation = getResources().getStringArray(exerciseResourceId);

            exercisesList.add(new Exercise(
                    exerciseInformation[0],   // Name of Exercise
                    exerciseInformation[1],   // Navigation icon
                    exerciseInformation[2],   // Image icon
                    String.valueOf(exerciseInformation[3]),   // Number of sets
                    String.valueOf(exerciseInformation[4]),   // Number of repetitions
                    String.valueOf(exerciseInformation[5]),   // Weight
                    exerciseInformation[6]    // Where or how to practice
            ));


        }
        //TEST
        //((Button)findViewById(R.id.menu_exercise_name)).setText(mExercisesList.get(mSelectedPlanet).getName());
        return exercisesList;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuItemClick(): " + menuItem);

        final int itemId = menuItem.getItemId();

        String toastMessage = "";

        switch (itemId) {
            case R.id.menu_exercise_name:
                toastMessage = mExercisesList.get(mSelectedPlanet).getName();
                break;
            case R.id.menu_number_of_sets:
                toastMessage = mExercisesList.get(mSelectedPlanet).getNumberofsets();
                break;
            case R.id.menu_number_of_repetitions:
                toastMessage = mExercisesList.get(mSelectedPlanet).getNumberofrepetitions();
                break;
            case R.id.menu_weights:
                toastMessage = mExercisesList.get(mSelectedPlanet).getWeights();
                break;
        }

        mWearableActionDrawer.getController().closeDrawer();

        if (toastMessage.length() > 0) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    toastMessage,
                    Toast.LENGTH_SHORT);
            toast.show();
            return true;
        } else {
            return false;
        }
    }

    // Updates content when user changes between items in the navigation drawer.
    @Override
    public void onItemSelected(int position) {
        Log.d(TAG, "WearableNavigationDrawerView triggered onItemSelected(): " + position);
        mSelectedPlanet = position;

        String selectedPlanetImage = mExercisesList.get(mSelectedPlanet).getImage();
        int drawableId =
                getResources().getIdentifier(selectedPlanetImage, "drawable", getPackageName());
        mPlanetFragment.updatePlanet(drawableId);
    }

    private final class NavigationAdapter
            extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {

        private final Context mContext;

        public NavigationAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mExercisesList.size();
        }

        @Override
        public String getItemText(int pos) {
            return mExercisesList.get(pos).getName();
        }

        @Override
        public Drawable getItemDrawable(int pos) {
            String navigationIcon = mExercisesList.get(pos).getNavigationIcon();

            int drawableNavigationIconId =
                    getResources().getIdentifier(navigationIcon, "drawable", getPackageName());

            return mContext.getDrawable(drawableNavigationIconId);
        }
    }

    /**
     * Fragment that appears in the "content_frame", just shows the currently selected planet.
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_IMAGE_ID = "planet_image_id";

        private ImageView mImageView;
        private ColorFilter mImageViewColorFilter;

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);

            mImageView = rootView.findViewById(R.id.image);

            int imageIdToLoad = getArguments().getInt(ARG_PLANET_IMAGE_ID);
            mImageView.setImageResource(imageIdToLoad);

            mImageViewColorFilter = mImageView.getColorFilter();

            return rootView;
        }

        public void updatePlanet(int imageId) {
            mImageView.setImageResource(imageId);
        }

        public void onEnterAmbientInFragment(Bundle ambientDetails) {
            Log.d(TAG, "PlanetFragment.onEnterAmbient() " + ambientDetails);

            // Convert image to grayscale for ambient mode.
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            mImageView.setColorFilter(filter);
        }

        /** Restores the UI to active (non-ambient) mode. */
        public void onExitAmbientInFragment() {
            Log.d(TAG, "PlanetFragment.onExitAmbient()");

            mImageView.setColorFilter(mImageViewColorFilter);
        }
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
    }

    private class MyAmbientCallback extends AmbientModeSupport.AmbientCallback {
        /** Prepares the UI for ambient mode. */
        @Override
        public void onEnterAmbient(Bundle ambientDetails) {
            super.onEnterAmbient(ambientDetails);
            Log.d(TAG, "onEnterAmbient() " + ambientDetails);

            mPlanetFragment.onEnterAmbientInFragment(ambientDetails);
            mWearableNavigationDrawer.getController().closeDrawer();
            mWearableActionDrawer.getController().closeDrawer();
        }

        /** Restores the UI to active (non-ambient) mode. */
        @Override
        public void onExitAmbient() {
            super.onExitAmbient();
            Log.d(TAG, "onExitAmbient()");

            mPlanetFragment.onExitAmbientInFragment();
            mWearableActionDrawer.getController().peekDrawer();
        }
    }
}
