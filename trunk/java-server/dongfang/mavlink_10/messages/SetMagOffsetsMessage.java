package dongfang.mavlink_10.messages;
public class SetMagOffsetsMessage extends MavlinkMessage {
  private short targetSystem;
  private short targetComponent;
  private short magOfsX;
  private short magOfsY;
  private short magOfsZ;

  public SetMagOffsetsMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 151; }

  public String getDescription() { return "set the magnetometer offsets"; }

  public int getExtraCRC() { return 219; }

  public int getLength() { return 8; }


  // a uint8_t
  public short getTargetSystem() { return targetSystem; }
  public void setTargetSystem(short targetSystem) { this.targetSystem=targetSystem; }

  // a uint8_t
  public short getTargetComponent() { return targetComponent; }
  public void setTargetComponent(short targetComponent) { this.targetComponent=targetComponent; }

  // a int16_t
  public short getMagOfsX() { return magOfsX; }
  public void setMagOfsX(short magOfsX) { this.magOfsX=magOfsX; }

  // a int16_t
  public short getMagOfsY() { return magOfsY; }
  public void setMagOfsY(short magOfsY) { this.magOfsY=magOfsY; }

  // a int16_t
  public short getMagOfsZ() { return magOfsZ; }
  public void setMagOfsZ(short magOfsZ) { this.magOfsZ=magOfsZ; }

  public String toString() {
    StringBuilder result = new StringBuilder("SET_MAG_OFFSETS");
    result.append(": ");
    result.append("target_system=");
    result.append(this.targetSystem);
    result.append(",");
    result.append("target_component=");
    result.append(this.targetComponent);
    result.append(",");
    result.append("mag_ofs_x=");
    result.append(this.magOfsX);
    result.append(",");
    result.append("mag_ofs_y=");
    result.append(this.magOfsY);
    result.append(",");
    result.append("mag_ofs_z=");
    result.append(this.magOfsZ);
    return result.toString();
  }
}
