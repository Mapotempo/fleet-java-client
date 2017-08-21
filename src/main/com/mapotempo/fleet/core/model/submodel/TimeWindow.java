package com.mapotempo.fleet.core.model.submodel;

import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.utils.DateHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeWindow extends SubModelBase
{
    // MAPOTEMPO KEY
    public static final String START = "start";
    public static final String END = "end";

    private Date mStart;
    private Date mEnd;

    public TimeWindow(Map map) {
        super(map);
    }

    /**
     * TimeWindow.
     * @param start start time
     * @param end end time
     */
    public TimeWindow(Date start, Date end) {
        this.mStart = start;
        this.mEnd = end;
    }

    @Override
    public void fromMap(Map map) {
        // TODO FIXME CHECK ISO
        String str = (String)map.get(START);
        this.mStart = DateHelper.fromStringISO8601(str);
        str = (String)map.get(END);
        this.mStart = DateHelper.fromStringISO8601(str);
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put(START, DateHelper.toStringISO8601(mStart));
        res.put(END, DateHelper.toStringISO8601(mEnd));
        return res;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            if(this.mStart == ((TimeWindow)obj).mStart)
                if(this.mEnd == ((TimeWindow)obj).mEnd)
                    return true;
        return false;
    }

    @Override
    public String toString() {
        return "(" + mStart + " ; " + mEnd + ")";
    }
}
