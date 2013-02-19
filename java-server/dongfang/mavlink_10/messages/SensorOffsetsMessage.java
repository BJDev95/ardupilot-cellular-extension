package dongfang.mavlink_10.messages;
public class SensorOffsetsMessage extends MavlinkMessage {
  private short magOfsX;
  private short magOfsY;
  private short magOfsZ;
  private float magDeclination;
  private int rawPress;
  private int rawTemp;
  private float gyroCalX;
  private float gyroCalY;
  private float gyroCalZ;
  private float accelCalX;
  private float accelCalY;
  private float accelCalZ;

  public SensorOffsetsMessage(int sequence, int systemId, int componentId) {
    super(sequence, systemId, componentId);
  }

  public int getId() { return 150; }

  public String getDescription() { return "Offsets and calibrations values for hardware         sensors. This makes it easier to debug the calibration process."; }

  public int getExtraCRC() { return 134; }

  public int getLength() { return 42; }


  // a int16_t
  public short getMagOfsX() { return magOfsX; }
  public void setMagOfsX(short magOfsX) { this.magOfsX=magOfsX; }

  // a int16_t
  public short getMagOfsY() { return magOfsY; }
  public void setMagOfsY(short magOfsY) { this.magOfsY=magOfsY; }

  // a int16_t
  public short getMagOfsZ() { return magOfsZ; }
  public void setMagOfsZ(short magOfsZ) { this.magOfsZ=magOfsZ; }

  // a float
  public float getMagDeclination() { return magDeclination; }
  public void setMagDeclination(float magDeclination) { this.magDeclination=magDeclination; }

  // a int32_t
  public int getRawPress() { return rawPress; }
  public void setRawPress(int rawPress) { this.rawPress=rawPress; }

  // a int32_t
  public int getRawTemp() { return rawTemp; }
  public void setRawTemp(int rawTemp) { this.rawTemp=rawTemp; }

  // a float
  public float getGyroCalX() { return gyroCalX; }
  public void setGyroCalX(float gyroCalX) { this.gyroCalX=gyroCalX; }

  // a float
  public float getGyroCalY() { return gyroCalY; }
  public void setGyroCalY(float gyroCalY) { this.gyroCalY=gyroCalY; }

  // a float
  public float getGyroCalZ() { return gyroCalZ; }
  public void setGyroCalZ(float gyroCalZ) { this.gyroCalZ=gyroCalZ; }

  // a float
  public float getAccelCalX() { return accelCalX; }
  public void setAccelCalX(float accelCalX) { this.accelCalX=accelCalX; }

  // a float
  public float getAccelCalY() { return accelCalY; }
  public void setAccelCalY(float accelCalY) { this.accelCalY=accelCalY; }

  // a float
  public float getAccelCalZ() { return accelCalZ; }
  public void setAccelCalZ(float accelCalZ) { this.accelCalZ=accelCalZ; }

  public String toString() {
    StringBuilder result = new StringBuilder("SENSOR_OFFSETS");
    result.append(": ");
    result.append("mag_ofs_x=");
    result.append(this.magOfsX);
    result.append(",");
    result.append("mag_ofs_y=");
    result.append(this.magOfsY);
    result.append(",");
    result.append("mag_ofs_z=");
    result.append(this.magOfsZ);
    result.append(",");
    result.append("mag_declination=");
    result.append(this.magDeclination);
    result.append(",");
    result.append("raw_press=");
    result.append(this.rawPress);
    result.append(",");
    result.append("raw_temp=");
    result.append(this.rawTemp);
    result.append(",");
    result.append("gyro_cal_x=");
    result.append(this.gyroCalX);
    result.append(",");
    result.append("gyro_cal_y=");
    result.append(this.gyroCalY);
    result.append(",");
    result.append("gyro_cal_z=");
    result.append(this.gyroCalZ);
    result.append(",");
    result.append("accel_cal_x=");
    result.append(this.accelCalX);
    result.append(",");
    result.append("accel_cal_y=");
    result.append(this.accelCalY);
    result.append(",");
    result.append("accel_cal_z=");
    result.append(this.accelCalZ);
    return result.toString();
  }
}
