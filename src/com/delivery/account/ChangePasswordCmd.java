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

public class ChangePasswordCmd extends AccountDbCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		Logger.debug("ChangePasswordCommand");
		final UserAccount account = SessionUtils.getLoggedUser(request.getSession());
		if (account == null || !account.isValid()) {
			mRedirect = "Login.jsp";
			return;
		}

		final String currentPwd = request.getParameter("current");
		final String newPwd = request.getParameter("new");

		if (StringUtils.isEmpty(currentPwd) || !account.getPassword().equals(currentPwd)) {
			request.setAttribute("errorMsg", "Senha atual inv‡lida");
			mRedirect = "user/change-pwd.jsp";
			Logger.debug("Senha atual inv‡lida");
			return;
		}

		if (StringUtils.isEmpty(newPwd)) {
			request.setAttribute("errorMsg", "Nova senha inv‡lida");
			mRedirect = "user/change-pwd.jsp";
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
					return dao.changeAccountPwd(account.getCpf(), newPwd);
				}
			});

	        if (success) {
	        	// Atualiza o novo password no objeto da sessao
	        	account.setPassword(newPwd);
	        	request.setAttribute("okMsg", "Sua senha foi alterada com sucesso!");
	        	mRedirect = "OkMessage.jsp";
	        } else {
				request.setAttribute("errorMsg", "N‹o foi poss’vel alterar a senha");
				mRedirect = "user/change-pwd.jsp";
				return;
	        }

		} catch (Exception e) {
			request.setAttribute("errorMsg", "N‹o foi poss’vel alterar a senha");
			mRedirect = "user/change-pwd.jsp";
			return;
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
