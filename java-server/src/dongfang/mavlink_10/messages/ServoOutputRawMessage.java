package dongfang.mavlink_10.messages;
public class ServoOutputRawMessage extends MavlinkMessage {
  private long timeBootMs;
  private short port;
  private int servo1Raw;
  private int servo2Raw;
  private int servo3Raw;
  private int servo4Raw;
  private int servo5Raw;
  private int servo6Raw;
  private int servo7Raw;
  private int servo8Raw;

  public ServoOutputRawMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 36; }

  public String getDescription() { return "The RAW values of the servo outputs (for RC input from the remote, use the RC_CHANNELS messages). The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%."; }

  public int getExtraCRC() { return 222; }

  public int getLength() { return 21; }


  // a uint32_t
  public long getTimeBootMs() { return timeBootMs; }
  public void setTimeBootMs(long timeBootMs) { this.timeBootMs=timeBootMs; }

  // a uint8_t
  public short getPort() { return port; }
  public void setPort(short port) { this.port=port; }

  // a uint16_t
  public int getServo1Raw() { return servo1Raw; }
  public void setServo1Raw(int servo1Raw) { this.servo1Raw=servo1Raw; }

  // a uint16_t
  public int getServo2Raw() { return servo2Raw; }
  public void setServo2Raw(int servo2Raw) { this.servo2Raw=servo2Raw; }

  // a uint16_t
  public int getServo3Raw() { return servo3Raw; }
  public void setServo3Raw(int servo3Raw) { this.servo3Raw=servo3Raw; }

  // a uint16_t
  public int getServo4Raw() { return servo4Raw; }
  public void setServo4Raw(int servo4Raw) { this.servo4Raw=servo4Raw; }

  // a uint16_t
  public int getServo5Raw() { return servo5Raw; }
  public void setServo5Raw(int servo5Raw) { this.servo5Raw=servo5Raw; }

  // a uint16_t
  public int getServo6Raw() { return servo6Raw; }
  public void setServo6Raw(int servo6Raw) { this.servo6Raw=servo6Raw; }

  // a uint16_t
  public int getServo7Raw() { return servo7Raw; }
  public void setServo7Raw(int servo7Raw) { this.servo7Raw=servo7Raw; }

  // a uint16_t
  public int getServo8Raw() { return servo8Raw; }
  public void setServo8Raw(int servo8Raw) { this.servo8Raw=servo8Raw; }

  public String toString() {
    StringBuilder result = new StringBuilder("SERVO_OUTPUT_RAW");
    result.append(": ");
    result.append("time_boot_ms=");
    result.append(this.timeBootMs);
    result.append(",");
    result.append("port=");
    result.append(this.port);
    result.append(",");
    result.append("servo1_raw=");
    result.append(this.servo1Raw);
    result.append(",");
    result.append("servo2_raw=");
    result.append(this.servo2Raw);
    result.append(",");
    result.append("servo3_raw=");
    result.append(this.servo3Raw);
    result.append(",");
    result.append("servo4_raw=");
    result.append(this.servo4Raw);
    result.append(",");
    result.append("servo5_raw=");
    result.append(this.servo5Raw);
    result.append(",");
    result.append("servo6_raw=");
    result.append(this.servo6Raw);
    result.append(",");
    result.append("servo7_raw=");
    result.append(this.servo7Raw);
    result.append(",");
    result.append("servo8_raw=");
    result.append(this.servo8Raw);
    return result.toString();
  }
}
