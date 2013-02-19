package dongfang.mavlink_10.messages;
public class VisionSpeedEstimateMessage extends MavlinkMessage {
  private long usec;
  private float x;
  private float y;
  private float z;

  public VisionSpeedEstimateMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 103; }

  public String getDescription() { return "<description missing>"; }

  public int getExtraCRC() { return 208; }

  public int getLength() { return 20; }


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

  public String toString() {
    StringBuilder result = new StringBuilder("VISION_SPEED_ESTIMATE");
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
    return result.toString();
  }
}
