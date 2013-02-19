package dongfang.mavlink_10.messages;
public class SimstateMessage extends MavlinkMessage {
  private float roll;
  private float pitch;
  private float yaw;
  private float xacc;
  private float yacc;
  private float zacc;
  private float xgyro;
  private float ygyro;
  private float zgyro;
  private float lat;
  private float lng;

  public SimstateMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 164; }

  public String getDescription() { return "Status of simulation environment, if used"; }

  public int getExtraCRC() { return 111; }

  public int getLength() { return 44; }


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
  public float getLat() { return lat; }
  public void setLat(float lat) { this.lat=lat; }

  // a float
  public float getLng() { return lng; }
  public void setLng(float lng) { this.lng=lng; }

  public String toString() {
    StringBuilder result = new StringBuilder("SIMSTATE");
    result.append(": ");
    result.append("roll=");
    result.append(this.roll);
    result.append(",");
    result.append("pitch=");
    result.append(this.pitch);
    result.append(",");
    result.append("yaw=");
    result.append(this.yaw);
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
    result.append("lat=");
    result.append(this.lat);
    result.append(",");
    result.append("lng=");
    result.append(this.lng);
    return result.toString();
  }
}
