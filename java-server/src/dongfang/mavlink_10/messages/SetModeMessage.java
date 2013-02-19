package dongfang.mavlink_10.messages;
public class SetModeMessage extends MavlinkMessage {
  private short targetSystem;
  private short baseMode;
  private long customMode;

  public SetModeMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 11; }

  public String getDescription() { return "Set the system mode, as defined by enum MAV_MODE. There is no target component id as the mode is by definition for the overall aircraft, not only for one component."; }

  public int getExtraCRC() { return 89; }

  public int getLength() { return 6; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getBaseMode() { return baseMode; }
  public void setBaseMode(short baseMode) { this.baseMode=baseMode; }

  // a uint32_t
  public long getCustomMode() { return customMode; }
  public void setCustomMode(long customMode) { this.customMode=customMode; }

  public String toString() {
    StringBuilder result = new StringBuilder("SET_MODE");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("base_mode=");
    result.append(this.baseMode);
    result.append(",");
    result.append("custom_mode=");
    result.append(this.customMode);
    return result.toString();
  }
}
