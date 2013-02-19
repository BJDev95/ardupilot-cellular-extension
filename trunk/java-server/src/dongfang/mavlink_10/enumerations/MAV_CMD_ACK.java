package dongfang.mavlink_10.enumerations;
public enum MAV_CMD_ACK {
  MAV_CMD_ACK_OK(0, "Command / mission item is ok."),
  MAV_CMD_ACK_ERR_FAIL(1, "Generic error message if none of the other reasons fails or if no detailed error reporting is implemented."),
  MAV_CMD_ACK_ERR_ACCESS_DENIED(2, "The system is refusing to accept this command from this source / communication partner."),
  MAV_CMD_ACK_ERR_NOT_SUPPORTED(3, "Command or mission item is not supported, other commands would be accepted."),
  MAV_CMD_ACK_ERR_COORDINATE_FRAME_NOT_SUPPORTED(4, "The coordinate frame of this command / mission item is not supported."),
  MAV_CMD_ACK_ERR_COORDINATES_OUT_OF_RANGE(5, "The coordinate frame of this command is ok, but he coordinate values exceed the safety limits of this system. This is a generic error, please use the more specific error messages below if possible."),
  MAV_CMD_ACK_ERR_X_LAT_OUT_OF_RANGE(6, "The X or latitude value is out of range."),
  MAV_CMD_ACK_ERR_Y_LON_OUT_OF_RANGE(7, "The Y or longitude value is out of range."),
  MAV_CMD_ACK_ERR_Z_ALT_OUT_OF_RANGE(8, "The Z or altitude value is out of range.");

  int value;
  String description;

  MAV_CMD_ACK(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_CMD_ACK forValue(int value) {
    switch(value) {
      case 0: return MAV_CMD_ACK_OK;
      case 1: return MAV_CMD_ACK_ERR_FAIL;
      case 2: return MAV_CMD_ACK_ERR_ACCESS_DENIED;
      case 3: return MAV_CMD_ACK_ERR_NOT_SUPPORTED;
      case 4: return MAV_CMD_ACK_ERR_COORDINATE_FRAME_NOT_SUPPORTED;
      case 5: return MAV_CMD_ACK_ERR_COORDINATES_OUT_OF_RANGE;
      case 6: return MAV_CMD_ACK_ERR_X_LAT_OUT_OF_RANGE;
      case 7: return MAV_CMD_ACK_ERR_Y_LON_OUT_OF_RANGE;
      case 8: return MAV_CMD_ACK_ERR_Z_ALT_OUT_OF_RANGE;
      default: return null;
    }
  }
}

