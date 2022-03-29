package com.example.testingapplication.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ImageView{
    @JsonProperty("_android:id")
    public String androidId;
    @JsonProperty("_android:layout_width")
    public String androidLayoutWidth;
    @JsonProperty("_android:layout_height")
    public String androidLayoutHeight;
    @JsonProperty("_app:srcCompat")
    public String appSrcCompat;
    @JsonProperty("_android:layout_x")
    public String androidLayoutX;
    @JsonProperty("_android:layout_y")
    public String androidLayoutY;
    @JsonProperty("_android:rotation")
    public String androidRotation;
    @JsonProperty("_android:background")
    public String androidBackground;
    @JsonProperty("_android:alpha")
    public String androidAlpha;
}