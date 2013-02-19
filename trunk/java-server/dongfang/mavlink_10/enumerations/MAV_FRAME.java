package dongfang.mavlink_10.enumerations;
public enum MAV_FRAME {
  MAV_FRAME_GLOBAL(0, "Global coordinate frame, WGS84 coordinate system. First value / x: latitude, second value / y: longitude, third value / z: positive altitude over mean sea level (MSL)"),
  MAV_FRAME_LOCAL_NED(1, "Local coordinate frame, Z-up (x: north, y: east, z: down)."),
  MAV_FRAME_MISSION(2, "NOT a coordinate frame, indicates a mission command."),
  MAV_FRAME_GLOBAL_RELATIVE_ALT(3, "Global coordinate frame, WGS84 coordinate system, relative altitude over ground with respect to the home position. First value / x: latitude, second value / y: longitude, third value / z: positive altitude with 0 being at the altitude of the home location."),
  MAV_FRAME_LOCAL_ENU(4, "Local coordinate frame, Z-down (x: east, y: north, z: up)");

  int value;
  String description;

  MAV_FRAME(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_FRAME forValue(int value) {
    switch(value) {
      case 0: return MAV_FRAME_GLOBAL;
      case 1: return MAV_FRAME_LOCAL_NED;
      case 2: return MAV_FRAME_MISSION;
      case 3: return MAV_FRAME_GLOBAL_RELATIVE_ALT;
      case 4: return MAV_FRAME_LOCAL_ENU;
      default: return null;
    }
  }
}

