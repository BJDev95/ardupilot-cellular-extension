package dongfang.mavlink_10.messages;
public class ParamValueMessage extends MavlinkMessage {
  private String paramId;
  private float paramValue;
  private short paramType;
  private int paramCount;
  private int paramIndex;

  public ParamValueMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 22; }

  public String getDescription() { return "Emit the value of a onboard parameter. The inclusion of param_count and param_index in the message allows the recipient to keep track of received parameters and allows him to re-request missing parameters after a loss or timeout."; }

  public int getExtraCRC() { return 220; }

  public int getLength() { return 25; }


  // a char[]
  public String getParamId() { return paramId; }
  public void setParamId(String paramId) { this.paramId=paramId; }

  // a float
  public float getParamValue() { return paramValue; }
  public void setParamValue(float paramValue) { this.paramValue=paramValue; }

  // a uint8_t
  public short getParamType() { return paramType; }
  public void setParamType(short paramType) { this.paramType=paramType; }

  // a uint16_t
  public int getParamCount() { return paramCount; }
  public void setParamCount(int paramCount) { this.paramCount=paramCount; }

  // a uint16_t
  public int getParamIndex() { return paramIndex; }
  public void setParamIndex(int paramIndex) { this.paramIndex=paramIndex; }

  public String toString() {
    StringBuilder result = new StringBuilder("PARAM_VALUE");
    result.append(": ");
    result.append("param_id=");
    result.append(this.paramId);
    result.append(",");
    result.append("param_value=");
    result.append(this.paramValue);
    result.append(",");
    result.append("param_type=");
    result.append(this.paramType);
    result.append(",");
    result.append("param_count=");
    result.append(this.paramCount);
    result.append(",");
    result.append("param_index=");
    result.append(this.paramIndex);
    return result.toString();
  }
}
