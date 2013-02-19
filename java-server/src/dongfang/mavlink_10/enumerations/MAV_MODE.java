package dongfang.mavlink_10.enumerations;
public enum MAV_MODE {
  MAV_MODE_PREFLIGHT(0, "System is not ready to fly, booting, calibrating, etc. No flag is set."),
  MAV_MODE_STABILIZE_DISARMED(80, "System is allowed to be active, under assisted RC control."),
  MAV_MODE_STABILIZE_ARMED(208, "System is allowed to be active, under assisted RC control."),
  MAV_MODE_MANUAL_DISARMED(64, "System is allowed to be active, under manual (RC) control, no stabilization"),
  MAV_MODE_MANUAL_ARMED(192, "System is allowed to be active, under manual (RC) control, no stabilization"),
  MAV_MODE_GUIDED_DISARMED(88, "System is allowed to be active, under autonomous control, manual setpoint"),
  MAV_MODE_GUIDED_ARMED(216, "System is allowed to be active, under autonomous control, manual setpoint"),
  MAV_MODE_AUTO_DISARMED(92, "System is allowed to be active, under autonomous control and navigation (the trajectory is decided onboard and not pre-programmed by MISSIONs)"),
  MAV_MODE_AUTO_ARMED(220, "System is allowed to be active, under autonomous control and navigation (the trajectory is decided onboard and not pre-programmed by MISSIONs)"),
  MAV_MODE_TEST_DISARMED(66, "UNDEFINED mode. This solely depends on the autopilot - use with caution, intended for developers only."),
  MAV_MODE_TEST_ARMED(194, "UNDEFINED mode. This solely depends on the autopilot - use with caution, intended for developers only.");

  int value;
  String description;

  MAV_MODE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_MODE forValue(int value) {
    switch(value) {
      case 0: return MAV_MODE_PREFLIGHT;
      case 80: return MAV_MODE_STABILIZE_DISARMED;
      case 208: return MAV_MODE_STABILIZE_ARMED;
      case 64: return MAV_MODE_MANUAL_DISARMED;
      case 192: return MAV_MODE_MANUAL_ARMED;
      case 88: return MAV_MODE_GUIDED_DISARMED;
      case 216: return MAV_MODE_GUIDED_ARMED;
      case 92: return MAV_MODE_AUTO_DISARMED;
      case 220: return MAV_MODE_AUTO_ARMED;
      case 66: return MAV_MODE_TEST_DISARMED;
      case 194: return MAV_MODE_TEST_ARMED;
      default: return null;
    }
  }
}

