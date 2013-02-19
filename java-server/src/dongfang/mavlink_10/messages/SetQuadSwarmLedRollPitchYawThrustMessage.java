package dongfang.mavlink_10.messages;
public class SetQuadSwarmLedRollPitchYawThrustMessage extends MavlinkMessage {
  private short group;
  private short mode;
  private short ledRed;
  private short ledBlue;
  private short ledGreen;
  private short roll;
  private short pitch;
  private short yaw;
  private int thrust;

  public SetQuadSwarmLedRollPitchYawThrustMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 63; }

  public String getDescription() { return "Setpoint for up to four quadrotors in a group / wing"; }

  public int getExtraCRC() { return 130; }

  public int getLength() { return 30; }


  // a uint8_t
  public short getGroup() { return group; }
  public void setGroup(short group) { this.group=group; }

  // a uint8_t
  public short getMode() { return mode; }
  public void setMode(short mode) { this.mode=mode; }

  // a uint8_t
  public short getLedRed() { return ledRed; }
  public void setLedRed(short ledRed) { this.ledRed=ledRed; }

  // a uint8_t
  public short getLedBlue() { return ledBlue; }
  public void setLedBlue(short ledBlue) { this.ledBlue=ledBlue; }

  // a uint8_t
  public short getLedGreen() { return ledGreen; }
  public void setLedGreen(short ledGreen) { this.ledGreen=ledGreen; }

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
    StringBuilder result = new StringBuilder("SET_QUAD_SWARM_LED_ROLL_PITCH_YAW_THRUST");
    result.append(": ");
    result.append("group=");
    result.append(this.group);
    result.append(",");
    result.append("mode=");
    result.append(this.mode);
    result.append(",");
    result.append("led_red=");
    result.append(this.ledRed);
    result.append(",");
    result.append("led_blue=");
    result.append(this.ledBlue);
    result.append(",");
    result.append("led_green=");
    result.append(this.ledGreen);
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
