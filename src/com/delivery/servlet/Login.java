package com.delivery.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.SessionUtils;
import com.delivery.account.UserAccount;
import com.delivery.admin.Admin;
import com.delivery.persistent.AccountDao;
import com.delivery.persistent.AdminDao;
import com.delivery.persistent.DaoManager;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 524109749251975454L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

			String user = req.getParameter("user");
			String password = req.getParameter("password");

			final UserAccount uA = new UserAccount();
			uA.setEmail(user);
			uA.setPassword(password);

			final Admin adm = new Admin();
			adm.setUserName(user);
			adm.setPassword(password);

			DaoManager manager = new DaoManager(dataSource);
			Object account = manager.execute(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					AccountDao dao = manager.getAccountDao();
					List<UserAccount> queryResult = dao.get(uA);

					if (queryResult != null && queryResult.size() == 1) {
						// Logado como usuario!
						return queryResult.get(0);
					}

					// Verifica se e login de administrador
					AdminDao aDao = manager.getAdminDao();
					List<Admin> adminResult = aDao.get(adm);
					if (!adm.isValid()) return null;

					if (adminResult != null && adminResult.size() == 1) {
						// Logado como administrador!
						return adminResult.get(0);
					}

					return null;
				}
			});

			if (account instanceof UserAccount) {
				// Usuario logado. Adiciona a sessao e carrega a pagina
				// inicial
				SessionUtils.setLoggedUser(req.getSession(), (UserAccount) account);
				req.setAttribute("action", "login");
				RequestDispatcher dispatcher = req.getRequestDispatcher("loging-in.jsp");
	            dispatcher.forward(req, resp);
			} else if (account instanceof Admin) {
				// Administrador logado. Adiciona a sessao e carrega a pagina
				// inicial
				SessionUtils.setLoggedAdmin(req.getSession(), (Admin) account);
				req.setAttribute("action", "login");
				RequestDispatcher dispatcher = req.getRequestDispatcher("loging-in.jsp");
	            dispatcher.forward(req, resp);
			} else {
				// Usuario nao foi logado. Carrega pagina de erro!
				req.setAttribute("loginMsg", "Usu‡rio ou senha incorretos!");
				req.setAttribute("defaultPage", "login");
				RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
	            dispatcher.forward(req, resp);
			}

		} catch (NamingException e) {
			// Usuario nao foi logado. Carrega pagina de erro!
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		if ("logout".equalsIgnoreCase(action)) {
			SessionUtils.clearSession(req.getSession()); //Desloga todo mundo e lipa a sessao
			req.setAttribute("action", "logout");
			RequestDispatcher dispatcher = req.getRequestDispatcher("loging-in.jsp");
            dispatcher.forward(req, resp);
		}
	}

}
