package dongfang.mavlink_10.messages;
public class MissionRequestPartialListMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short startIndex;
  private short endIndex;

  public MissionRequestPartialListMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 37; }

  public String getDescription() { return "Request a partial list of mission items from the system/component. http://qgroundcontrol.org/mavlink/waypoint_protocol. If start and end index are the same, just send one waypoint."; }

  public int getExtraCRC() { return 212; }

  public int getLength() { return 6; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a int16_t
  public short getStartIndex() { return startIndex; }
  public void setStartIndex(short startIndex) { this.startIndex=startIndex; }

  // a int16_t
  public short getEndIndex() { return endIndex; }
  public void setEndIndex(short endIndex) { this.endIndex=endIndex; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_REQUEST_PARTIAL_LIST");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("start_index=");
    result.append(this.startIndex);
    result.append(",");
    result.append("end_index=");
    result.append(this.endIndex);
    return result.toString();
  }
}
