package dongfang.mavlink_10.messages;
public class ChangeOperatorControlMessage extends MavlinkMessage {
  private short targetSystem;
  private short controlRequest;
  private short version;
  private String passkey;

  public ChangeOperatorControlMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 5; }

  public String getDescription() { return "Request to control this MAV"; }

  public int getExtraCRC() { return 217; }

  public int getLength() { return 28; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getControlRequest() { return controlRequest; }
  public void setControlRequest(short controlRequest) { this.controlRequest=controlRequest; }

  // a uint8_t
  public short getVersion() { return version; }
  public void setVersion(short version) { this.version=version; }

  // a char[]
  public String getPasskey() { return passkey; }
  public void setPasskey(String passkey) { this.passkey=passkey; }

  public String toString() {
    StringBuilder result = new StringBuilder("CHANGE_OPERATOR_CONTROL");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("control_request=");
    result.append(this.controlRequest);
    result.append(",");
    result.append("version=");
    result.append(this.version);
    result.append(",");
    result.append("passkey=");
    result.append(this.passkey);
    return result.toString();
  }
}
