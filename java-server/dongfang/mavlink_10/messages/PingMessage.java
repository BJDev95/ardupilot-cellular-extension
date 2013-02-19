package dongfang.mavlink_10.messages;
public class PingMessage extends MavlinkMessage {
  private long timeUsec;
  private long seq;
  private short targetSystem;
  private short targetComponent;

  public PingMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 4; }

  public String getDescription() { return "A ping message either requesting or responding to a ping. This allows to measure the system latencies, including serial port, radio modem and UDP connections."; }

  public int getExtraCRC() { return 237; }

  public int getLength() { return 14; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a uint32_t
  public long getSeq() { return seq; }
  public void setSeq(long seq) { this.seq=seq; }

  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  public String toString() {
    StringBuilder result = new StringBuilder("PING");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("seq=");
    result.append(this.seq);
    result.append(",");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    return result.toString();
  }
}
