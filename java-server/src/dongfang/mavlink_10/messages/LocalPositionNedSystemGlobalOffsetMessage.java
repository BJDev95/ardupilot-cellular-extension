package dongfang.mavlink_10.messages;
public class LocalPositionNedSystemGlobalOffsetMessage extends MavlinkMessage {
  private long timeBootMs;
  private float x;
  private float y;
  private float z;
  private float roll;
  private float pitch;
  private float yaw;

  public LocalPositionNedSystemGlobalOffsetMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 89; }

  public String getDescription() { return "The offset in X, Y, Z and yaw between the LOCAL_POSITION_NED messages of MAV X and the global coordinate frame in NED coordinates. Coordinate frame is right-handed, Z-axis down (aeronautical frame, NED / north-east-down convention)"; }

  public int getExtraCRC() { return 231; }

  public int getLength() { return 28; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

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
  public float getRoll() { return roll; }
  public void setRoll(float roll) { this.roll=roll; }

  // a float
  public float getPitch() { return pitch; }
  public void setPitch(float pitch) { this.pitch=pitch; }

  // a float
  public float getYaw() { return yaw; }
  public void setYaw(float yaw) { this.yaw=yaw; }

  public String toString() {
    StringBuilder result = new StringBuilder("LOCAL_POSITION_NED_SYSTEM_GLOBAL_OFFSET");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
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
    result.append("roll=");
    result.append(this.roll);
    result.append(",");
    result.append("pitch=");
    result.append(this.pitch);
    result.append(",");
    result.append("yaw=");
    result.append(this.yaw);
    return result.toString();
  }
}
