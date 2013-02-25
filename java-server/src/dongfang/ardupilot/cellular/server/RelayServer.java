package dongfang.ardupilot.cellular.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import java.net.DatagramPacket;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import java.util.List;
import java.util.LinkedList;

import dongfang.mavlink_10.messages.MavlinkMessage;
import dongfang.mavlink_10.serialization.CRC16InputStreamDecorator;
import dongfang.mavlink_10.serialization.EndOfStreamResult;
import dongfang.mavlink_10.serialization.MavlinkReceiveResult;
import dongfang.mavlink_10.serialization.BadChecksumResult;
import dongfang.mavlink_10.serialization.MessageReceivedResult;
import dongfang.mavlink_10.serialization.UnknownMessageReceivedResult;
import dongfang.mavlink_10.serialization.EndOfStreamResult;
import dongfang.mavlink_10.serialization.IOExceptionResult;
import dongfang.mavlink_10.serialization.MavlinkUnserializer;

/**
 * This is a simple relay server for solving the problem of making data streams
 * meet on the internet. Problem: To connect a UAV (a drone) with a cellular
 * data connection with a ground station (a computer used byt he pilot to
 * control the progress of the drone's flight). A UAV with IP cellular has no
 * fixed IP address and UDP messages sent sent from outside cannot be routed in
 * such a way as to reach it (google for "UDP hole punching" to see what one
 * problem is). A ground station in the field is in the same situation (UDP as
 * well as TCP). Solution (for now): Run this program on a host computer with a
 * fixed IP address and with a TCP and a UDP port routed to that host from the
 * internet. Now both ends of the link can connect to the host computer and send
 * and receive data. The program will simply receive a number of connections,
 * and relay anything that arrives on one connection to all the others.
 * Disclaimer: This is for evaluation purposes and for inspiration only. Do not
 * use this program for actual flight. Startup parameters: A TCP port number and
 * a UDP port number. (eg.
 * "java dongfang.ardupilot.cellular.server.JavaRelayServer 10000 10000").
 */

public class RelayServer {
	final static int MAX_CONNECTION_COUNT = 10;
	final static int MAX_DATAGRAM_LENGTH = 1500;
	final static int MAX_MESSAGE_BACKLOG = 64;

	enum Protocols {
		TCP, UDP
	}

	abstract class Connection {
		LinkedList<Message> messageQueue = new LinkedList<Message>();

		volatile long lastHeardFromTimestamp;
		volatile boolean isOpen;

		abstract Protocols getProtocol();

		void open() {
			isOpen = true;
			new TransmitWorker(this).start();
		}

		void close() throws IOException {
			isOpen = false;
		}

		boolean isOpen() {
			return isOpen;
		}

		void kill() throws IOException {
			System.err.println("Killing connection: " + this);
			close();
			RelayServer.this.removeConnection(this);
		}

		abstract void send(byte[] data, int offset, int length)
				throws IOException;

		abstract SocketAddress getRemote();

		public String toString() {
			return getProtocol() + ":" + getRemote();
		}

		void queueMessage(Message message) {
			synchronized (messageQueue) {
				// To keep the queue at resonable length, we throw away oldest
				// data.
				if (messageQueue.size() >= MAX_MESSAGE_BACKLOG) {
					dequeueMessage();
				}
				messageQueue.add(message);
			}
		}

		Message dequeueMessage() {
			synchronized (messageQueue) {
				return messageQueue.pollFirst();
			}
		}
	}

	boolean crash = false;
	List<Connection> connections = new LinkedList<Connection>();
	MAVLinkInterpreter mavlink = new MAVLinkInterpreter();
	FileLogger logger = new FileLogger();

	class Message {
		Connection receivedConnection;
		byte[] data;

		Message(byte[] data, int offset, int length,
				Connection receivedConnection) {
			this.data = new byte[length];
			System.arraycopy(data, offset, this.data, 0, length);
		}
	}

	class UDPConnection extends Connection {
		DatagramSocket socket;
		SocketAddress remote;

		UDPConnection(DatagramSocket socket, SocketAddress remote) {
			this.socket = socket;
			this.remote = remote;
		}

		Protocols getProtocol() {
			return Protocols.UDP;
		}

		void send(byte[] data, int offset, int length) throws IOException {
			DatagramPacket packet = new DatagramPacket(data, offset, length,
					remote);
			socket.send(packet);
		}

		SocketAddress getRemote() {
			return remote;
		}
	}

	class TCPConnection extends Connection implements Runnable {
		Socket socket;

