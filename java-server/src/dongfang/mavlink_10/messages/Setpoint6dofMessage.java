package dongfang.mavlink_10.messages;
public class Setpoint6dofMessage extends MavlinkMessage {
  private short targetSystem;
  private float transX;
  private float transY;
  private float transZ;
  private float rotX;
  private float rotY;
  private float rotZ;

  public Setpoint6dofMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 149; }

  public String getDescription() { return "Set the 6 DOF setpoint for a attitude and position controller."; }

  public int getExtraCRC() { return 0; }

  public int getLength() { return 25; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a float
  public float getTransX() { return transX; }
  public void setTransX(float transX) { this.transX=transX; }

  // a float
  public float getTransY() { return transY; }
  public void setTransY(float transY) { this.transY=transY; }

  // a float
  public float getTransZ() { return transZ; }
  public void setTransZ(float transZ) { this.transZ=transZ; }

  // a float
  public float getRotX() { return rotX; }
  public void setRotX(float rotX) { this.rotX=rotX; }

  // a float
  public float getRotY() { return rotY; }
  public void setRotY(float rotY) { this.rotY=rotY; }

  // a float
  public float getRotZ() { return rotZ; }
  public void setRotZ(float rotZ) { this.rotZ=rotZ; }

  public String toString() {
    StringBuilder result = new StringBuilder("SETPOINT_6DOF");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("trans_x=");
    result.append(this.transX);
    result.append(",");
    result.append("trans_y=");
    result.append(this.transY);
    result.append(",");
    result.append("trans_z=");
    result.append(this.transZ);
    result.append(",");
    result.append("rot_x=");
    result.append(this.rotX);
    result.append(",");
    result.append("rot_y=");
    result.append(this.rotY);
    result.append(",");
    result.append("rot_z=");
    result.append(this.rotZ);
    return result.toString();
  }
}
