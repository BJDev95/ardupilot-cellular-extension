package dongfang.mavlink_10.messages;
public class RcChannelsOverrideMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private int chan1Raw;
  private int chan2Raw;
  private int chan3Raw;
  private int chan4Raw;
  private int chan5Raw;
  private int chan6Raw;
  private int chan7Raw;
  private int chan8Raw;

  public RcChannelsOverrideMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 70; }

  public String getDescription() { return "The RAW values of the RC channels sent to the MAV to override info received from the RC radio. A value of -1 means no change to that channel. A value of 0 means control of that channel should be released back to the RC radio. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%. Individual receivers/transmitters might violate this specification."; }

  public int getExtraCRC() { return 124; }

  public int getLength() { return 18; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint16_t
  public int getChan1Raw() { return chan1Raw; }
  public void setChan1Raw(int chan1Raw) { this.chan1Raw=chan1Raw; }

  // a uint16_t
  public int getChan2Raw() { return chan2Raw; }
  public void setChan2Raw(int chan2Raw) { this.chan2Raw=chan2Raw; }

  // a uint16_t
  public int getChan3Raw() { return chan3Raw; }
  public void setChan3Raw(int chan3Raw) { this.chan3Raw=chan3Raw; }

  // a uint16_t
  public int getChan4Raw() { return chan4Raw; }
  public void setChan4Raw(int chan4Raw) { this.chan4Raw=chan4Raw; }

  // a uint16_t
  public int getChan5Raw() { return chan5Raw; }
  public void setChan5Raw(int chan5Raw) { this.chan5Raw=chan5Raw; }

  // a uint16_t
  public int getChan6Raw() { return chan6Raw; }
  public void setChan6Raw(int chan6Raw) { this.chan6Raw=chan6Raw; }

  // a uint16_t
  public int getChan7Raw() { return chan7Raw; }
  public void setChan7Raw(int chan7Raw) { this.chan7Raw=chan7Raw; }

  // a uint16_t
  public int getChan8Raw() { return chan8Raw; }
  public void setChan8Raw(int chan8Raw) { this.chan8Raw=chan8Raw; }

  public String toString() {
    StringBuilder result = new StringBuilder("RC_CHANNELS_OVERRIDE");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("chan1_raw=");
    result.append(this.chan1Raw);
    result.append(",");
    result.append("chan2_raw=");
    result.append(this.chan2Raw);
    result.append(",");
    result.append("chan3_raw=");
    result.append(this.chan3Raw);
    result.append(",");
    result.append("chan4_raw=");
    result.append(this.chan4Raw);
    result.append(",");
    result.append("chan5_raw=");
    result.append(this.chan5Raw);
    result.append(",");
    result.append("chan6_raw=");
    result.append(this.chan6Raw);
    result.append(",");
    result.append("chan7_raw=");
    result.append(this.chan7Raw);
    result.append(",");
    result.append("chan8_raw=");
    result.append(this.chan8Raw);
    return result.toString();
  }
}
