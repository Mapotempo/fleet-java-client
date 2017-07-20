package com.mapotempo.fleet.core.accessor;

import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.utils.DateHelper;

import java.lang.reflect.*;
import java.util.*;

/**
 * Factory.
 */
class Factory<T> {

    private DatabaseHandler mDatabaseHandler;

    private Class<T> mClazz;

    private DocumentBase mDocumentAnnotation;

    private Constructor<T> mConstructor;

    /**
     * Factory.
     * @param clazz The class
     * @param databaseHandler The databaseHandler for subobject
     * @throws CoreException
     */
    Factory(Class<T> clazz, DatabaseHandler databaseHandler) throws CoreException {

        mDatabaseHandler = databaseHandler;

        mClazz = clazz;

        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);

        if(mDocumentAnnotation == null)
            throw new CoreException("e");
        try {
            mConstructor = mClazz.getConstructor();
        } catch (NoSuchMethodException e) {
            System.err.println("In Class : " + mClazz.getName() + ", no default constructor define.");
            throw new CoreException(e);
        }
    }

    /**
     * TODO process Array
     * @param document document entry
     * @return an instance of T
     * @throws CoreException
     */
    public T getInstance(Document document) throws CoreException {
        Map properties = document.getProperties();

        try {
            T instance = mConstructor.newInstance();
            mClazz.getAnnotation(DocumentBase.class);
            for(Field field : instance.getClass().getFields()) {
                FieldBase fieldBase = field.getAnnotation(FieldBase.class);
                if (fieldBase != null) {
                    Object value = properties.get(fieldBase.name());
                    if (value != null) {
                        // POUR LES ARRAYS ON RECUPERE LE TYPE DU TEMPLATE POUR LA RECURCIVITE
                        Class<?> templateType = null;
                        try {
                            if(field.getType().asSubclass(List.class) != null) {
                                ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                                templateType = (Class<?>) stringListType.getActualTypeArguments()[0];
                            }
                        } catch (ClassCastException e) {
                        }

                        Object fieldValue = theMagicFunction(value, field.getType(), templateType);
                        field.set(instance, fieldValue);
                    }
                }
            }
            return instance;
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * theMagicFunction
     *
     * @param value
     * @param fieldType
     * @param templateType
     * @return
     */
    private Object theMagicFunction(Object value, Class fieldType, Class templateType)
    {
        Object res = null;

        try {
            // CAS 1 : le champs est un document ID donc on va le chercher dans le bucket
            if(fieldType.getAnnotation(DocumentBase.class) != null) {
                Access access = new Access(fieldType, mDatabaseHandler);
                res = access.get(value.toString());
            }
            // CAS 2 : le champ est un Array ET value est un array (ATTENTION UNE LIST NE POURRA PAS CONTENIR UNE AUTRE LIST !!!!!!)
            else if (value instanceof ArrayList && (fieldType.asSubclass(List.class) != null) && templateType != null) {
                res = fieldType.getConstructor().newInstance();

                for(Object array_value: (ArrayList)value) {
                    Object array_res = theMagicFunction(array_value, templateType, null);
                    ((List)res).add(array_res);
                }
            }
            // CAS 3 : le champ est un SubModelBase ET value est une map
            else if (value instanceof Map && (fieldType.asSubclass(SubModelBase.class) != null)) {
                res =  fieldType.getConstructor(Map.class, DatabaseHandler.class)
                        .newInstance(value, mDatabaseHandler);
            }
            // CAS 4 : When a document property was update the true type was return by couchbase-lite.
            else if (fieldType.equals(value.getClass())) {
               res = value;
            }
            // CAS 5 : le champ est un Enum ET value et une String
            else if (fieldType.isEnum() && value instanceof String) {
                res = fieldType.getMethod("valueOf", String.class).invoke(fieldType, value);
            }
            // CAS 6 : le champ est une Date et value et une String
            else if(fieldType.equals(Date.class) && value instanceof String){
                res = DateHelper.dateFromString(value.toString());
            }
            // CAS 7 : Base or String Type
            else if (isBaseType(value.getClass())){
                res = toObject(fieldType, value.toString());
            }
            else {
                //throw new CoreException("Can't affect field : " + field.getName() + " in Class " + mClazz.getName());
            }
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return res;
    }



    private static boolean isBaseType( Class clazz) {
        if( Boolean.class == clazz || boolean.class == clazz) return true;
        if( Byte.class == clazz || byte.class == clazz) return true;
        if( Short.class == clazz || short.class == clazz) return true;
        if( Integer.class == clazz || int.class == clazz) return true;
        if( Long.class == clazz || long.class == clazz) return true;
        if( Float.class == clazz || float.class == clazz) return true;
        if( Double.class == clazz || double.class == clazz) return true;
        return false;
    }

    /**
     * toObject.
     * @param clazz the target type
     * @param value the value to convert
     * @return type convert if possible
     */
    private static Object toObject( Class clazz, String value ) {
        if( Boolean.class == clazz || boolean.class == clazz) return Boolean.parseBoolean( value );
        if( Byte.class == clazz || byte.class == clazz) return Byte.parseByte( value );
        if( Short.class == clazz || short.class == clazz) return Short.parseShort( value );
        if( Integer.class == clazz || int.class == clazz) return Integer.parseInt( value );
        if( Long.class == clazz || long.class == clazz) return Long.parseLong( value );
        if( Float.class == clazz || float.class == clazz) return Float.parseFloat( value );
        if( Double.class == clazz || double.class == clazz) return Double.parseDouble( value );
        return value;
    }
}
