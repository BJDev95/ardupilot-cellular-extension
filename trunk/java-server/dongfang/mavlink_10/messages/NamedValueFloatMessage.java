package dongfang.mavlink_10.messages;
public class NamedValueFloatMessage extends MavlinkMessage {
  private long timeBootMs;
  private String name;
  private float value;

  public NamedValueFloatMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 251; }

  public String getDescription() { return "Send a key-value pair as float. The use of this message is discouraged for normal packets, but a quite efficient way for testing new messages and getting experimental debug output."; }

  public int getExtraCRC() { return 170; }

  public int getLength() { return 18; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a char[]
  public String getName() { return name; }
  public void setName(String name) { this.name=name; }

  // a float
  public float getValue() { return value; }
  public void setValue(float value) { this.value=value; }

  public String toString() {
    StringBuilder result = new StringBuilder("NAMED_VALUE_FLOAT");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("name=");
    result.append(this.name);
    result.append(",");
    result.append("value=");
    result.append(this.value);
    return result.toString();
  }
}
