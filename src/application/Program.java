package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Department dep = new Department(1, "Books");
		
		Seller s = new Seller(21, "Rodrigo", "rmarnon@yahoo", new Date(), 1500.00, dep);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		//Ao invez de new SellerDaoJDBC, Dessa forma o main nao conhece a implementacao, so a interface
		//Eh uma forma de fazer injecao de dependencia sem explicitar a implementacao
		
		System.out.println(s);
	}

}
