package org.anticheat.zues.util.hwid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.anticheat.zues.Zues;
import org.anticheat.zues.util.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class HWIDChecker {

	public static Zues instance;
	
	private static Connection connection;
	private String host, database, username, password;
	private int port;
	

	public HWIDChecker(Zues plugin) {
		instance = plugin;
		host = "173.212.203.225";
		port = 3306;
		database = "s35_ZuesAntiCheatHWID";
		username = "u35_hVcWGmhDaN";
		password = "7ABBU6tqlGqSpmJn";
		try {     
            openConnection();
            Statement statement = connection.createStatement();  
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS servers(id int NOT NULL AUTO_INCREMENT, ip varchar(255), hwid varchar(255), PRIMARY KEY(id))");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!check(Zues.HWIDConfig)) {
					ServerUtils.logPluginError(0, "L%4lRoxygJ");
				}
			}
		}.runTaskTimer(plugin, 15 * 60, 15 * 60 * 5);
	}
	
	public static boolean check(String s) {
		try {
			URL url = new URL(Math.decryptBase64("aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L3RDRlEycWJG"));
			url.openConnection();
			ArrayList<String> keys = new ArrayList<String>();
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				keys.add(line);
			}
			if(keys.contains(Math.encryptSha256(s))) {
				insert(Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort(), s);
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void openConnection() throws SQLException, ClassNotFoundException {
		if(connection != null && !connection.isClosed()) {
			return;
		}
		
		synchronized (this) {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
	public static void insert(String ip, String hwid) throws Exception {
		try {
			Connection con = getConnection();
			PreparedStatement posted = con.prepareStatement("REPLACE INTO servers (ip, hwid) VALUES ('" + ip + "', '" + hwid + "')");
			posted.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}