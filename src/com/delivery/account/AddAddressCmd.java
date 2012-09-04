package com.delivery.account;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.SessionUtils;
import com.delivery.engine.command.AccountDbCommand;
import com.delivery.persistent.AddressDao;
import com.delivery.persistent.DaoManager;

public class AddAddressCmd extends AccountDbCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        // Dados do endereco
	        String addrZip 			= request.getParameter("cep");
	        String addrStreet 		= request.getParameter("rua");
	        String addrNumber 		= request.getParameter("num");
	        String addrCompl 		= request.getParameter("comp");
	        String addrNeighborhood = request.getParameter("bair");
	        String addrCity 		= request.getParameter("cidade");
	        String addrUf 			= request.getParameter("uf");

	        final Address newAddress = new Address();
	        newAddress.setZipCode(addrZip);
	        newAddress.setStreet(addrStreet);
	        newAddress.setNumber(Integer.parseInt(addrNumber));
	        newAddress.setCompl(addrCompl);
	        newAddress.setNeighborhood(addrNeighborhood);
	        newAddress.setCity(addrCity);
	        newAddress.setUF(addrUf);

	        UserAccount account = SessionUtils.getLoggedUser(request.getSession());
	        if (account == null || !account.isValid()) {
	        	mRedirect = "Login.jsp";
	        	return;
	        }

	        final long userCPF = account.getCpf();

	        DaoManager daoManager = new DaoManager(dataSource);
	        Address addr = (Address) daoManager.transaction(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					newAddress.setUserAccountId(userCPF);
					ArrayList<Address> addrToInsert = new ArrayList<Address>();
					AddressDao addrDao = manager.getAddressDao();

					addrToInsert.add(newAddress);
					int[] saved = addrDao.save(addrToInsert);
					if (saved == null || saved.length == 0) {
						manager.cancelTransaction();
					}
					return newAddress;
				}
			});

			if (addr != null) {
				request.setAttribute("cmd", "SelectOrderAddress");
				mRedirect = "/Order";
			} else {
				request.setAttribute("errorMsg", "Não foi possível adicionar novo endereço");
				mRedirect = "ErrorMessage.jsp";
			}

		} catch (NamingException e) {
            Logger.error("NamingException", e);
		} catch (NumberFormatException nfe) {
			Logger.error("Erro ao inserir novo endereco", nfe);
		}

	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
