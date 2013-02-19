package dongfang.mavlink_10.messages;
public class MountStatusMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int pointingA;
  private int pointingB;
  private int pointingC;

  public MountStatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 158; }

  public String getDescription() { return "Message with some status from APM to GCS about camera or antenna mount"; }

  public int getExtraCRC() { return 134; }

  public int getLength() { return 14; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a int32_t
  public int getPointingA() { return pointingA; }
  public void setPointingA(int pointingA) { this.pointingA=pointingA; }

  // a int32_t
  public int getPointingB() { return pointingB; }
  public void setPointingB(int pointingB) { this.pointingB=pointingB; }

  // a int32_t
  public int getPointingC() { return pointingC; }
  public void setPointingC(int pointingC) { this.pointingC=pointingC; }

  public String toString() {
    StringBuilder result = new StringBuilder("MOUNT_STATUS");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("pointing_a=");
    result.append(this.pointingA);
    result.append(",");
    result.append("pointing_b=");
    result.append(this.pointingB);
    result.append(",");
    result.append("pointing_c=");
    result.append(this.pointingC);
    return result.toString();
  }
}
