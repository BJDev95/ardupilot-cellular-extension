package dongfang.mavlink_10.messages;
public class MemoryVectMessage extends MavlinkMessage {
  private int address;
  private short ver;
  private short type;
  private byte value;

  public MemoryVectMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 249; }

  public String getDescription() { return "Send raw controller memory. The use of this message is discouraged for normal packets, but a quite efficient way for testing new messages and getting experimental debug output."; }

  public int getExtraCRC() { return 204; }

  public int getLength() { return 36; }


  // a uint16_t
  public int getAddress() { return address; }
  public void setAddress(int address) { this.address=address; }

  // a uint8_t
  public short getVer() { return ver; }
  public void setVer(short ver) { this.ver=ver; }

  // a uint8_t
  public short getType() { return type; }
  public void setType(short type) { this.type=type; }

  // a int8_t
  public byte getValue() { return value; }
  public void setValue(byte value) { this.value=value; }

  public String toString() {
    StringBuilder result = new StringBuilder("MEMORY_VECT");
    result.append(": ");
    result.append("address=");
    result.append(this.address);
    result.append(",");
    result.append("ver=");
    result.append(this.ver);
    result.append(",");
    result.append("type=");
    result.append(this.type);
    result.append(",");
    result.append("value=");
    result.append(this.value);
    return result.toString();
  }
}