		TCPConnection(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				InputStream is = socket.getInputStream();
				byte[] buf = new byte[MAX_DATAGRAM_LENGTH];
				while (isOpen) {
					int length = is.read(buf);
					if (length < 0) {
						System.err
								.println("Read EOS on TCP; asssume socket closed.");
						kill();
					} else {
						this.lastHeardFromTimestamp = System
								.currentTimeMillis();
						RelayServer.this.broadcastToAllExcept(buf, 0, length,
								this);
					}
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}

		Protocols getProtocol() {
			return Protocols.TCP;
		}

		void open() {
			super.open();
			new Thread(this).start();
		}

		void close() throws IOException {
			super.close();
			socket.close();
		}

		void send(byte[] data, int offset, int length) throws IOException {
			if (socket.isConnected()) {
				OutputStream os = socket.getOutputStream();
				os.write(data, offset, length);
			} else {
				System.err
						.println("Socket not connected, assume closed by remote.");
				kill();
			}
		}

		SocketAddress getRemote() {
			return socket.getRemoteSocketAddress();
		}
	}

	// This thread transmits whatever is in a transmit buffer.
	// Common impl. for TCP and UDP.
	// The purpose of this deferred transmission is to avoid blocking receivers
	// - if receivers did transmission
	// directly that might happen and could be very harmful.
	class TransmitWorker extends Thread {
		Connection myConnection;

		TransmitWorker(Connection myConnection) {
			this.myConnection = myConnection;
		}

		public void run() {
			while (myConnection.isOpen()) {
				Message nextMessage = myConnection.dequeueMessage();
				if (nextMessage != null) {
					try {
						myConnection.send(nextMessage.data, 0,
								nextMessage.data.length);
					} catch (IOException ex) {
						System.err.println(ex);
					}
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
					}
				}
			}
		}
	}

	// TODO: Buffer this, have a worker thread do the stuff instead.
	void broadcastToAllExcept(Message message, Connection exception) {
		// String asString = new String(data, offset, length);
		// System.out.println("broadcasting: " + asString);
		synchronized (connections) {
			for (Connection connection : connections) {
				if (connection != exception)
					connection.queueMessage(message);
			}
		}
	}

	void broadcastToAllExcept(byte[] data, int offset, int length,
			Connection exception) {
		Message message = new Message(data, offset, length, exception);
		broadcastToAllExcept(message, exception);
		mavlink.deliver(data, offset, length);
	}

	Connection findConnection(Protocols protocol, SocketAddress address) {
		synchronized (connections) {
			for (Connection connection : connections) {
				if (connection.getProtocol() == protocol
						&& connection.getRemote().equals(address))
					return connection;
			}
			return null;
		}
	}

	void removeConnection(Connection connection) {
		synchronized (connections) {
			connections.remove(connection);
		}
	}

	void addConnection(Connection newConnection) throws IOException {
		// see if we can handle more, if not, throw away one connection...
		synchronized (connections) {
			if (connections.size() >= MAX_CONNECTION_COUNT) {
				long oldest = Long.MAX_VALUE;
				Connection killConnection = null;
				for (Connection temp : connections) {
					if (temp.lastHeardFromTimestamp < oldest) {
						oldest = temp.lastHeardFromTimestamp;
						killConnection = temp;
					}
				}
				if (killConnection != null) {
					System.err
							.println("Killing oldest connection (we are full!)");
					killConnection.kill();
				}
			}
			connections.add(newConnection);
			System.err.println("Added new connection: " + newConnection);
		}
	}

	class UDPConnectionAcceptor extends Thread {
		DatagramSocket serverSocket;

		UDPConnectionAcceptor(int serverPort) throws SocketException {
			serverSocket = new DatagramSocket(serverPort);
			System.err.println("Binding UDP listener to "
					+ serverSocket.getLocalSocketAddress());
		}

		public void run() {
			byte[] buf = new byte[MAX_DATAGRAM_LENGTH];
			DatagramPacket packet = new DatagramPacket(buf, MAX_DATAGRAM_LENGTH);
			while (!crash) {
				try {
					serverSocket.receive(packet);
					Connection alreadyExistingConnection = findConnection(
							Protocols.UDP, packet.getSocketAddress());
					if (alreadyExistingConnection == null) {
						alreadyExistingConnection = new UDPConnection(
								serverSocket, packet.getSocketAddress());
						alreadyExistingConnection.open();
						addConnection(alreadyExistingConnection);
					}
					// recieve(packet);
					alreadyExistingConnection.lastHeardFromTimestamp = System
							.currentTimeMillis();
					broadcastToAllExcept(packet.getData(), packet.getOffset(),
							packet.getLength(), alreadyExistingConnection);
				} catch (IOException ex) {
					System.err.println(ex);
				}
			}
		}
	}

	class TCPConnectionAcceptor extends Thread {
		ServerSocket serverSocket;

		TCPConnectionAcceptor(int serverPort) throws IOException {
			serverSocket = new ServerSocket(serverPort);
			System.err.println("Binding TCP listener to "
					+ serverSocket.getLocalSocketAddress());
		}

