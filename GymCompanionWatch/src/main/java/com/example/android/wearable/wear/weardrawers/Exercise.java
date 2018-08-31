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

/**
 * Represents planet for app.
 */
public class Exercise {

    private String name;
    private String navigationIcon;
    private String image;
    private String numberofsets;
    private String numberofrepetitions;
    private String weights;
    private String whereorhow;

    public Exercise(
            String name,
            String navigationIcon,
            String image,
            String numberofsets,
            String numberofrepetitions,
            String weights,
            String whereorhow
    ) {

        this.name = name;
        this.navigationIcon = navigationIcon;
        this.image = image;
        this.numberofsets = numberofsets;
        this.numberofrepetitions = numberofrepetitions;
        this.weights = weights;
        this.whereorhow = whereorhow;
    }

    public String getName() {
        return name;
    }

    public String getNavigationIcon() {
        return navigationIcon;
    }

    public String getImage() {
        return image;
    }

    public String getNumberofsets() {
        return numberofsets;
    }

    public String getNumberofrepetitions() { return numberofrepetitions; }

    public String getWeights() { return weights; }

    public String getWhereorhow() { return whereorhow; }
}