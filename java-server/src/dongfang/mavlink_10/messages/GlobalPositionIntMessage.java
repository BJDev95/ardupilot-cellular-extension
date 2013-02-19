package dongfang.mavlink_10.messages;
public class GlobalPositionIntMessage extends MavlinkMessage {
  private long timeBootMs;
  private int lat;
  private int lon;
  private int alt;
  private int relativeAlt;
  private short vx;
  private short vy;
  private short vz;
  private int hdg;

  public GlobalPositionIntMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 33; }

  public String getDescription() { return "The filtered global position (e.g. fused GPS and accelerometers). The position is in GPS-frame (right-handed, Z-up). It                is designed as scaled integer message since the resolution of float is not sufficient."; }

  public int getExtraCRC() { return 104; }

  public int getLength() { return 28; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a int32_t
  public int getLat() { return lat; }
  public void setLat(int lat) { this.lat=lat; }

  // a int32_t
  public int getLon() { return lon; }
  public void setLon(int lon) { this.lon=lon; }

  // a int32_t
  public int getAlt() { return alt; }
  public void setAlt(int alt) { this.alt=alt; }

  // a int32_t
  public int getRelativeAlt() { return relativeAlt; }
  public void setRelativeAlt(int relativeAlt) { this.relativeAlt=relativeAlt; }

  // a int16_t
  public short getVx() { return vx; }
  public void setVx(short vx) { this.vx=vx; }

  // a int16_t
  public short getVy() { return vy; }
  public void setVy(short vy) { this.vy=vy; }

  // a int16_t
  public short getVz() { return vz; }
  public void setVz(short vz) { this.vz=vz; }

  // a uint16_t
  public int getHdg() { return hdg; }
  public void setHdg(int hdg) { this.hdg=hdg; }

  public String toString() {
    StringBuilder result = new StringBuilder("GLOBAL_POSITION_INT");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
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
    result.append("relative_alt=");
    result.append(this.relativeAlt);
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
    result.append("hdg=");
    result.append(this.hdg);
    return result.toString();
  }
}
