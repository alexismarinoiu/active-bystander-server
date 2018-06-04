package uk.avocado.data;

import static java.lang.Math.*;

public class LocationUtils {

  public static int EARTH_RADIUS = 6371000; // metres

  public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
    double deltaLong = toRadians(abs(long1 - long2));
    double lat1Rad = toRadians(lat1);
    double lat2Rad = toRadians(lat2);
    double numerator = sqrt(pow((cos(lat2Rad) * sin(deltaLong)), 2)
        + pow((cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(deltaLong)), 2));
    double denominator = sin(lat1Rad) * sin(lat2Rad) + cos(lat1Rad) * cos(lat2Rad) * cos(deltaLong);
    return floor(EARTH_RADIUS * atan2(numerator, denominator));
  }
}