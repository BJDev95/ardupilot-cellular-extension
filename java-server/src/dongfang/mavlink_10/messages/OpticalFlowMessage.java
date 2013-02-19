package dongfang.mavlink_10.messages;
public class OpticalFlowMessage extends MavlinkMessage {
  private long timeUsec;
  private short sensorId;
  private short flowX;
  private short flowY;
  private float flowCompMX;
  private float flowCompMY;
  private short quality;
  private float groundDistance;

  public OpticalFlowMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 100; }

  public String getDescription() { return "Optical flow from a flow sensor (e.g. optical mouse sensor)"; }

  public int getExtraCRC() { return 175; }

  public int getLength() { return 26; }


  // a uint64_t
  public long getTimeUsec() { return timeUsec; }
  public void setTimeUsec(long timeUsec) { this.timeUsec=timeUsec; }

  // a uint8_t
  public short getSensorId() { return sensorId; }
  public void setSensorId(short sensorId) { this.sensorId=sensorId; }

  // a int16_t
  public short getFlowX() { return flowX; }
  public void setFlowX(short flowX) { this.flowX=flowX; }

  // a int16_t
  public short getFlowY() { return flowY; }
  public void setFlowY(short flowY) { this.flowY=flowY; }

  // a float
  public float getFlowCompMX() { return flowCompMX; }
  public void setFlowCompMX(float flowCompMX) { this.flowCompMX=flowCompMX; }

  // a float
  public float getFlowCompMY() { return flowCompMY; }
  public void setFlowCompMY(float flowCompMY) { this.flowCompMY=flowCompMY; }

  // a uint8_t
  public short getQuality() { return quality; }
  public void setQuality(short quality) { this.quality=quality; }

  // a float
  public float getGroundDistance() { return groundDistance; }
  public void setGroundDistance(float groundDistance) { this.groundDistance=groundDistance; }

  public String toString() {
    StringBuilder result = new StringBuilder("OPTICAL_FLOW");
    result.append(": ");
    result.append("time_usec=");
    result.append(this.timeUsec);
    result.append(",");
    result.append("sensor_id=");
    result.append(this.sensorId);
    result.append(",");
    result.append("flow_x=");
    result.append(this.flowX);
    result.append(",");
    result.append("flow_y=");
    result.append(this.flowY);
    result.append(",");
    result.append("flow_comp_m_x=");
    result.append(this.flowCompMX);
    result.append(",");
    result.append("flow_comp_m_y=");
    result.append(this.flowCompMY);
    result.append(",");
    result.append("quality=");
    result.append(this.quality);
    result.append(",");
    result.append("ground_distance=");
    result.append(this.groundDistance);
    return result.toString();
  }
}
