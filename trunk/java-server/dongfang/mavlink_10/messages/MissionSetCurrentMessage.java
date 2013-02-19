package dongfang.mavlink_10.messages;
public class MissionSetCurrentMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int seq;

  public MissionSetCurrentMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 41; }

  public String getDescription() { return "Set the mission item with sequence number seq as current item. This means that the MAV will continue to this mission item on the shortest path (not following the mission items in-between)."; }

  public int getExtraCRC() { return 28; }

  public int getLength() { return 4; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint16_t
  public int getSeq() { return seq; }
  public void setSeq(int seq) { this.seq=seq; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_SET_CURRENT");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("seq=");
    result.append(this.seq);
    return result.toString();
  }
}
