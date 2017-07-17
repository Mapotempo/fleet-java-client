package com.mapotempo.fleet.core.accessor;

import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Analyzer.
 */
class Analyzer<T> {

    private Class<T> mClazz;

    private DocumentBase mDocumentAnnotation;

    private Field mIdField;

    /**
     * Analyzer.
     * @param clazz the class type to analyze (should be conform).
     * @throws CoreException Mapotempo exception.
     */
    Analyzer(Class<T> clazz) throws CoreException {
        mClazz = clazz;
        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);
        analyzeType();
    }

    /**
     * analyzeType.
     * Analyse du type template :
     *  - Vérification de l'annotation DocumentBase
     *  - Vérification des champs doublons
     *  - Vérification de l'existance de la clef primaire.
     *  - To Completed.
     */
    private void analyzeType() throws CoreException {
        // Definition de l'annotationd de l'annotation
        if (mDocumentAnnotation == null) {
            throw new CoreException("In Class : " + mClazz.getName() + ", annotation DocumentBase is not defined.");
        }

        boolean primary = false;

        // Champ en doublon
        Map<String, String> mapData = new HashMap<String, String>();
        for (Field field : mClazz.getFields()) {
            FieldBase baseField = field.getAnnotation(FieldBase.class);
            if (baseField != null) {
                if(baseField.name().equals("_id" ) && field.getType().equals(String.class)) {
                    mIdField = field;
                    primary = true;
                }

                if(mapData.get(baseField.name()) != null) {
                    throw new CoreException("In Class : " + mClazz.getName() + ", FieldBase : " + baseField.name() + " already defined.");
                } else {
                    mapData.put(baseField.name(), baseField.name());
                }
            }
        }

        // Definition de la clef primaire.
        if(!primary)
            throw new CoreException("In Class : " + mClazz.getName() + ", no primary key '_id' found.");
    }

    public Map<String, Object> getData(T data) throws CoreException
    {
        Map<String, Object> mapData = new HashMap<String, Object>();
        for(Field field : mClazz.getFields()) {
            FieldBase baseField = field.getAnnotation(FieldBase.class);
            if (baseField != null) {
                try {
                    Object value = field.get(data);
                    if(value != null) {
                        if(baseField.foreign()) {
                            // Creation d'une instance d'analyze pour verifier que la foreign key est bien un type conforme.
                            Analyzer analyzer = new Analyzer(value.getClass());
                            mapData.put(baseField.name(), mIdField.get(value));
                        }
                        else if(value instanceof SubModelBase) {
                            SubModelBase base = (SubModelBase)value;
                            //mapData.putAll(base.toMap());
                            mapData.put(baseField.name(), base.toMap());
                        }
                        else if(IsBaseType(field.getType())){
                            mapData.put(baseField.name(), value);
                        }
                        else {
                            // NOTHING
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        mapData.put(mDocumentAnnotation.type_field(), mDocumentAnnotation.type());
        return mapData;
    }

    /** IsBaseType.
     * Base type : Boolean/boolean, Byte/byte, Short/short, Integer/int, Long/lon, Float/float, Double/double
     * @param clazz Clazz to analyze
     * @return true if clazz is base type
     */
    private static boolean IsBaseType(Class clazz) {
        if( Boolean.class == clazz || boolean.class == clazz) return true;
        if( Byte.class == clazz || byte.class == clazz) return true;
        if( Short.class == clazz || short.class == clazz) return true;
        if( Integer.class == clazz || int.class == clazz) return true;
        if( Long.class == clazz || long.class == clazz) return true;
        if( Float.class == clazz || float.class == clazz) return true;
        if( Double.class == clazz || double.class == clazz) return true;
        if( String.class == clazz) return true;
        return false;
    }
}
