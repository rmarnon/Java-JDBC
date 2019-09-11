package examples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
					"INSERT INTO SELLER "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
					//Final -> Segundo paraametro, e sofre sobrecarga com esse metodo tipo enumerado
			
			//Cada ? tem sua posicao e equivale ao tipo a ser inserido no st.set(...)
			st.setString(1, "Carla Rodrigues");
			st.setString(2, "carla@gmail.com");
			st.setDate(3, new java.sql.Date(sdf.parse("27/12/1991").getTime()));
			//Data deve ser instanciado objeto java.sql.Date, convertido "parse", e no final .getTime()
			st.setDouble(4, 1450.00);
			st.setInt(5, 4);
			
			int rowsAffected = st.executeUpdate();
			//Retorna as linhas afetadas, no caso foi inserido uma linha inteira
			
			if (rowsAffected > 0) {//Se foi add 1 ou + linhas
				ResultSet rs = st.getGeneratedKeys();//Gera key e retorna p/ um ResultSet
				while (rs.next()) {//Pula campos e ao chegar na ultima retorna false
					int id = rs.getInt(1);//Tabela auxiliar com uma coluna com os Ids -> 1 no lugar "Id"
					System.out.println("Done! Id = " + id);
				}
			}			
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
