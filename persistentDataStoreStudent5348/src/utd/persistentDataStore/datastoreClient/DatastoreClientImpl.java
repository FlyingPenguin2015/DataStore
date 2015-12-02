package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte data[]) throws ClientException
	{
		logger.debug("Executing Write Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");

			//send write command to server
			StreamUtil.writeLine("write" + "\n", outputStream);
			
			//send filename to server
			StreamUtil.writeLine(name + "\n", outputStream);
			
			//send size of data to write to server
			StreamUtil.writeLine(data.length + "\n", outputStream);
			
			//send data to write to server
			StreamUtil.writeLine(data + "\n", outputStream);			
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream).toLowerCase();
			logger.debug("Response " + result);
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException
	{
		logger.debug("Executing Read Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");

			//send read command to server
			StreamUtil.writeLine("read" + "\n", outputStream);
			
			//send filename to server
			StreamUtil.writeLine(name + "\n", outputStream);

			//get response from server
			logger.debug("Reading Response");
			String response = StreamUtil.readLine(inputStream).toLowerCase();
			
			//check if command was valid
			if (response.equals("ok")) {
				//command was valid, get number of bytes to read
				int length = Integer.parseInt(StreamUtil.readLine(inputStream));
				
				//read indicated number of bytes into stream
				byte[] result = StreamUtil.readData(length, inputStream);
				
				//return result
				logger.debug("Response " + result);
				return result;
			}
			else {
				//error, invalid command, throw exception detailing error
				throw new ClientException("Error, invalid command: " + response);
			}
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException
	{
		logger.debug("Executing Delete Operation");
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException
	{
		logger.debug("Executing Directory Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");

			//send directory command to server
			StreamUtil.writeLine("directory" + "\n", outputStream);

			//get response from server
			logger.debug("Reading Response");
			String response = StreamUtil.readLine(inputStream).toLowerCase();
			
			//check if command was valid
			if (response.equals("ok")) {
				//command was valid, get number of filenames to read
				int numFileNames = Integer.parseInt(StreamUtil.readLine(inputStream));
				
				//create and populate a List<String> of filenames
				List<String> filenames = new ArrayList<String>();
				for (int i = 0; i < numFileNames; i++) {
					//get the next filename and add it to the list of filenames
					String filename = StreamUtil.readLine(inputStream);
					filenames.add(filename);
				}
				
				//return the list of filenames
				return (filenames);
			}
			else {
				//error, invalid command, throw exception detailing error
				throw new ClientException("Error, invalid command: " + response);
			}
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

}
