package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;// Cria a dependencia da conexao.

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;// Qdo a dependencia e forcada, no Factory deve ser passado DB.getConnection(*)
	}

	@Override
	public void insert(Department dep) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("Insert Into Department (Name) Values (?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, dep.getName());
			
			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					dep.setId(id);
				}
				DB.closeResultSet(rs);
			} 
			else {
				throw new DbException("Unexpected error No rows affected");
			}
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatment(st);
		}
	}

	@Override
	public void update(Department dep) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("Update Department Set Name = ? Where Id = ?");
			
			st.setString(1, dep.getName());
			st.setInt(2, dep.getId());
			
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(st);
		}
	}

	@Override
	public void deletById(Integer id) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("Delete from Department Where Id = ?");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();

			if (rowsAffected == 0) {
				throw new DbIntegrityException("Id not found");
			}
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatment(st);
		}
	}

	@Override
	public Department findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		
		try {
			st = conn.prepareStatement("Select * from Department Where Id = ?", 
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, id);			
			rs = st.executeQuery();
			
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			return null;			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatment(st);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));		
		return dep;
	}

	@Override
	public List<Department> findAll() {

		Statement st = null;
		ResultSet rs = null;
				
		try {
			st = conn.createStatement();
			rs = st.executeQuery("Select * From Department Order By Name");
			
			List<Department> list = new ArrayList<>();
			
			while(rs.next()) {
				Department dep = instantiateDepartment(rs);
				list.add(dep);				
			}
			return list;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(st);
			DB.closeResultSet(rs);
		}
	}
}
