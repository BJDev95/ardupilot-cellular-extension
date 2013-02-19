package dongfang.mavlink_10.enumerations;
public enum MAV_MODE_FLAG_DECODE_POSITION {
  MAV_MODE_FLAG_DECODE_POSITION_SAFETY(128, "First bit:  10000000"),
  MAV_MODE_FLAG_DECODE_POSITION_MANUAL(64, "Second bit: 01000000"),
  MAV_MODE_FLAG_DECODE_POSITION_HIL(32, "Third bit:  00100000"),
  MAV_MODE_FLAG_DECODE_POSITION_STABILIZE(16, "Fourth bit: 00010000"),
  MAV_MODE_FLAG_DECODE_POSITION_GUIDED(8, "Fifth bit:  00001000"),
  MAV_MODE_FLAG_DECODE_POSITION_AUTO(4, "Sixt bit:   00000100"),
  MAV_MODE_FLAG_DECODE_POSITION_TEST(2, "Seventh bit: 00000010"),
  MAV_MODE_FLAG_DECODE_POSITION_CUSTOM_MODE(1, "Eighth bit: 00000001");

  int value;
  String description;

  MAV_MODE_FLAG_DECODE_POSITION(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_MODE_FLAG_DECODE_POSITION forValue(int value) {
    switch(value) {
      case 128: return MAV_MODE_FLAG_DECODE_POSITION_SAFETY;
      case 64: return MAV_MODE_FLAG_DECODE_POSITION_MANUAL;
      case 32: return MAV_MODE_FLAG_DECODE_POSITION_HIL;
      case 16: return MAV_MODE_FLAG_DECODE_POSITION_STABILIZE;
      case 8: return MAV_MODE_FLAG_DECODE_POSITION_GUIDED;
      case 4: return MAV_MODE_FLAG_DECODE_POSITION_AUTO;
      case 2: return MAV_MODE_FLAG_DECODE_POSITION_TEST;
      case 1: return MAV_MODE_FLAG_DECODE_POSITION_CUSTOM_MODE;
      default: return null;
    }
  }
}

