package dongfang.mavlink_10.messages;
public class AuthKeyMessage extends MavlinkMessage {
  private String key;

  public AuthKeyMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 7; }

  public String getDescription() { return "Emit an encrypted signature / key identifying this system. PLEASE NOTE: This protocol has been kept simple, so transmitting the key requires an encrypted channel for true safety."; }

  public int getExtraCRC() { return 119; }

  public int getLength() { return 32; }


  // a char[]
  public String getKey() { return key; }
  public void setKey(String key) { this.key=key; }

  public String toString() {
    StringBuilder result = new StringBuilder("AUTH_KEY");
    result.append(": ");
    result.append("key=");
    result.append(this.key);
    return result.toString();
  }
}
