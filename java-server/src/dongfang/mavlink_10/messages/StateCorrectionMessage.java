package dongfang.mavlink_10.messages;
public class StateCorrectionMessage extends MavlinkMessage {
  private float xerr;
  private float yerr;
  private float zerr;
  private float rollerr;
  private float pitcherr;
  private float yawerr;
  private float vxerr;
  private float vyerr;
  private float vzerr;

  public StateCorrectionMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 64; }

  public String getDescription() { return "Corrects the systems state by adding an error correction term to the position and velocity, and by rotating the attitude by a correction angle."; }

  public int getExtraCRC() { return 130; }

  public int getLength() { return 36; }


  // a float
  public float getXerr() { return xerr; }
  public void setXerr(float xerr) { this.xerr=xerr; }

  // a float
  public float getYerr() { return yerr; }
  public void setYerr(float yerr) { this.yerr=yerr; }

  // a float
  public float getZerr() { return zerr; }
  public void setZerr(float zerr) { this.zerr=zerr; }

  // a float
  public float getRollerr() { return rollerr; }
  public void setRollerr(float rollerr) { this.rollerr=rollerr; }

  // a float
  public float getPitcherr() { return pitcherr; }
  public void setPitcherr(float pitcherr) { this.pitcherr=pitcherr; }

  // a float
  public float getYawerr() { return yawerr; }
  public void setYawerr(float yawerr) { this.yawerr=yawerr; }

  // a float
  public float getVxerr() { return vxerr; }
  public void setVxerr(float vxerr) { this.vxerr=vxerr; }

  // a float
  public float getVyerr() { return vyerr; }
  public void setVyerr(float vyerr) { this.vyerr=vyerr; }

  // a float
  public float getVzerr() { return vzerr; }
  public void setVzerr(float vzerr) { this.vzerr=vzerr; }

  public String toString() {
    StringBuilder result = new StringBuilder("STATE_CORRECTION");
    result.append(": ");
    result.append("xErr=");
    result.append(this.xerr);
    result.append(",");
    result.append("yErr=");
    result.append(this.yerr);
    result.append(",");
    result.append("zErr=");
    result.append(this.zerr);
    result.append(",");
    result.append("rollErr=");
    result.append(this.rollerr);
    result.append(",");
    result.append("pitchErr=");
    result.append(this.pitcherr);
    result.append(",");
    result.append("yawErr=");
    result.append(this.yawerr);
    result.append(",");
    result.append("vxErr=");
    result.append(this.vxerr);
    result.append(",");
    result.append("vyErr=");
    result.append(this.vyerr);
    result.append(",");
    result.append("vzErr=");
    result.append(this.vzerr);
    return result.toString();
  }
}
