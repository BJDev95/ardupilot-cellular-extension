package dongfang.mavlink_10.messages;
public class SafetySetAllowedAreaMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short frame;
  private float p1x;
  private float p1y;
  private float p1z;
  private float p2x;
  private float p2y;
  private float p2z;

  public SafetySetAllowedAreaMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 54; }

  public String getDescription() { return "Set a safety zone (volume), which is defined by two corners of a cube. This message can be used to tell the MAV which setpoints/MISSIONs to accept and which to reject. Safety areas are often enforced by national or competition regulations."; }

  public int getExtraCRC() { return 15; }

  public int getLength() { return 27; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getFrame() { return frame; }
  public void setFrame(short frame) { this.frame=frame; }

  // a float
  public float getP1x() { return p1x; }
  public void setP1x(float p1x) { this.p1x=p1x; }

  // a float
  public float getP1y() { return p1y; }
  public void setP1y(float p1y) { this.p1y=p1y; }

  // a float
  public float getP1z() { return p1z; }
  public void setP1z(float p1z) { this.p1z=p1z; }

  // a float
  public float getP2x() { return p2x; }
  public void setP2x(float p2x) { this.p2x=p2x; }

  // a float
  public float getP2y() { return p2y; }
  public void setP2y(float p2y) { this.p2y=p2y; }

  // a float
  public float getP2z() { return p2z; }
  public void setP2z(float p2z) { this.p2z=p2z; }

  public String toString() {
    StringBuilder result = new StringBuilder("SAFETY_SET_ALLOWED_AREA");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("frame=");
    result.append(this.frame);
    result.append(",");
    result.append("p1x=");
    result.append(this.p1x);
    result.append(",");
    result.append("p1y=");
    result.append(this.p1y);
    result.append(",");
    result.append("p1z=");
    result.append(this.p1z);
    result.append(",");
    result.append("p2x=");
    result.append(this.p2x);
    result.append(",");
    result.append("p2y=");
    result.append(this.p2y);
    result.append(",");
    result.append("p2z=");
    result.append(this.p2z);
    return result.toString();
  }
}
