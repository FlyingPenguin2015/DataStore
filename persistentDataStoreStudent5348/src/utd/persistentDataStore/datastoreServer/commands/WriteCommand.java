package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;


public class WriteCommand extends ServerCommand {

	private static Logger logger = Logger.getLogger(ReadCommand.class);
	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		

		String name = StreamUtil.readLine(inputStream);
		if(name==null)throw new ServerException("Can not read name");
		logger.debug("name: " + name);
		String size = StreamUtil.readLine(inputStream);
		if(size==null)throw new ServerException("Can not read size");
		int length = Integer.parseInt(size);		
		logger.debug("length: " + length);
	
		//byte[] data = new byte[length];
		if(inputStream.available()<length)throw new ServerException("Can not read N bytes");
		
		byte[] data = StreamUtil.readData(length, inputStream);
		FileUtil.writeData(name, data);
		sendOK();

		logger.debug("Finished Write Command");


    	
    }
    
		
	

}
