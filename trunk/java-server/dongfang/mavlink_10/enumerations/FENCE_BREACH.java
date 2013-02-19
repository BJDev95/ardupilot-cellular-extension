package dongfang.mavlink_10.enumerations;
public enum FENCE_BREACH {
  FENCE_BREACH_NONE(0, "No last fence breach"),
  FENCE_BREACH_MINALT(1, "Breached minimum altitude"),
  FENCE_BREACH_MAXALT(2, "Breached minimum altitude"),
  FENCE_BREACH_BOUNDARY(3, "Breached fence boundary");

  int value;
  String description;

  FENCE_BREACH(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static FENCE_BREACH forValue(int value) {
    switch(value) {
      case 0: return FENCE_BREACH_NONE;
      case 1: return FENCE_BREACH_MINALT;
      case 2: return FENCE_BREACH_MAXALT;
      case 3: return FENCE_BREACH_BOUNDARY;
      default: return null;
    }
  }
}

