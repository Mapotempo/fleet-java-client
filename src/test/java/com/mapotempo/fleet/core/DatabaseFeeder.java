package com.mapotempo.fleet.core;

import com.couchbase.lite.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseFeeder {
    private static String path = "src/test/java/json";

    public enum Dataset {
        DATASET_1("dataset_1"),
        DATASET_2("dataset_2");

        String mPath = "dataset_1";

        Dataset(String path) {
            mPath = path;
        }
    }

    private static FileFilter jsonfilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".json") && pathname.isFile();
        }
    };

    public static void Feed(Database database, Dataset dataset) throws CouchbaseLiteException, IOException {
        Path json_dir_path = Paths.get("src/test/java/json/" + dataset.mPath);
        File json_dir = json_dir_path.toFile();
        File[] files = json_dir.listFiles(jsonfilter);
        if (files != null) {
            for (File f : files) {
                Map<String, Object> result = (Map<String, Object>) new ObjectMapper().readValue(f, HashMap.class);
                String id = (String) result.get("_id");
                Document doc = database.getDocument(id);
                doc.putProperties(result);
            }
        }
    }

    public static void Clear(Database database) throws CouchbaseLiteException, IOException {
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        QueryEnumerator result = query.run();
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            String docId = row.getDocumentId();
            Document doc = database.getDocument(docId);
            doc.delete();
        }
    }
}