package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.utils.DateHelper;
import com.mapotempo.fleet.core.base.FieldBase;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * ModelBase.
 */
public class ModelBase {
    @Override
    public String toString() {
        String res = this.getClass().getSimpleName();

        for(Field field : this.getClass().getFields()) {
            FieldBase baseField = field.getAnnotation(FieldBase.class);
            if (baseField != null) {
                try {
                    res +=  "\n";
                    if (field.getType().equals(Date.class)) {
                        Date date = (Date)field.get(this);
                        res = res + "        " + String.format("%-20s %s" ,baseField.name().toUpperCase(), ": " + DateHelper.displayDate(date));
                    }
                    else if(baseField.foreign()) {
                        res = res + "        " + String.format("%-20s %s", baseField.name().toUpperCase(), ":\n");
                        res = res + "        {\n" + String.format("%-20s", field.get(this));
                        res = res + "\n        }";
                    }
                    else
                        res = res + "        " + String.format("%-20s %s" ,baseField.name().toUpperCase(), ": " + field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    @FieldBase(name = "_id")
    public String mId;

    @FieldBase(name = "_rev")
    public String mRef;

    @FieldBase(name = "owner")
    public String mOwner;

    @Override
    public boolean equals(Object obj) {
        // On ne compare pas les references dans cette fonction.
        if(this.mId.equals(((ModelBase)obj).mId))
            return true;
        return false;
    }
}
