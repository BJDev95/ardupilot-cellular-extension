package dongfang.mavlink_10.messages;
public class RawImuMessage extends MavlinkMessage {
  private long timeUsec;
  private short xacc;
  private short yacc;
  private short zacc;
  private short xgyro;
  private short ygyro;
  private short zgyro;
  private short xmag;
  private short ymag;
  private short zmag;

  public RawImuMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 27; }

  public String getDescription() { return "The RAW IMU readings for the usual 9DOF sensor setup. This message should always contain the true raw values without any scaling to allow data capture and system debugging."; }

  public int getExtraCRC() { return 144; }

  public int getLength() { return 26; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a int16_t
  public short getXacc() { return xacc; }
  public void setXacc(short xacc) { this.xacc=xacc; }

  // a int16_t
  public short getYacc() { return yacc; }
  public void setYacc(short yacc) { this.yacc=yacc; }

  // a int16_t
  public short getZacc() { return zacc; }
  public void setZacc(short zacc) { this.zacc=zacc; }

  // a int16_t
  public short getXgyro() { return xgyro; }
  public void setXgyro(short xgyro) { this.xgyro=xgyro; }

  // a int16_t
  public short getYgyro() { return ygyro; }
  public void setYgyro(short ygyro) { this.ygyro=ygyro; }

  // a int16_t
  public short getZgyro() { return zgyro; }
  public void setZgyro(short zgyro) { this.zgyro=zgyro; }

  // a int16_t
  public short getXmag() { return xmag; }
  public void setXmag(short xmag) { this.xmag=xmag; }

  // a int16_t
  public short getYmag() { return ymag; }
  public void setYmag(short ymag) { this.ymag=ymag; }

  // a int16_t
  public short getZmag() { return zmag; }
  public void setZmag(short zmag) { this.zmag=zmag; }

  public String toString() {
    StringBuilder result = new StringBuilder("RAW_IMU");
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
    return result.toString();
  }
}
