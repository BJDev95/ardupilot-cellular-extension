package dongfang.mavlink_10.messages;
public class RcChannelsScaledMessage extends MavlinkMessage {
  private long timeBootMs;
  private short port;
  private short chan1Scaled;
  private short chan2Scaled;
  private short chan3Scaled;
  private short chan4Scaled;
  private short chan5Scaled;
  private short chan6Scaled;
  private short chan7Scaled;
  private short chan8Scaled;
  private short rssi;

  public RcChannelsScaledMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 34; }

  public String getDescription() { return "The scaled values of the RC channels received. (-100%) -10000, (0%) 0, (100%) 10000. Channels that are inactive should be set to 65535."; }

  public int getExtraCRC() { return 237; }

  public int getLength() { return 22; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a uint8_t
  public short getPort() { return port; }
  public void setPort(short port) { this.port=port; }

  // a int16_t
  public short getChan1Scaled() { return chan1Scaled; }
  public void setChan1Scaled(short chan1Scaled) { this.chan1Scaled=chan1Scaled; }

  // a int16_t
  public short getChan2Scaled() { return chan2Scaled; }
  public void setChan2Scaled(short chan2Scaled) { this.chan2Scaled=chan2Scaled; }

  // a int16_t
  public short getChan3Scaled() { return chan3Scaled; }
  public void setChan3Scaled(short chan3Scaled) { this.chan3Scaled=chan3Scaled; }

  // a int16_t
  public short getChan4Scaled() { return chan4Scaled; }
  public void setChan4Scaled(short chan4Scaled) { this.chan4Scaled=chan4Scaled; }

  // a int16_t
  public short getChan5Scaled() { return chan5Scaled; }
  public void setChan5Scaled(short chan5Scaled) { this.chan5Scaled=chan5Scaled; }

  // a int16_t
  public short getChan6Scaled() { return chan6Scaled; }
  public void setChan6Scaled(short chan6Scaled) { this.chan6Scaled=chan6Scaled; }

  // a int16_t
  public short getChan7Scaled() { return chan7Scaled; }
  public void setChan7Scaled(short chan7Scaled) { this.chan7Scaled=chan7Scaled; }

  // a int16_t
  public short getChan8Scaled() { return chan8Scaled; }
  public void setChan8Scaled(short chan8Scaled) { this.chan8Scaled=chan8Scaled; }

  // a uint8_t
  public short getRssi() { return rssi; }
  public void setRssi(short rssi) { this.rssi=rssi; }

  public String toString() {
    StringBuilder result = new StringBuilder("RC_CHANNELS_SCALED");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("port=");
    result.append(this.port);
    result.append(",");
    result.append("chan1_scaled=");
    result.append(this.chan1Scaled);
    result.append(",");
    result.append("chan2_scaled=");
    result.append(this.chan2Scaled);
    result.append(",");
    result.append("chan3_scaled=");
    result.append(this.chan3Scaled);
    result.append(",");
    result.append("chan4_scaled=");
    result.append(this.chan4Scaled);
    result.append(",");
    result.append("chan5_scaled=");
    result.append(this.chan5Scaled);
    result.append(",");
    result.append("chan6_scaled=");
    result.append(this.chan6Scaled);
    result.append(",");
    result.append("chan7_scaled=");
    result.append(this.chan7Scaled);
    result.append(",");
    result.append("chan8_scaled=");
    result.append(this.chan8Scaled);
    result.append(",");
    result.append("rssi=");
    result.append(this.rssi);
    return result.toString();
  }
}
