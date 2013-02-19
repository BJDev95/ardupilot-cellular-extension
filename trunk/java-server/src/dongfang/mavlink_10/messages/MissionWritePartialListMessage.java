package dongfang.mavlink_10.messages;
public class MissionWritePartialListMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short startIndex;
  private short endIndex;

  public MissionWritePartialListMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 38; }

  public String getDescription() { return "This message is sent to the MAV to write a partial list. If start index == end index, only one item will be transmitted / updated. If the start index is NOT 0 and above the current list size, this request should be REJECTED!"; }

  public int getExtraCRC() { return 9; }

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
    StringBuilder result = new StringBuilder("MISSION_WRITE_PARTIAL_LIST");
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
