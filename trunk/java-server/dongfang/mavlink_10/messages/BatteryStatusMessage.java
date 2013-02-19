package dongfang.mavlink_10.messages;
public class BatteryStatusMessage extends MavlinkMessage {
  private short accuId;
  private int voltageCell1;
  private int voltageCell2;
  private int voltageCell3;
  private int voltageCell4;
  private int voltageCell5;
  private int voltageCell6;
  private short currentBattery;
  private byte batteryRemaining;

  public BatteryStatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 147; }

  public String getDescription() { return "Transmitte battery informations for a accu pack."; }

  public int getExtraCRC() { return 0; }

  public int getLength() { return 16; }


  // a uint8_t
  public short getAccuId() { return accuId; }
  public void setAccuId(short accuId) { this.accuId=accuId; }

  // a uint16_t
  public int getVoltageCell1() { return voltageCell1; }
  public void setVoltageCell1(int voltageCell1) { this.voltageCell1=voltageCell1; }

  // a uint16_t
  public int getVoltageCell2() { return voltageCell2; }
  public void setVoltageCell2(int voltageCell2) { this.voltageCell2=voltageCell2; }

  // a uint16_t
  public int getVoltageCell3() { return voltageCell3; }
  public void setVoltageCell3(int voltageCell3) { this.voltageCell3=voltageCell3; }

  // a uint16_t
  public int getVoltageCell4() { return voltageCell4; }
  public void setVoltageCell4(int voltageCell4) { this.voltageCell4=voltageCell4; }

  // a uint16_t
  public int getVoltageCell5() { return voltageCell5; }
  public void setVoltageCell5(int voltageCell5) { this.voltageCell5=voltageCell5; }

  // a uint16_t
  public int getVoltageCell6() { return voltageCell6; }
  public void setVoltageCell6(int voltageCell6) { this.voltageCell6=voltageCell6; }

  // a int16_t
  public short getCurrentBattery() { return currentBattery; }
  public void setCurrentBattery(short currentBattery) { this.currentBattery=currentBattery; }

  // a int8_t
  public byte getBatteryRemaining() { return batteryRemaining; }
  public void setBatteryRemaining(byte batteryRemaining) { this.batteryRemaining=batteryRemaining; }

  public String toString() {
    StringBuilder result = new StringBuilder("BATTERY_STATUS");
    result.append(": ");
    result.append("accu_id=");
    result.append(this.accuId);
    result.append(",");
    result.append("voltage_cell_1=");
    result.append(this.voltageCell1);
    result.append(",");
    result.append("voltage_cell_2=");
    result.append(this.voltageCell2);
    result.append(",");
    result.append("voltage_cell_3=");
    result.append(this.voltageCell3);
    result.append(",");
    result.append("voltage_cell_4=");
    result.append(this.voltageCell4);
    result.append(",");
    result.append("voltage_cell_5=");
    result.append(this.voltageCell5);
    result.append(",");
    result.append("voltage_cell_6=");
    result.append(this.voltageCell6);
    result.append(",");
    result.append("current_battery=");
    result.append(this.currentBattery);
    result.append(",");
    result.append("battery_remaining=");
    result.append(this.batteryRemaining);
    return result.toString();
  }
}
