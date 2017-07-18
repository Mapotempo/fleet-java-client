package com.mapotempo.fleet.core.accessor;

import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.DocumentBase;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

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
        try {
            T instance = mConstructor.newInstance();
            mClazz.getAnnotation(DocumentBase.class);
            for(Field field : instance.getClass().getFields()) {
                FieldBase fieldBase = field.getAnnotation(FieldBase.class);
                if (fieldBase != null) {
                    Object value = document.getProperty(fieldBase.name());
                    if (value != null) {
                        // Foreigner
                        if(fieldBase.foreign() == true) {
                            Class clazz = field.getType();
                            Access access = new Access(clazz, mDatabaseHandler);
                            Object model = access.get(value.toString());
                            field.set(instance, model);
                        }
                        // Classic
                        else {
                            // Generate SubModelBase Field
                            if (value instanceof Array) {
                                System.err.println("Array no implemented !");
                            }
                            // SubModelBase type
                            else if (value instanceof Map) {
                                if (field.getType().asSubclass(SubModelBase.class) != null) {
                                    field.set(instance, field.getType().getConstructor(Map.class, DatabaseHandler.class).newInstance(value, mDatabaseHandler));
                                }
                            }
                            // When a document property was update the true type was return by couchbase-lite.
                            else if (field.getType().equals(value.getClass())) {
                                field.set(instance, value);
                            }
                            // Enum Type
                            else if (field.getType().isEnum()) {
                                Class enumType = field.getType();
                                field.set(instance, enumType.getMethod("valueOf", String.class).invoke(enumType, value));
                            }
                            // Date Type
                            else if(field.getType().equals(Date.class)){
                                System.out.println(value);
                                System.out.println(value.getClass().getSimpleName());
                                field.set(instance, DateHelper.dateFromString((String)value));
                            }
                            // Base or String Type
                            else if (isBaseType(value.getClass())){
                                field.set(instance, toObject(field.getType(), value.toString()));
                            }
                            else {
                                throw new CoreException("Can't affect field : " + field.getName() + " in Class " + mClazz.getName());
                            }
                        }
                    }
                }
            }
            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
