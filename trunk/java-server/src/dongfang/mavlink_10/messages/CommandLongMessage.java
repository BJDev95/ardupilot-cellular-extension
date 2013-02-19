package dongfang.mavlink_10.messages;
public class CommandLongMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int command;
  private short confirmation;
  private float param1;
  private float param2;
  private float param3;
  private float param4;
  private float param5;
  private float param6;
  private float param7;

  public CommandLongMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 76; }

  public String getDescription() { return "Send a command with up to four parameters to the MAV"; }

  public int getExtraCRC() { return 152; }

  public int getLength() { return 33; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint16_t
  public int getCommand() { return command; }
  public void setCommand(int command) { this.command=command; }

  // a uint8_t
  public short getConfirmation() { return confirmation; }
  public void setConfirmation(short confirmation) { this.confirmation=confirmation; }

  // a float
  public float getParam1() { return param1; }
  public void setParam1(float param1) { this.param1=param1; }

  // a float
  public float getParam2() { return param2; }
  public void setParam2(float param2) { this.param2=param2; }

  // a float
  public float getParam3() { return param3; }
  public void setParam3(float param3) { this.param3=param3; }

  // a float
  public float getParam4() { return param4; }
  public void setParam4(float param4) { this.param4=param4; }

  // a float
  public float getParam5() { return param5; }
  public void setParam5(float param5) { this.param5=param5; }

  // a float
  public float getParam6() { return param6; }
  public void setParam6(float param6) { this.param6=param6; }

  // a float
  public float getParam7() { return param7; }
  public void setParam7(float param7) { this.param7=param7; }

  public String toString() {
    StringBuilder result = new StringBuilder("COMMAND_LONG");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("command=");
    result.append(this.command);
    result.append(",");
    result.append("confirmation=");
    result.append(this.confirmation);
    result.append(",");
    result.append("param1=");
    result.append(this.param1);
    result.append(",");
    result.append("param2=");
    result.append(this.param2);
    result.append(",");
    result.append("param3=");
    result.append(this.param3);
    result.append(",");
    result.append("param4=");
    result.append(this.param4);
    result.append(",");
    result.append("param5=");
    result.append(this.param5);
    result.append(",");
    result.append("param6=");
    result.append(this.param6);
    result.append(",");
    result.append("param7=");
    result.append(this.param7);
    return result.toString();
  }
}
