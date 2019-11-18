package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Booms {
    @SerializedName("col")
    @Expose
    private Integer col;
    @SerializedName("remainTime")
    @Expose
    private Integer remainTime;
    @SerializedName("row")
    @Expose
    private Integer row;
    @SerializedName("playerId")
    @Expose
    private String playerId;

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(Integer remainTime) {
        this.remainTime = remainTime;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
