package dongfang.mavlink_10.messages;
public class SetLocalPositionSetpointMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short coordinateFrame;
  private float x;
  private float y;
  private float z;
  private float yaw;

  public SetLocalPositionSetpointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 50; }

  public String getDescription() { return "Set the setpoint for a local position controller. This is the position in local coordinates the MAV should fly to. This message is sent by the path/MISSION planner to the onboard position controller. As some MAVs have a degree of freedom in yaw (e.g. all helicopters/quadrotors), the desired yaw angle is part of the message."; }

  public int getExtraCRC() { return 214; }

  public int getLength() { return 19; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getCoordinateFrame() { return coordinateFrame; }
  public void setCoordinateFrame(short coordinateFrame) { this.coordinateFrame=coordinateFrame; }

  // a float
  public float getX() { return x; }
  public void setX(float x) { this.x=x; }

  // a float
  public float getY() { return y; }
  public void setY(float y) { this.y=y; }

  // a float
  public float getZ() { return z; }
  public void setZ(float z) { this.z=z; }

  // a float
  public float getYaw() { return yaw; }
  public void setYaw(float yaw) { this.yaw=yaw; }

  public String toString() {
    StringBuilder result = new StringBuilder("SET_LOCAL_POSITION_SETPOINT");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("coordinate_frame=");
    result.append(this.coordinateFrame);
    result.append(",");
    result.append("x=");
    result.append(this.x);
    result.append(",");
    result.append("y=");
    result.append(this.y);
    result.append(",");
    result.append("z=");
    result.append(this.z);
    result.append(",");
    result.append("yaw=");
    result.append(this.yaw);
    return result.toString();
  }
}
