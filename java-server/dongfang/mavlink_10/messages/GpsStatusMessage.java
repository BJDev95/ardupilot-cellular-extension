package dongfang.mavlink_10.messages;
public class GpsStatusMessage extends MavlinkMessage {
  private short satellitesVisible;
  private short satellitePrn;
  private short satelliteUsed;
  private short satelliteElevation;
  private short satelliteAzimuth;
  private short satelliteSnr;

  public GpsStatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 25; }

  public String getDescription() { return "The positioning status, as reported by GPS. This message is intended to display status information about each satellite visible to the receiver. See message GLOBAL_POSITION for the global position estimate. This message can contain information for up to 20 satellites."; }

  public int getExtraCRC() { return 23; }

  public int getLength() { return 101; }


  // a uint8_t
  public short getSatellitesVisible() { return satellitesVisible; }
  public void setSatellitesVisible(short satellitesVisible) { this.satellitesVisible=satellitesVisible; }

  // a uint8_t
  public short getSatellitePrn() { return satellitePrn; }
  public void setSatellitePrn(short satellitePrn) { this.satellitePrn=satellitePrn; }

  // a uint8_t
  public short getSatelliteUsed() { return satelliteUsed; }
  public void setSatelliteUsed(short satelliteUsed) { this.satelliteUsed=satelliteUsed; }

  // a uint8_t
  public short getSatelliteElevation() { return satelliteElevation; }
  public void setSatelliteElevation(short satelliteElevation) { this.satelliteElevation=satelliteElevation; }

  // a uint8_t
  public short getSatelliteAzimuth() { return satelliteAzimuth; }
  public void setSatelliteAzimuth(short satelliteAzimuth) { this.satelliteAzimuth=satelliteAzimuth; }

  // a uint8_t
  public short getSatelliteSnr() { return satelliteSnr; }
  public void setSatelliteSnr(short satelliteSnr) { this.satelliteSnr=satelliteSnr; }

  public String toString() {
    StringBuilder result = new StringBuilder("GPS_STATUS");
    result.append(": ");
    result.append("satellites_visible=");
    result.append(this.satellitesVisible);
    result.append(",");
    result.append("satellite_prn=");
    result.append(this.satellitePrn);
    result.append(",");
    result.append("satellite_used=");
    result.append(this.satelliteUsed);
    result.append(",");
    result.append("satellite_elevation=");
    result.append(this.satelliteElevation);
    result.append(",");
    result.append("satellite_azimuth=");
    result.append(this.satelliteAzimuth);
    result.append(",");
    result.append("satellite_snr=");
    result.append(this.satelliteSnr);
    return result.toString();
  }
}
