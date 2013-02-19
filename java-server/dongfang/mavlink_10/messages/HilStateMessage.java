package dongfang.mavlink_10.messages;
public class HilStateMessage extends MavlinkMessage {
  private long timeUsec;
  private float roll;
  private float pitch;
  private float yaw;
  private float rollspeed;
  private float pitchspeed;
  private float yawspeed;
  private int lat;
  private int lon;
  private int alt;
  private short vx;
  private short vy;
  private short vz;
  private short xacc;
  private short yacc;
  private short zacc;

  public HilStateMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 90; }

  public String getDescription() { return "Sent from simulation to autopilot. This packet is useful for high throughput applications such as hardware in the loop simulations."; }

  public int getExtraCRC() { return 183; }

  public int getLength() { return 56; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a float
  public float getRoll() { return roll; }
  public void setRoll(float roll) { this.roll=roll; }

  // a float
  public float getPitch() { return pitch; }
  public void setPitch(float pitch) { this.pitch=pitch; }

  // a float
  public float getYaw() { return yaw; }
  public void setYaw(float yaw) { this.yaw=yaw; }

  // a float
  public float getRollspeed() { return rollspeed; }
  public void setRollspeed(float rollspeed) { this.rollspeed=rollspeed; }

  // a float
  public float getPitchspeed() { return pitchspeed; }
  public void setPitchspeed(float pitchspeed) { this.pitchspeed=pitchspeed; }

  // a float
  public float getYawspeed() { return yawspeed; }
  public void setYawspeed(float yawspeed) { this.yawspeed=yawspeed; }

  // a int32_t
  public int getLat() { return lat; }
  public void setLat(int lat) { this.lat=lat; }

  // a int32_t
  public int getLon() { return lon; }
  public void setLon(int lon) { this.lon=lon; }

  // a int32_t
  public int getAlt() { return alt; }
  public void setAlt(int alt) { this.alt=alt; }

  // a int16_t
  public short getVx() { return vx; }
  public void setVx(short vx) { this.vx=vx; }

  // a int16_t
  public short getVy() { return vy; }
  public void setVy(short vy) { this.vy=vy; }

  // a int16_t
  public short getVz() { return vz; }
  public void setVz(short vz) { this.vz=vz; }

  // a int16_t
  public short getXacc() { return xacc; }
  public void setXacc(short xacc) { this.xacc=xacc; }

  // a int16_t
  public short getYacc() { return yacc; }
  public void setYacc(short yacc) { this.yacc=yacc; }

  // a int16_t
  public short getZacc() { return zacc; }
  public void setZacc(short zacc) { this.zacc=zacc; }

  public String toString() {
    StringBuilder result = new StringBuilder("HIL_STATE");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("roll=");
    result.append(this.roll);
    result.append(",");
    result.append("pitch=");
    result.append(this.pitch);
    result.append(",");
    result.append("yaw=");
    result.append(this.yaw);
    result.append(",");
    result.append("rollspeed=");
    result.append(this.rollspeed);
    result.append(",");
    result.append("pitchspeed=");
    result.append(this.pitchspeed);
    result.append(",");
    result.append("yawspeed=");
    result.append(this.yawspeed);
    result.append(",");
    result.append("lat=");
    result.append(this.lat);
    result.append(",");
    result.append("lon=");
    result.append(this.lon);
    result.append(",");
    result.append("alt=");
    result.append(this.alt);
    result.append(",");
    result.append("vx=");
    result.append(this.vx);
    result.append(",");
    result.append("vy=");
    result.append(this.vy);
    result.append(",");
    result.append("vz=");
    result.append(this.vz);
    result.append(",");
    result.append("xacc=");
    result.append(this.xacc);
    result.append(",");
    result.append("yacc=");
    result.append(this.yacc);
    result.append(",");
    result.append("zacc=");
    result.append(this.zacc);
    return result.toString();
  }
}
