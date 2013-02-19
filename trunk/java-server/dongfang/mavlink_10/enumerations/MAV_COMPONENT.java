package dongfang.mavlink_10.enumerations;
public enum MAV_COMPONENT {
  MAV_COMP_ID_ALL(0, ""),
  MAV_COMP_ID_GPS(220, ""),
  MAV_COMP_ID_MISSIONPLANNER(190, ""),
  MAV_COMP_ID_PATHPLANNER(195, ""),
  MAV_COMP_ID_MAPPER(180, ""),
  MAV_COMP_ID_CAMERA(100, ""),
  MAV_COMP_ID_IMU(200, ""),
  MAV_COMP_ID_IMU_2(201, ""),
  MAV_COMP_ID_IMU_3(202, ""),
  MAV_COMP_ID_UDP_BRIDGE(240, ""),
  MAV_COMP_ID_UART_BRIDGE(241, ""),
  MAV_COMP_ID_SYSTEM_CONTROL(250, ""),
  MAV_COMP_ID_SERVO1(140, ""),
  MAV_COMP_ID_SERVO2(141, ""),
  MAV_COMP_ID_SERVO3(142, ""),
  MAV_COMP_ID_SERVO4(143, ""),
  MAV_COMP_ID_SERVO5(144, ""),
  MAV_COMP_ID_SERVO6(145, ""),
  MAV_COMP_ID_SERVO7(146, ""),
  MAV_COMP_ID_SERVO8(147, ""),
  MAV_COMP_ID_SERVO9(148, ""),
  MAV_COMP_ID_SERVO10(149, ""),
  MAV_COMP_ID_SERVO11(150, ""),
  MAV_COMP_ID_SERVO12(151, ""),
  MAV_COMP_ID_SERVO13(152, ""),
  MAV_COMP_ID_SERVO14(153, "");

  int value;
  String description;

  MAV_COMPONENT(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAV_COMPONENT forValue(int value) {
    switch(value) {
      case 0: return MAV_COMP_ID_ALL;
      case 220: return MAV_COMP_ID_GPS;
      case 190: return MAV_COMP_ID_MISSIONPLANNER;
      case 195: return MAV_COMP_ID_PATHPLANNER;
      case 180: return MAV_COMP_ID_MAPPER;
      case 100: return MAV_COMP_ID_CAMERA;
      case 200: return MAV_COMP_ID_IMU;
      case 201: return MAV_COMP_ID_IMU_2;
      case 202: return MAV_COMP_ID_IMU_3;
      case 240: return MAV_COMP_ID_UDP_BRIDGE;
      case 241: return MAV_COMP_ID_UART_BRIDGE;
      case 250: return MAV_COMP_ID_SYSTEM_CONTROL;
      case 140: return MAV_COMP_ID_SERVO1;
      case 141: return MAV_COMP_ID_SERVO2;
      case 142: return MAV_COMP_ID_SERVO3;
      case 143: return MAV_COMP_ID_SERVO4;
      case 144: return MAV_COMP_ID_SERVO5;
      case 145: return MAV_COMP_ID_SERVO6;
      case 146: return MAV_COMP_ID_SERVO7;
      case 147: return MAV_COMP_ID_SERVO8;
      case 148: return MAV_COMP_ID_SERVO9;
      case 149: return MAV_COMP_ID_SERVO10;
      case 150: return MAV_COMP_ID_SERVO11;
      case 151: return MAV_COMP_ID_SERVO12;
      case 152: return MAV_COMP_ID_SERVO13;
      case 153: return MAV_COMP_ID_SERVO14;
      default: return null;
    }
  }
}

