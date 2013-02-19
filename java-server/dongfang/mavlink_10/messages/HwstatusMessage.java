package dongfang.mavlink_10.messages;
public class HwstatusMessage extends MavlinkMessage {
  private int vcc;
  private short i2cerr;

  public HwstatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 165; }

  public String getDescription() { return "Status of key hardware"; }

  public int getExtraCRC() { return 21; }

  public int getLength() { return 3; }


  // a uint16_t
  public int getVcc() { return vcc; }
  public void setVcc(int vcc) { this.vcc=vcc; }

  // a uint8_t
  public short getI2cerr() { return i2cerr; }
  public void setI2cerr(short i2cerr) { this.i2cerr=i2cerr; }

  public String toString() {
    StringBuilder result = new StringBuilder("HWSTATUS");
    result.append(": ");
    result.append("Vcc=");
    result.append(this.vcc);
    result.append(",");
    result.append("I2Cerr=");
    result.append(this.i2cerr);
    return result.toString();
  }
}
