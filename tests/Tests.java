import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class Tests {


    @Test
    public void checkValidityOfLatAndLongValues(){
        InvitedCustomers ic = new InvitedCustomers();
        assert(ic.validLatOrLong(70, 150));
        assertFalse(ic.validLatOrLong(91, 150));
        assertFalse(ic.validLatOrLong(-91, 150));
        assertFalse(ic.validLatOrLong(-67, -181));
        assertFalse(ic.validLatOrLong(-91, 190));
        assertFalse(ic.validLatOrLong(-91, 192));
        assertFalse(ic.validLatOrLong(100, 200));
    }

    @Test
    public void testDistance(){
        DistanceCalculator calc = new DistanceCalculator();
        assertEquals(313.2, calc.distance(51.92893, -10.27699, 53.339428, -6.257664), 0.1);
        assertEquals(13147.8 , calc.distance(57.26475, -1.4875, -60.485487, 12.27699), 1);
        assertEquals(9898.53 , calc.distance(57.26475, -5.4875, -30.485487, 12.27699), 1);
        assertEquals(4281.3 , calc.distance(69.26475, -15.4875, 35.485487, 18.27699), 1);
        assertEquals(922.5 , calc.distance(10.474575, 2.4467, 12.485487, -5.76758), 0.1);
        assertEquals(198.97  , calc.distance(12.58687, 4.2545, 11.1365, 5.3256), 0.1);
        assertEquals(50.67  , calc.distance(12.58687, 4.2545, 12.1365, 4.3256), 0.1);

        //values with 1 outside their correct range
        assertEquals(-1.0, calc.distance(-100.4848, 20.58687, 4.2545, 12.1365));
        assertEquals(-1.0, calc.distance(-10.4848, 200.58687, 4.2545, 12.1365));
        assertEquals(-1.0, calc.distance(-10.4848, 20.58687, 220.2545, 12.1365));
        assertEquals(-1.0, calc.distance(-10.4848, 20.58687, 22.2545, -181.1365));
    }


    @Test
    public void testJsonFileRead(){
        InvitedCustomers ic = new InvitedCustomers();

        JSONArray array = ic.readJSONDataToArray("testjson");
        String json = "[{\"user_id\":1,\"latitude\":\"52.986375\",\"name\":\"Person1 \",\"longitude\":\"-6.043701\"},{\"user_id\":2,\"latitude\"" +
                ":\"52.986375\",\"name\":\"Person2 \",\"longitude\":\"-6.043701\"},{\"user_id\":3,\"latitude\":\"52.986375\"," +
                "\"name\":\"Person3 \",\"longitude\":\"-6.043701\"},{\"user_id\":4,\"latitude\"" +
                ":\"52.986375\",\"name\":\"Person4 \",\"longitude\":\"-6.043701\"},{\"user_id\":5,\"latitude\":\"52.986375\",\"name\":\"Person5 \"" +
                ",\"longitude\":\"-6.043701\"},{\"user_id\":6,\"latitude\":\"52.986375\",\"name\":\"Person6 \",\"longitude\":\"-6.043701\"}]";

        assertEquals(json, array.toString());
    }


    @Test
    public void checkOutputForCustomersWithinRange(){
        InvitedCustomers ic = new InvitedCustomers();
        JSONArray array = new JSONArray();
        array.put(new JSONObject("{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}"));
        array.put(new JSONObject("{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}"));
        array.put(new JSONObject("{\"latitude\": \"52.2559432\", \"user_id\": 9, \"name\": \"Jack Dempsey\", \"longitude\": \"-7.1048927\"}"));
        array.put(new JSONObject("{\"latitude\": \"54.0894797\", \"user_id\": 8, \"name\": \"Eoin Ahearn\", \"longitude\": \"-6.18671\"}"));
        array.put(new JSONObject("{\"latitude\": \"54.133333\", \"user_id\": 24, \"name\": \"Rose Enright\", \"longitude\": \"-6.433333\"}"));

        TreeMap<Integer, String> customersToInvite = ic.getCustomersWithinRange(array, 100);

        String str = "";
        for(Map.Entry<Integer, String> entry: customersToInvite.entrySet()){
            str += entry.getKey() + " " + entry.getValue() + " ";
        }
        assertEquals("8 Eoin Ahearn 12 Christina McArdle 24 Rose Enright ", str);

        customersToInvite = ic.getCustomersWithinRange(array, 50);

        str = "";
        for(Map.Entry<Integer, String> entry: customersToInvite.entrySet()){
            str += entry.getKey() + " " + entry.getValue() + " ";
        }
        assertEquals("12 Christina McArdle ", str);

        array = new JSONArray();
        array.put(new JSONObject("{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}"));
        array.put(new JSONObject("{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}"));
        array.put(new JSONObject("{\"latitude\": \"52.2559432\", \"user_id\": 9, \"name\": \"Jack Dempsey\", \"longitude\": \"-7.1048927\"}"));
        array.put(new JSONObject("{\"latitude\": \"54.0894797\", \"user_id\": 8, \"name\": \"Eoin Ahearn\", \"longitude\": \"-6.18671\"}"));
        array.put(new JSONObject("{\"latitude\": \"54.133333\", \"user_id\": 24, \"name\": \"Rose Enright\", \"longitude\": \"-6.433333\"}"));
        array.put(new JSONObject("{\"latitude\": \"53.1489345\", \"user_id\": 31, \"name\": \"Alan Behan\", \"longitude\": \"-6.8422408\"}"));
        array.put(new JSONObject("{\"latitude\": \"53.0033946\", \"user_id\": 39, \"name\": \"Lisa Ahearn\", \"longitude\": \"-6.3877505\"}"));
        array.put(new JSONObject("{\"latitude\": \"54.080556\", \"user_id\": 23, \"name\": \"Eoin Gallagher\", \"longitude\": \"-6.361944\"}"));
        array.put(new JSONObject("{\"latitude\": \"53.4692815\", \"user_id\": 7, \"name\": \"Frank Kehoe\", \"longitude\": \"-9.436036\"}"));

        customersToInvite = ic.getCustomersWithinRange(array, 150);

        str = "";
        for(Map.Entry<Integer, String> entry: customersToInvite.entrySet()){
            str += entry.getKey() + " " + entry.getValue() + " ";
        }
        assertEquals("8 Eoin Ahearn 9 Jack Dempsey 12 Christina McArdle 23 Eoin Gallagher 24 Rose Enright 31 Alan Behan 39 Lisa Ahearn ", str);
    }
}
