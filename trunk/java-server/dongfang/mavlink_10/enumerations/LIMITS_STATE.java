package dongfang.mavlink_10.enumerations;
public enum LIMITS_STATE {
  LIMITS_INIT(0, " pre-initialization"),
  LIMITS_DISABLED(1, " disabled"),
  LIMITS_ENABLED(2, " checking limits"),
  LIMITS_TRIGGERED(3, " a limit has been breached"),
  LIMITS_RECOVERING(4, " taking action eg. RTL"),
  LIMITS_RECOVERED(5, " we're no longer in breach of a limit");

  int value;
  String description;

  LIMITS_STATE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static LIMITS_STATE forValue(int value) {
    switch(value) {
      case 0: return LIMITS_INIT;
      case 1: return LIMITS_DISABLED;
      case 2: return LIMITS_ENABLED;
      case 3: return LIMITS_TRIGGERED;
      case 4: return LIMITS_RECOVERING;
      case 5: return LIMITS_RECOVERED;
      default: return null;
    }
  }
}

