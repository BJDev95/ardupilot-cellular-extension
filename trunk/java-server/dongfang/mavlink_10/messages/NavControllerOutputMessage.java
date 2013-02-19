package dongfang.mavlink_10.messages;
public class NavControllerOutputMessage extends MavlinkMessage {
  private float navRoll;
  private float navPitch;
  private short navBearing;
  private short targetBearing;
  private int wpDist;
  private float altError;
  private float aspdError;
  private float xtrackError;

  public NavControllerOutputMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 62; }

  public String getDescription() { return "Outputs of the APM navigation controller. The primary use of this message is to check the response and signs of the controller before actual flight and to assist with tuning controller parameters."; }

  public int getExtraCRC() { return 183; }

  public int getLength() { return 26; }


  // a float
  public float getNavRoll() { return navRoll; }
  public void setNavRoll(float navRoll) { this.navRoll=navRoll; }

  // a float
  public float getNavPitch() { return navPitch; }
  public void setNavPitch(float navPitch) { this.navPitch=navPitch; }

  // a int16_t
  public short getNavBearing() { return navBearing; }
  public void setNavBearing(short navBearing) { this.navBearing=navBearing; }

  // a int16_t
  public short getTargetBearing() { return targetBearing; }
  public void setTargetBearing(short targetBearing) { this.targetBearing=targetBearing; }

  // a uint16_t
  public int getWpDist() { return wpDist; }
  public void setWpDist(int wpDist) { this.wpDist=wpDist; }

  // a float
  public float getAltError() { return altError; }
  public void setAltError(float altError) { this.altError=altError; }

  // a float
  public float getAspdError() { return aspdError; }
  public void setAspdError(float aspdError) { this.aspdError=aspdError; }

  // a float
  public float getXtrackError() { return xtrackError; }
  public void setXtrackError(float xtrackError) { this.xtrackError=xtrackError; }

  public String toString() {
    StringBuilder result = new StringBuilder("NAV_CONTROLLER_OUTPUT");
    result.append(": ");
    result.append("nav_roll=");
    result.append(this.navRoll);
    result.append(",");
    result.append("nav_pitch=");
    result.append(this.navPitch);
    result.append(",");
    result.append("nav_bearing=");
    result.append(this.navBearing);
    result.append(",");
    result.append("target_bearing=");
    result.append(this.targetBearing);
    result.append(",");
    result.append("wp_dist=");
    result.append(this.wpDist);
    result.append(",");
    result.append("alt_error=");
    result.append(this.altError);
    result.append(",");
    result.append("aspd_error=");
    result.append(this.aspdError);
    result.append(",");
    result.append("xtrack_error=");
    result.append(this.xtrackError);
    return result.toString();
  }
}
