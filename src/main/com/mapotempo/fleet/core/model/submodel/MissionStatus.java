package com.mapotempo.fleet.core.model.submodel;

import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

public class MissionStatus extends SubModelBase {

    // MAPOTEMPO KEY
    public static final String LABEL = "label";
    public static final String COLOR = "color";

    private String mLabel;

    private int mColor;

    /**
     * MissionStatus
     * @param map
     */
    public MissionStatus(Map map) {
        super(map);
    }

    /**
     * MissionStatus.
     * @param label label
     * @param color color
     */
    public MissionStatus(String label, int color) {
        this.mLabel = label;
        this.mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public String getLabel() {
        return mLabel;
    }

    @Override
    public void fromMap(Map map) {
        this.mLabel = map.get(LABEL).toString();
        this.mColor = Integer.parseInt(map.get(COLOR).toString(), 16);
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put(LABEL, mLabel);
        res.put(COLOR, Integer.toHexString(mColor));
        return res;
    }
}
