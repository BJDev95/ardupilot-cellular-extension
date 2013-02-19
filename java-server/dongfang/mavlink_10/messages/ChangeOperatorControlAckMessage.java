package dongfang.mavlink_10.messages;
public class ChangeOperatorControlAckMessage extends MavlinkMessage {
  private short gcsSystemId;
  private short controlRequest;
  private short ack;

  public ChangeOperatorControlAckMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 6; }

  public String getDescription() { return "Accept / deny control of this MAV"; }

  public int getExtraCRC() { return 104; }

  public int getLength() { return 3; }


  // a uint8_t
  public short getGcsSystemId() { return gcsSystemId; }
  public void setGcsSystemId(short gcsSystemId) { this.gcsSystemId=gcsSystemId; }

  // a uint8_t
  public short getControlRequest() { return controlRequest; }
  public void setControlRequest(short controlRequest) { this.controlRequest=controlRequest; }

  // a uint8_t
  public short getAck() { return ack; }
  public void setAck(short ack) { this.ack=ack; }

  public String toString() {
    StringBuilder result = new StringBuilder("CHANGE_OPERATOR_CONTROL_ACK");
    result.append(": ");
    result.append("gcs_system_id=");
    result.append(this.gcsSystemId);
    result.append(",");
    result.append("control_request=");
    result.append(this.controlRequest);
    result.append(",");
    result.append("ack=");
    result.append(this.ack);
    return result.toString();
  }
}
