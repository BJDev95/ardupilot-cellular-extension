package dongfang.mavlink_10.messages;
public class StatustextMessage extends MavlinkMessage {
  private short severity;
  private String text;

  public StatustextMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 253; }

  public String getDescription() { return "Status text message. These messages are printed in yellow in the COMM console of QGroundControl. WARNING: They consume quite some bandwidth, so use only for important status and error messages. If implemented wisely, these messages are buffered on the MCU and sent only at a limited rate (e.g. 10 Hz)."; }

  public int getExtraCRC() { return 83; }

  public int getLength() { return 51; }


  // a uint8_t
  public short getSeverity() { return severity; }
  public void setSeverity(short severity) { this.severity=severity; }

  // a char[]
  public String getText() { return text; }
  public void setText(String text) { this.text=text; }

  public String toString() {
    StringBuilder result = new StringBuilder("STATUSTEXT");
    result.append(": ");
    result.append("severity=");
    result.append(this.severity);
    result.append(",");
    result.append("text=");
    result.append(this.text);
    return result.toString();
  }
}
