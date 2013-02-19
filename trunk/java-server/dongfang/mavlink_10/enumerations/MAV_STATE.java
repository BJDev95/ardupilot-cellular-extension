package dongfang.mavlink_10.enumerations;
public enum MAV_STATE {
  MAV_STATE_UNINIT(0, "Uninitialized system, state is unknown."),
  MAV_STATE_BOOT(1, "System is booting up."),
  MAV_STATE_CALIBRATING(2, "System is calibrating and not flight-ready."),
  MAV_STATE_STANDBY(3, "System is grounded and on standby. It can be launched any time."),
  MAV_STATE_ACTIVE(4, "System is active and might be already airborne. Motors are engaged."),
  MAV_STATE_CRITICAL(5, "System is in a non-normal flight mode. It can however still navigate."),
  MAV_STATE_EMERGENCY(6, "System is in a non-normal flight mode. It lost control over parts or over the whole airframe. It is in mayday and going down."),
  MAV_STATE_POWEROFF(7, "System just initialized its power-down sequence, will shut down now.");

  int value;
  String description;

  MAV_STATE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_STATE forValue(int value) {
    switch(value) {
      case 0: return MAV_STATE_UNINIT;
      case 1: return MAV_STATE_BOOT;
      case 2: return MAV_STATE_CALIBRATING;
      case 3: return MAV_STATE_STANDBY;
      case 4: return MAV_STATE_ACTIVE;
      case 5: return MAV_STATE_CRITICAL;
      case 6: return MAV_STATE_EMERGENCY;
      case 7: return MAV_STATE_POWEROFF;
      default: return null;
    }
  }
}

