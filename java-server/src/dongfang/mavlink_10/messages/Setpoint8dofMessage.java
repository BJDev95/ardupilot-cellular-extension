package dongfang.mavlink_10.messages;
public class Setpoint8dofMessage extends MavlinkMessage {
  private short targetSystem;
  private float val1;
  private float val2;
  private float val3;
  private float val4;
  private float val5;
  private float val6;
  private float val7;
  private float val8;

  public Setpoint8dofMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 148; }

  public String getDescription() { return "Set the 8 DOF setpoint for a controller."; }

  public int getExtraCRC() { return 0; }

  public int getLength() { return 33; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a float
  public float getVal1() { return val1; }
  public void setVal1(float val1) { this.val1=val1; }

  // a float
  public float getVal2() { return val2; }
  public void setVal2(float val2) { this.val2=val2; }

  // a float
  public float getVal3() { return val3; }
  public void setVal3(float val3) { this.val3=val3; }

  // a float
  public float getVal4() { return val4; }
  public void setVal4(float val4) { this.val4=val4; }

  // a float
  public float getVal5() { return val5; }
  public void setVal5(float val5) { this.val5=val5; }

  // a float
  public float getVal6() { return val6; }
  public void setVal6(float val6) { this.val6=val6; }

  // a float
  public float getVal7() { return val7; }
  public void setVal7(float val7) { this.val7=val7; }

  // a float
  public float getVal8() { return val8; }
  public void setVal8(float val8) { this.val8=val8; }

  public String toString() {
    StringBuilder result = new StringBuilder("SETPOINT_8DOF");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("val1=");
    result.append(this.val1);
    result.append(",");
    result.append("val2=");
    result.append(this.val2);
    result.append(",");
    result.append("val3=");
    result.append(this.val3);
    result.append(",");
    result.append("val4=");
    result.append(this.val4);
    result.append(",");
    result.append("val5=");
    result.append(this.val5);
    result.append(",");
    result.append("val6=");
    result.append(this.val6);
    result.append(",");
    result.append("val7=");
    result.append(this.val7);
    result.append(",");
    result.append("val8=");
    result.append(this.val8);
    return result.toString();
  }
}
