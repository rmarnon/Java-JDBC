package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	/*
	 * Classe auxiliar responsavel por instanciar os Daos
	 * A classe expoe um metodo que retorna o tipo da interface, mas internamente ela instancia uma 
	 * implementacao, isso eh macete p/ nao precisar expor a implementacao, deixando somente a interface
	 */
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
		//Obriga a passar uma conexao como argumento quando e criado um metodo de conectar na classe JDBC
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
