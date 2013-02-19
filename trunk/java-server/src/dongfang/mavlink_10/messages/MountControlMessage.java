package dongfang.mavlink_10.messages;
public class MountControlMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int inputA;
  private int inputB;
  private int inputC;
  private short savePosition;

  public MountControlMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 157; }

  public String getDescription() { return "Message to control a camera mount, directional antenna, etc."; }

  public int getExtraCRC() { return 21; }

  public int getLength() { return 15; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a int32_t
  public int getInputA() { return inputA; }
  public void setInputA(int inputA) { this.inputA=inputA; }

  // a int32_t
  public int getInputB() { return inputB; }
  public void setInputB(int inputB) { this.inputB=inputB; }

  // a int32_t
  public int getInputC() { return inputC; }
  public void setInputC(int inputC) { this.inputC=inputC; }

  // a uint8_t
  public short getSavePosition() { return savePosition; }
  public void setSavePosition(short savePosition) { this.savePosition=savePosition; }

  public String toString() {
    StringBuilder result = new StringBuilder("MOUNT_CONTROL");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("input_a=");
    result.append(this.inputA);
    result.append(",");
    result.append("input_b=");
    result.append(this.inputB);
    result.append(",");
    result.append("input_c=");
    result.append(this.inputC);
    result.append(",");
    result.append("save_position=");
    result.append(this.savePosition);
    return result.toString();
  }
}
