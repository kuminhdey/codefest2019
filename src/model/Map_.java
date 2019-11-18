
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Map_ {

    @SerializedName("myId")
    @Expose
    public String myId;
    @SerializedName("size")
    @Expose
    public Size size;
    @SerializedName("spoils")
    @Expose
    public Spoils spoils;
    @SerializedName("players")
    @Expose
    public Players players;
    @SerializedName("gameStatus")
    @Expose
    public GameStatus gameStatus;
    @SerializedName("bombs")
    @Expose
    public Bombs bombs;
    @SerializedName("map")
    @Expose
    public Map______ map;

}
