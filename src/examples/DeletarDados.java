package examples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbIntegrityException;

public class DeletarDados {

	public static void main(String[] args) {

		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = DB.getConnection();
			st = conn.prepareStatement(
					"Delete from Department where Id = ?");
			
			st.setInt(1, 2);//Forco o erro pra lancar minha excecao DbIntegrityException
			
			int rowsAffected = st.executeUpdate();
			
			System.out.println("Done! Rows affected: " + rowsAffected);
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());//Lanco minha Excecao personalizada
		}
		finally {
			DB.closeStatment(st);
			DB.closeConnection();
		}
	}

}
