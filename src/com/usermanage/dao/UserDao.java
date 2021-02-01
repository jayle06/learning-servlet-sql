package com.usermanage.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.usermanage.model.User;



public class UserDao {
	public Connection getConnection() throws ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;"
																	+ "databaseName=Users;"
																	+ "user=sa;"
																	+ "password=123456789");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public void insertUser(User user) throws ClassNotFoundException{
		String sql = "insert into users(name, email, country) values(?,?,?)";
		Connection conn = getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName() );
			ps.setString(2, user.getEmail() );
			ps.setString(3, user.getCountry() );
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User selectUser(int id) throws ClassNotFoundException {
		User user = null;
		String sql = "select * from users where id = ?";
		Connection conn = getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(name, email, country);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	public List<User> listUser() throws ClassNotFoundException {
		List<User> user = new ArrayList<>();
		String sql = "select * from users";
		Connection conn = getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String contry = rs.getString("country");
				user.add(new User(id, name, email, contry));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	public boolean deleteUser(int id) throws SQLException, ClassNotFoundException {
		boolean rowDeleted;
		String sql = "delete from users where id = ?";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, id);
			rowDeleted = ps.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
		boolean rowUpdate;
		String sql = "update users set name = ?, email = ?, country = ? where id = ?";
		
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getCountry());
			ps.setInt(4, user.getId());
			rowUpdate = ps.executeUpdate() > 0;
		}
		return rowUpdate;
	}
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
