package dongfang.mavlink_10.messages;
public class DebugMessage extends MavlinkMessage {
  private long timeBootMs;
  private short ind;
  private float value;

  public DebugMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 254; }

  public String getDescription() { return "Send a debug value. The index is used to discriminate between values. These values show up in the plot of QGroundControl as DEBUG N."; }

  public int getExtraCRC() { return 46; }

  public int getLength() { return 9; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a uint8_t
  public short getInd() { return ind; }
  public void setInd(short ind) { this.ind=ind; }

  // a float
  public float getValue() { return value; }
  public void setValue(float value) { this.value=value; }

  public String toString() {
    StringBuilder result = new StringBuilder("DEBUG");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("ind=");
    result.append(this.ind);
    result.append(",");
    result.append("value=");
    result.append(this.value);
    return result.toString();
  }
}
