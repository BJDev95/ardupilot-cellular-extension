package dongfang.mavlink_10.messages;
public class ViconPositionEstimateMessage extends MavlinkMessage {
  private long usec;
  private float x;
  private float y;
  private float z;
  private float roll;
  private float pitch;
  private float yaw;

  public ViconPositionEstimateMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 104; }

  public String getDescription() { return "<description missing>"; }

  public int getExtraCRC() { return 56; }

  public int getLength() { return 32; }


  // a uint64_t
  public long getUsec() { return usec; }
  public void setUsec(long usec) { this.usec=usec; }

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
    StringBuilder result = new StringBuilder("VICON_POSITION_ESTIMATE");
    result.append(": ");
    result.append("usec=");
    result.append(this.usec);
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
