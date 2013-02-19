package dongfang.mavlink_10.enumerations;
public enum MAV_MOUNT_MODE {
  MAV_MOUNT_MODE_RETRACT(0, "Load and keep safe position (Roll,Pitch,Yaw) from EEPROM and stop stabilization"),
  MAV_MOUNT_MODE_NEUTRAL(1, "Load and keep neutral position (Roll,Pitch,Yaw) from EEPROM."),
  MAV_MOUNT_MODE_MAVLINK_TARGETING(2, "Load neutral position and start MAVLink Roll,Pitch,Yaw control with stabilization"),
  MAV_MOUNT_MODE_RC_TARGETING(3, "Load neutral position and start RC Roll,Pitch,Yaw control with stabilization"),
  MAV_MOUNT_MODE_GPS_POINT(4, "Load neutral position and start to point to Lat,Lon,Alt");

  int value;
  String description;

  MAV_MOUNT_MODE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_MOUNT_MODE forValue(int value) {
    switch(value) {
      case 0: return MAV_MOUNT_MODE_RETRACT;
      case 1: return MAV_MOUNT_MODE_NEUTRAL;
      case 2: return MAV_MOUNT_MODE_MAVLINK_TARGETING;
      case 3: return MAV_MOUNT_MODE_RC_TARGETING;
      case 4: return MAV_MOUNT_MODE_GPS_POINT;
      default: return null;
    }
  }
}

