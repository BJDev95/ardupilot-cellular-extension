package dongfang.mavlink_10.messages;
public class MissionCountMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int count;

  public MissionCountMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 44; }

  public String getDescription() { return "This message is emitted as response to MISSION_REQUEST_LIST by the MAV and to initiate a write transaction. The GCS can then request the individual mission item based on the knowledge of the total number of MISSIONs."; }

  public int getExtraCRC() { return 221; }

  public int getLength() { return 4; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint16_t
  public int getCount() { return count; }
  public void setCount(int count) { this.count=count; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_COUNT");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("count=");
    result.append(this.count);
    return result.toString();
  }
}
