package dongfang.mavlink_10.messages;
public class MissionCurrentMessage extends MavlinkMessage {
  private int seq;

  public MissionCurrentMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 42; }

  public String getDescription() { return "Message that announces the sequence number of the current active mission item. The MAV will fly towards this mission item."; }

  public int getExtraCRC() { return 28; }

  public int getLength() { return 2; }


  // a uint16_t
  public int getSeq() { return seq; }
  public void setSeq(int seq) { this.seq=seq; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_CURRENT");
    result.append(": ");
    result.append("seq=");
    result.append(this.seq);
    return result.toString();
  }
}
