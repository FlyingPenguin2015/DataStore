package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;
public class DeleteCommand extends ServerCommand{
	private static Logger logger = Logger.getLogger(DeleteCommand.class);
	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		String name = StreamUtil.readLine(inputStream);
		if(name==null) throw new ServerException("Cannot get name");
		logger.debug("name: " + name);
		boolean flag = false;
		flag =FileUtil.deleteData(name);
		if(flag==true)sendOK();
		else sendError("Can not find the file");
		
		
	}

}
