package dongfang.mavlink_10.messages;
public class HilRcInputsRawMessage extends MavlinkMessage {
  private long timeUsec;
  private int chan1Raw;
  private int chan2Raw;
  private int chan3Raw;
  private int chan4Raw;
  private int chan5Raw;
  private int chan6Raw;
  private int chan7Raw;
  private int chan8Raw;
  private int chan9Raw;
  private int chan10Raw;
  private int chan11Raw;
  private int chan12Raw;
  private short rssi;

  public HilRcInputsRawMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 92; }

  public String getDescription() { return "Sent from simulation to autopilot. The RAW values of the RC channels received. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%. Individual receivers/transmitters might violate this specification."; }

  public int getExtraCRC() { return 54; }

  public int getLength() { return 33; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a uint16_t
  public int getChan1Raw() { return chan1Raw; }
  public void setChan1Raw(int chan1Raw) { this.chan1Raw=chan1Raw; }

  // a uint16_t
  public int getChan2Raw() { return chan2Raw; }
  public void setChan2Raw(int chan2Raw) { this.chan2Raw=chan2Raw; }

  // a uint16_t
  public int getChan3Raw() { return chan3Raw; }
  public void setChan3Raw(int chan3Raw) { this.chan3Raw=chan3Raw; }

  // a uint16_t
  public int getChan4Raw() { return chan4Raw; }
  public void setChan4Raw(int chan4Raw) { this.chan4Raw=chan4Raw; }

  // a uint16_t
  public int getChan5Raw() { return chan5Raw; }
  public void setChan5Raw(int chan5Raw) { this.chan5Raw=chan5Raw; }

  // a uint16_t
  public int getChan6Raw() { return chan6Raw; }
  public void setChan6Raw(int chan6Raw) { this.chan6Raw=chan6Raw; }

  // a uint16_t
  public int getChan7Raw() { return chan7Raw; }
  public void setChan7Raw(int chan7Raw) { this.chan7Raw=chan7Raw; }

  // a uint16_t
  public int getChan8Raw() { return chan8Raw; }
  public void setChan8Raw(int chan8Raw) { this.chan8Raw=chan8Raw; }

  // a uint16_t
  public int getChan9Raw() { return chan9Raw; }
  public void setChan9Raw(int chan9Raw) { this.chan9Raw=chan9Raw; }

  // a uint16_t
  public int getChan10Raw() { return chan10Raw; }
  public void setChan10Raw(int chan10Raw) { this.chan10Raw=chan10Raw; }

  // a uint16_t
  public int getChan11Raw() { return chan11Raw; }
  public void setChan11Raw(int chan11Raw) { this.chan11Raw=chan11Raw; }

  // a uint16_t
  public int getChan12Raw() { return chan12Raw; }
  public void setChan12Raw(int chan12Raw) { this.chan12Raw=chan12Raw; }

  // a uint8_t
  public short getRssi() { return rssi; }
  public void setRssi(short rssi) { this.rssi=rssi; }

  public String toString() {
    StringBuilder result = new StringBuilder("HIL_RC_INPUTS_RAW");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("chan1_raw=");
    result.append(this.chan1Raw);
    result.append(",");
    result.append("chan2_raw=");
    result.append(this.chan2Raw);
    result.append(",");
    result.append("chan3_raw=");
    result.append(this.chan3Raw);
    result.append(",");
    result.append("chan4_raw=");
    result.append(this.chan4Raw);
    result.append(",");
    result.append("chan5_raw=");
    result.append(this.chan5Raw);
    result.append(",");
    result.append("chan6_raw=");
    result.append(this.chan6Raw);
    result.append(",");
    result.append("chan7_raw=");
    result.append(this.chan7Raw);
    result.append(",");
    result.append("chan8_raw=");
    result.append(this.chan8Raw);
    result.append(",");
    result.append("chan9_raw=");
    result.append(this.chan9Raw);
    result.append(",");
    result.append("chan10_raw=");
    result.append(this.chan10Raw);
    result.append(",");
    result.append("chan11_raw=");
    result.append(this.chan11Raw);
    result.append(",");
    result.append("chan12_raw=");
    result.append(this.chan12Raw);
    result.append(",");
    result.append("rssi=");
    result.append(this.rssi);
    return result.toString();
  }
}
