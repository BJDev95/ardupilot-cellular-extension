package dongfang.mavlink_10.messages;
public class GpsGlobalOriginMessage extends MavlinkMessage {
  private int latitude;
  private int longitude;
  private int altitude;

  public GpsGlobalOriginMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 49; }

  public String getDescription() { return "Once the MAV sets a new GPS-Local correspondence, this message announces the origin (0,0,0) position"; }

  public int getExtraCRC() { return 39; }

  public int getLength() { return 12; }


  // a int32_t
  public int getLatitude() { return latitude; }
  public void setLatitude(int latitude) { this.latitude=latitude; }

  // a int32_t
  public int getLongitude() { return longitude; }
  public void setLongitude(int longitude) { this.longitude=longitude; }

  // a int32_t
  public int getAltitude() { return altitude; }
  public void setAltitude(int altitude) { this.altitude=altitude; }

  public String toString() {
    StringBuilder result = new StringBuilder("GPS_GLOBAL_ORIGIN");
    result.append(": ");
    result.append("latitude=");
    result.append(this.latitude);
    result.append(",");
    result.append("longitude=");
    result.append(this.longitude);
    result.append(",");
    result.append("altitude=");
    result.append(this.altitude);
    return result.toString();
  }
}
