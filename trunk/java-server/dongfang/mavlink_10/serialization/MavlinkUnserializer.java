package dongfang.mavlink_10.serialization;
import dongfang.mavlink_10.messages.*;
import java.io.IOException;
import java.io.InputStream;
public class MavlinkUnserializer {
  private InputStream in;
  
  public MavlinkUnserializer(InputStream in) {
    this.in = in;
  }

  private short readUInt8() throws IOException {
  	return(short)in.read();
  }

  private byte readInt8() throws IOException {
  	return(byte)in.read();
  }

  private int readUInt16() throws IOException {
  	return in.read() |  (in.read() << 8);
  }

  private short readInt16() throws IOException {
  	return (short)(in.read() | (in.read() << 8));
  }

  private long readUInt32() throws IOException {
  	return (long)in.read() | ((long)(in.read()) << 8) | ((long)(in.read()) << 16) | (((long)in.read()) << 24);
  }

  private int readInt32() throws IOException {
  	return in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
  }

  // Uh-oh ... java doesnt have an unsigned 64-bit or greater type. That would be BigInteger then..
  private long readUInt64() throws IOException {	
    return readInt64();
  }

  private long readInt64() throws IOException {
  	return (long)in.read() | ((long)(in.read()) << 8) | ((long)(in.read()) << 16) | (((long)in.read()) << 24)
  	| ((long)(in.read()) << 32) | ((long)(in.read()) << 40) | ((long)(in.read()) << 48) | (((long)in.read()) << 56);
  }
  
  private static final boolean REVERSE_FLOAT = false;
  
  private float readFloat() throws IOException {
   	return Float.intBitsToFloat(readInt32());
  }
  
  private String readChars(int length) throws IOException {
    char[] asChars = new char[length];
    for (int i=0; i<length; i++) {
      asChars[i] = (char)in.read();
    }
    return new String(asChars);
  }

  private short[] readArray(int length) throws IOException {
    short[] result = new short[length];
    for (int i=0; i<length; i++) {
      result[i] = (short)in.read();
    }
    return result;
  }
  
