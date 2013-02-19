package dongfang.mavlink_10.messages;
public class HeartbeatMessage extends MavlinkMessage {
  private short type;
  private short autopilot;
  private short baseMode;
  private long customMode;
  private short systemStatus;
  private short mavlinkVersion;

  public HeartbeatMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 0; }

  public String getDescription() { return "The heartbeat message shows that a system is present and responding. The type of the MAV and Autopilot hardware allow the receiving system to treat further messages from this system appropriate (e.g. by laying out the user interface based on the autopilot)."; }

  public int getExtraCRC() { return 50; }

  public int getLength() { return 9; }


  // a uint8_t
  public short getType() { return type; }
  public void setType(short type) { this.type=type; }

  // a uint8_t
  public short getAutopilot() { return autopilot; }
  public void setAutopilot(short autopilot) { this.autopilot=autopilot; }

  // a uint8_t
  public short getBaseMode() { return baseMode; }
  public void setBaseMode(short baseMode) { this.baseMode=baseMode; }

  // a uint32_t
  public long getCustomMode() { return customMode; }
  public void setCustomMode(long customMode) { this.customMode=customMode; }

  // a uint8_t
  public short getSystemStatus() { return systemStatus; }
  public void setSystemStatus(short systemStatus) { this.systemStatus=systemStatus; }

  // a uint8_t_mavlink_version
  public short getMavlinkVersion() { return mavlinkVersion; }
  public void setMavlinkVersion(short mavlinkVersion) { this.mavlinkVersion=mavlinkVersion; }

  public String toString() {
    StringBuilder result = new StringBuilder("HEARTBEAT");
    result.append(": ");
    result.append("type=");
    result.append(this.type);
    result.append(",");
    result.append("autopilot=");
    result.append(this.autopilot);
    result.append(",");
    result.append("base_mode=");
    result.append(this.baseMode);
    result.append(",");
    result.append("custom_mode=");
    result.append(this.customMode);
    result.append(",");
    result.append("system_status=");
    result.append(this.systemStatus);
    result.append(",");
    result.append("mavlink_version=");
    result.append(this.mavlinkVersion);
    return result.toString();
  }
}
