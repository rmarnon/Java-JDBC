package examples;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Transacao {

	public static void main(String[] args) {

		Connection conn = null;		
		Statement st = null;
		
		try {
			conn = DB.getConnection();	
			
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			conn.setAutoCommit(false);
			//Nao eh pra confirmar alteracoes automaticamente, ficarao pendentes esperando confirmacao
			
			st = conn.createStatement();
			
			int rows1 = st.executeUpdate("Update Seller set BaseSalary = 2570 where DepartmentId = 1");
			
			//int x = 1; //Forca o programa quebrar, atualizando apenas o rows1, e nao o rows 2
			//if (x <2) {throw new SQLException("Fake error");}
			
			int rows2 = st.executeUpdate("Update Seller set BaseSalary = 3590 where DepartmentId = 2");
			
			conn.commit();
			//Confirma que agora sim, minha transacao terminou e pode ser alterado, protegendo o bloco ^
			//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			
			System.out.println("Rows 1 affected: " + rows1);
			System.out.println("Rows 2 affected: " + rows2);
			
		}
		catch (SQLException e) {//Quando o "Fake error" acontece, ele vem p ca e encerra a aplicacao
			try {
				conn.rollback();//Se der rollback, entao deu erro na aplicacao e transacao nao ocorreu
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
			} 
			catch (SQLException e1) {//Se cair aqui eh porque deu erro no rollback
				throw new DbException("Error trying to rollback! Caused by: " + e.getMessage());
			}
		}
		finally {
			DB.closeStatment(st);
			DB.closeConnection();
		}
	}
}