  public MavlinkMessage unserializeMessage(int length, int sequence, int systemId, int componentId) throws IOException {
    MavlinkMessage result;
    int messageId = in.read();
    switch(messageId) {
    case 0: {
      HeartbeatMessage message = new HeartbeatMessage(sequence, systemId, componentId);
      message.setCustomMode(readUInt32());
      message.setType(readUInt8());
      message.setAutopilot(readUInt8());
      message.setBaseMode(readUInt8());
      message.setSystemStatus(readUInt8());
      
      result = message;
      break;
    }
    case 1: {
      SysStatusMessage message = new SysStatusMessage(sequence, systemId, componentId);
      message.setOnboardControlSensorsPresent(readUInt32());
      message.setOnboardControlSensorsEnabled(readUInt32());
      message.setOnboardControlSensorsHealth(readUInt32());
      message.setLoad(readUInt16());
      message.setVoltageBattery(readUInt16());
      message.setCurrentBattery(readInt16());
      message.setDropRateComm(readUInt16());
      message.setErrorsComm(readUInt16());
      message.setErrorsCount1(readUInt16());
      message.setErrorsCount2(readUInt16());
      message.setErrorsCount3(readUInt16());
      message.setErrorsCount4(readUInt16());
      message.setBatteryRemaining(readInt8());
      result = message;
      break;
    }
    case 2: {
      SystemTimeMessage message = new SystemTimeMessage(sequence, systemId, componentId);
      message.setTimeUnixUsec(readUInt64());
      message.setTimeBootMs(readUInt32());
      result = message;
      break;
    }
    case 4: {
      PingMessage message = new PingMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setSeq(readUInt32());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 5: {
      ChangeOperatorControlMessage message = new ChangeOperatorControlMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setControlRequest(readUInt8());
      message.setVersion(readUInt8());
      message.setPasskey(readChars(25));
      result = message;
      break;
    }
    case 6: {
      ChangeOperatorControlAckMessage message = new ChangeOperatorControlAckMessage(sequence, systemId, componentId);
      message.setGcsSystemId(readUInt8());
      message.setControlRequest(readUInt8());
      message.setAck(readUInt8());
      result = message;
      break;
    }
    case 7: {
      AuthKeyMessage message = new AuthKeyMessage(sequence, systemId, componentId);
      message.setKey(readChars(32));
      result = message;
      break;
    }
    case 11: {
      SetModeMessage message = new SetModeMessage(sequence, systemId, componentId);
      message.setCustomMode(readUInt32());
      message.setTargetSystem(readUInt8());
      message.setBaseMode(readUInt8());
      result = message;
      break;
    }
    case 20: {
      ParamRequestReadMessage message = new ParamRequestReadMessage(sequence, systemId, componentId);
      message.setParamIndex(readInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setParamId(readChars(16));
      result = message;
      break;
    }
    case 21: {
      ParamRequestListMessage message = new ParamRequestListMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 22: {
      ParamValueMessage message = new ParamValueMessage(sequence, systemId, componentId);
      message.setParamValue(readFloat());
      message.setParamCount(readUInt16());
      message.setParamIndex(readUInt16());
      message.setParamId(readChars(16));
      message.setParamType(readUInt8());
      result = message;
      break;
    }
    case 23: {
      ParamSetMessage message = new ParamSetMessage(sequence, systemId, componentId);
      message.setParamValue(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setParamId(readChars(16));
      message.setParamType(readUInt8());
      result = message;
      break;
    }
    case 24: {
      GpsRawIntMessage message = new GpsRawIntMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setLat(readInt32());
      message.setLon(readInt32());
      message.setAlt(readInt32());
      message.setEph(readUInt16());
      message.setEpv(readUInt16());
      message.setVel(readUInt16());
      message.setCog(readUInt16());
      message.setFixType(readUInt8());
      message.setSatellitesVisible(readUInt8());
      result = message;
      break;
    }
    case 25: {
      GpsStatusMessage message = new GpsStatusMessage(sequence, systemId, componentId);
      message.setSatellitesVisible(readUInt8());
      message.setSatellitePrn(readUInt8());
      message.setSatelliteUsed(readUInt8());
      message.setSatelliteElevation(readUInt8());
      message.setSatelliteAzimuth(readUInt8());
      message.setSatelliteSnr(readUInt8());
      result = message;
      break;
    }
    case 26: {
      ScaledImuMessage message = new ScaledImuMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setXacc(readInt16());
      message.setYacc(readInt16());
      message.setZacc(readInt16());
      message.setXgyro(readInt16());
      message.setYgyro(readInt16());
      message.setZgyro(readInt16());
      message.setXmag(readInt16());
      message.setYmag(readInt16());
      message.setZmag(readInt16());
      result = message;
      break;
    }
    case 27: {
      RawImuMessage message = new RawImuMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setXacc(readInt16());
      message.setYacc(readInt16());
      message.setZacc(readInt16());
      message.setXgyro(readInt16());
      message.setYgyro(readInt16());
      message.setZgyro(readInt16());
      message.setXmag(readInt16());
      message.setYmag(readInt16());
      message.setZmag(readInt16());
      result = message;
      break;
    }
    case 28: {
      RawPressureMessage message = new RawPressureMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setPressAbs(readInt16());
      message.setPressDiff1(readInt16());
      message.setPressDiff2(readInt16());
      message.setTemperature(readInt16());
      result = message;
      break;
    }
    case 29: {
      ScaledPressureMessage message = new ScaledPressureMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setPressAbs(readFloat());
      message.setPressDiff(readFloat());
      message.setTemperature(readInt16());
      result = message;
      break;
    }
    case 30: {
      AttitudeMessage message = new AttitudeMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      message.setRollspeed(readFloat());
      message.setPitchspeed(readFloat());
      message.setYawspeed(readFloat());
      result = message;
      break;
    }
    case 31: {
      AttitudeQuaternionMessage message = new AttitudeQuaternionMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setQ1(readFloat());
      message.setQ2(readFloat());
      message.setQ3(readFloat());
      message.setQ4(readFloat());
      message.setRollspeed(readFloat());
      message.setPitchspeed(readFloat());
      message.setYawspeed(readFloat());
      result = message;
      break;
    }
    case 32: {
      LocalPositionNedMessage message = new LocalPositionNedMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setVx(readFloat());
      message.setVy(readFloat());
      message.setVz(readFloat());
      result = message;
      break;
    }
    case 33: {
      GlobalPositionIntMessage message = new GlobalPositionIntMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setLat(readInt32());
      message.setLon(readInt32());
      message.setAlt(readInt32());
      message.setRelativeAlt(readInt32());
      message.setVx(readInt16());
      message.setVy(readInt16());
      message.setVz(readInt16());
      message.setHdg(readUInt16());
      result = message;
      break;
    }
    case 34: {
      RcChannelsScaledMessage message = new RcChannelsScaledMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setChan1Scaled(readInt16());
      message.setChan2Scaled(readInt16());
      message.setChan3Scaled(readInt16());
      message.setChan4Scaled(readInt16());
      message.setChan5Scaled(readInt16());
      message.setChan6Scaled(readInt16());
      message.setChan7Scaled(readInt16());
      message.setChan8Scaled(readInt16());
      message.setPort(readUInt8());
      message.setRssi(readUInt8());
      result = message;
      break;
    }
    case 35: {
      RcChannelsRawMessage message = new RcChannelsRawMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setChan1Raw(readUInt16());
      message.setChan2Raw(readUInt16());
      message.setChan3Raw(readUInt16());
      message.setChan4Raw(readUInt16());
      message.setChan5Raw(readUInt16());
      message.setChan6Raw(readUInt16());
      message.setChan7Raw(readUInt16());
      message.setChan8Raw(readUInt16());
      message.setPort(readUInt8());
      message.setRssi(readUInt8());
      result = message;
      break;
    }
    case 36: {
      ServoOutputRawMessage message = new ServoOutputRawMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setServo1Raw(readUInt16());
      message.setServo2Raw(readUInt16());
      message.setServo3Raw(readUInt16());
      message.setServo4Raw(readUInt16());
      message.setServo5Raw(readUInt16());
      message.setServo6Raw(readUInt16());
      message.setServo7Raw(readUInt16());
      message.setServo8Raw(readUInt16());
      message.setPort(readUInt8());
      result = message;
      break;
    }
    case 37: {
      MissionRequestPartialListMessage message = new MissionRequestPartialListMessage(sequence, systemId, componentId);
      message.setStartIndex(readInt16());
      message.setEndIndex(readInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 38: {
      MissionWritePartialListMessage message = new MissionWritePartialListMessage(sequence, systemId, componentId);
      message.setStartIndex(readInt16());
      message.setEndIndex(readInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 39: {
      MissionItemMessage message = new MissionItemMessage(sequence, systemId, componentId);
      message.setParam1(readFloat());
      message.setParam2(readFloat());
      message.setParam3(readFloat());
      message.setParam4(readFloat());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setSeq(readUInt16());
      message.setCommand(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setFrame(readUInt8());
      message.setCurrent(readUInt8());
      message.setAutocontinue(readUInt8());
      result = message;
      break;
    }
    case 40: {
      MissionRequestMessage message = new MissionRequestMessage(sequence, systemId, componentId);
      message.setSeq(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 41: {
      MissionSetCurrentMessage message = new MissionSetCurrentMessage(sequence, systemId, componentId);
      message.setSeq(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 42: {
      MissionCurrentMessage message = new MissionCurrentMessage(sequence, systemId, componentId);
      message.setSeq(readUInt16());
      result = message;
      break;
    }
    case 43: {
      MissionRequestListMessage message = new MissionRequestListMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 44: {
      MissionCountMessage message = new MissionCountMessage(sequence, systemId, componentId);
      message.setCount(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 45: {
      MissionClearAllMessage message = new MissionClearAllMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 46: {
      MissionItemReachedMessage message = new MissionItemReachedMessage(sequence, systemId, componentId);
      message.setSeq(readUInt16());
      result = message;
      break;
    }
    case 47: {
      MissionAckMessage message = new MissionAckMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setType(readUInt8());
      result = message;
      break;
    }
    case 48: {
      SetGpsGlobalOriginMessage message = new SetGpsGlobalOriginMessage(sequence, systemId, componentId);
      message.setLatitude(readInt32());
      message.setLongitude(readInt32());
      message.setAltitude(readInt32());
      message.setTargetSystem(readUInt8());
      result = message;
      break;
    }
    case 49: {
      GpsGlobalOriginMessage message = new GpsGlobalOriginMessage(sequence, systemId, componentId);
      message.setLatitude(readInt32());
      message.setLongitude(readInt32());
      message.setAltitude(readInt32());
      result = message;
      break;
    }
    case 50: {
      SetLocalPositionSetpointMessage message = new SetLocalPositionSetpointMessage(sequence, systemId, componentId);
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setYaw(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setCoordinateFrame(readUInt8());
      result = message;
      break;
    }
    case 51: {
      LocalPositionSetpointMessage message = new LocalPositionSetpointMessage(sequence, systemId, componentId);
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setYaw(readFloat());
      message.setCoordinateFrame(readUInt8());
      result = message;
      break;
    }
    case 52: {
      GlobalPositionSetpointIntMessage message = new GlobalPositionSetpointIntMessage(sequence, systemId, componentId);
      message.setLatitude(readInt32());
      message.setLongitude(readInt32());
      message.setAltitude(readInt32());
      message.setYaw(readInt16());
      message.setCoordinateFrame(readUInt8());
      result = message;
      break;
    }
    case 53: {
      SetGlobalPositionSetpointIntMessage message = new SetGlobalPositionSetpointIntMessage(sequence, systemId, componentId);
      message.setLatitude(readInt32());
      message.setLongitude(readInt32());
      message.setAltitude(readInt32());
      message.setYaw(readInt16());
      message.setCoordinateFrame(readUInt8());
      result = message;
      break;
    }
    case 54: {
      SafetySetAllowedAreaMessage message = new SafetySetAllowedAreaMessage(sequence, systemId, componentId);
      message.setP1x(readFloat());
      message.setP1y(readFloat());
      message.setP1z(readFloat());
      message.setP2x(readFloat());
      message.setP2y(readFloat());
      message.setP2z(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setFrame(readUInt8());
      result = message;
      break;
    }
    case 55: {
      SafetyAllowedAreaMessage message = new SafetyAllowedAreaMessage(sequence, systemId, componentId);
      message.setP1x(readFloat());
      message.setP1y(readFloat());
      message.setP1z(readFloat());
      message.setP2x(readFloat());
      message.setP2y(readFloat());
      message.setP2z(readFloat());
      message.setFrame(readUInt8());
      result = message;
      break;
    }
    case 56: {
      SetRollPitchYawThrustMessage message = new SetRollPitchYawThrustMessage(sequence, systemId, componentId);
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      message.setThrust(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 57: {
      SetRollPitchYawSpeedThrustMessage message = new SetRollPitchYawSpeedThrustMessage(sequence, systemId, componentId);
      message.setRollSpeed(readFloat());
      message.setPitchSpeed(readFloat());
      message.setYawSpeed(readFloat());
      message.setThrust(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 58: {
      RollPitchYawThrustSetpointMessage message = new RollPitchYawThrustSetpointMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      message.setThrust(readFloat());
      result = message;
      break;
    }
    case 59: {
      RollPitchYawSpeedThrustSetpointMessage message = new RollPitchYawSpeedThrustSetpointMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setRollSpeed(readFloat());
      message.setPitchSpeed(readFloat());
      message.setYawSpeed(readFloat());
      message.setThrust(readFloat());
      result = message;
      break;
    }
    case 60: {
      SetQuadMotorsSetpointMessage message = new SetQuadMotorsSetpointMessage(sequence, systemId, componentId);
      message.setMotorFrontNw(readUInt16());
      message.setMotorRightNe(readUInt16());
      message.setMotorBackSe(readUInt16());
      message.setMotorLeftSw(readUInt16());
      message.setTargetSystem(readUInt8());
      result = message;
      break;
    }
    case 61: {
      SetQuadSwarmRollPitchYawThrustMessage message = new SetQuadSwarmRollPitchYawThrustMessage(sequence, systemId, componentId);
      message.setRoll(readInt16());
      message.setPitch(readInt16());
      message.setYaw(readInt16());
      message.setThrust(readUInt16());
      message.setGroup(readUInt8());
      message.setMode(readUInt8());
      result = message;
      break;
    }
    case 62: {
      NavControllerOutputMessage message = new NavControllerOutputMessage(sequence, systemId, componentId);
      message.setNavRoll(readFloat());
      message.setNavPitch(readFloat());
      message.setAltError(readFloat());
      message.setAspdError(readFloat());
      message.setXtrackError(readFloat());
      message.setNavBearing(readInt16());
      message.setTargetBearing(readInt16());
      message.setWpDist(readUInt16());
      result = message;
      break;
    }
    case 63: {
      SetQuadSwarmLedRollPitchYawThrustMessage message = new SetQuadSwarmLedRollPitchYawThrustMessage(sequence, systemId, componentId);
      message.setRoll(readInt16());
      message.setPitch(readInt16());
      message.setYaw(readInt16());
      message.setThrust(readUInt16());
      message.setGroup(readUInt8());
      message.setMode(readUInt8());
      message.setLedRed(readUInt8());
      message.setLedBlue(readUInt8());
      message.setLedGreen(readUInt8());
      result = message;
      break;
    }
    case 64: {
      StateCorrectionMessage message = new StateCorrectionMessage(sequence, systemId, componentId);
      message.setXerr(readFloat());
      message.setYerr(readFloat());
      message.setZerr(readFloat());
      message.setRollerr(readFloat());
      message.setPitcherr(readFloat());
      message.setYawerr(readFloat());
      message.setVxerr(readFloat());
      message.setVyerr(readFloat());
      message.setVzerr(readFloat());
      result = message;
      break;
    }
    case 66: {
      RequestDataStreamMessage message = new RequestDataStreamMessage(sequence, systemId, componentId);
      message.setReqMessageRate(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setReqStreamId(readUInt8());
      message.setStartStop(readUInt8());
      result = message;
      break;
    }
    case 67: {
      DataStreamMessage message = new DataStreamMessage(sequence, systemId, componentId);
      message.setMessageRate(readUInt16());
      message.setStreamId(readUInt8());
      message.setOnOff(readUInt8());
      result = message;
      break;
    }
    case 69: {
      ManualControlMessage message = new ManualControlMessage(sequence, systemId, componentId);
      message.setX(readInt16());
      message.setY(readInt16());
      message.setZ(readInt16());
      message.setR(readInt16());
      message.setButtons(readUInt16());
      message.setTarget(readUInt8());
      result = message;
      break;
    }
    case 70: {
      RcChannelsOverrideMessage message = new RcChannelsOverrideMessage(sequence, systemId, componentId);
      message.setChan1Raw(readUInt16());
      message.setChan2Raw(readUInt16());
      message.setChan3Raw(readUInt16());
      message.setChan4Raw(readUInt16());
      message.setChan5Raw(readUInt16());
      message.setChan6Raw(readUInt16());
      message.setChan7Raw(readUInt16());
      message.setChan8Raw(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 74: {
      VfrHudMessage message = new VfrHudMessage(sequence, systemId, componentId);
      message.setAirspeed(readFloat());
      message.setGroundspeed(readFloat());
      message.setAlt(readFloat());
      message.setClimb(readFloat());
      message.setHeading(readInt16());
      message.setThrottle(readUInt16());
      result = message;
      break;
    }
    case 76: {
      CommandLongMessage message = new CommandLongMessage(sequence, systemId, componentId);
      message.setParam1(readFloat());
      message.setParam2(readFloat());
      message.setParam3(readFloat());
      message.setParam4(readFloat());
      message.setParam5(readFloat());
      message.setParam6(readFloat());
      message.setParam7(readFloat());
      message.setCommand(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setConfirmation(readUInt8());
      result = message;
      break;
    }
    case 77: {
      CommandAckMessage message = new CommandAckMessage(sequence, systemId, componentId);
      message.setCommand(readUInt16());
      message.setResult(readUInt8());
      result = message;
      break;
    }
    case 89: {
      LocalPositionNedSystemGlobalOffsetMessage message = new LocalPositionNedSystemGlobalOffsetMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      result = message;
      break;
    }
    case 90: {
      HilStateMessage message = new HilStateMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      message.setRollspeed(readFloat());
      message.setPitchspeed(readFloat());
      message.setYawspeed(readFloat());
      message.setLat(readInt32());
      message.setLon(readInt32());
      message.setAlt(readInt32());
      message.setVx(readInt16());
      message.setVy(readInt16());
      message.setVz(readInt16());
      message.setXacc(readInt16());
      message.setYacc(readInt16());
      message.setZacc(readInt16());
      result = message;
      break;
    }
    case 91: {
      HilControlsMessage message = new HilControlsMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setRollAilerons(readFloat());
      message.setPitchElevator(readFloat());
      message.setYawRudder(readFloat());
      message.setThrottle(readFloat());
      message.setAux1(readFloat());
      message.setAux2(readFloat());
      message.setAux3(readFloat());
      message.setAux4(readFloat());
      message.setMode(readUInt8());
      message.setNavMode(readUInt8());
      result = message;
      break;
    }
    case 92: {
      HilRcInputsRawMessage message = new HilRcInputsRawMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setChan1Raw(readUInt16());
      message.setChan2Raw(readUInt16());
      message.setChan3Raw(readUInt16());
      message.setChan4Raw(readUInt16());
      message.setChan5Raw(readUInt16());
      message.setChan6Raw(readUInt16());
      message.setChan7Raw(readUInt16());
      message.setChan8Raw(readUInt16());
      message.setChan9Raw(readUInt16());
      message.setChan10Raw(readUInt16());
      message.setChan11Raw(readUInt16());
      message.setChan12Raw(readUInt16());
      message.setRssi(readUInt8());
      result = message;
      break;
    }
    case 100: {
      OpticalFlowMessage message = new OpticalFlowMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setFlowCompMX(readFloat());
      message.setFlowCompMY(readFloat());
      message.setGroundDistance(readFloat());
      message.setFlowX(readInt16());
      message.setFlowY(readInt16());
      message.setSensorId(readUInt8());
      message.setQuality(readUInt8());
      result = message;
      break;
    }
    case 101: {
      GlobalVisionPositionEstimateMessage message = new GlobalVisionPositionEstimateMessage(sequence, systemId, componentId);
      message.setUsec(readUInt64());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      result = message;
      break;
    }
    case 102: {
      VisionPositionEstimateMessage message = new VisionPositionEstimateMessage(sequence, systemId, componentId);
      message.setUsec(readUInt64());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      result = message;
      break;
    }
    case 103: {
      VisionSpeedEstimateMessage message = new VisionSpeedEstimateMessage(sequence, systemId, componentId);
      message.setUsec(readUInt64());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      result = message;
      break;
    }
    case 104: {
      ViconPositionEstimateMessage message = new ViconPositionEstimateMessage(sequence, systemId, componentId);
      message.setUsec(readUInt64());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      result = message;
      break;
    }
    case 105: {
      HighresImuMessage message = new HighresImuMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setXacc(readFloat());
      message.setYacc(readFloat());
      message.setZacc(readFloat());
      message.setXgyro(readFloat());
      message.setYgyro(readFloat());
      message.setZgyro(readFloat());
      message.setXmag(readFloat());
      message.setYmag(readFloat());
      message.setZmag(readFloat());
      message.setAbsPressure(readFloat());
      message.setDiffPressure(readFloat());
      message.setPressureAlt(readFloat());
      message.setTemperature(readFloat());
      message.setFieldsUpdated(readUInt16());
      result = message;
      break;
    }
    case 147: {
      BatteryStatusMessage message = new BatteryStatusMessage(sequence, systemId, componentId);
      message.setVoltageCell1(readUInt16());
      message.setVoltageCell2(readUInt16());
      message.setVoltageCell3(readUInt16());
      message.setVoltageCell4(readUInt16());
      message.setVoltageCell5(readUInt16());
      message.setVoltageCell6(readUInt16());
      message.setCurrentBattery(readInt16());
      message.setAccuId(readUInt8());
      message.setBatteryRemaining(readInt8());
      result = message;
      break;
    }
    case 148: {
      Setpoint8dofMessage message = new Setpoint8dofMessage(sequence, systemId, componentId);
      message.setVal1(readFloat());
      message.setVal2(readFloat());
      message.setVal3(readFloat());
      message.setVal4(readFloat());
      message.setVal5(readFloat());
      message.setVal6(readFloat());
      message.setVal7(readFloat());
      message.setVal8(readFloat());
      message.setTargetSystem(readUInt8());
      result = message;
      break;
    }
    case 149: {
      Setpoint6dofMessage message = new Setpoint6dofMessage(sequence, systemId, componentId);
      message.setTransX(readFloat());
      message.setTransY(readFloat());
      message.setTransZ(readFloat());
      message.setRotX(readFloat());
      message.setRotY(readFloat());
      message.setRotZ(readFloat());
      message.setTargetSystem(readUInt8());
      result = message;
      break;
    }
    case 249: {
      MemoryVectMessage message = new MemoryVectMessage(sequence, systemId, componentId);
      message.setAddress(readUInt16());
      message.setVer(readUInt8());
      message.setType(readUInt8());
      message.setValue(readInt8());
      result = message;
      break;
    }
    case 250: {
      DebugVectMessage message = new DebugVectMessage(sequence, systemId, componentId);
      message.setTimeUsec(readUInt64());
      message.setX(readFloat());
      message.setY(readFloat());
      message.setZ(readFloat());
      message.setName(readChars(10));
      result = message;
      break;
    }
    case 251: {
      NamedValueFloatMessage message = new NamedValueFloatMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setValue(readFloat());
      message.setName(readChars(10));
      result = message;
      break;
    }
    case 252: {
      NamedValueIntMessage message = new NamedValueIntMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setValue(readInt32());
      message.setName(readChars(10));
      result = message;
      break;
    }
    case 253: {
      StatustextMessage message = new StatustextMessage(sequence, systemId, componentId);
      message.setSeverity(readUInt8());
      message.setText(readChars(50));
      result = message;
      break;
    }
    case 254: {
      DebugMessage message = new DebugMessage(sequence, systemId, componentId);
      message.setTimeBootMs(readUInt32());
      message.setValue(readFloat());
      message.setInd(readUInt8());
      result = message;
      break;
    }
    case 150: {
      SensorOffsetsMessage message = new SensorOffsetsMessage(sequence, systemId, componentId);
      message.setMagDeclination(readFloat());
      message.setRawPress(readInt32());
      message.setRawTemp(readInt32());
      message.setGyroCalX(readFloat());
      message.setGyroCalY(readFloat());
      message.setGyroCalZ(readFloat());
      message.setAccelCalX(readFloat());
      message.setAccelCalY(readFloat());
      message.setAccelCalZ(readFloat());
      message.setMagOfsX(readInt16());
      message.setMagOfsY(readInt16());
      message.setMagOfsZ(readInt16());
      result = message;
      break;
    }
    case 151: {
      SetMagOffsetsMessage message = new SetMagOffsetsMessage(sequence, systemId, componentId);
      message.setMagOfsX(readInt16());
      message.setMagOfsY(readInt16());
      message.setMagOfsZ(readInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 152: {
      MeminfoMessage message = new MeminfoMessage(sequence, systemId, componentId);
      message.setBrkval(readUInt16());
      message.setFreemem(readUInt16());
      result = message;
      break;
    }
    case 153: {
      ApAdcMessage message = new ApAdcMessage(sequence, systemId, componentId);
      message.setAdc1(readUInt16());
      message.setAdc2(readUInt16());
      message.setAdc3(readUInt16());
      message.setAdc4(readUInt16());
      message.setAdc5(readUInt16());
      message.setAdc6(readUInt16());
      result = message;
      break;
    }
    case 154: {
      DigicamConfigureMessage message = new DigicamConfigureMessage(sequence, systemId, componentId);
      message.setExtraValue(readFloat());
      message.setShutterSpeed(readUInt16());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setMode(readUInt8());
      message.setAperture(readUInt8());
      message.setIso(readUInt8());
      message.setExposureType(readUInt8());
      message.setCommandId(readUInt8());
      message.setEngineCutOff(readUInt8());
      message.setExtraParam(readUInt8());
      result = message;
      break;
    }
    case 155: {
      DigicamControlMessage message = new DigicamControlMessage(sequence, systemId, componentId);
      message.setExtraValue(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setSession(readUInt8());
      message.setZoomPos(readUInt8());
      message.setZoomStep(readInt8());
      message.setFocusLock(readUInt8());
      message.setShot(readUInt8());
      message.setCommandId(readUInt8());
      message.setExtraParam(readUInt8());
      result = message;
      break;
    }
    case 156: {
      MountConfigureMessage message = new MountConfigureMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setMountMode(readUInt8());
      message.setStabRoll(readUInt8());
      message.setStabPitch(readUInt8());
      message.setStabYaw(readUInt8());
      result = message;
      break;
    }
    case 157: {
      MountControlMessage message = new MountControlMessage(sequence, systemId, componentId);
      message.setInputA(readInt32());
      message.setInputB(readInt32());
      message.setInputC(readInt32());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setSavePosition(readUInt8());
      result = message;
      break;
    }
    case 158: {
      MountStatusMessage message = new MountStatusMessage(sequence, systemId, componentId);
      message.setPointingA(readInt32());
      message.setPointingB(readInt32());
      message.setPointingC(readInt32());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      result = message;
      break;
    }
    case 160: {
      FencePointMessage message = new FencePointMessage(sequence, systemId, componentId);
      message.setLat(readFloat());
      message.setLng(readFloat());
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setIdx(readUInt8());
      message.setCount(readUInt8());
      result = message;
      break;
    }
    case 161: {
      FenceFetchPointMessage message = new FenceFetchPointMessage(sequence, systemId, componentId);
      message.setTargetSystem(readUInt8());
      message.setTargetComponent(readUInt8());
      message.setIdx(readUInt8());
      result = message;
      break;
    }
    case 162: {
      FenceStatusMessage message = new FenceStatusMessage(sequence, systemId, componentId);
      message.setBreachTime(readUInt32());
      message.setBreachCount(readUInt16());
      message.setBreachStatus(readUInt8());
      message.setBreachType(readUInt8());
      result = message;
      break;
    }
    case 163: {
      AhrsMessage message = new AhrsMessage(sequence, systemId, componentId);
      message.setOmegaix(readFloat());
      message.setOmegaiy(readFloat());
      message.setOmegaiz(readFloat());
      message.setAccelWeight(readFloat());
      message.setRenormVal(readFloat());
      message.setErrorRp(readFloat());
      message.setErrorYaw(readFloat());
      result = message;
      break;
    }
    case 164: {
      SimstateMessage message = new SimstateMessage(sequence, systemId, componentId);
      message.setRoll(readFloat());
      message.setPitch(readFloat());
      message.setYaw(readFloat());
      message.setXacc(readFloat());
      message.setYacc(readFloat());
      message.setZacc(readFloat());
      message.setXgyro(readFloat());
      message.setYgyro(readFloat());
      message.setZgyro(readFloat());
      message.setLat(readFloat());
      message.setLng(readFloat());
      result = message;
      break;
    }
    case 165: {
      HwstatusMessage message = new HwstatusMessage(sequence, systemId, componentId);
      message.setVcc(readUInt16());
      message.setI2cerr(readUInt8());
      result = message;
      break;
    }
    case 166: {
      RadioMessage message = new RadioMessage(sequence, systemId, componentId);
      message.setRxerrors(readUInt16());
      message.setFixed(readUInt16());
      message.setRssi(readUInt8());
      message.setRemrssi(readUInt8());
      message.setTxbuf(readUInt8());
      message.setNoise(readUInt8());
      message.setRemnoise(readUInt8());
      result = message;
      break;
    }
    case 167: {
      LimitsStatusMessage message = new LimitsStatusMessage(sequence, systemId, componentId);
      message.setLastTrigger(readUInt32());
      message.setLastAction(readUInt32());
      message.setLastRecovery(readUInt32());
      message.setLastClear(readUInt32());
      message.setBreachCount(readUInt16());
      message.setLimitsState(readUInt8());
      message.setModsEnabled(readUInt8());
      message.setModsRequired(readUInt8());
      message.setModsTriggered(readUInt8());
      result = message;
      break;
    }
    case 168: {
      WindMessage message = new WindMessage(sequence, systemId, componentId);
      message.setDirection(readFloat());
      message.setSpeed(readFloat());
      message.setSpeedZ(readFloat());
      result = message;
      break;
    }
    case 169: {
      Data16Message message = new Data16Message(sequence, systemId, componentId);
      message.setType(readUInt8());
      message.setLen(readUInt8());
      message.setData(readUInt8());
      result = message;
      break;
    }
    case 170: {
      Data32Message message = new Data32Message(sequence, systemId, componentId);
      message.setType(readUInt8());
      message.setLen(readUInt8());
      message.setData(readUInt8());
      result = message;
      break;
    }
    case 171: {
      Data64Message message = new Data64Message(sequence, systemId, componentId);
      message.setType(readUInt8());
      message.setLen(readUInt8());
      message.setData(readUInt8());
      result = message;
      break;
    }
    case 172: {
      Data96Message message = new Data96Message(sequence, systemId, componentId);
      message.setType(readUInt8());
      message.setLen(readUInt8());
      message.setData(readUInt8());
      result = message;
      break;
    }
	default: result = null;
    }
    return result;
  }
}
