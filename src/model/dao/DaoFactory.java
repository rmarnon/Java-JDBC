package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	/*
	 * Classe auxiliar responsavel por instanciar os Daos
	 * A classe expoe um metodo que retorna o tipo da interface, mas internamente ela instancia uma 
	 * implementacao, isso eh macete p/ nao precisar expor a implementacao, deixando somente a interface
	 */
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
	}	
}
