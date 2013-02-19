package dongfang.mavlink_10.messages;
public class FenceFetchPointMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short idx;

  public FenceFetchPointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 161; }

  public String getDescription() { return "Request a current fence point from MAV"; }

  public int getExtraCRC() { return 68; }

  public int getLength() { return 3; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getIdx() { return idx; }
  public void setIdx(short idx) { this.idx=idx; }

  public String toString() {
    StringBuilder result = new StringBuilder("FENCE_FETCH_POINT");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("idx=");
    result.append(this.idx);
    return result.toString();
  }
}
