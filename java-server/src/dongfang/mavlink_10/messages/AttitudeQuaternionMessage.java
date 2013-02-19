package dongfang.mavlink_10.messages;
public class AttitudeQuaternionMessage extends MavlinkMessage {
  private long timeBootMs;
  private float q1;
  private float q2;
  private float q3;
  private float q4;
  private float rollspeed;
  private float pitchspeed;
  private float yawspeed;

  public AttitudeQuaternionMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 31; }

  public String getDescription() { return "The attitude in the aeronautical frame (right-handed, Z-down, X-front, Y-right), expressed as quaternion."; }

  public int getExtraCRC() { return 246; }

  public int getLength() { return 32; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a float
  public float getQ1() { return q1; }
  public void setQ1(float q1) { this.q1=q1; }

  // a float
  public float getQ2() { return q2; }
  public void setQ2(float q2) { this.q2=q2; }

  // a float
  public float getQ3() { return q3; }
  public void setQ3(float q3) { this.q3=q3; }

  // a float
  public float getQ4() { return q4; }
  public void setQ4(float q4) { this.q4=q4; }

  // a float
  public float getRollspeed() { return rollspeed; }
  public void setRollspeed(float rollspeed) { this.rollspeed=rollspeed; }

  // a float
  public float getPitchspeed() { return pitchspeed; }
  public void setPitchspeed(float pitchspeed) { this.pitchspeed=pitchspeed; }

  // a float
  public float getYawspeed() { return yawspeed; }
  public void setYawspeed(float yawspeed) { this.yawspeed=yawspeed; }

  public String toString() {
    StringBuilder result = new StringBuilder("ATTITUDE_QUATERNION");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("q1=");
    result.append(this.q1);
    result.append(",");
    result.append("q2=");
    result.append(this.q2);
    result.append(",");
    result.append("q3=");
    result.append(this.q3);
    result.append(",");
    result.append("q4=");
    result.append(this.q4);
    result.append(",");
    result.append("rollspeed=");
    result.append(this.rollspeed);
    result.append(",");
    result.append("pitchspeed=");
    result.append(this.pitchspeed);
    result.append(",");
    result.append("yawspeed=");
    result.append(this.yawspeed);
    return result.toString();
  }
}
