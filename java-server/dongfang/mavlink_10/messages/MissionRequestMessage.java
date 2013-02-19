package dongfang.mavlink_10.messages;
public class MissionRequestMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int seq;

  public MissionRequestMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 40; }

  public String getDescription() { return "Request the information of the mission item with the sequence number seq. The response of the system to this message should be a MISSION_ITEM message. http://qgroundcontrol.org/mavlink/waypoint_protocol"; }

  public int getExtraCRC() { return 230; }

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
    StringBuilder result = new StringBuilder("MISSION_REQUEST");
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
