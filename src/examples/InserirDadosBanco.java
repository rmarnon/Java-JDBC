package examples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import db.DB;

public class InserirDadosBanco {

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Connection conn = null;
		PreparedStatement st = null;

		
		try {
			conn = DB.getConnection();
			st = conn.prepareStatement(//String de insercao de dados SQL deixando dados ? para depois
			"INSERT INTO SELLER (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?,?,?,?,?)");
			
			//Cada ? tem sua posicao e equivale ao tipo a ser inserido no st.set(...)
			st.setString(1, "Rodrigo Marnon");
			st.setString(2, "rmarnon@yahoo.com.br");
			st.setDate(3, new java.sql.Date(sdf.parse("29/04/1982").getTime()));
			//Data deve ser instanciado objeto java.sql.Date, convertido "parse", e no final .getTime()
			st.setDouble(4, 1545.00);
			st.setInt(5, 1);
			
			int rowsAffected = st.executeUpdate();
			//Retorna as linhas afetadas, no caso foi inserido uma linha inteira
			
			System.out.println("Done! Rows affected " + rowsAffected);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatment(st);
			DB.closeConnection();
		}
	}
}
