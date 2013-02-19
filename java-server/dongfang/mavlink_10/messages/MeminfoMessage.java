package dongfang.mavlink_10.messages;
public class MeminfoMessage extends MavlinkMessage {
  private int brkval;
  private int freemem;

  public MeminfoMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 152; }

  public String getDescription() { return "state of APM memory"; }

  public int getExtraCRC() { return 208; }

  public int getLength() { return 4; }


  // a uint16_t
  public int getBrkval() { return brkval; }
  public void setBrkval(int brkval) { this.brkval=brkval; }

  // a uint16_t
  public int getFreemem() { return freemem; }
  public void setFreemem(int freemem) { this.freemem=freemem; }

  public String toString() {
    StringBuilder result = new StringBuilder("MEMINFO");
    result.append(": ");
    result.append("brkval=");
    result.append(this.brkval);
    result.append(",");
    result.append("freemem=");
    result.append(this.freemem);
    return result.toString();
  }
}
