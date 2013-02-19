package dongfang.mavlink_10.messages;
public class MissionClearAllMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;

  public MissionClearAllMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 45; }

  public String getDescription() { return "Delete all mission items at once."; }

  public int getExtraCRC() { return 232; }

  public int getLength() { return 2; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_CLEAR_ALL");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    return result.toString();
  }
}
