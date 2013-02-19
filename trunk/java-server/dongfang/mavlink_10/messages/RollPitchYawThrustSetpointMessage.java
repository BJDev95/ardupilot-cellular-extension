package dongfang.mavlink_10.messages;
public class RollPitchYawThrustSetpointMessage extends MavlinkMessage {
  private long timeBootMs;
  private float roll;
  private float pitch;
  private float yaw;
  private float thrust;

  public RollPitchYawThrustSetpointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 58; }

  public String getDescription() { return "Setpoint in roll, pitch, yaw currently active on the system."; }

  public int getExtraCRC() { return 239; }

  public int getLength() { return 20; }


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
  public float getThrust() { return thrust; }
  public void setThrust(float thrust) { this.thrust=thrust; }

  public String toString() {
    StringBuilder result = new StringBuilder("ROLL_PITCH_YAW_THRUST_SETPOINT");
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
    result.append("thrust=");
    result.append(this.thrust);
    return result.toString();
  }
}
