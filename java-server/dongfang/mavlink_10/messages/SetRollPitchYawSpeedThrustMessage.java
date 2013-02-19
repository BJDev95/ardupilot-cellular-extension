package dongfang.mavlink_10.messages;
public class SetRollPitchYawSpeedThrustMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private float rollSpeed;
  private float pitchSpeed;
  private float yawSpeed;
  private float thrust;

  public SetRollPitchYawSpeedThrustMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 57; }

  public String getDescription() { return "Set roll, pitch and yaw."; }

  public int getExtraCRC() { return 24; }

  public int getLength() { return 18; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

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
    StringBuilder result = new StringBuilder("SET_ROLL_PITCH_YAW_SPEED_THRUST");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
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
