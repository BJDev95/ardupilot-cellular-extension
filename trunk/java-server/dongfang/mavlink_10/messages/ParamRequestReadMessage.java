package dongfang.mavlink_10.messages;
public class ParamRequestReadMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private String paramId;
  private short paramIndex;

  public ParamRequestReadMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 20; }

  public String getDescription() { return "Request to read the onboard parameter with the param_id string id. Onboard parameters are stored as key[const char*] -> value[float]. This allows to send a parameter to any other component (such as the GCS) without the need of previous knowledge of possible parameter names. Thus the same GCS can store different parameters for different autopilots. See also http://qgroundcontrol.org/parameter_interface for a full documentation of QGroundControl and IMU code."; }

  public int getExtraCRC() { return 214; }

  public int getLength() { return 20; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a char[]
  public String getParamId() { return paramId; }
  public void setParamId(String paramId) { this.paramId=paramId; }

  // a int16_t
  public short getParamIndex() { return paramIndex; }
  public void setParamIndex(short paramIndex) { this.paramIndex=paramIndex; }

  public String toString() {
    StringBuilder result = new StringBuilder("PARAM_REQUEST_READ");
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
    result.append("param_index=");
    result.append(this.paramIndex);
    return result.toString();
  }
}
