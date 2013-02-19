package dongfang.mavlink_10.messages;
public class LocalPositionSetpointMessage extends MavlinkMessage {
  private short coordinateFrame;
  private float x;
  private float y;
  private float z;
  private float yaw;

  public LocalPositionSetpointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 51; }

  public String getDescription() { return "Transmit the current local setpoint of the controller to other MAVs (collision avoidance) and to the GCS."; }

  public int getExtraCRC() { return 223; }

  public int getLength() { return 17; }


  // a uint8_t
  public short getCoordinateFrame() { return coordinateFrame; }
  public void setCoordinateFrame(short coordinateFrame) { this.coordinateFrame=coordinateFrame; }

  // a float
  public float getX() { return x; }
  public void setX(float x) { this.x=x; }

  // a float
  public float getY() { return y; }
  public void setY(float y) { this.y=y; }

  // a float
  public float getZ() { return z; }
  public void setZ(float z) { this.z=z; }

  // a float
  public float getYaw() { return yaw; }
  public void setYaw(float yaw) { this.yaw=yaw; }

  public String toString() {
    StringBuilder result = new StringBuilder("LOCAL_POSITION_SETPOINT");
    result.append(": ");
    result.append("coordinate_frame=");
    result.append(this.coordinateFrame);
    result.append(",");
    result.append("x=");
    result.append(this.x);
    result.append(",");
    result.append("y=");
    result.append(this.y);
    result.append(",");
    result.append("z=");
    result.append(this.z);
    result.append(",");
    result.append("yaw=");
    result.append(this.yaw);
    return result.toString();
  }
}
