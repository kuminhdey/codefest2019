
package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Players {

    @SerializedName("myArrayList")
    @Expose
    public List<MyArrayList> myArrayList = null;

}
