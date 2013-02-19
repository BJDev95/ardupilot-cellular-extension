package dongfang.mavlink_10.messages;
public class ParamRequestListMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;

  public ParamRequestListMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 21; }

  public String getDescription() { return "Request all parameters of this component. After his request, all parameters are emitted."; }

  public int getExtraCRC() { return 159; }

  public int getLength() { return 2; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  public String toString() {
    StringBuilder result = new StringBuilder("PARAM_REQUEST_LIST");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    return result.toString();
  }
}
