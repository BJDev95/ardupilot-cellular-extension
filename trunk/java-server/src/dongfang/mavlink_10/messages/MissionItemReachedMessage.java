package dongfang.mavlink_10.messages;
public class MissionItemReachedMessage extends MavlinkMessage {
  private int seq;

  public MissionItemReachedMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 46; }

  public String getDescription() { return "A certain mission item has been reached. The system will either hold this position (or circle on the orbit) or (if the autocontinue on the WP was set) continue to the next MISSION."; }

  public int getExtraCRC() { return 11; }

  public int getLength() { return 2; }


  // a uint16_t
  public int getSeq() { return seq; }
  public void setSeq(int seq) { this.seq=seq; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_ITEM_REACHED");
    result.append(": ");
    result.append("seq=");
    result.append(this.seq);
    return result.toString();
  }
}
