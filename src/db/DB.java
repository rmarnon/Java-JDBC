package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	/*
	 * Metodo para conexao com banco de dados
	 * Iniciar conn uma conexao null fora do metodo (Objeto de conexao do JDBC)
	 * Objeto props do tipo Properties chama o metodo para carregar o arquivo db.properties de conexao
	 * props tem o metodo getProperty() que recebe a url do db.properties e retorna numa String
	 * DriverManager -> classe JDBC que chama getConnection, passando a url, propriedades como parametro
	 */
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props); 
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	/*
	 * Metodo auxiliar pra fechar a conexao com banco de dados
	 * Se conexao estiver instanciada (aberta), entao fecha
	 */
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	/*
	 * Metodo para carregar arquivo com propriedades de conexao db.properties
	 * FileInputStream "abre" o arquivo db.properties diretamente da raiz do projeto
	 * Objeto "props" do tipo Properties tem o metodo de leitura load() apontando para objeto fs
	 * IOException tambem trata FileNotFoundException
	 */
	
	private static Properties loadProperties() {
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	//Metodo auxiliar pra fechar o Statement
	public static void closeStatment(Statement st) {
		if (st != null) {
			try {
				st.close();
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	//Metodo auxiliar pra fechar o ResultSet
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
}
