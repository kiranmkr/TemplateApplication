package com.example.testingapplication.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class AbsoluteLayout{
    @JsonProperty("ImageView")
    public ArrayList<ImageView> imageView;
    @JsonProperty("TextView")
    public ArrayList<TextView> textView;
    @JsonProperty("_xmlns:android")
    public String xmlnsAndroid;
    @JsonProperty("_xmlns:app")
    public String xmlnsApp;
    @JsonProperty("_android:layout_width")
    public String androidLayoutWidth;
    @JsonProperty("_android:layout_height")
    public String androidLayoutHeight;
    @JsonProperty("_android:background")
    public String androidBackground;
}
