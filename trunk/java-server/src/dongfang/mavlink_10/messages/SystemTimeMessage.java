package dongfang.mavlink_10.messages;
public class SystemTimeMessage extends MavlinkMessage {
  private long timeUnixUsec;
  private long timeBootMs;

  public SystemTimeMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 2; }

  public String getDescription() { return "The system time is the time of the master clock, typically the computer clock of the main onboard computer."; }

  public int getExtraCRC() { return 137; }

  public int getLength() { return 12; }


  // a uint64_t
  public long getTimeUnixUsec() { return timeUnixUsec; }
  public void setTimeUnixUsec(long timeUnixUsec) { this.timeUnixUsec=timeUnixUsec; }

  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  public String toString() {
    StringBuilder result = new StringBuilder("SYSTEM_TIME");
    result.append(": ");
    result.append("time_unix_usec=");
    result.append(this.timeUnixUsec);
    result.append(",");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    return result.toString();
  }
}
