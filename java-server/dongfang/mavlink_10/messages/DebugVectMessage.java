package dongfang.mavlink_10.messages;
public class DebugVectMessage extends MavlinkMessage {
  private String name;
  private long timeUsec;
  private float x;
  private float y;
  private float z;

  public DebugVectMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 250; }

  public String getDescription() { return "<description missing>"; }

  public int getExtraCRC() { return 49; }

  public int getLength() { return 30; }


  // a char[]
  public String getName() { return name; }
  public void setName(String name) { this.name=name; }

  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

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
    StringBuilder result = new StringBuilder("DEBUG_VECT");
    result.append(": ");
    result.append("name=");
    result.append(this.name);
    result.append(",");
    result.append("time_usec=");
    result.append(this.timeUsec);
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
