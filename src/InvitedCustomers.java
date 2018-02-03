import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/*
Outputs customers from a json file (name stored in config file) that are within a great-circle distance range.
Distance is calculated by using the origin latitude and longitude stored in config file
with each of the customers lat and long values.
 */

public class InvitedCustomers {

    private static final String CONFIG_FILE = "config.properties";


    //creates and returns properties object based off of config file data
    public Properties getProperties(){
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(CONFIG_FILE);
        } catch (IOException ex) {
            System.out.println("Invalid file name given for config file");
            ex.printStackTrace();
        }

        // load the properties file
        try {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Properties file failed to load");
            e.printStackTrace();
        }

        return properties;
    }


    //reads the json file and puts each json object into a json array which is returned
    public JSONArray readJSONDataToArray(String jsonFileProperty) {
        Properties properties = getProperties();
        JSONArray customers = new JSONArray();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(properties.getProperty(jsonFileProperty)));
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name given for json file in config");
            e.printStackTrace();
        }

        String line = "";
        try {
            while((line = br.readLine()) != null){
                customers.put(new JSONObject(line));
            }
        } catch (IOException e) {
            System.out.println("BufferedReader failed to read from file");
            e.printStackTrace();
        }
        return customers;
    }


    //checks if lat and long lie within their ranges
    public boolean validLatOrLong(double lat, double longitude){
        boolean valid = true;
        if(lat < -90 || lat > 90 || longitude < -180 || longitude > 180){
            valid = false;
        }
        return valid;
    }

    /*creates and retrieves a tree map with customers' associated id and name for those within distance range (km).
    InvitedCustomers are sorted by id in ascending order automatically by tree map
     */
    public TreeMap<Integer, String> getCustomersWithinRange(JSONArray customers, int range){
        TreeMap<Integer, String> customersToInvite = new TreeMap<>();
        JSONObject customer;
        DistanceCalculator calc = new DistanceCalculator();
        Properties properties = getProperties();

        //get the lat and long of origin location from config file
        double originLatitude = Double.parseDouble(properties.getProperty("latitude"));
        double originLongitude = Double.parseDouble(properties.getProperty("longitude"));

        if(!validLatOrLong(originLatitude, originLongitude)){
            System.out.println("Invalid latitude and/or longitude given in the config file");
        }


        double dist = 0.0;
        double customerLat = 0.0;
        double customerLong = 0.0;

        //finds each customer within specified range of distance and adds them (id and name) to a tree map
        for(int i=0; i<customers.length(); i++){
            customer = customers.getJSONObject(i);

            customerLat = Double.parseDouble(customer.get("latitude").toString());
            customerLong = Double.parseDouble(customer.get("longitude").toString());

            if(!validLatOrLong(customerLat, customerLong)){
                System.out.println("Invalid latitude and/or longitude values found for entry:" +
                        customer.get("user_id") + " " + customer.get("name" + "\n"));
            }
            else {
                dist = calc.distance(originLatitude, originLongitude, Double.parseDouble(customer.get("latitude").toString()), Double.parseDouble(customer
                        .get("longitude").toString()));
                if(dist <= range){
                    customersToInvite.put((Integer) customer.get("user_id"), (String) customer.get("name"));
                }
            }
        }
        return customersToInvite;
    }


    public static void main(String[] args){
        InvitedCustomers ic = new InvitedCustomers();
        TreeMap<Integer, String> customersToInvite = ic.getCustomersWithinRange(ic.readJSONDataToArray("jsonfile"), 100);

        for(Map.Entry<Integer, String> entry: customersToInvite.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

}
