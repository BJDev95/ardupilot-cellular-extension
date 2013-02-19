package dongfang.mavlink_10.messages;
public class RcChannelsRawMessage extends MavlinkMessage {
  private long timeBootMs;
  private short port;
  private int chan1Raw;
  private int chan2Raw;
  private int chan3Raw;
  private int chan4Raw;
  private int chan5Raw;
  private int chan6Raw;
  private int chan7Raw;
  private int chan8Raw;
  private short rssi;

  public RcChannelsRawMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 35; }

  public String getDescription() { return "The RAW values of the RC channels received. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%. Individual receivers/transmitters might violate this specification."; }

  public int getExtraCRC() { return 244; }

  public int getLength() { return 22; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a uint8_t
  public short getPort() { return port; }
  public void setPort(short port) { this.port=port; }

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

  // a uint8_t
  public short getRssi() { return rssi; }
  public void setRssi(short rssi) { this.rssi=rssi; }

  public String toString() {
    StringBuilder result = new StringBuilder("RC_CHANNELS_RAW");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("port=");
    result.append(this.port);
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
    result.append("rssi=");
    result.append(this.rssi);
    return result.toString();
  }
}
