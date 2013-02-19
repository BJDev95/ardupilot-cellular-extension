package dongfang.mavlink_10.messages;
public class SetRollPitchYawThrustMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private float roll;
  private float pitch;
  private float yaw;
  private float thrust;

  public SetRollPitchYawThrustMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 56; }

  public String getDescription() { return "Set roll, pitch and yaw."; }

  public int getExtraCRC() { return 100; }

  public int getLength() { return 18; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

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
    StringBuilder result = new StringBuilder("SET_ROLL_PITCH_YAW_THRUST");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
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
