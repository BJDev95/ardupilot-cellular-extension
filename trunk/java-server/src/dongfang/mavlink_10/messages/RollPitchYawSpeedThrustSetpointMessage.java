package dongfang.mavlink_10.messages;
public class RollPitchYawSpeedThrustSetpointMessage extends MavlinkMessage {
  private long timeBootMs;
  private float rollSpeed;
  private float pitchSpeed;
  private float yawSpeed;
  private float thrust;

  public RollPitchYawSpeedThrustSetpointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 59; }

  public String getDescription() { return "Setpoint in rollspeed, pitchspeed, yawspeed currently active on the system."; }

  public int getExtraCRC() { return 238; }

  public int getLength() { return 20; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a float
  public float getRollSpeed() { return rollSpeed; }
  public void setRollSpeed(float rollSpeed) { this.rollSpeed=rollSpeed; }

  // a float
  public float getPitchSpeed() { return pitchSpeed; }
  public void setPitchSpeed(float pitchSpeed) { this.pitchSpeed=pitchSpeed; }

  // a float
  public float getYawSpeed() { return yawSpeed; }
  public void setYawSpeed(float yawSpeed) { this.yawSpeed=yawSpeed; }

  // a float
  public float getThrust() { return thrust; }
  public void setThrust(float thrust) { this.thrust=thrust; }

  public String toString() {
    StringBuilder result = new StringBuilder("ROLL_PITCH_YAW_SPEED_THRUST_SETPOINT");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("roll_speed=");
    result.append(this.rollSpeed);
    result.append(",");
    result.append("pitch_speed=");
    result.append(this.pitchSpeed);
    result.append(",");
    result.append("yaw_speed=");
    result.append(this.yawSpeed);
    result.append(",");
    result.append("thrust=");
    result.append(this.thrust);
    return result.toString();
  }
}
