package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class ListCommand extends ServerCommand {
	private static Logger logger = Logger.getLogger(ListCommand.class);
	@Override
	public void run() throws IOException, ServerException
	{
		
			List<String> list = new ArrayList<String>();
		    list = FileUtil.directory(); 
			String num = String.valueOf(list.size());
			StreamUtil.writeLine(num, outputStream);
			for(int i=0;i<list.size();i++)
			{
			   StreamUtil.writeLine(list.get(i), outputStream);
			}
			
			
				
			logger.debug("Finished List Command");
		
	}
}
