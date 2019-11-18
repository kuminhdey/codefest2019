package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Drive_Player {
    @SerializedName("myArrayList")
    @Expose
    public List<Object> myArrayList = null;
}
