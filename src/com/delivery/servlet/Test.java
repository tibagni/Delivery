package com.delivery.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class Test extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Connection con = null;
        try {
            con = dataSource.getConnection();
            // Após estabelecermos a conexão com o banco de dados
            // Utilizamos o método createStatement de con para criar o Statement
            Statement stm = con.createStatement();

            // Vamos executar o seguinte comando SQL :
            String SQL = "select * from cliente";

            // Definido o Statement, executamos a query no banco de dados
            ResultSet rs = stm.executeQuery(SQL);

            // O método next() informa se houve resultados e posiciona o cursor do banco
            // na próxima linha disponível para recuperação
            // Como esperamos várias linhas utilizamos um laço para recuperar os dados
            while(rs.next())
            {

               // Os métodos getXXX recuperam os dados de acordo com o tipo SQL do dado:
               String tit = rs.getString("nome_cliente");
               String aut = rs.getString("email_cliente");

               // As variáveis tit, aut e totalFaixas contém os valores retornados
               // pela query. Vamos imprimí-los

               System.out.println(tit + ": " + aut);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
