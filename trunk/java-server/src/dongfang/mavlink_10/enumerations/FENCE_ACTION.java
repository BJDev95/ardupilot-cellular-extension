package dongfang.mavlink_10.enumerations;
public enum FENCE_ACTION {
  FENCE_ACTION_NONE(0, "Disable fenced mode"),
  FENCE_ACTION_GUIDED(1, "Switched to guided mode to return point (fence point 0)"),
  FENCE_ACTION_REPORT(2, "Report fence breach, but don't take action");

  int value;
  String description;

  FENCE_ACTION(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static FENCE_ACTION forValue(int value) {
    switch(value) {
      case 0: return FENCE_ACTION_NONE;
      case 1: return FENCE_ACTION_GUIDED;
      case 2: return FENCE_ACTION_REPORT;
      default: return null;
    }
  }
}

