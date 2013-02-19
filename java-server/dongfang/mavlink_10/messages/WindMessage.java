package dongfang.mavlink_10.messages;
public class WindMessage extends MavlinkMessage {
  private float direction;
  private float speed;
  private float speedZ;

  public WindMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 168; }

  public String getDescription() { return "Wind estimation"; }

  public int getExtraCRC() { return 1; }

  public int getLength() { return 12; }


  // a float
  public float getDirection() { return direction; }
  public void setDirection(float direction) { this.direction=direction; }

  // a float
  public float getSpeed() { return speed; }
  public void setSpeed(float speed) { this.speed=speed; }

  // a float
  public float getSpeedZ() { return speedZ; }
  public void setSpeedZ(float speedZ) { this.speedZ=speedZ; }

  public String toString() {
    StringBuilder result = new StringBuilder("WIND");
    result.append(": ");
    result.append("direction=");
    result.append(this.direction);
    result.append(",");
    result.append("speed=");
    result.append(this.speed);
    result.append(",");
    result.append("speed_z=");
    result.append(this.speedZ);
    return result.toString();
  }
}
