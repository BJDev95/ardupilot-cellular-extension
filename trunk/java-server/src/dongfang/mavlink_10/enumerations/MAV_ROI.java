package dongfang.mavlink_10.enumerations;
public enum MAV_ROI {
  MAV_ROI_NONE(0, "No region of interest."),
  MAV_ROI_WPNEXT(1, "Point toward next MISSION."),
  MAV_ROI_WPINDEX(2, "Point toward given MISSION."),
  MAV_ROI_LOCATION(3, "Point toward fixed location."),
  MAV_ROI_TARGET(4, "Point toward of given id.");

  int value;
  String description;

  MAV_ROI(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_ROI forValue(int value) {
    switch(value) {
      case 0: return MAV_ROI_NONE;
      case 1: return MAV_ROI_WPNEXT;
      case 2: return MAV_ROI_WPINDEX;
      case 3: return MAV_ROI_LOCATION;
      case 4: return MAV_ROI_TARGET;
      default: return null;
    }
  }
}

