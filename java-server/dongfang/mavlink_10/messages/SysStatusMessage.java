package dongfang.mavlink_10.messages;
public class SysStatusMessage extends MavlinkMessage {
  private long onboardControlSensorsPresent;
  private long onboardControlSensorsEnabled;
  private long onboardControlSensorsHealth;
  private int load;
  private int voltageBattery;
  private short currentBattery;
  private byte batteryRemaining;
  private int dropRateComm;
  private int errorsComm;
  private int errorsCount1;
  private int errorsCount2;
  private int errorsCount3;
  private int errorsCount4;

  public SysStatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 1; }

  public String getDescription() { return "The general system state. If the system is following the MAVLink standard, the system state is mainly defined by three orthogonal states/modes: The system mode, which is either LOCKED (motors shut down and locked), MANUAL (system under RC control), GUIDED (system with autonomous position control, position setpoint controlled manually) or AUTO (system guided by path/waypoint planner). The NAV_MODE defined the current flight state: LIFTOFF (often an open-loop maneuver), LANDING, WAYPOINTS or VECTOR. This represents the internal navigation state machine. The system status shows wether the system is currently active or not and if an emergency occured. During the CRITICAL and EMERGENCY states the MAV is still considered to be active, but should start emergency procedures autonomously. After a failure occured it should first move from active to critical to allow manual intervention and then move to emergency after a certain timeout."; }

  public int getExtraCRC() { return 124; }

  public int getLength() { return 31; }


  // a uint32_t
  public long getOnboardControlSensorsPresent() { return onboardControlSensorsPresent; }
  public void setOnboardControlSensorsPresent(long onboardControlSensorsPresent) { this.onboardControlSensorsPresent=onboardControlSensorsPresent; }

  // a uint32_t
  public long getOnboardControlSensorsEnabled() { return onboardControlSensorsEnabled; }
  public void setOnboardControlSensorsEnabled(long onboardControlSensorsEnabled) { this.onboardControlSensorsEnabled=onboardControlSensorsEnabled; }

  // a uint32_t
  public long getOnboardControlSensorsHealth() { return onboardControlSensorsHealth; }
  public void setOnboardControlSensorsHealth(long onboardControlSensorsHealth) { this.onboardControlSensorsHealth=onboardControlSensorsHealth; }

  // a uint16_t
  public int getLoad() { return load; }
  public void setLoad(int load) { this.load=load; }

  // a uint16_t
  public int getVoltageBattery() { return voltageBattery; }
  public void setVoltageBattery(int voltageBattery) { this.voltageBattery=voltageBattery; }

  // a int16_t
  public short getCurrentBattery() { return currentBattery; }
  public void setCurrentBattery(short currentBattery) { this.currentBattery=currentBattery; }

  // a int8_t
  public byte getBatteryRemaining() { return batteryRemaining; }
  public void setBatteryRemaining(byte batteryRemaining) { this.batteryRemaining=batteryRemaining; }

  // a uint16_t
  public int getDropRateComm() { return dropRateComm; }
  public void setDropRateComm(int dropRateComm) { this.dropRateComm=dropRateComm; }

  // a uint16_t
  public int getErrorsComm() { return errorsComm; }
  public void setErrorsComm(int errorsComm) { this.errorsComm=errorsComm; }

  // a uint16_t
  public int getErrorsCount1() { return errorsCount1; }
  public void setErrorsCount1(int errorsCount1) { this.errorsCount1=errorsCount1; }

  // a uint16_t
  public int getErrorsCount2() { return errorsCount2; }
  public void setErrorsCount2(int errorsCount2) { this.errorsCount2=errorsCount2; }

  // a uint16_t
  public int getErrorsCount3() { return errorsCount3; }
  public void setErrorsCount3(int errorsCount3) { this.errorsCount3=errorsCount3; }

  // a uint16_t
  public int getErrorsCount4() { return errorsCount4; }
  public void setErrorsCount4(int errorsCount4) { this.errorsCount4=errorsCount4; }

  public String toString() {
    StringBuilder result = new StringBuilder("SYS_STATUS");
    result.append(": ");
    result.append("onboard_control_sensors_present=");
    result.append(this.onboardControlSensorsPresent);
    result.append(",");
    result.append("onboard_control_sensors_enabled=");
    result.append(this.onboardControlSensorsEnabled);
    result.append(",");
    result.append("onboard_control_sensors_health=");
    result.append(this.onboardControlSensorsHealth);
    result.append(",");
    result.append("load=");
    result.append(this.load);
    result.append(",");
    result.append("voltage_battery=");
    result.append(this.voltageBattery);
    result.append(",");
    result.append("current_battery=");
    result.append(this.currentBattery);
    result.append(",");
    result.append("battery_remaining=");
    result.append(this.batteryRemaining);
    result.append(",");
    result.append("drop_rate_comm=");
    result.append(this.dropRateComm);
    result.append(",");
    result.append("errors_comm=");
    result.append(this.errorsComm);
    result.append(",");
    result.append("errors_count1=");
    result.append(this.errorsCount1);
    result.append(",");
    result.append("errors_count2=");
    result.append(this.errorsCount2);
    result.append(",");
    result.append("errors_count3=");
    result.append(this.errorsCount3);
    result.append(",");
    result.append("errors_count4=");
    result.append(this.errorsCount4);
    return result.toString();
  }
}
