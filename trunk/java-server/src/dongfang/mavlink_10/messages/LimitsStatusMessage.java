package dongfang.mavlink_10.messages;
public class LimitsStatusMessage extends MavlinkMessage {
  private short limitsState;
  private long lastTrigger;
  private long lastAction;
  private long lastRecovery;
  private long lastClear;
  private int breachCount;
  private short modsEnabled;
  private short modsRequired;
  private short modsTriggered;

  public LimitsStatusMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 167; }

  public String getDescription() { return "Status of AP_Limits. Sent in extended 	    status stream when AP_Limits is enabled"; }

  public int getExtraCRC() { return 144; }

  public int getLength() { return 22; }


  // a uint8_t
  public short getLimitsState() { return limitsState; }
  public void setLimitsState(short limitsState) { this.limitsState=limitsState; }

  // a uint32_t
  public long getLastTrigger() { return lastTrigger; }
  public void setLastTrigger(long lastTrigger) { this.lastTrigger=lastTrigger; }

  // a uint32_t
  public long getLastAction() { return lastAction; }
  public void setLastAction(long lastAction) { this.lastAction=lastAction; }

  // a uint32_t
  public long getLastRecovery() { return lastRecovery; }
  public void setLastRecovery(long lastRecovery) { this.lastRecovery=lastRecovery; }

  // a uint32_t
  public long getLastClear() { return lastClear; }
  public void setLastClear(long lastClear) { this.lastClear=lastClear; }

  // a uint16_t
  public int getBreachCount() { return breachCount; }
  public void setBreachCount(int breachCount) { this.breachCount=breachCount; }

  // a uint8_t
  public short getModsEnabled() { return modsEnabled; }
  public void setModsEnabled(short modsEnabled) { this.modsEnabled=modsEnabled; }

  // a uint8_t
  public short getModsRequired() { return modsRequired; }
  public void setModsRequired(short modsRequired) { this.modsRequired=modsRequired; }

  // a uint8_t
  public short getModsTriggered() { return modsTriggered; }
  public void setModsTriggered(short modsTriggered) { this.modsTriggered=modsTriggered; }

  public String toString() {
    StringBuilder result = new StringBuilder("LIMITS_STATUS");
    result.append(": ");
    result.append("limits_state=");
    result.append(this.limitsState);
    result.append(",");
    result.append("last_trigger=");
    result.append(this.lastTrigger);
    result.append(",");
    result.append("last_action=");
    result.append(this.lastAction);
    result.append(",");
    result.append("last_recovery=");
    result.append(this.lastRecovery);
    result.append(",");
    result.append("last_clear=");
    result.append(this.lastClear);
    result.append(",");
    result.append("breach_count=");
    result.append(this.breachCount);
    result.append(",");
    result.append("mods_enabled=");
    result.append(this.modsEnabled);
    result.append(",");
    result.append("mods_required=");
    result.append(this.modsRequired);
    result.append(",");
    result.append("mods_triggered=");
    result.append(this.modsTriggered);
    return result.toString();
  }
}
