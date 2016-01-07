package login;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.sql.DataSource;
import javax.annotation.Resource;

@Named
@SessionScoped
public class LoginData implements Serializable {
	private static final String connection_url = "jdbc:postgresql://localhost:5432/TantooDB";
	private String userName;
	private String password;
	private String email;
	private String description;
	private boolean show = false;
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean getShow() {
		return this.show;
	}
	
	public void showHide() {
		this.show = !this.show;
	}

	public List<LoginData> getUsers() {
		//System.out.println("Getting users...");
		List<LoginData> result = new ArrayList<>();;
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "postgres");
			user.put("password", "admin");
			Connection conn = DriverManager.getConnection(connection_url, user);
			
			try {
				String sql = "select * from users";
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
				
				while(rs.next()) {
					LoginData log = new LoginData();
					log.setUserName(rs.getString(1));
					log.setPassword(rs.getString(2));
					log.setEmail(rs.getString(3));
					result.add(log);
				}
			}
			
			finally {
				conn.close();
			}
			
		} catch (SQLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		return result;
	}
	
	public String checkLogin() {
		System.out.println("Logging in");
		List<LoginData> list = getUsers();
		System.out.println(list.size());
		
		for(int i= 0; i< list.size();i++) {
			
			if(this.userName.equals(list.get(i).getUserName()) && this.password.equals(list.get(i).getPassword())) {
				System.out.println("Login success");
				return "success";
			}	
		}
		
		System.out.println("Login failure");
		return "failure";
	}
	
	public String add() {
		int i = 0;
		System.out.println("Inserting into database...");
		// Don't continue if user name field is empty
		if(true) {
			PreparedStatement ps = null;
			Connection conn = null; 
			try {
				Class.forName("org.postgresql.Driver");
				Properties user = new Properties();
				user.put("user", "postgres");
				user.put("password", "admin");
				conn = DriverManager.getConnection(connection_url, user);
				
				String sql = "INSERT INTO users(name, password, email) VALUES(?,?,?)";
				ps = conn.prepareStatement(sql); 
				ps.setString(1, this.userName);
				ps.setString(2, this.password);
				ps.setString(3, this.email);
	
				i = ps.executeUpdate();
				System.out.println("Data Added Successfully");
				
			} catch(Exception e) {
				System.out.println(e); 
			} finally {
				try {
					conn.close();
					ps.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(i > 0) {
			return "output";
		} else {
			return "invalid";
		  } 
	}
	
	public String forgotPW() {
		System.out.println("TEST MOTHERFUCKER");
		return "Test string";
	}
	
}
