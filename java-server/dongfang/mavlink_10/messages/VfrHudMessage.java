package dongfang.mavlink_10.messages;
public class VfrHudMessage extends MavlinkMessage {
  private float airspeed;
  private float groundspeed;
  private short heading;
  private int throttle;
  private float alt;
  private float climb;

  public VfrHudMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 74; }

  public String getDescription() { return "Metrics typically displayed on a HUD for fixed wing aircraft"; }

  public int getExtraCRC() { return 20; }

  public int getLength() { return 20; }


  // a float
  public float getAirspeed() { return airspeed; }
  public void setAirspeed(float airspeed) { this.airspeed=airspeed; }

  // a float
  public float getGroundspeed() { return groundspeed; }
  public void setGroundspeed(float groundspeed) { this.groundspeed=groundspeed; }

  // a int16_t
  public short getHeading() { return heading; }
  public void setHeading(short heading) { this.heading=heading; }

  // a uint16_t
  public int getThrottle() { return throttle; }
  public void setThrottle(int throttle) { this.throttle=throttle; }

  // a float
  public float getAlt() { return alt; }
  public void setAlt(float alt) { this.alt=alt; }

  // a float
  public float getClimb() { return climb; }
  public void setClimb(float climb) { this.climb=climb; }

  public String toString() {
    StringBuilder result = new StringBuilder("VFR_HUD");
    result.append(": ");
    result.append("airspeed=");
    result.append(this.airspeed);
    result.append(",");
    result.append("groundspeed=");
    result.append(this.groundspeed);
    result.append(",");
    result.append("heading=");
    result.append(this.heading);
    result.append(",");
    result.append("throttle=");
    result.append(this.throttle);
    result.append(",");
    result.append("alt=");
    result.append(this.alt);
    result.append(",");
    result.append("climb=");
    result.append(this.climb);
    return result.toString();
  }
}
