package dongfang.mavlink_10.serialization;
import dongfang.mavlink_10.messages.*;
import java.io.IOException;
import java.io.OutputStream;
public class MavlinkSerializer implements MavlinkMessageVisitor {

  private OutputStream out;

  private void writeUInt8(short data) throws IOException {
  	out.write(data);
  }

  private void writeInt8(byte data) throws IOException {
  	out.write(data);
  }
  
  private void writeUInt16(int data) throws IOException {
  	out.write(data);
  	out.write(data>>>8);
  }
  
  private void writeInt16(short data) throws IOException {
  	out.write(data);
  	out.write(data>>>8);
  }
  
  private void writeUInt32(long data) throws IOException {
  	out.write((int)data);
  	out.write((int)(data>>>8));
  	out.write((int)(data>>>16));
  	out.write((int)(data>>>24));
  }
  
  private void writeInt32(int data) throws IOException {
  	out.write((int)data);
  	out.write((int)(data>>>8));
  	out.write((int)(data>>>16));
  	out.write((int)(data>>>24));
  }
  
  private void writeUInt64(long data) throws IOException {
  	out.write((int)data);
  	out.write((int)(data>>>8));
  	out.write((int)(data>>>16));
  	out.write((int)(data>>>24));
  	out.write((int)(data>>>32));
  	out.write((int)(data>>>40));
  	out.write((int)(data>>>48));
  	out.write((int)(data>>>56));
  }
  
  private void writeInt64(long data) throws IOException {
  	out.write((int)data);
  	out.write((int)(data>>>8));
  	out.write((int)(data>>>16));
  	out.write((int)(data>>>24));
  	out.write((int)(data>>>32));
  	out.write((int)(data>>>40));
  	out.write((int)(data>>>48));
  	out.write((int)(data>>>56));
  }
  
  private void writeFloat(float data) throws IOException {
  	int asInt = Float.floatToIntBits(data);
  	out.write((int)(asInt>>>24));
  	out.write((int)(asInt>>>16));
  	out.write((int)(asInt>>>8));
  	out.write((int)asInt);
  }
  
  private void writeChars(String data, int length) throws IOException {
    for (int i=0; i<length; i++) {
      char c = (i<data.length() ? data.charAt(i) : 0);
      out.write(c);
    }
  }
  
  private void writeArray(short[] data, int length) throws IOException {
    for (int i=0; i<length; i++) {
      short b = data[i];
      out.write(b);
    }
  }
  
  public void visit(HeartbeatMessage message) throws IOException {
    writeUInt32(message.getCustomMode());
    writeUInt8(message.getType());
    writeUInt8(message.getAutopilot());
    writeUInt8(message.getBaseMode());
    writeUInt8(message.getSystemStatus());
    
  }

  public void visit(SysStatusMessage message) throws IOException {
    writeUInt32(message.getOnboardControlSensorsPresent());
    writeUInt32(message.getOnboardControlSensorsEnabled());
    writeUInt32(message.getOnboardControlSensorsHealth());
    writeUInt16(message.getLoad());
    writeUInt16(message.getVoltageBattery());
    writeInt16(message.getCurrentBattery());
    writeUInt16(message.getDropRateComm());
    writeUInt16(message.getErrorsComm());
    writeUInt16(message.getErrorsCount1());
    writeUInt16(message.getErrorsCount2());
    writeUInt16(message.getErrorsCount3());
    writeUInt16(message.getErrorsCount4());
    writeInt8(message.getBatteryRemaining());
  }

  public void visit(SystemTimeMessage message) throws IOException {
    writeUInt64(message.getTimeUnixUsec());
    writeUInt32(message.getTimeBootMs());
  }

  public void visit(PingMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeUInt32(message.getSeq());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(ChangeOperatorControlMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getControlRequest());
    writeUInt8(message.getVersion());
    writeChars(message.getPasskey(), 25);
  }

  public void visit(ChangeOperatorControlAckMessage message) throws IOException {
    writeUInt8(message.getGcsSystemId());
    writeUInt8(message.getControlRequest());
    writeUInt8(message.getAck());
  }

