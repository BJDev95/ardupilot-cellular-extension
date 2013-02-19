package dongfang.mavlink_10.enumerations;
public enum MAVLINK_DATA_STREAM_TYPE {
  MAVLINK_DATA_STREAM_IMG_JPEG(0, ""),
  MAVLINK_DATA_STREAM_IMG_BMP(1, ""),
  MAVLINK_DATA_STREAM_IMG_RAW8U(2, ""),
  MAVLINK_DATA_STREAM_IMG_RAW32U(3, ""),
  MAVLINK_DATA_STREAM_IMG_PGM(4, ""),
  MAVLINK_DATA_STREAM_IMG_PNG(5, "");

  int value;
  String description;

  MAVLINK_DATA_STREAM_TYPE(int value, String description) {
    this.value=value;
    this.description=description;
  }

  public static MAVLINK_DATA_STREAM_TYPE forValue(int value) {
    switch(value) {
      case 0: return MAVLINK_DATA_STREAM_IMG_JPEG;
      case 1: return MAVLINK_DATA_STREAM_IMG_BMP;
      case 2: return MAVLINK_DATA_STREAM_IMG_RAW8U;
      case 3: return MAVLINK_DATA_STREAM_IMG_RAW32U;
      case 4: return MAVLINK_DATA_STREAM_IMG_PGM;
      case 5: return MAVLINK_DATA_STREAM_IMG_PNG;
      default: return null;
    }
  }
}

