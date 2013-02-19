package dongfang.mavlink_10.enumerations;
public enum MAV_AUTOPILOT {
  MAV_AUTOPILOT_GENERIC(0, "Generic autopilot, full support for everything"),
  MAV_AUTOPILOT_PIXHAWK(1, "PIXHAWK autopilot, http://pixhawk.ethz.ch"),
  MAV_AUTOPILOT_SLUGS(2, "SLUGS autopilot, http://slugsuav.soe.ucsc.edu"),
  MAV_AUTOPILOT_ARDUPILOTMEGA(3, "ArduPilotMega / ArduCopter, http://diydrones.com"),
  MAV_AUTOPILOT_OPENPILOT(4, "OpenPilot, http://openpilot.org"),
  MAV_AUTOPILOT_GENERIC_WAYPOINTS_ONLY(5, "Generic autopilot only supporting simple waypoints"),
  MAV_AUTOPILOT_GENERIC_WAYPOINTS_AND_SIMPLE_NAVIGATION_ONLY(6, "Generic autopilot supporting waypoints and other simple navigation commands"),
  MAV_AUTOPILOT_GENERIC_MISSION_FULL(7, "Generic autopilot supporting the full mission command set"),
  MAV_AUTOPILOT_INVALID(8, "No valid autopilot, e.g. a GCS or other MAVLink component"),
  MAV_AUTOPILOT_PPZ(9, "PPZ UAV - http://nongnu.org/paparazzi"),
  MAV_AUTOPILOT_UDB(10, "UAV Dev Board"),
  MAV_AUTOPILOT_FP(11, "FlexiPilot"),
  MAV_AUTOPILOT_PX4(12, "PX4 Autopilot - http://pixhawk.ethz.ch/px4/");

  int value;
  String description;

  MAV_AUTOPILOT(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_AUTOPILOT forValue(int value) {
    switch(value) {
      case 0: return MAV_AUTOPILOT_GENERIC;
      case 1: return MAV_AUTOPILOT_PIXHAWK;
      case 2: return MAV_AUTOPILOT_SLUGS;
      case 3: return MAV_AUTOPILOT_ARDUPILOTMEGA;
      case 4: return MAV_AUTOPILOT_OPENPILOT;
      case 5: return MAV_AUTOPILOT_GENERIC_WAYPOINTS_ONLY;
      case 6: return MAV_AUTOPILOT_GENERIC_WAYPOINTS_AND_SIMPLE_NAVIGATION_ONLY;
      case 7: return MAV_AUTOPILOT_GENERIC_MISSION_FULL;
      case 8: return MAV_AUTOPILOT_INVALID;
      case 9: return MAV_AUTOPILOT_PPZ;
      case 10: return MAV_AUTOPILOT_UDB;
      case 11: return MAV_AUTOPILOT_FP;
      case 12: return MAV_AUTOPILOT_PX4;
      default: return null;
    }
  }
}

