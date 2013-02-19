package dongfang.mavlink_10.messages;
public class ScaledPressureMessage extends MavlinkMessage {
  private long timeBootMs;
  private float pressAbs;
  private float pressDiff;
  private short temperature;

  public ScaledPressureMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 29; }

  public String getDescription() { return "The pressure readings for the typical setup of one absolute and differential pressure sensor. The units are as specified in each field."; }

  public int getExtraCRC() { return 115; }

  public int getLength() { return 14; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a float
  public float getPressAbs() { return pressAbs; }
  public void setPressAbs(float pressAbs) { this.pressAbs=pressAbs; }

  // a float
  public float getPressDiff() { return pressDiff; }
  public void setPressDiff(float pressDiff) { this.pressDiff=pressDiff; }

  // a int16_t
  public short getTemperature() { return temperature; }
  public void setTemperature(short temperature) { this.temperature=temperature; }

  public String toString() {
    StringBuilder result = new StringBuilder("SCALED_PRESSURE");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("press_abs=");
    result.append(this.pressAbs);
    result.append(",");
    result.append("press_diff=");
    result.append(this.pressDiff);
    result.append(",");
    result.append("temperature=");
    result.append(this.temperature);
    return result.toString();
  }
}
