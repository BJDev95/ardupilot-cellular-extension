package dongfang.mavlink_10.messages;
public class DigicamConfigureMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short mode;
  private int shutterSpeed;
  private short aperture;
  private short iso;
  private short exposureType;
  private short commandId;
  private short engineCutOff;
  private short extraParam;
  private float extraValue;

  public DigicamConfigureMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 154; }

  public String getDescription() { return "Configure on-board Camera Control System."; }

  public int getExtraCRC() { return 84; }

  public int getLength() { return 15; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getMode() { return mode; }
  public void setMode(short mode) { this.mode=mode; }

  // a uint16_t
  public int getShutterSpeed() { return shutterSpeed; }
  public void setShutterSpeed(int shutterSpeed) { this.shutterSpeed=shutterSpeed; }

  // a uint8_t
  public short getAperture() { return aperture; }
  public void setAperture(short aperture) { this.aperture=aperture; }

  // a uint8_t
  public short getIso() { return iso; }
  public void setIso(short iso) { this.iso=iso; }

  // a uint8_t
  public short getExposureType() { return exposureType; }
  public void setExposureType(short exposureType) { this.exposureType=exposureType; }

  // a uint8_t
  public short getCommandId() { return commandId; }
  public void setCommandId(short commandId) { this.commandId=commandId; }

  // a uint8_t
  public short getEngineCutOff() { return engineCutOff; }
  public void setEngineCutOff(short engineCutOff) { this.engineCutOff=engineCutOff; }

  // a uint8_t
  public short getExtraParam() { return extraParam; }
  public void setExtraParam(short extraParam) { this.extraParam=extraParam; }

  // a float
  public float getExtraValue() { return extraValue; }
  public void setExtraValue(float extraValue) { this.extraValue=extraValue; }

  public String toString() {
    StringBuilder result = new StringBuilder("DIGICAM_CONFIGURE");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("mode=");
    result.append(this.mode);
    result.append(",");
    result.append("shutter_speed=");
    result.append(this.shutterSpeed);
    result.append(",");
    result.append("aperture=");
    result.append(this.aperture);
    result.append(",");
    result.append("iso=");
    result.append(this.iso);
    result.append(",");
    result.append("exposure_type=");
    result.append(this.exposureType);
    result.append(",");
    result.append("command_id=");
    result.append(this.commandId);
    result.append(",");
    result.append("engine_cut_off=");
    result.append(this.engineCutOff);
    result.append(",");
    result.append("extra_param=");
    result.append(this.extraParam);
    result.append(",");
    result.append("extra_value=");
    result.append(this.extraValue);
    return result.toString();
  }
}
