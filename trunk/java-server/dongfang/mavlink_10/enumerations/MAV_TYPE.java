package dongfang.mavlink_10.enumerations;
public enum MAV_TYPE {
  MAV_TYPE_GENERIC(0, "Generic micro air vehicle."),
  MAV_TYPE_FIXED_WING(1, "Fixed wing aircraft."),
  MAV_TYPE_QUADROTOR(2, "Quadrotor"),
  MAV_TYPE_COAXIAL(3, "Coaxial helicopter"),
  MAV_TYPE_HELICOPTER(4, "Normal helicopter with tail rotor."),
  MAV_TYPE_ANTENNA_TRACKER(5, "Ground installation"),
  MAV_TYPE_GCS(6, "Operator control unit / ground control station"),
  MAV_TYPE_AIRSHIP(7, "Airship, controlled"),
  MAV_TYPE_FREE_BALLOON(8, "Free balloon, uncontrolled"),
  MAV_TYPE_ROCKET(9, "Rocket"),
  MAV_TYPE_GROUND_ROVER(10, "Ground rover"),
  MAV_TYPE_SURFACE_BOAT(11, "Surface vessel, boat, ship"),
  MAV_TYPE_SUBMARINE(12, "Submarine"),
  MAV_TYPE_HEXAROTOR(13, "Hexarotor"),
  MAV_TYPE_OCTOROTOR(14, "Octorotor"),
  MAV_TYPE_TRICOPTER(15, "Octorotor"),
  MAV_TYPE_FLAPPING_WING(16, "Flapping wing"),
  MAV_TYPE_KITE(17, "Flapping wing");

  int value;
  String description;

  MAV_TYPE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_TYPE forValue(int value) {
    switch(value) {
      case 0: return MAV_TYPE_GENERIC;
      case 1: return MAV_TYPE_FIXED_WING;
      case 2: return MAV_TYPE_QUADROTOR;
      case 3: return MAV_TYPE_COAXIAL;
      case 4: return MAV_TYPE_HELICOPTER;
      case 5: return MAV_TYPE_ANTENNA_TRACKER;
      case 6: return MAV_TYPE_GCS;
      case 7: return MAV_TYPE_AIRSHIP;
      case 8: return MAV_TYPE_FREE_BALLOON;
      case 9: return MAV_TYPE_ROCKET;
      case 10: return MAV_TYPE_GROUND_ROVER;
      case 11: return MAV_TYPE_SURFACE_BOAT;
      case 12: return MAV_TYPE_SUBMARINE;
      case 13: return MAV_TYPE_HEXAROTOR;
      case 14: return MAV_TYPE_OCTOROTOR;
      case 15: return MAV_TYPE_TRICOPTER;
      case 16: return MAV_TYPE_FLAPPING_WING;
      case 17: return MAV_TYPE_KITE;
      default: return null;
    }
  }
}

