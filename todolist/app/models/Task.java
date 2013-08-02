package models;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task {

    public String key;
    public String label;

    static CouchbaseClient client = null;
    static {

        try {

            List<URI> hosts = Arrays.asList(new URI("http://127.0.0.1:8091/pools"));
            String bucket = "default";
            String password = "";
            client = new CouchbaseClient(hosts, bucket, password);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static List<Task> all() {

        ArrayList<Task> all = new ArrayList<Task>();

        try {

            // 1: Load the View infos
            String designDoc = "dev_tasks";
            String viewName = "list_tasks";
            View view = client.getView(designDoc, viewName);

            // 2: Create a Query object to customize the Query
            Query query = new Query();
            query.setIncludeDocs(true); // Include the full document body

            // 3: Actually Query the View and return the results
            ViewResponse response = client.query(view, query);

            // 4: Iterate over the Data and print out the full document
            for (ViewRow row : response) {
                Task task = new Task();
                task.key = row.getKey();
                task.label = row.getDocument().toString();
                all.add(task);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return all;
    }

    public static void create(Task task) {

        try {
            client.set(""+System.currentTimeMillis(), task.label).get();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void delete(String id) {
        try {
            client.delete(id);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

}

