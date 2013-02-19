package dongfang.mavlink_10.messages;
public class SetGpsGlobalOriginMessage extends MavlinkMessage {
  private short targetSystem;
  private int latitude;
  private int longitude;
  private int altitude;

  public SetGpsGlobalOriginMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 48; }

  public String getDescription() { return "As local waypoints exist, the global MISSION reference allows to transform between the local coordinate frame and the global (GPS) coordinate frame. This can be necessary when e.g. in- and outdoor settings are connected and the MAV should move from in- to outdoor."; }

  public int getExtraCRC() { return 41; }

  public int getLength() { return 13; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

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
    StringBuilder result = new StringBuilder("SET_GPS_GLOBAL_ORIGIN");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
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
