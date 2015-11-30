package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class ReadCommand extends ServerCommand {
	
	private static Logger logger = Logger.getLogger(ReadCommand.class);

	@Override
	public void run() throws IOException, ServerException 
	{
		//Read command name from socket input
		String name = StreamUtil.readLine(inputStream);
		if(name==null)throw new ServerException("can not read name");
		logger.debug("Command Name: " + name);
		
		// Read bytes from the file with that command name
		byte [] data = FileUtil.readData(name);
		if(data.length==0)throw new ServerException("no data in this file");
		//Put data into stream
		StreamUtil.writeData(data, outputStream);
		
		logger.debug("Finished reading message");
	}

}

