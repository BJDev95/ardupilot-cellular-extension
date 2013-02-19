package dongfang.mavlink_10.messages;
public class HilControlsMessage extends MavlinkMessage {
  private long timeUsec;
  private float rollAilerons;
  private float pitchElevator;
  private float yawRudder;
  private float throttle;
  private float aux1;
  private float aux2;
  private float aux3;
  private float aux4;
  private short mode;
  private short navMode;

  public HilControlsMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 91; }

  public String getDescription() { return "Sent from autopilot to simulation. Hardware in the loop control outputs"; }

  public int getExtraCRC() { return 63; }

  public int getLength() { return 42; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a float
  public float getRollAilerons() { return rollAilerons; }
  public void setRollAilerons(float rollAilerons) { this.rollAilerons=rollAilerons; }

  // a float
  public float getPitchElevator() { return pitchElevator; }
  public void setPitchElevator(float pitchElevator) { this.pitchElevator=pitchElevator; }

  // a float
  public float getYawRudder() { return yawRudder; }
  public void setYawRudder(float yawRudder) { this.yawRudder=yawRudder; }

  // a float
  public float getThrottle() { return throttle; }
  public void setThrottle(float throttle) { this.throttle=throttle; }

  // a float
  public float getAux1() { return aux1; }
  public void setAux1(float aux1) { this.aux1=aux1; }

  // a float
  public float getAux2() { return aux2; }
  public void setAux2(float aux2) { this.aux2=aux2; }

  // a float
  public float getAux3() { return aux3; }
  public void setAux3(float aux3) { this.aux3=aux3; }

  // a float
  public float getAux4() { return aux4; }
  public void setAux4(float aux4) { this.aux4=aux4; }

  // a uint8_t
  public short getMode() { return mode; }
  public void setMode(short mode) { this.mode=mode; }

  // a uint8_t
  public short getNavMode() { return navMode; }
  public void setNavMode(short navMode) { this.navMode=navMode; }

  public String toString() {
    StringBuilder result = new StringBuilder("HIL_CONTROLS");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("roll_ailerons=");
    result.append(this.rollAilerons);
    result.append(",");
    result.append("pitch_elevator=");
    result.append(this.pitchElevator);
    result.append(",");
    result.append("yaw_rudder=");
    result.append(this.yawRudder);
    result.append(",");
    result.append("throttle=");
    result.append(this.throttle);
    result.append(",");
    result.append("aux1=");
    result.append(this.aux1);
    result.append(",");
    result.append("aux2=");
    result.append(this.aux2);
    result.append(",");
    result.append("aux3=");
    result.append(this.aux3);
    result.append(",");
    result.append("aux4=");
    result.append(this.aux4);
    result.append(",");
    result.append("mode=");
    result.append(this.mode);
    result.append(",");
    result.append("nav_mode=");
    result.append(this.navMode);
    return result.toString();
  }
}
