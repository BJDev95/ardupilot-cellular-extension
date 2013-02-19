package dongfang.mavlink_10.messages;
public class CommandAckMessage extends MavlinkMessage {
  private int command;
  private short result;

  public CommandAckMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 77; }

  public String getDescription() { return "Report status of a command. Includes feedback wether the command was executed."; }

  public int getExtraCRC() { return 143; }

  public int getLength() { return 3; }


  // a uint16_t
  public int getCommand() { return command; }
  public void setCommand(int command) { this.command=command; }

  // a uint8_t
  public short getResult() { return result; }
  public void setResult(short result) { this.result=result; }

  public String toString() {
    StringBuilder result = new StringBuilder("COMMAND_ACK");
    result.append(": ");
    result.append("command=");
    result.append(this.command);
    result.append(",");
    result.append("result=");
    result.append(this.result);
    return result.toString();
  }
}
