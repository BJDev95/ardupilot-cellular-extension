package dongfang.mavlink_10.messages;
public class Data32Message extends MavlinkMessage {
  private short type;
  private short len;
  private short data;

  public Data32Message(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 170; }

  public String getDescription() { return "Data packet, size 32"; }

  public int getExtraCRC() { return 0; }

  public int getLength() { return 34; }


  // a uint8_t
  public short getType() { return type; }
  public void setType(short type) { this.type=type; }

  // a uint8_t
  public short getLen() { return len; }
  public void setLen(short len) { this.len=len; }

  // a uint8_t
  public short getData() { return data; }
  public void setData(short data) { this.data=data; }

  public String toString() {
    StringBuilder result = new StringBuilder("DATA32");
    result.append(": ");
    result.append("type=");
    result.append(this.type);
    result.append(",");
    result.append("len=");
    result.append(this.len);
    result.append(",");
    result.append("data=");
    result.append(this.data);
    return result.toString();
  }
}
