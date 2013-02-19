package dongfang.mavlink_10.messages;
public class MountConfigureMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short mountMode;
  private short stabRoll;
  private short stabPitch;
  private short stabYaw;

  public MountConfigureMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 156; }

  public String getDescription() { return "Message to configure a camera mount, directional antenna, etc."; }

  public int getExtraCRC() { return 19; }

  public int getLength() { return 6; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getMountMode() { return mountMode; }
  public void setMountMode(short mountMode) { this.mountMode=mountMode; }

  // a uint8_t
  public short getStabRoll() { return stabRoll; }
  public void setStabRoll(short stabRoll) { this.stabRoll=stabRoll; }

  // a uint8_t
  public short getStabPitch() { return stabPitch; }
  public void setStabPitch(short stabPitch) { this.stabPitch=stabPitch; }

  // a uint8_t
  public short getStabYaw() { return stabYaw; }
  public void setStabYaw(short stabYaw) { this.stabYaw=stabYaw; }

  public String toString() {
    StringBuilder result = new StringBuilder("MOUNT_CONFIGURE");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("mount_mode=");
    result.append(this.mountMode);
    result.append(",");
    result.append("stab_roll=");
    result.append(this.stabRoll);
    result.append(",");
    result.append("stab_pitch=");
    result.append(this.stabPitch);
    result.append(",");
    result.append("stab_yaw=");
    result.append(this.stabYaw);
    return result.toString();
  }
}
