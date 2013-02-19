package dongfang.mavlink_10.enumerations;
public enum MAV_CMD {
  MAV_CMD_DO_DIGICAM_CONFIGURE(202, "Mission command to configure an on-board camera controller system."),
  MAV_CMD_DO_DIGICAM_CONTROL(203, "Mission command to control an on-board camera controller system."),
  MAV_CMD_DO_MOUNT_CONFIGURE(204, "Mission command to configure a camera or antenna mount"),
  MAV_CMD_DO_MOUNT_CONTROL(205, "Mission command to control a camera or antenna mount");

  int value;
  String description;

  MAV_CMD(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_CMD forValue(int value) {
    switch(value) {
      case 202: return MAV_CMD_DO_DIGICAM_CONFIGURE;
      case 203: return MAV_CMD_DO_DIGICAM_CONTROL;
      case 204: return MAV_CMD_DO_MOUNT_CONFIGURE;
      case 205: return MAV_CMD_DO_MOUNT_CONTROL;
      default: return null;
    }
  }
}

