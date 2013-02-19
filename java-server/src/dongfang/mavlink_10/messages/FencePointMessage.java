package dongfang.mavlink_10.messages;
public class FencePointMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short idx;
  private short count;
  private float lat;
  private float lng;

  public FencePointMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 160; }

  public String getDescription() { return "A fence point. Used to set a point when from 	      GCS -> MAV. Also used to return a point from MAV -> GCS"; }

  public int getExtraCRC() { return 78; }

  public int getLength() { return 12; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a uint8_t
  public short getIdx() { return idx; }
  public void setIdx(short idx) { this.idx=idx; }

  // a uint8_t
  public short getCount() { return count; }
  public void setCount(short count) { this.count=count; }

  // a float
  public float getLat() { return lat; }
  public void setLat(float lat) { this.lat=lat; }

  // a float
  public float getLng() { return lng; }
  public void setLng(float lng) { this.lng=lng; }

  public String toString() {
    StringBuilder result = new StringBuilder("FENCE_POINT");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("idx=");
    result.append(this.idx);
    result.append(",");
    result.append("count=");
    result.append(this.count);
    result.append(",");
    result.append("lat=");
    result.append(this.lat);
    result.append(",");
    result.append("lng=");
    result.append(this.lng);
    return result.toString();
  }
}
