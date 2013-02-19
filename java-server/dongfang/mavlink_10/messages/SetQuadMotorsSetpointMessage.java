package dongfang.mavlink_10.messages;
public class SetQuadMotorsSetpointMessage extends MavlinkMessage {
  private short targetSystem;
  private int motorFrontNw;
  private int motorRightNe;
  private int motorBackSe;
  private int motorLeftSw;

  public SetQuadMotorsSetpointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 60; }

  public String getDescription() { return "Setpoint in the four motor speeds"; }

  public int getExtraCRC() { return 30; }

  public int getLength() { return 9; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint16_t
  public int getMotorFrontNw() { return motorFrontNw; }
  public void setMotorFrontNw(int motorFrontNw) { this.motorFrontNw=motorFrontNw; }

  // a uint16_t
  public int getMotorRightNe() { return motorRightNe; }
  public void setMotorRightNe(int motorRightNe) { this.motorRightNe=motorRightNe; }

  // a uint16_t
  public int getMotorBackSe() { return motorBackSe; }
  public void setMotorBackSe(int motorBackSe) { this.motorBackSe=motorBackSe; }

  // a uint16_t
  public int getMotorLeftSw() { return motorLeftSw; }
  public void setMotorLeftSw(int motorLeftSw) { this.motorLeftSw=motorLeftSw; }

  public String toString() {
    StringBuilder result = new StringBuilder("SET_QUAD_MOTORS_SETPOINT");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("motor_front_nw=");
    result.append(this.motorFrontNw);
    result.append(",");
    result.append("motor_right_ne=");
    result.append(this.motorRightNe);
    result.append(",");
    result.append("motor_back_se=");
    result.append(this.motorBackSe);
    result.append(",");
    result.append("motor_left_sw=");
    result.append(this.motorLeftSw);
    return result.toString();
  }
}
