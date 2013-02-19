package dongfang.mavlink_10.messages;
public class LocalPositionNedMessage extends MavlinkMessage {
  private long timeBootMs;
  private float x;
  private float y;
  private float z;
  private float vx;
  private float vy;
  private float vz;

  public LocalPositionNedMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 32; }

  public String getDescription() { return "The filtered local position (e.g. fused computer vision and accelerometers). Coordinate frame is right-handed, Z-axis down (aeronautical frame, NED / north-east-down convention)"; }

  public int getExtraCRC() { return 185; }

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
  public float getVx() { return vx; }
  public void setVx(float vx) { this.vx=vx; }

  // a float
  public float getVy() { return vy; }
  public void setVy(float vy) { this.vy=vy; }

  // a float
  public float getVz() { return vz; }
  public void setVz(float vz) { this.vz=vz; }

  public String toString() {
    StringBuilder result = new StringBuilder("LOCAL_POSITION_NED");
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
    result.append("vx=");
    result.append(this.vx);
    result.append(",");
    result.append("vy=");
    result.append(this.vy);
    result.append(",");
    result.append("vz=");
    result.append(this.vz);
    return result.toString();
  }
}
