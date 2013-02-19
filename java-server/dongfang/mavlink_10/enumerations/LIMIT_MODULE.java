package dongfang.mavlink_10.enumerations;
public enum LIMIT_MODULE {
  LIMIT_GPSLOCK(1, " pre-initialization"),
  LIMIT_GEOFENCE(2, " disabled"),
  LIMIT_ALTITUDE(4, " checking limits");

  int value;
  String description;

  LIMIT_MODULE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static LIMIT_MODULE forValue(int value) {
    switch(value) {
      case 1: return LIMIT_GPSLOCK;
      case 2: return LIMIT_GEOFENCE;
      case 4: return LIMIT_ALTITUDE;
      default: return null;
    }
  }
}

