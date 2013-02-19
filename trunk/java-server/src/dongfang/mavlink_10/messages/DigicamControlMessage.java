package dongfang.mavlink_10.messages;
public class DigicamControlMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short session;
  private short zoomPos;
  private byte zoomStep;
  private short focusLock;
  private short shot;
  private short commandId;
  private short extraParam;
  private float extraValue;

  public DigicamControlMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 155; }

  public String getDescription() { return "Control on-board Camera Control System to take shots."; }

  public int getExtraCRC() { return 22; }

  public int getLength() { return 13; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getSession() { return session; }
  public void setSession(short session) { this.session=session; }

  // a uint8_t
  public short getZoomPos() { return zoomPos; }
  public void setZoomPos(short zoomPos) { this.zoomPos=zoomPos; }

  // a int8_t
  public byte getZoomStep() { return zoomStep; }
  public void setZoomStep(byte zoomStep) { this.zoomStep=zoomStep; }

  // a uint8_t
  public short getFocusLock() { return focusLock; }
  public void setFocusLock(short focusLock) { this.focusLock=focusLock; }

  // a uint8_t
  public short getShot() { return shot; }
  public void setShot(short shot) { this.shot=shot; }

  // a uint8_t
  public short getCommandId() { return commandId; }
  public void setCommandId(short commandId) { this.commandId=commandId; }

  // a uint8_t
  public short getExtraParam() { return extraParam; }
  public void setExtraParam(short extraParam) { this.extraParam=extraParam; }

  // a float
  public float getExtraValue() { return extraValue; }
  public void setExtraValue(float extraValue) { this.extraValue=extraValue; }

  public String toString() {
    StringBuilder result = new StringBuilder("DIGICAM_CONTROL");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("session=");
    result.append(this.session);
    result.append(",");
    result.append("zoom_pos=");
    result.append(this.zoomPos);
    result.append(",");
    result.append("zoom_step=");
    result.append(this.zoomStep);
    result.append(",");
    result.append("focus_lock=");
    result.append(this.focusLock);
    result.append(",");
    result.append("shot=");
    result.append(this.shot);
    result.append(",");
    result.append("command_id=");
    result.append(this.commandId);
    result.append(",");
    result.append("extra_param=");
    result.append(this.extraParam);
    result.append(",");
    result.append("extra_value=");
    result.append(this.extraValue);
    return result.toString();
  }
}
