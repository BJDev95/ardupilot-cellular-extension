package dongfang.mavlink_10.enumerations;
public enum MAV_GOTO {
  MAV_GOTO_DO_HOLD(0, "Hold at the current position."),
  MAV_GOTO_DO_CONTINUE(1, "Continue with the next item in mission execution."),
  MAV_GOTO_HOLD_AT_CURRENT_POSITION(2, "Hold at the current position of the system"),
  MAV_GOTO_HOLD_AT_SPECIFIED_POSITION(3, "Hold at the position specified in the parameters of the DO_HOLD action");

  int value;
  String description;

  MAV_GOTO(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_GOTO forValue(int value) {
    switch(value) {
      case 0: return MAV_GOTO_DO_HOLD;
      case 1: return MAV_GOTO_DO_CONTINUE;
      case 2: return MAV_GOTO_HOLD_AT_CURRENT_POSITION;
      case 3: return MAV_GOTO_HOLD_AT_SPECIFIED_POSITION;
      default: return null;
    }
  }
}

