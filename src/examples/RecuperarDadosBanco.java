package examples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;

public class RecuperarDadosBanco {

	public static void main(String[] args) {

		Connection conn =  null;			
		Statement st = null;				
		ResultSet rs = null;		//Objeto com resultado da query em formato de tabela
		
		try {
			conn = DB.getConnection();//Conecta ao banco de dados
			
			st = conn.createStatement();//Cria um objeto do tipo Statement
			
			rs = st.executeQuery("SELECT * FROM DEPARTMENT");//Monta uma consulta/comando SQL (String)
			
			while (rs.next()) {//Por padrao inicia na posicao 0
				System.out.println(rs.getInt("Id") + ", " +  rs.getString("Name"));
				//Impressao sera de acordo com o tipo da coluna/campo da tabela e seu respectivo nome
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatment(st);
			DB.closeResultSet(rs);
			DB.closeConnection();
		}
	}
}
