package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void insert(Seller seller) {
		
		PreparedStatement st = null;
				
		try {
			st = conn.prepareStatement("Insert into Seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "Values "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
					//Retorna o Id do vendedor inserido
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {//Significa que inseriu
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {//Se existir esse cara
					int id = rs.getInt(1);//Pega o Id gerado
					seller.setId(id);//Insere novo Id					
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
	public void update(Seller seller) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("Update Seller Set "
					+ "Name = ?, "
					+ "Email = ?, "
					+ "BirthDate = ?, "
					+ "BaseSalary = ?, "
					+ "DepartmentId = ?  "
					+ "Where Id = ?");

			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			st.setInt(6, seller.getId());//Id Seller
			
			st.executeUpdate();//Executa update
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(st);
		}	
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("Delete from Seller Where Id = ?");
			
			st.setInt(1, id);
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected < 1) {
				throw new DbException("Id not found! ");			
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
		
		Statement st = null;//Poderia ser o PreparedStatement sem ?
		ResultSet rs = null;
		
		try {
			
			st = conn.createStatement();			
			rs = st.executeQuery(
					"SELECT Seller.*, Department.Name DepName "
					+ "from Seller inner join Department "
					+ "on Department.Id = Seller.DepartmentId "
					+ "order by Name;");//Opcional ordenar por nome
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();		
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}		
				Seller seller = instanteSeller(rs, dep);
				list.add(seller);
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

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT Seller.*, Department.Name DepName "
					+ "from Seller inner join Department "
					+ "on Department.Id = Seller.DepartmentId "
					+ "where DepartmentId = ? "
					+ "order by Name;");
			
			st.setInt(1, department.getId());	
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();//Armazenar Seller e Department
			Map<Integer, Department> map = new HashMap<>();
			//chave sera o proprio DepartmentId e sera usado pra nao duplicar os departamentos			
			
			while (rs.next()) {//Usar while e nao if, pois se trata de uma lista
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				//Verifica com o "get" o map p/ ver se ja existe um Department instanciado, se nao:
				
				if (dep == null) {//Caso nao exista
					dep = instantiateDepartment(rs);//Instanciara um novo Departamento
					map.put(rs.getInt("DepartmentId"), dep);
					//Salva o Departamento dentro do "map" para fazer futuras verificacoes
					//-> dep apontara pro departamento, existindo ou nao
				}		
				Seller seller = instanteSeller(rs, dep);
				list.add(seller);
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
