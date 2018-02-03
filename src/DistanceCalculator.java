//used to calculate the great circle distance between two latitude and longitude points
public class DistanceCalculator {

    private static final double EARTH_RADIUS = 6371.0;

    //using the haversine formula
    public double distance(double latitude1, double longitude1, double latitude2, double longitude2){

        double dist = -1;
        if(latitude1 < -90 || latitude1 > 90 || latitude2 < -90 || latitude2 > 90 ||
                longitude1 < -180 || longitude1 > 180 || longitude2 < -180 || longitude2 > 180){
            System.out.println("Invalid latitude and/or longitude values. Latitude must be within -90 -> 90. Longitude must be within -180 -> 180");
        }
        else {

            double diffLat = Math.toRadians(Math.abs(latitude1 - latitude2));
            double diffLong = Math.toRadians(Math.abs((longitude1 - longitude2)));
            double a = Math.pow(Math.sin(diffLat / 2), 2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.pow(Math.sin
                    (diffLong / 2), 2);
            double b = 2 * Math.asin(Math.sqrt(a));
           dist = EARTH_RADIUS * b;
        }
        return dist;
    }
}
