package dongfang.mavlink_10.enumerations;
public enum MAV_DATA_STREAM {
  MAV_DATA_STREAM_ALL(0, "Enable all data streams"),
  MAV_DATA_STREAM_RAW_SENSORS(1, "Enable IMU_RAW, GPS_RAW, GPS_STATUS packets."),
  MAV_DATA_STREAM_EXTENDED_STATUS(2, "Enable GPS_STATUS, CONTROL_STATUS, AUX_STATUS"),
  MAV_DATA_STREAM_RC_CHANNELS(3, "Enable RC_CHANNELS_SCALED, RC_CHANNELS_RAW, SERVO_OUTPUT_RAW"),
  MAV_DATA_STREAM_RAW_CONTROLLER(4, "Enable ATTITUDE_CONTROLLER_OUTPUT, POSITION_CONTROLLER_OUTPUT, NAV_CONTROLLER_OUTPUT."),
  MAV_DATA_STREAM_POSITION(6, "Enable LOCAL_POSITION, GLOBAL_POSITION/GLOBAL_POSITION_INT messages."),
  MAV_DATA_STREAM_EXTRA1(10, "Dependent on the autopilot"),
  MAV_DATA_STREAM_EXTRA2(11, "Dependent on the autopilot"),
  MAV_DATA_STREAM_EXTRA3(12, "Dependent on the autopilot");

  int value;
  String description;

  MAV_DATA_STREAM(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_DATA_STREAM forValue(int value) {
    switch(value) {
      case 0: return MAV_DATA_STREAM_ALL;
      case 1: return MAV_DATA_STREAM_RAW_SENSORS;
      case 2: return MAV_DATA_STREAM_EXTENDED_STATUS;
      case 3: return MAV_DATA_STREAM_RC_CHANNELS;
      case 4: return MAV_DATA_STREAM_RAW_CONTROLLER;
      case 6: return MAV_DATA_STREAM_POSITION;
      case 10: return MAV_DATA_STREAM_EXTRA1;
      case 11: return MAV_DATA_STREAM_EXTRA2;
      case 12: return MAV_DATA_STREAM_EXTRA3;
      default: return null;
    }
  }
}

