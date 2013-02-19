package dongfang.mavlink_10.messages;
public class ParamSetMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private String paramId;
  private float paramValue;
  private short paramType;

  public ParamSetMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 23; }

  public String getDescription() { return "Set a parameter value TEMPORARILY to RAM. It will be reset to default on system reboot. Send the ACTION MAV_ACTION_STORAGE_WRITE to PERMANENTLY write the RAM contents to EEPROM. IMPORTANT: The receiving component should acknowledge the new parameter value by sending a param_value message to all communication partners. This will also ensure that multiple GCS all have an up-to-date list of all parameters. If the sending GCS did not receive a PARAM_VALUE message within its timeout time, it should re-send the PARAM_SET message."; }

  public int getExtraCRC() { return 168; }

  public int getLength() { return 23; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a char[]
  public String getParamId() { return paramId; }
  public void setParamId(String paramId) { this.paramId=paramId; }

  // a float
  public float getParamValue() { return paramValue; }
  public void setParamValue(float paramValue) { this.paramValue=paramValue; }

  // a uint8_t
  public short getParamType() { return paramType; }
  public void setParamType(short paramType) { this.paramType=paramType; }

  public String toString() {
    StringBuilder result = new StringBuilder("PARAM_SET");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("param_id=");
    result.append(this.paramId);
    result.append(",");
    result.append("param_value=");
    result.append(this.paramValue);
    result.append(",");
    result.append("param_type=");
    result.append(this.paramType);
    return result.toString();
  }
}
