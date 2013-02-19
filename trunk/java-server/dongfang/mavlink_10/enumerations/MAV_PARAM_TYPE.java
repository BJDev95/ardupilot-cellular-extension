package dongfang.mavlink_10.enumerations;
public enum MAV_PARAM_TYPE {
  MAV_PARAM_TYPE_UINT8(1, "8-bit unsigned integer"),
  MAV_PARAM_TYPE_INT8(2, "8-bit signed integer"),
  MAV_PARAM_TYPE_UINT16(3, "16-bit unsigned integer"),
  MAV_PARAM_TYPE_INT16(4, "16-bit signed integer"),
  MAV_PARAM_TYPE_UINT32(5, "32-bit unsigned integer"),
  MAV_PARAM_TYPE_INT32(6, "32-bit signed integer"),
  MAV_PARAM_TYPE_UINT64(7, "64-bit unsigned integer"),
  MAV_PARAM_TYPE_INT64(8, "64-bit signed integer"),
  MAV_PARAM_TYPE_REAL32(9, "32-bit floating-point"),
  MAV_PARAM_TYPE_REAL64(10, "64-bit floating-point");

  int value;
  String description;

  MAV_PARAM_TYPE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_PARAM_TYPE forValue(int value) {
    switch(value) {
      case 1: return MAV_PARAM_TYPE_UINT8;
      case 2: return MAV_PARAM_TYPE_INT8;
      case 3: return MAV_PARAM_TYPE_UINT16;
      case 4: return MAV_PARAM_TYPE_INT16;
      case 5: return MAV_PARAM_TYPE_UINT32;
      case 6: return MAV_PARAM_TYPE_INT32;
      case 7: return MAV_PARAM_TYPE_UINT64;
      case 8: return MAV_PARAM_TYPE_INT64;
      case 9: return MAV_PARAM_TYPE_REAL32;
      case 10: return MAV_PARAM_TYPE_REAL64;
      default: return null;
    }
  }
}

