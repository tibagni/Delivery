package com.delivery.order;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.SessionUtils;
import com.delivery.account.Address;
import com.delivery.account.UserAccount;
import com.delivery.engine.command.OrderCommand;
import com.delivery.persistent.AddressDao;
import com.delivery.persistent.DaoManager;

public class SelectOrderAddressCmd extends OrderCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

			UserAccount user = SessionUtils.getLoggedUser(request.getSession());
			if (user == null) {
				mRedirect = "Login.jsp";
				return;
			}

			final long accountId = user.getCpf();

			DaoManager daoManager = new DaoManager(dataSource);
			@SuppressWarnings("unchecked")
			List<Address> addresses = (List<Address>) daoManager.execute(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					AddressDao dao = manager.getAddressDao();
					Address param = new Address();
					param.setUserAccountId(accountId);
					return dao.get(param);
				}
			});

			if (addresses == null || addresses.size() == 0) {
				// Nao deveria acontecer. Mas se acontecer devemos mandar o usuario
				// para a pagina de cadastro de novo endereco
				// (Em addressSelector tem um link ara cadasro de endereco)
				mRedirect = "order/AddressSelector.jsp";
			} else {
				// Tudo ok, adiciona a lista de enderecos no request
				// E manda o usuario para o addressSelector
				request.setAttribute("AddressList", addresses);
				mRedirect = "order/AddressSelector.jsp";
			}

		} catch (NamingException e) {
            Logger.error("NamingException", e);
		}

	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
