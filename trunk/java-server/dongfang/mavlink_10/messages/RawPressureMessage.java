package dongfang.mavlink_10.messages;
public class RawPressureMessage extends MavlinkMessage {
  private long timeUsec;
  private short pressAbs;
  private short pressDiff1;
  private short pressDiff2;
  private short temperature;

  public RawPressureMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 28; }

  public String getDescription() { return "The RAW pressure readings for the typical setup of one absolute pressure and one differential pressure sensor. The sensor values should be the raw, UNSCALED ADC values."; }

  public int getExtraCRC() { return 67; }

  public int getLength() { return 16; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a int16_t
  public short getPressAbs() { return pressAbs; }
  public void setPressAbs(short pressAbs) { this.pressAbs=pressAbs; }

  // a int16_t
  public short getPressDiff1() { return pressDiff1; }
  public void setPressDiff1(short pressDiff1) { this.pressDiff1=pressDiff1; }

  // a int16_t
  public short getPressDiff2() { return pressDiff2; }
  public void setPressDiff2(short pressDiff2) { this.pressDiff2=pressDiff2; }

  // a int16_t
  public short getTemperature() { return temperature; }
  public void setTemperature(short temperature) { this.temperature=temperature; }

  public String toString() {
    StringBuilder result = new StringBuilder("RAW_PRESSURE");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("press_abs=");
    result.append(this.pressAbs);
    result.append(",");
    result.append("press_diff1=");
    result.append(this.pressDiff1);
    result.append(",");
    result.append("press_diff2=");
    result.append(this.pressDiff2);
    result.append(",");
    result.append("temperature=");
    result.append(this.temperature);
    return result.toString();
  }
}
