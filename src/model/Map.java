
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Map {

    @SerializedName("player_id")
    @Expose
    private String playerId;
    @SerializedName("map_info")
    @Expose
    private MapInfo mapInfo;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("direction")
    @Expose
    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    public void setMapInfo(MapInfo mapInfo) {
        this.mapInfo = mapInfo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
