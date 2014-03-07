package com.google.test.openbox.server.log;

import com.google.test.openbox.server.Server;

/**
 * this is server log mode and its common behaviors interface
 * 
 * @author Jerry Cai
 * 
 */
public interface ServerLog {
	
	Server getServer();

	String getHome();

	String getName();

	String getPath();

	int getCurrentLineNum();

	String getContentByCommand(String command);

	String getLastestContent(int fromLineNum);

	String getContentBetween(int beginLineNum, int endLineNum);

	String grepContentByKeys(String... keys);

	String grepContentByKeysFrom(int beginLineNum , String... keys);
	
	void deleteLog();
	
	void emptyLogByNewLogContent(String newLogContent);

}
