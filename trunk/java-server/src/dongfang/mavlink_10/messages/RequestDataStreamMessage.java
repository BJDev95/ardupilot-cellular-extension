package dongfang.mavlink_10.messages;
public class RequestDataStreamMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short reqStreamId;
  private int reqMessageRate;
  private short startStop;

  public RequestDataStreamMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 66; }

  public String getDescription() { return "<description missing>"; }

  public int getExtraCRC() { return 148; }

  public int getLength() { return 6; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getReqStreamId() { return reqStreamId; }
  public void setReqStreamId(short reqStreamId) { this.reqStreamId=reqStreamId; }

  // a uint16_t
  public int getReqMessageRate() { return reqMessageRate; }
  public void setReqMessageRate(int reqMessageRate) { this.reqMessageRate=reqMessageRate; }

  // a uint8_t
  public short getStartStop() { return startStop; }
  public void setStartStop(short startStop) { this.startStop=startStop; }

  public String toString() {
    StringBuilder result = new StringBuilder("REQUEST_DATA_STREAM");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("req_stream_id=");
    result.append(this.reqStreamId);
    result.append(",");
    result.append("req_message_rate=");
    result.append(this.reqMessageRate);
    result.append(",");
    result.append("start_stop=");
    result.append(this.startStop);
    return result.toString();
  }
}
