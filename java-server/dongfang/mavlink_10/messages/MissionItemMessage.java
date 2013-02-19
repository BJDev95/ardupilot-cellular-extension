package dongfang.mavlink_10.messages;
public class MissionItemMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int seq;
  private short frame;
  private int command;
  private short current;
  private short autocontinue;
  private float param1;
  private float param2;
  private float param3;
  private float param4;
  private float x;
  private float y;
  private float z;

  public MissionItemMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 39; }

  public String getDescription() { return "Message encoding a mission item. This message is emitted to announce                 the presence of a mission item and to set a mission item on the system. The mission item can be either in x, y, z meters (type: LOCAL) or x:lat, y:lon, z:altitude. Local frame is Z-down, right handed (NED), global frame is Z-up, right handed (ENU). See also http://qgroundcontrol.org/mavlink/waypoint_protocol."; }

  public int getExtraCRC() { return 254; }

  public int getLength() { return 37; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint16_t
  public int getSeq() { return seq; }
  public void setSeq(int seq) { this.seq=seq; }

  // a uint8_t
  public short getFrame() { return frame; }
  public void setFrame(short frame) { this.frame=frame; }

  // a uint16_t
  public int getCommand() { return command; }
  public void setCommand(int command) { this.command=command; }

  // a uint8_t
  public short getCurrent() { return current; }
  public void setCurrent(short current) { this.current=current; }

  // a uint8_t
  public short getAutocontinue() { return autocontinue; }
  public void setAutocontinue(short autocontinue) { this.autocontinue=autocontinue; }

  // a float
  public float getParam1() { return param1; }
  public void setParam1(float param1) { this.param1=param1; }

  // a float
  public float getParam2() { return param2; }
  public void setParam2(float param2) { this.param2=param2; }

  // a float
  public float getParam3() { return param3; }
  public void setParam3(float param3) { this.param3=param3; }

  // a float
  public float getParam4() { return param4; }
  public void setParam4(float param4) { this.param4=param4; }

  // a float
  public float getX() { return x; }
  public void setX(float x) { this.x=x; }

  // a float
  public float getY() { return y; }
  public void setY(float y) { this.y=y; }

  // a float
  public float getZ() { return z; }
  public void setZ(float z) { this.z=z; }

  public String toString() {
    StringBuilder result = new StringBuilder("MISSION_ITEM");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("seq=");
    result.append(this.seq);
    result.append(",");
    result.append("frame=");
    result.append(this.frame);
    result.append(",");
    result.append("command=");
    result.append(this.command);
    result.append(",");
    result.append("current=");
    result.append(this.current);
    result.append(",");
    result.append("autocontinue=");
    result.append(this.autocontinue);
    result.append(",");
    result.append("param1=");
    result.append(this.param1);
    result.append(",");
    result.append("param2=");
    result.append(this.param2);
    result.append(",");
    result.append("param3=");
    result.append(this.param3);
    result.append(",");
    result.append("param4=");
    result.append(this.param4);
    result.append(",");
    result.append("x=");
    result.append(this.x);
    result.append(",");
    result.append("y=");
    result.append(this.y);
    result.append(",");
    result.append("z=");
    result.append(this.z);
    return result.toString();
  }
}
