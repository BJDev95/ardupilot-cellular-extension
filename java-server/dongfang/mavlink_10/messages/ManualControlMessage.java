package dongfang.mavlink_10.messages;
public class ManualControlMessage extends MavlinkMessage {
  private short target;
  private short x;
  private short y;
  private short z;
  private short r;
  private int buttons;

  public ManualControlMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 69; }

  public String getDescription() { return "This message provides an API for manually controlling the vehicle using standard joystick axes nomenclature, along with a joystick-like input device. Unused axes can be disabled an buttons are also transmit as boolean values of their "; }

  public int getExtraCRC() { return 52; }

  public int getLength() { return 11; }


  // a uint8_t
  public short getTarget() { return target; }
  public void setTarget(short target) { this.target=target; }

  // a int16_t
  public short getX() { return x; }
  public void setX(short x) { this.x=x; }

  // a int16_t
  public short getY() { return y; }
  public void setY(short y) { this.y=y; }

  // a int16_t
  public short getZ() { return z; }
  public void setZ(short z) { this.z=z; }

  // a int16_t
  public short getR() { return r; }
  public void setR(short r) { this.r=r; }

  // a uint16_t
  public int getButtons() { return buttons; }
  public void setButtons(int buttons) { this.buttons=buttons; }

  public String toString() {
    StringBuilder result = new StringBuilder("MANUAL_CONTROL");
    result.append(": ");
    result.append("target=");
    result.append(this.target);
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
    result.append("r=");
    result.append(this.r);
    result.append(",");
    result.append("buttons=");
    result.append(this.buttons);
    return result.toString();
  }
}
