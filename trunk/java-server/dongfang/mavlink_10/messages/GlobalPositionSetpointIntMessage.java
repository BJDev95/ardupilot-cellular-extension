package dongfang.mavlink_10.messages;
public class GlobalPositionSetpointIntMessage extends MavlinkMessage {
  private short coordinateFrame;
  private int latitude;
  private int longitude;
  private int altitude;
  private short yaw;

  public GlobalPositionSetpointIntMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 52; }

  public String getDescription() { return "Transmit the current local setpoint of the controller to other MAVs (collision avoidance) and to the GCS."; }

  public int getExtraCRC() { return 141; }

  public int getLength() { return 15; }


  // a uint8_t
  public short getCoordinateFrame() { return coordinateFrame; }
  public void setCoordinateFrame(short coordinateFrame) { this.coordinateFrame=coordinateFrame; }

  // a int32_t
  public int getLatitude() { return latitude; }
  public void setLatitude(int latitude) { this.latitude=latitude; }

  // a int32_t
  public int getLongitude() { return longitude; }
  public void setLongitude(int longitude) { this.longitude=longitude; }

  // a int32_t
  public int getAltitude() { return altitude; }
  public void setAltitude(int altitude) { this.altitude=altitude; }

  // a int16_t
  public short getYaw() { return yaw; }
  public void setYaw(short yaw) { this.yaw=yaw; }

  public String toString() {
    StringBuilder result = new StringBuilder("GLOBAL_POSITION_SETPOINT_INT");
    result.append(": ");
    result.append("coordinate_frame=");
    result.append(this.coordinateFrame);
    result.append(",");
    result.append("latitude=");
    result.append(this.latitude);
    result.append(",");
    result.append("longitude=");
    result.append(this.longitude);
    result.append(",");
    result.append("altitude=");
    result.append(this.altitude);
    result.append(",");
    result.append("yaw=");
    result.append(this.yaw);
    return result.toString();
  }
}
