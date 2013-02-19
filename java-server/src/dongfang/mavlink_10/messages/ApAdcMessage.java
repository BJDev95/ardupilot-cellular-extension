package dongfang.mavlink_10.messages;
public class ApAdcMessage extends MavlinkMessage {
  private int adc1;
  private int adc2;
  private int adc3;
  private int adc4;
  private int adc5;
  private int adc6;

  public ApAdcMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 153; }

  public String getDescription() { return "raw ADC output"; }

  public int getExtraCRC() { return 188; }

  public int getLength() { return 12; }


  // a uint16_t
  public int getAdc1() { return adc1; }
  public void setAdc1(int adc1) { this.adc1=adc1; }

  // a uint16_t
  public int getAdc2() { return adc2; }
  public void setAdc2(int adc2) { this.adc2=adc2; }

  // a uint16_t
  public int getAdc3() { return adc3; }
  public void setAdc3(int adc3) { this.adc3=adc3; }

  // a uint16_t
  public int getAdc4() { return adc4; }
  public void setAdc4(int adc4) { this.adc4=adc4; }

  // a uint16_t
  public int getAdc5() { return adc5; }
  public void setAdc5(int adc5) { this.adc5=adc5; }

  // a uint16_t
  public int getAdc6() { return adc6; }
  public void setAdc6(int adc6) { this.adc6=adc6; }

  public String toString() {
    StringBuilder result = new StringBuilder("AP_ADC");
    result.append(": ");
    result.append("adc1=");
    result.append(this.adc1);
    result.append(",");
    result.append("adc2=");
    result.append(this.adc2);
    result.append(",");
    result.append("adc3=");
    result.append(this.adc3);
    result.append(",");
    result.append("adc4=");
    result.append(this.adc4);
    result.append(",");
    result.append("adc5=");
    result.append(this.adc5);
    result.append(",");
    result.append("adc6=");
    result.append(this.adc6);
    return result.toString();
  }
}
