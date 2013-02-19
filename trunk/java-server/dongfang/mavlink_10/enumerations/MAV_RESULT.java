package dongfang.mavlink_10.enumerations;
public enum MAV_RESULT {
  MAV_RESULT_ACCEPTED(0, "Command ACCEPTED and EXECUTED"),
  MAV_RESULT_TEMPORARILY_REJECTED(1, "Command TEMPORARY REJECTED/DENIED"),
  MAV_RESULT_DENIED(2, "Command PERMANENTLY DENIED"),
  MAV_RESULT_UNSUPPORTED(3, "Command UNKNOWN/UNSUPPORTED"),
  MAV_RESULT_FAILED(4, "Command executed, but failed");

  int value;
  String description;

  MAV_RESULT(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_RESULT forValue(int value) {
    switch(value) {
      case 0: return MAV_RESULT_ACCEPTED;
      case 1: return MAV_RESULT_TEMPORARILY_REJECTED;
      case 2: return MAV_RESULT_DENIED;
      case 3: return MAV_RESULT_UNSUPPORTED;
      case 4: return MAV_RESULT_FAILED;
      default: return null;
    }
  }
}