		public void run() {
			while (!crash) {
				try {
					Socket clientSocket = serverSocket.accept();
					Connection alreadyExistingConnection = findConnection(
							Protocols.TCP,
							clientSocket.getRemoteSocketAddress());
					if (alreadyExistingConnection == null) {
						alreadyExistingConnection = new TCPConnection(
								clientSocket);
						alreadyExistingConnection.open();
						addConnection(alreadyExistingConnection);
					}
				} catch (IOException ex) {
					System.err.println(ex);
				}
			}
		}
	}

	class MAVLinkInterpreter extends Thread {
		OutputStream spillway = null; //System.out;
		final int MAGIC_BYTE = 0xfe; // oops does not belong here at all.
		PipedOutputStream sink;
		CRC16InputStreamDecorator inputCRC;
		MavlinkUnserializer unserializer;

		void init() throws IOException {
			sink = new PipedOutputStream();
			PipedInputStream source = new PipedInputStream(sink);
			inputCRC = new CRC16InputStreamDecorator(source);
			unserializer = new MavlinkUnserializer(inputCRC);
		}
		
		void deliver(byte[] data, int offset, int length) {
			try {
				sink.write(data, offset, length);
			} catch(IOException ex) {
				System.err.println(ex);
			}
		}
		
		private MavlinkReceiveResult readNextMessage() {
			int inputByte;
			while (true) {
				try {
					while ((inputByte = inputCRC.read()) != MAGIC_BYTE
							&& inputByte >= 0) {
						if (spillway != null) spillway.write(inputByte);
					}
					if (inputByte == -1)
						return new EndOfStreamResult(); // stream ended.
					inputCRC.resetCRC16();
					int len = inputCRC.read();
					int seq = inputCRC.read();
					int sysId = inputCRC.read();
					int componentId = inputCRC.read();
					MavlinkMessage message = unserializer.unserializeMessage(
							len, seq, sysId, componentId);
					if (message == null) {
						return new UnknownMessageReceivedResult(-1);
					} else {
						if (message.getLength() != len) {
							System.err
									.println("Strange message length! Message "
											+ message + ", template length "
											+ message.getLength()
											+ ", stream length " + len);
						}
						inputCRC.addToCRC16(message.getExtraCRC());
					}
					int computedChecksum = inputCRC.getCRC16();
					int receivedChecksum = inputCRC.read()
							| (inputCRC.read() << 8);

					if (computedChecksum == receivedChecksum)
						return new MessageReceivedResult(message);
					else
						return new BadChecksumResult(message);
				} catch (IOException ex) {
					return new IOExceptionResult(ex);
				}
			}
		}

		public void run() {
			MavlinkReceiveResult result;
			do {
				result = readNextMessage();
				System.out.println(result);
			} while (!(result instanceof EndOfStreamResult));
		}
	}

	void start(int TCPPort, int UDPPort, String logFileName) {
		try {
			new UDPConnectionAcceptor(UDPPort).start();
		} catch (SocketException ex) {
			System.err.println("Cannot bind UDP socket to port " + UDPPort
					+ ", already in use? Reserved (<1024 are restricted)?");
			crash = true;
		}
		if (!crash)
			try {
				new TCPConnectionAcceptor(TCPPort).start();
			} catch (IOException ex) {
				System.err.println("Cannot bind TCP socket to port " + TCPPort
						+ ", already in use? Reserved (<1024 are restricted)?");
				crash = true;
			}
		if (!crash) {
			try {
				mavlink.init();
				mavlink.start();
			} catch (IOException ex) {
				System.err.println("There was some prob starting MAVLink interpreter.");
			}
		}
		if (!crash) {
			try {
				fileLogger.init(logFileName);
				fileLogger.start();
			} catch (IOException ex) {
				System.err.println("There was some prob with the file logger.");
			}
		}
	}

	class FileLogger extends Thread {
		PipedOutputStream sink;
		PipedInputStream source;
		FileOutputStream dumpFile;

		void init(String fileName) throws IOException {
			sink = new PipedOutputStream();
			source = new PipedInputStream(sink);
			dumpFile = new FileOutputStream(fileName);
		}
		
		void deliver(byte[] data, int offset, int length) {
			try {
				sink.write(data, offset, length);
			} catch(IOException ex) {
				System.err.println(ex);
			}
		}

		public void run() {
			int length;
			byte[] buffer = new byte[512];
			while (true) {
				try {
					if ((length = source.read(buffer)) > 0) {
						dumpFile.write(buffer, 0, length);
					}
				} catch(IOException ex) {
					System.err.println(ex);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Usage: RelayServer <TCP port number> <UDP port number> <log file name>");
			System.exit(1);
		}
		int TCPPort = Integer.parseInt(args[0]);
		int UDPPort = Integer.parseInt(args[1]);
		String logFileName = args[2];

		new RelayServer().start(TCPPort, UDPPort, logFileName);
	}
}