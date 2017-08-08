package com.mapotempo.fleet.core.exception;

import com.couchbase.lite.CouchbaseLiteException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * CoreException.
 */
public class CoreException extends Exception{
    public CoreException(String string) {
        super(string);
    }
}
