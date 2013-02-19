package dongfang.mavlink_10.messages;
public class GpsRawIntMessage extends MavlinkMessage {
  private long timeUsec;
  private short fixType;
  private int lat;
  private int lon;
  private int alt;
  private int eph;
  private int epv;
  private int vel;
  private int cog;
  private short satellitesVisible;

  public GpsRawIntMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 24; }

  public String getDescription() { return "The global position, as returned by the Global Positioning System (GPS). This is                 NOT the global position estimate of the sytem, but rather a RAW sensor value. See message GLOBAL_POSITION for the global position estimate. Coordinate frame is right-handed, Z-axis up (GPS frame)."; }

  public int getExtraCRC() { return 24; }

  public int getLength() { return 30; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a uint8_t
  public short getFixType() { return fixType; }
  public void setFixType(short fixType) { this.fixType=fixType; }

  // a int32_t
  public int getLat() { return lat; }
  public void setLat(int lat) { this.lat=lat; }

  // a int32_t
  public int getLon() { return lon; }
  public void setLon(int lon) { this.lon=lon; }

  // a int32_t
  public int getAlt() { return alt; }
  public void setAlt(int alt) { this.alt=alt; }

  // a uint16_t
  public int getEph() { return eph; }
  public void setEph(int eph) { this.eph=eph; }

  // a uint16_t
  public int getEpv() { return epv; }
  public void setEpv(int epv) { this.epv=epv; }

  // a uint16_t
  public int getVel() { return vel; }
  public void setVel(int vel) { this.vel=vel; }

  // a uint16_t
  public int getCog() { return cog; }
  public void setCog(int cog) { this.cog=cog; }

  // a uint8_t
  public short getSatellitesVisible() { return satellitesVisible; }
  public void setSatellitesVisible(short satellitesVisible) { this.satellitesVisible=satellitesVisible; }

  public String toString() {
    StringBuilder result = new StringBuilder("GPS_RAW_INT");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("fix_type=");
    result.append(this.fixType);
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
    result.append("eph=");
    result.append(this.eph);
    result.append(",");
    result.append("epv=");
    result.append(this.epv);
    result.append(",");
    result.append("vel=");
    result.append(this.vel);
    result.append(",");
    result.append("cog=");
    result.append(this.cog);
    result.append(",");
    result.append("satellites_visible=");
    result.append(this.satellitesVisible);
    return result.toString();
  }
}
