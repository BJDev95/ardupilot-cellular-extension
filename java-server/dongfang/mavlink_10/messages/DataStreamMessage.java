package dongfang.mavlink_10.messages;
public class DataStreamMessage extends MavlinkMessage {
  private short streamId;
  private int messageRate;
  private short onOff;

  public DataStreamMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 67; }

  public String getDescription() { return "<description missing>"; }

  public int getExtraCRC() { return 21; }

  public int getLength() { return 4; }


  // a uint8_t
  public short getStreamId() { return streamId; }
  public void setStreamId(short streamId) { this.streamId=streamId; }

  // a uint16_t
  public int getMessageRate() { return messageRate; }
  public void setMessageRate(int messageRate) { this.messageRate=messageRate; }

  // a uint8_t
  public short getOnOff() { return onOff; }
  public void setOnOff(short onOff) { this.onOff=onOff; }

  public String toString() {
    StringBuilder result = new StringBuilder("DATA_STREAM");
    result.append(": ");
    result.append("stream_id=");
    result.append(this.streamId);
    result.append(",");
    result.append("message_rate=");
    result.append(this.messageRate);
    result.append(",");
    result.append("on_off=");
    result.append(this.onOff);
    return result.toString();
  }
}
