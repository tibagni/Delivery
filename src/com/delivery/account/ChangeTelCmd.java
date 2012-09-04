package com.delivery.account;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.SessionUtils;
import com.delivery.engine.command.AccountDbCommand;
import com.delivery.persistent.AccountDao;
import com.delivery.persistent.DaoManager;
import com.delivery.util.StringUtils;

public class ChangeTelCmd extends AccountDbCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		Logger.debug("ChangeTelCommand");
		final UserAccount account = SessionUtils.getLoggedUser(request.getSession());
		if (account == null || !account.isValid()) {
			mRedirect = "Login.jsp";
			return;
		}
		final String tel = request.getParameter("tel");

		if (StringUtils.isEmpty(tel)) {
			request.setAttribute("errorMsg", "Telefone inv‡lido");
			mRedirect = "user/change-tel.jsp";
			return;
		}

		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        DaoManager daoManager = new DaoManager(dataSource);
	        Boolean success = (Boolean) daoManager.transaction(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					AccountDao dao = manager.getAccountDao();
					return dao.changeAccountTel(account.getCpf(), tel);
				}
			});

	        if (success) {
	        	// Atualiza o novo telefone no objeto da sessao
	        	account.setTel(tel);
	        	request.setAttribute("okMsg", "Seu telefone foi alterado com sucesso!");
	        	mRedirect = "user/changing-tel.jsp";
	        } else {
				request.setAttribute("errorMsg", "N‹o foi poss’vel alterar o telefone");
				mRedirect = "user/change-tel.jsp";
				return;
	        }

		} catch (Exception e) {
			request.setAttribute("errorMsg", "N‹o foi poss’vel alterar o telefone");
			mRedirect = "user/change-tel.jsp";
			return;
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
