package dongfang.mavlink_10.enumerations;
public enum MAV_MISSION_RESULT {
  MAV_MISSION_ACCEPTED(0, "mission accepted OK"),
  MAV_MISSION_ERROR(1, "generic error / not accepting mission commands at all right now"),
  MAV_MISSION_UNSUPPORTED_FRAME(2, "coordinate frame is not supported"),
  MAV_MISSION_UNSUPPORTED(3, "command is not supported"),
  MAV_MISSION_NO_SPACE(4, "mission item exceeds storage space"),
  MAV_MISSION_INVALID(5, "one of the parameters has an invalid value"),
  MAV_MISSION_INVALID_PARAM1(6, "param1 has an invalid value"),
  MAV_MISSION_INVALID_PARAM2(7, "param2 has an invalid value"),
  MAV_MISSION_INVALID_PARAM3(8, "param3 has an invalid value"),
  MAV_MISSION_INVALID_PARAM4(9, "param4 has an invalid value"),
  MAV_MISSION_INVALID_PARAM5_X(10, "x/param5 has an invalid value"),
  MAV_MISSION_INVALID_PARAM6_Y(11, "y/param6 has an invalid value"),
  MAV_MISSION_INVALID_PARAM7(12, "param7 has an invalid value"),
  MAV_MISSION_INVALID_SEQUENCE(13, "received waypoint out of sequence"),
  MAV_MISSION_DENIED(14, "not accepting any mission commands from this communication partner");

  int value;
  String description;

  MAV_MISSION_RESULT(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_MISSION_RESULT forValue(int value) {
    switch(value) {
      case 0: return MAV_MISSION_ACCEPTED;
      case 1: return MAV_MISSION_ERROR;
      case 2: return MAV_MISSION_UNSUPPORTED_FRAME;
      case 3: return MAV_MISSION_UNSUPPORTED;
      case 4: return MAV_MISSION_NO_SPACE;
      case 5: return MAV_MISSION_INVALID;
      case 6: return MAV_MISSION_INVALID_PARAM1;
      case 7: return MAV_MISSION_INVALID_PARAM2;
      case 8: return MAV_MISSION_INVALID_PARAM3;
      case 9: return MAV_MISSION_INVALID_PARAM4;
      case 10: return MAV_MISSION_INVALID_PARAM5_X;
      case 11: return MAV_MISSION_INVALID_PARAM6_Y;
      case 12: return MAV_MISSION_INVALID_PARAM7;
      case 13: return MAV_MISSION_INVALID_SEQUENCE;
      case 14: return MAV_MISSION_DENIED;
      default: return null;
    }
  }
}

