package dongfang.mavlink_10.messages;
public class FenceStatusMessage extends MavlinkMessage {
  private short breachStatus;
  private int breachCount;
  private short breachType;
  private long breachTime;

  public FenceStatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 162; }

  public String getDescription() { return "Status of geo-fencing. Sent in extended 	    status stream when fencing enabled"; }

  public int getExtraCRC() { return 189; }

  public int getLength() { return 8; }


  // a uint8_t
  public short getBreachStatus() { return breachStatus; }
  public void setBreachStatus(short breachStatus) { this.breachStatus=breachStatus; }

  // a uint16_t
  public int getBreachCount() { return breachCount; }
  public void setBreachCount(int breachCount) { this.breachCount=breachCount; }

  // a uint8_t
  public short getBreachType() { return breachType; }
  public void setBreachType(short breachType) { this.breachType=breachType; }

  // a uint32_t
  public long getBreachTime() { return breachTime; }
  public void setBreachTime(long breachTime) { this.breachTime=breachTime; }

  public String toString() {
    StringBuilder result = new StringBuilder("FENCE_STATUS");
    result.append(": ");
    result.append("breach_status=");
    result.append(this.breachStatus);
    result.append(",");
    result.append("breach_count=");
    result.append(this.breachCount);
    result.append(",");
    result.append("breach_type=");
    result.append(this.breachType);
    result.append(",");
    result.append("breach_time=");
    result.append(this.breachTime);
    return result.toString();
  }
}
