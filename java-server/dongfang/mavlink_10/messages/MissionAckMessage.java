package dongfang.mavlink_10.messages;
public class MissionAckMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short type;

  public MissionAckMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 47; }

  public String getDescription() { return "Ack message during MISSION handling. The type field states if this message is a positive ack (type=0) or if an error happened (type=non-zero)."; }

  public int getExtraCRC() { return 153; }

  public int getLength() { return 3; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getType() { return type; }
  public void setType(short type) { this.type=type; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_ACK");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("type=");
    result.append(this.type);
    return result.toString();
  }
}
