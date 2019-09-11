package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {//Deixa o esqueleto montado

	private Connection conn;//Dependencia da conexao
	
	public SellerDaoJDBC(Connection conn) {//Construtor pra forcar a injecao de dependencia
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT S.*, D.Name DepName "
					+ "FROM Seller S INNER JOIN Department D "
					+ "ON D.Id = S.DepartmentId "
					+ "WHERE S.Id = ?");
			
			st.setInt(1, id);//Atribui o id como parametro do ? que sera consultado pela query (Ex 3)
			rs = st.executeQuery();
			//O comando SQL acima sera executado (Consulta) e retorna tipo tabela abaixo
			
			/*
			 * |Id | Name |    Email   |  BirthDate | BaseSalary | DepartmentId | DepName
			 * |3  | Alex | alex@email | 1988-01-15 |    2200    |      1       |Computers
			 */
			
			if(rs.next()) {//verifica se consulta trouxe algum dado
				
				Department dep = instantiateDepartment(rs);				
				Seller seller = instanteSeller(rs, dep);				
				return seller;
			}			
			return null;//Nao existe vendedor com esse Id				
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(st);
			DB.closeResultSet(rs);
		}		
	}

	private Seller instanteSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(dep);//Associacao direta ja passa objeto como parametro		
		return seller;			
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));//Set no objeto; rs.get ->tipo dado, "nome coluna"
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findall() {
		// TODO Auto-generated method stub
		return null;
	}

}
