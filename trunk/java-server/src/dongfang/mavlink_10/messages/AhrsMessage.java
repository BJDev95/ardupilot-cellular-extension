package dongfang.mavlink_10.messages;
public class AhrsMessage extends MavlinkMessage {
  private float omegaix;
  private float omegaiy;
  private float omegaiz;
  private float accelWeight;
  private float renormVal;
  private float errorRp;
  private float errorYaw;

  public AhrsMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 163; }

  public String getDescription() { return "Status of DCM attitude estimator"; }

  public int getExtraCRC() { return 127; }

  public int getLength() { return 28; }


  // a float
  public float getOmegaix() { return omegaix; }
  public void setOmegaix(float omegaix) { this.omegaix=omegaix; }

  // a float
  public float getOmegaiy() { return omegaiy; }
  public void setOmegaiy(float omegaiy) { this.omegaiy=omegaiy; }

  // a float
  public float getOmegaiz() { return omegaiz; }
  public void setOmegaiz(float omegaiz) { this.omegaiz=omegaiz; }

  // a float
  public float getAccelWeight() { return accelWeight; }
  public void setAccelWeight(float accelWeight) { this.accelWeight=accelWeight; }

  // a float
  public float getRenormVal() { return renormVal; }
  public void setRenormVal(float renormVal) { this.renormVal=renormVal; }

  // a float
  public float getErrorRp() { return errorRp; }
  public void setErrorRp(float errorRp) { this.errorRp=errorRp; }

  // a float
  public float getErrorYaw() { return errorYaw; }
  public void setErrorYaw(float errorYaw) { this.errorYaw=errorYaw; }

  public String toString() {
    StringBuilder result = new StringBuilder("AHRS");
    result.append(": ");
    result.append("omegaIx=");
    result.append(this.omegaix);
    result.append(",");
    result.append("omegaIy=");
    result.append(this.omegaiy);
    result.append(",");
    result.append("omegaIz=");
    result.append(this.omegaiz);
    result.append(",");
    result.append("accel_weight=");
    result.append(this.accelWeight);
    result.append(",");
    result.append("renorm_val=");
    result.append(this.renormVal);
    result.append(",");
    result.append("error_rp=");
    result.append(this.errorRp);
    result.append(",");
    result.append("error_yaw=");
    result.append(this.errorYaw);
    return result.toString();
  }
}
