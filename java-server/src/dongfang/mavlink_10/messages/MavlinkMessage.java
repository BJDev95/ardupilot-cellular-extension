package dongfang.mavlink_10.messages;

public abstract class MavlinkMessage {
	protected int sequence;
	protected int systemId;
	protected int componentId;

	protected MavlinkMessage(int sequence, int systemId, int componentId) {
	    this.sequence = sequence;
	    this.systemId = systemId;
	    this.componentId = componentId;
	}
	
	public int getSequence() { 
		return sequence;
	}
	
	public int getSystemId() { 
		return systemId; 
	}

	public int getComponentId() { 
		return componentId; 
	}
	
	// Return the message Id.
	public abstract int getId();
	
	// Human readable description.
	public abstract String getDescription();
	
	// A hash code of the fields. Included in CRC but not in the serialized message. Intended to prevent
	// disasters otherwise happening when the list of fields is changed from one version to another of
	// a message.
	public abstract int getExtraCRC();
	
	// Return the expected message length.
	public abstract int getLength();
}
