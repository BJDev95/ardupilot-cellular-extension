package dongfang.mavlink_10.messages;
public class SetQuadSwarmRollPitchYawThrustMessage extends MavlinkMessage {
  private short group;
  private short mode;
  private short roll;
  private short pitch;
  private short yaw;
  private int thrust;

  public SetQuadSwarmRollPitchYawThrustMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 61; }

  public String getDescription() { return "Setpoint for up to four quadrotors in a group / wing"; }

  public int getExtraCRC() { return 240; }

  public int getLength() { return 18; }


  // a uint8_t
  public short getGroup() { return group; }
  public void setGroup(short group) { this.group=group; }

  // a uint8_t
  public short getMode() { return mode; }
  public void setMode(short mode) { this.mode=mode; }

  // a int16_t
  public short getRoll() { return roll; }
  public void setRoll(short roll) { this.roll=roll; }

  // a int16_t
  public short getPitch() { return pitch; }
  public void setPitch(short pitch) { this.pitch=pitch; }

  // a int16_t
  public short getYaw() { return yaw; }
  public void setYaw(short yaw) { this.yaw=yaw; }

  // a uint16_t
  public int getThrust() { return thrust; }
  public void setThrust(int thrust) { this.thrust=thrust; }

  public String toString() {
    StringBuilder result = new StringBuilder("SET_QUAD_SWARM_ROLL_PITCH_YAW_THRUST");
    result.append(": ");
    result.append("group=");
    result.append(this.group);
    result.append(",");
    result.append("mode=");
    result.append(this.mode);
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
