package db;

import java.io.IOException;
import java.util.Properties;

public class DBConfig {

	private static final Properties props = new Properties();
	public static String a;
	static{
		try {
			props.load(DBConfig.class.getResourceAsStream("db.cfg.properties"));
			String os = System.getProperty("os.name");
			if(os.toLowerCase().startsWith("win"))
			{
				a=props.getProperty("IP1");
			}
			else
				a=props.getProperty("IP2");
			String a=props.getProperty("IP");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static final String IP=a;
	public static final String PORT = props.getProperty("PORT");
	public static final String DBNAME = props.getProperty("DBNAME");
	public static final String ACCOUNT = props.getProperty("ACCOUNT");
	public static final String PASSWORD = props.getProperty("PASSWORD");
}
