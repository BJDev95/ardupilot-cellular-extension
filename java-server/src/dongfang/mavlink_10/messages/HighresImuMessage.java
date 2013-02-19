package dongfang.mavlink_10.messages;
public class HighresImuMessage extends MavlinkMessage {
  private long timeUsec;
  private float xacc;
  private float yacc;
  private float zacc;
  private float xgyro;
  private float ygyro;
  private float zgyro;
  private float xmag;
  private float ymag;
  private float zmag;
  private float absPressure;
  private float diffPressure;
  private float pressureAlt;
  private float temperature;
  private int fieldsUpdated;

  public HighresImuMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 105; }

  public String getDescription() { return "The IMU readings in SI units in NED body frame"; }

  public int getExtraCRC() { return 0; }

  public int getLength() { return 62; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a float
  public float getXacc() { return xacc; }
  public void setXacc(float xacc) { this.xacc=xacc; }

  // a float
  public float getYacc() { return yacc; }
  public void setYacc(float yacc) { this.yacc=yacc; }

  // a float
  public float getZacc() { return zacc; }
  public void setZacc(float zacc) { this.zacc=zacc; }

  // a float
  public float getXgyro() { return xgyro; }
  public void setXgyro(float xgyro) { this.xgyro=xgyro; }

  // a float
  public float getYgyro() { return ygyro; }
  public void setYgyro(float ygyro) { this.ygyro=ygyro; }

  // a float
  public float getZgyro() { return zgyro; }
  public void setZgyro(float zgyro) { this.zgyro=zgyro; }

  // a float
  public float getXmag() { return xmag; }
  public void setXmag(float xmag) { this.xmag=xmag; }

  // a float
  public float getYmag() { return ymag; }
  public void setYmag(float ymag) { this.ymag=ymag; }

  // a float
  public float getZmag() { return zmag; }
  public void setZmag(float zmag) { this.zmag=zmag; }

  // a float
  public float getAbsPressure() { return absPressure; }
  public void setAbsPressure(float absPressure) { this.absPressure=absPressure; }

  // a float
  public float getDiffPressure() { return diffPressure; }
  public void setDiffPressure(float diffPressure) { this.diffPressure=diffPressure; }

  // a float
  public float getPressureAlt() { return pressureAlt; }
  public void setPressureAlt(float pressureAlt) { this.pressureAlt=pressureAlt; }

  // a float
  public float getTemperature() { return temperature; }
  public void setTemperature(float temperature) { this.temperature=temperature; }

  // a uint16_t
  public int getFieldsUpdated() { return fieldsUpdated; }
  public void setFieldsUpdated(int fieldsUpdated) { this.fieldsUpdated=fieldsUpdated; }

  public String toString() {
    StringBuilder result = new StringBuilder("HIGHRES_IMU");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("xacc=");
    result.append(this.xacc);
    result.append(",");
    result.append("yacc=");
    result.append(this.yacc);
    result.append(",");
    result.append("zacc=");
    result.append(this.zacc);
    result.append(",");
    result.append("xgyro=");
    result.append(this.xgyro);
    result.append(",");
    result.append("ygyro=");
    result.append(this.ygyro);
    result.append(",");
    result.append("zgyro=");
    result.append(this.zgyro);
    result.append(",");
    result.append("xmag=");
    result.append(this.xmag);
    result.append(",");
    result.append("ymag=");
    result.append(this.ymag);
    result.append(",");
    result.append("zmag=");
    result.append(this.zmag);
    result.append(",");
    result.append("abs_pressure=");
    result.append(this.absPressure);
    result.append(",");
    result.append("diff_pressure=");
    result.append(this.diffPressure);
    result.append(",");
    result.append("pressure_alt=");
    result.append(this.pressureAlt);
    result.append(",");
    result.append("temperature=");
    result.append(this.temperature);
    result.append(",");
    result.append("fields_updated=");
    result.append(this.fieldsUpdated);
    return result.toString();
  }
}
