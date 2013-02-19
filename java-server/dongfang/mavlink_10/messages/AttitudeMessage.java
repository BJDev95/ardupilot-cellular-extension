package dongfang.mavlink_10.messages;
public class AttitudeMessage extends MavlinkMessage {
  private long timeBootMs;
  private float roll;
  private float pitch;
  private float yaw;
  private float rollspeed;
  private float pitchspeed;
  private float yawspeed;

  public AttitudeMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 30; }

  public String getDescription() { return "The attitude in the aeronautical frame (right-handed, Z-down, X-front, Y-right)."; }

  public int getExtraCRC() { return 39; }

  public int getLength() { return 28; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

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

  public String toString() {
    StringBuilder result = new StringBuilder("ATTITUDE");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
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
    return result.toString();
  }
}
