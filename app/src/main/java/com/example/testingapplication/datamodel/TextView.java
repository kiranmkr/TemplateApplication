package com.example.testingapplication.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TextView {
    @JsonProperty("_android:id")
    public String androidId;
    @JsonProperty("_android:layout_width")
    public String androidLayoutWidth;
    @JsonProperty("_android:layout_height")
    public String androidLayoutHeight;
    @JsonProperty("_android:layout_x")
    public String androidLayoutX;
    @JsonProperty("_android:layout_y")
    public String androidLayoutY;
    @JsonProperty("_android:background")
    public String androidBackground;
    @JsonProperty("_android:letterSpacing")
    public String androidLetterSpacing;
    @JsonProperty("_android:rotation")
    public String androidRotation;
    @JsonProperty("_android:text")
    public String androidText;
    @JsonProperty("_android:textColor")
    public String androidTextColor;
    @JsonProperty("_android:textSize")
    public String androidTextSize;
    @JsonProperty("_android:textAlignment")
    public String androidTextAlignment;
    @JsonProperty("_android:alpha")
    public String androidAlpha;
    @JsonProperty("_android:fontFamily")
    public String androidFontFamily;
}
