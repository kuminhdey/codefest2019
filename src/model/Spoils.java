
package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spoils {

    @SerializedName("myArrayList")
    @Expose
    public List<SpoilsList> myArrayList = null;

}