  public void visit(AuthKeyMessage message) throws IOException {
    writeChars(message.getKey(), 32);
  }

  public void visit(SetModeMessage message) throws IOException {
    writeUInt32(message.getCustomMode());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getBaseMode());
  }

  public void visit(ParamRequestReadMessage message) throws IOException {
    writeInt16(message.getParamIndex());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeChars(message.getParamId(), 16);
  }

  public void visit(ParamRequestListMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(ParamValueMessage message) throws IOException {
    writeFloat(message.getParamValue());
    writeUInt16(message.getParamCount());
    writeUInt16(message.getParamIndex());
    writeChars(message.getParamId(), 16);
    writeUInt8(message.getParamType());
  }

  public void visit(ParamSetMessage message) throws IOException {
    writeFloat(message.getParamValue());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeChars(message.getParamId(), 16);
    writeUInt8(message.getParamType());
  }

  public void visit(GpsRawIntMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeInt32(message.getLat());
    writeInt32(message.getLon());
    writeInt32(message.getAlt());
    writeUInt16(message.getEph());
    writeUInt16(message.getEpv());
    writeUInt16(message.getVel());
    writeUInt16(message.getCog());
    writeUInt8(message.getFixType());
    writeUInt8(message.getSatellitesVisible());
  }

  public void visit(GpsStatusMessage message) throws IOException {
    writeUInt8(message.getSatellitesVisible());
    writeUInt8(message.getSatellitePrn());
    writeUInt8(message.getSatelliteUsed());
    writeUInt8(message.getSatelliteElevation());
    writeUInt8(message.getSatelliteAzimuth());
    writeUInt8(message.getSatelliteSnr());
  }

  public void visit(ScaledImuMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeInt16(message.getXacc());
    writeInt16(message.getYacc());
    writeInt16(message.getZacc());
    writeInt16(message.getXgyro());
    writeInt16(message.getYgyro());
    writeInt16(message.getZgyro());
    writeInt16(message.getXmag());
    writeInt16(message.getYmag());
    writeInt16(message.getZmag());
  }

  public void visit(RawImuMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeInt16(message.getXacc());
    writeInt16(message.getYacc());
    writeInt16(message.getZacc());
    writeInt16(message.getXgyro());
    writeInt16(message.getYgyro());
    writeInt16(message.getZgyro());
    writeInt16(message.getXmag());
    writeInt16(message.getYmag());
    writeInt16(message.getZmag());
  }

  public void visit(RawPressureMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeInt16(message.getPressAbs());
    writeInt16(message.getPressDiff1());
    writeInt16(message.getPressDiff2());
    writeInt16(message.getTemperature());
  }

  public void visit(ScaledPressureMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getPressAbs());
    writeFloat(message.getPressDiff());
    writeInt16(message.getTemperature());
  }

  public void visit(AttitudeMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
    writeFloat(message.getRollspeed());
    writeFloat(message.getPitchspeed());
    writeFloat(message.getYawspeed());
  }

  public void visit(AttitudeQuaternionMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getQ1());
    writeFloat(message.getQ2());
    writeFloat(message.getQ3());
    writeFloat(message.getQ4());
    writeFloat(message.getRollspeed());
    writeFloat(message.getPitchspeed());
    writeFloat(message.getYawspeed());
  }

  public void visit(LocalPositionNedMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getVx());
    writeFloat(message.getVy());
    writeFloat(message.getVz());
  }

  public void visit(GlobalPositionIntMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeInt32(message.getLat());
    writeInt32(message.getLon());
    writeInt32(message.getAlt());
    writeInt32(message.getRelativeAlt());
    writeInt16(message.getVx());
    writeInt16(message.getVy());
    writeInt16(message.getVz());
    writeUInt16(message.getHdg());
  }

  public void visit(RcChannelsScaledMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeInt16(message.getChan1Scaled());
    writeInt16(message.getChan2Scaled());
    writeInt16(message.getChan3Scaled());
    writeInt16(message.getChan4Scaled());
    writeInt16(message.getChan5Scaled());
    writeInt16(message.getChan6Scaled());
    writeInt16(message.getChan7Scaled());
    writeInt16(message.getChan8Scaled());
    writeUInt8(message.getPort());
    writeUInt8(message.getRssi());
  }

  public void visit(RcChannelsRawMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeUInt16(message.getChan1Raw());
    writeUInt16(message.getChan2Raw());
    writeUInt16(message.getChan3Raw());
    writeUInt16(message.getChan4Raw());
    writeUInt16(message.getChan5Raw());
    writeUInt16(message.getChan6Raw());
    writeUInt16(message.getChan7Raw());
    writeUInt16(message.getChan8Raw());
    writeUInt8(message.getPort());
    writeUInt8(message.getRssi());
  }

  public void visit(ServoOutputRawMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeUInt16(message.getServo1Raw());
    writeUInt16(message.getServo2Raw());
    writeUInt16(message.getServo3Raw());
    writeUInt16(message.getServo4Raw());
    writeUInt16(message.getServo5Raw());
    writeUInt16(message.getServo6Raw());
    writeUInt16(message.getServo7Raw());
    writeUInt16(message.getServo8Raw());
    writeUInt8(message.getPort());
  }

  public void visit(MissionRequestPartialListMessage message) throws IOException {
    writeInt16(message.getStartIndex());
    writeInt16(message.getEndIndex());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionWritePartialListMessage message) throws IOException {
    writeInt16(message.getStartIndex());
    writeInt16(message.getEndIndex());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionItemMessage message) throws IOException {
    writeFloat(message.getParam1());
    writeFloat(message.getParam2());
    writeFloat(message.getParam3());
    writeFloat(message.getParam4());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeUInt16(message.getSeq());
    writeUInt16(message.getCommand());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getFrame());
    writeUInt8(message.getCurrent());
    writeUInt8(message.getAutocontinue());
  }

  public void visit(MissionRequestMessage message) throws IOException {
    writeUInt16(message.getSeq());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionSetCurrentMessage message) throws IOException {
    writeUInt16(message.getSeq());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionCurrentMessage message) throws IOException {
    writeUInt16(message.getSeq());
  }

  public void visit(MissionRequestListMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionCountMessage message) throws IOException {
    writeUInt16(message.getCount());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionClearAllMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MissionItemReachedMessage message) throws IOException {
    writeUInt16(message.getSeq());
  }

  public void visit(MissionAckMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getType());
  }

  public void visit(SetGpsGlobalOriginMessage message) throws IOException {
    writeInt32(message.getLatitude());
    writeInt32(message.getLongitude());
    writeInt32(message.getAltitude());
    writeUInt8(message.getTargetSystem());
  }

  public void visit(GpsGlobalOriginMessage message) throws IOException {
    writeInt32(message.getLatitude());
    writeInt32(message.getLongitude());
    writeInt32(message.getAltitude());
  }

  public void visit(SetLocalPositionSetpointMessage message) throws IOException {
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getYaw());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getCoordinateFrame());
  }

  public void visit(LocalPositionSetpointMessage message) throws IOException {
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getYaw());
    writeUInt8(message.getCoordinateFrame());
  }

  public void visit(GlobalPositionSetpointIntMessage message) throws IOException {
    writeInt32(message.getLatitude());
    writeInt32(message.getLongitude());
    writeInt32(message.getAltitude());
    writeInt16(message.getYaw());
    writeUInt8(message.getCoordinateFrame());
  }

  public void visit(SetGlobalPositionSetpointIntMessage message) throws IOException {
    writeInt32(message.getLatitude());
    writeInt32(message.getLongitude());
    writeInt32(message.getAltitude());
    writeInt16(message.getYaw());
    writeUInt8(message.getCoordinateFrame());
  }

  public void visit(SafetySetAllowedAreaMessage message) throws IOException {
    writeFloat(message.getP1x());
    writeFloat(message.getP1y());
    writeFloat(message.getP1z());
    writeFloat(message.getP2x());
    writeFloat(message.getP2y());
    writeFloat(message.getP2z());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getFrame());
  }

  public void visit(SafetyAllowedAreaMessage message) throws IOException {
    writeFloat(message.getP1x());
    writeFloat(message.getP1y());
    writeFloat(message.getP1z());
    writeFloat(message.getP2x());
    writeFloat(message.getP2y());
    writeFloat(message.getP2z());
    writeUInt8(message.getFrame());
  }

  public void visit(SetRollPitchYawThrustMessage message) throws IOException {
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
    writeFloat(message.getThrust());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(SetRollPitchYawSpeedThrustMessage message) throws IOException {
    writeFloat(message.getRollSpeed());
    writeFloat(message.getPitchSpeed());
    writeFloat(message.getYawSpeed());
    writeFloat(message.getThrust());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(RollPitchYawThrustSetpointMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
    writeFloat(message.getThrust());
  }

  public void visit(RollPitchYawSpeedThrustSetpointMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getRollSpeed());
    writeFloat(message.getPitchSpeed());
    writeFloat(message.getYawSpeed());
    writeFloat(message.getThrust());
  }

  public void visit(SetQuadMotorsSetpointMessage message) throws IOException {
    writeUInt16(message.getMotorFrontNw());
    writeUInt16(message.getMotorRightNe());
    writeUInt16(message.getMotorBackSe());
    writeUInt16(message.getMotorLeftSw());
    writeUInt8(message.getTargetSystem());
  }

  public void visit(SetQuadSwarmRollPitchYawThrustMessage message) throws IOException {
    writeInt16(message.getRoll());
    writeInt16(message.getPitch());
    writeInt16(message.getYaw());
    writeUInt16(message.getThrust());
    writeUInt8(message.getGroup());
    writeUInt8(message.getMode());
  }

  public void visit(NavControllerOutputMessage message) throws IOException {
    writeFloat(message.getNavRoll());
    writeFloat(message.getNavPitch());
    writeFloat(message.getAltError());
    writeFloat(message.getAspdError());
    writeFloat(message.getXtrackError());
    writeInt16(message.getNavBearing());
    writeInt16(message.getTargetBearing());
    writeUInt16(message.getWpDist());
  }

  public void visit(SetQuadSwarmLedRollPitchYawThrustMessage message) throws IOException {
    writeInt16(message.getRoll());
    writeInt16(message.getPitch());
    writeInt16(message.getYaw());
    writeUInt16(message.getThrust());
    writeUInt8(message.getGroup());
    writeUInt8(message.getMode());
    writeUInt8(message.getLedRed());
    writeUInt8(message.getLedBlue());
    writeUInt8(message.getLedGreen());
  }

  public void visit(StateCorrectionMessage message) throws IOException {
    writeFloat(message.getXerr());
    writeFloat(message.getYerr());
    writeFloat(message.getZerr());
    writeFloat(message.getRollerr());
    writeFloat(message.getPitcherr());
    writeFloat(message.getYawerr());
    writeFloat(message.getVxerr());
    writeFloat(message.getVyerr());
    writeFloat(message.getVzerr());
  }

  public void visit(RequestDataStreamMessage message) throws IOException {
    writeUInt16(message.getReqMessageRate());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getReqStreamId());
    writeUInt8(message.getStartStop());
  }

  public void visit(DataStreamMessage message) throws IOException {
    writeUInt16(message.getMessageRate());
    writeUInt8(message.getStreamId());
    writeUInt8(message.getOnOff());
  }

  public void visit(ManualControlMessage message) throws IOException {
    writeInt16(message.getX());
    writeInt16(message.getY());
    writeInt16(message.getZ());
    writeInt16(message.getR());
    writeUInt16(message.getButtons());
    writeUInt8(message.getTarget());
  }

  public void visit(RcChannelsOverrideMessage message) throws IOException {
    writeUInt16(message.getChan1Raw());
    writeUInt16(message.getChan2Raw());
    writeUInt16(message.getChan3Raw());
    writeUInt16(message.getChan4Raw());
    writeUInt16(message.getChan5Raw());
    writeUInt16(message.getChan6Raw());
    writeUInt16(message.getChan7Raw());
    writeUInt16(message.getChan8Raw());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(VfrHudMessage message) throws IOException {
    writeFloat(message.getAirspeed());
    writeFloat(message.getGroundspeed());
    writeFloat(message.getAlt());
    writeFloat(message.getClimb());
    writeInt16(message.getHeading());
    writeUInt16(message.getThrottle());
  }

  public void visit(CommandLongMessage message) throws IOException {
    writeFloat(message.getParam1());
    writeFloat(message.getParam2());
    writeFloat(message.getParam3());
    writeFloat(message.getParam4());
    writeFloat(message.getParam5());
    writeFloat(message.getParam6());
    writeFloat(message.getParam7());
    writeUInt16(message.getCommand());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getConfirmation());
  }

  public void visit(CommandAckMessage message) throws IOException {
    writeUInt16(message.getCommand());
    writeUInt8(message.getResult());
  }

  public void visit(LocalPositionNedSystemGlobalOffsetMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
  }

  public void visit(HilStateMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
    writeFloat(message.getRollspeed());
    writeFloat(message.getPitchspeed());
    writeFloat(message.getYawspeed());
    writeInt32(message.getLat());
    writeInt32(message.getLon());
    writeInt32(message.getAlt());
    writeInt16(message.getVx());
    writeInt16(message.getVy());
    writeInt16(message.getVz());
    writeInt16(message.getXacc());
    writeInt16(message.getYacc());
    writeInt16(message.getZacc());
  }

  public void visit(HilControlsMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeFloat(message.getRollAilerons());
    writeFloat(message.getPitchElevator());
    writeFloat(message.getYawRudder());
    writeFloat(message.getThrottle());
    writeFloat(message.getAux1());
    writeFloat(message.getAux2());
    writeFloat(message.getAux3());
    writeFloat(message.getAux4());
    writeUInt8(message.getMode());
    writeUInt8(message.getNavMode());
  }

  public void visit(HilRcInputsRawMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeUInt16(message.getChan1Raw());
    writeUInt16(message.getChan2Raw());
    writeUInt16(message.getChan3Raw());
    writeUInt16(message.getChan4Raw());
    writeUInt16(message.getChan5Raw());
    writeUInt16(message.getChan6Raw());
    writeUInt16(message.getChan7Raw());
    writeUInt16(message.getChan8Raw());
    writeUInt16(message.getChan9Raw());
    writeUInt16(message.getChan10Raw());
    writeUInt16(message.getChan11Raw());
    writeUInt16(message.getChan12Raw());
    writeUInt8(message.getRssi());
  }

  public void visit(OpticalFlowMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeFloat(message.getFlowCompMX());
    writeFloat(message.getFlowCompMY());
    writeFloat(message.getGroundDistance());
    writeInt16(message.getFlowX());
    writeInt16(message.getFlowY());
    writeUInt8(message.getSensorId());
    writeUInt8(message.getQuality());
  }

  public void visit(GlobalVisionPositionEstimateMessage message) throws IOException {
    writeUInt64(message.getUsec());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
  }

  public void visit(VisionPositionEstimateMessage message) throws IOException {
    writeUInt64(message.getUsec());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
  }

  public void visit(VisionSpeedEstimateMessage message) throws IOException {
    writeUInt64(message.getUsec());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
  }

  public void visit(ViconPositionEstimateMessage message) throws IOException {
    writeUInt64(message.getUsec());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
  }

  public void visit(HighresImuMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeFloat(message.getXacc());
    writeFloat(message.getYacc());
    writeFloat(message.getZacc());
    writeFloat(message.getXgyro());
    writeFloat(message.getYgyro());
    writeFloat(message.getZgyro());
    writeFloat(message.getXmag());
    writeFloat(message.getYmag());
    writeFloat(message.getZmag());
    writeFloat(message.getAbsPressure());
    writeFloat(message.getDiffPressure());
    writeFloat(message.getPressureAlt());
    writeFloat(message.getTemperature());
    writeUInt16(message.getFieldsUpdated());
  }

  public void visit(BatteryStatusMessage message) throws IOException {
    writeUInt16(message.getVoltageCell1());
    writeUInt16(message.getVoltageCell2());
    writeUInt16(message.getVoltageCell3());
    writeUInt16(message.getVoltageCell4());
    writeUInt16(message.getVoltageCell5());
    writeUInt16(message.getVoltageCell6());
    writeInt16(message.getCurrentBattery());
    writeUInt8(message.getAccuId());
    writeInt8(message.getBatteryRemaining());
  }

  public void visit(Setpoint8dofMessage message) throws IOException {
    writeFloat(message.getVal1());
    writeFloat(message.getVal2());
    writeFloat(message.getVal3());
    writeFloat(message.getVal4());
    writeFloat(message.getVal5());
    writeFloat(message.getVal6());
    writeFloat(message.getVal7());
    writeFloat(message.getVal8());
    writeUInt8(message.getTargetSystem());
  }

  public void visit(Setpoint6dofMessage message) throws IOException {
    writeFloat(message.getTransX());
    writeFloat(message.getTransY());
    writeFloat(message.getTransZ());
    writeFloat(message.getRotX());
    writeFloat(message.getRotY());
    writeFloat(message.getRotZ());
    writeUInt8(message.getTargetSystem());
  }

  public void visit(MemoryVectMessage message) throws IOException {
    writeUInt16(message.getAddress());
    writeUInt8(message.getVer());
    writeUInt8(message.getType());
    writeInt8(message.getValue());
  }

  public void visit(DebugVectMessage message) throws IOException {
    writeUInt64(message.getTimeUsec());
    writeFloat(message.getX());
    writeFloat(message.getY());
    writeFloat(message.getZ());
    writeChars(message.getName(), 10);
  }

  public void visit(NamedValueFloatMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getValue());
    writeChars(message.getName(), 10);
  }

  public void visit(NamedValueIntMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeInt32(message.getValue());
    writeChars(message.getName(), 10);
  }

  public void visit(StatustextMessage message) throws IOException {
    writeUInt8(message.getSeverity());
    writeChars(message.getText(), 50);
  }

  public void visit(DebugMessage message) throws IOException {
    writeUInt32(message.getTimeBootMs());
    writeFloat(message.getValue());
    writeUInt8(message.getInd());
  }

  public void visit(SensorOffsetsMessage message) throws IOException {
    writeFloat(message.getMagDeclination());
    writeInt32(message.getRawPress());
    writeInt32(message.getRawTemp());
    writeFloat(message.getGyroCalX());
    writeFloat(message.getGyroCalY());
    writeFloat(message.getGyroCalZ());
    writeFloat(message.getAccelCalX());
    writeFloat(message.getAccelCalY());
    writeFloat(message.getAccelCalZ());
    writeInt16(message.getMagOfsX());
    writeInt16(message.getMagOfsY());
    writeInt16(message.getMagOfsZ());
  }

  public void visit(SetMagOffsetsMessage message) throws IOException {
    writeInt16(message.getMagOfsX());
    writeInt16(message.getMagOfsY());
    writeInt16(message.getMagOfsZ());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(MeminfoMessage message) throws IOException {
    writeUInt16(message.getBrkval());
    writeUInt16(message.getFreemem());
  }

  public void visit(ApAdcMessage message) throws IOException {
    writeUInt16(message.getAdc1());
    writeUInt16(message.getAdc2());
    writeUInt16(message.getAdc3());
    writeUInt16(message.getAdc4());
    writeUInt16(message.getAdc5());
    writeUInt16(message.getAdc6());
  }

  public void visit(DigicamConfigureMessage message) throws IOException {
    writeFloat(message.getExtraValue());
    writeUInt16(message.getShutterSpeed());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getMode());
    writeUInt8(message.getAperture());
    writeUInt8(message.getIso());
    writeUInt8(message.getExposureType());
    writeUInt8(message.getCommandId());
    writeUInt8(message.getEngineCutOff());
    writeUInt8(message.getExtraParam());
  }

  public void visit(DigicamControlMessage message) throws IOException {
    writeFloat(message.getExtraValue());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getSession());
    writeUInt8(message.getZoomPos());
    writeInt8(message.getZoomStep());
    writeUInt8(message.getFocusLock());
    writeUInt8(message.getShot());
    writeUInt8(message.getCommandId());
    writeUInt8(message.getExtraParam());
  }

  public void visit(MountConfigureMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getMountMode());
    writeUInt8(message.getStabRoll());
    writeUInt8(message.getStabPitch());
    writeUInt8(message.getStabYaw());
  }

  public void visit(MountControlMessage message) throws IOException {
    writeInt32(message.getInputA());
    writeInt32(message.getInputB());
    writeInt32(message.getInputC());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getSavePosition());
  }

  public void visit(MountStatusMessage message) throws IOException {
    writeInt32(message.getPointingA());
    writeInt32(message.getPointingB());
    writeInt32(message.getPointingC());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
  }

  public void visit(FencePointMessage message) throws IOException {
    writeFloat(message.getLat());
    writeFloat(message.getLng());
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getIdx());
    writeUInt8(message.getCount());
  }

  public void visit(FenceFetchPointMessage message) throws IOException {
    writeUInt8(message.getTargetSystem());
    writeUInt8(message.getTargetComponent());
    writeUInt8(message.getIdx());
  }

  public void visit(FenceStatusMessage message) throws IOException {
    writeUInt32(message.getBreachTime());
    writeUInt16(message.getBreachCount());
    writeUInt8(message.getBreachStatus());
    writeUInt8(message.getBreachType());
  }

  public void visit(AhrsMessage message) throws IOException {
    writeFloat(message.getOmegaix());
    writeFloat(message.getOmegaiy());
    writeFloat(message.getOmegaiz());
    writeFloat(message.getAccelWeight());
    writeFloat(message.getRenormVal());
    writeFloat(message.getErrorRp());
    writeFloat(message.getErrorYaw());
  }

  public void visit(SimstateMessage message) throws IOException {
    writeFloat(message.getRoll());
    writeFloat(message.getPitch());
    writeFloat(message.getYaw());
    writeFloat(message.getXacc());
    writeFloat(message.getYacc());
    writeFloat(message.getZacc());
    writeFloat(message.getXgyro());
    writeFloat(message.getYgyro());
    writeFloat(message.getZgyro());
    writeFloat(message.getLat());
    writeFloat(message.getLng());
  }

  public void visit(HwstatusMessage message) throws IOException {
    writeUInt16(message.getVcc());
    writeUInt8(message.getI2cerr());
  }

  public void visit(RadioMessage message) throws IOException {
    writeUInt16(message.getRxerrors());
    writeUInt16(message.getFixed());
    writeUInt8(message.getRssi());
    writeUInt8(message.getRemrssi());
    writeUInt8(message.getTxbuf());
    writeUInt8(message.getNoise());
    writeUInt8(message.getRemnoise());
  }

  public void visit(LimitsStatusMessage message) throws IOException {
    writeUInt32(message.getLastTrigger());
    writeUInt32(message.getLastAction());
    writeUInt32(message.getLastRecovery());
    writeUInt32(message.getLastClear());
    writeUInt16(message.getBreachCount());
    writeUInt8(message.getLimitsState());
    writeUInt8(message.getModsEnabled());
    writeUInt8(message.getModsRequired());
    writeUInt8(message.getModsTriggered());
  }

  public void visit(WindMessage message) throws IOException {
    writeFloat(message.getDirection());
    writeFloat(message.getSpeed());
    writeFloat(message.getSpeedZ());
  }

  public void visit(Data16Message message) throws IOException {
    writeUInt8(message.getType());
    writeUInt8(message.getLen());
    writeUInt8(message.getData());
  }

  public void visit(Data32Message message) throws IOException {
    writeUInt8(message.getType());
    writeUInt8(message.getLen());
    writeUInt8(message.getData());
  }

  public void visit(Data64Message message) throws IOException {
    writeUInt8(message.getType());
    writeUInt8(message.getLen());
    writeUInt8(message.getData());
  }

  public void visit(Data96Message message) throws IOException {
    writeUInt8(message.getType());
    writeUInt8(message.getLen());
    writeUInt8(message.getData());
  }

}
