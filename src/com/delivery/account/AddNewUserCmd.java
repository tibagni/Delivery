package com.delivery.account;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.AccountDbCommand;
import com.delivery.persistent.AccountDao;
import com.delivery.persistent.AddressDao;
import com.delivery.persistent.DaoManager;
import com.delivery.util.StringUtils;

public class AddNewUserCmd extends AccountDbCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		try {
        // Get DataSource
        Context initContext  = new InitialContext();
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

        // Dados pessoais
        String userName 	= request.getParameter("nome"); // Nome do usuario, nao e o login. O login e o email!
        String userCpf 		= request.getParameter("cpf");
        String userTel      = request.getParameter("tel");
        String userEmail    = request.getParameter("email");
        String userPassword = request.getParameter("pwd");

        // Dados do endereco
        String addrZip 			= request.getParameter("cep");
        String addrStreet 		= request.getParameter("rua");
        String addrNumber 		= request.getParameter("num");
        String addrCompl 		= request.getParameter("comp");
        String addrNeighborhood = request.getParameter("bair");
        String addrCity 		= request.getParameter("cidade");
        String addrUf 			= request.getParameter("uf");

        final UserAccount newAccount = new UserAccount();
        newAccount.setName(userName);
        newAccount.setCpf(userCpf);
        newAccount.setTel(userTel);
        newAccount.setEmail(userEmail);
        newAccount.setPassword(userPassword);

        checkUserAccount(newAccount);

        final Address newAddress = new Address();
        newAddress.setZipCode(addrZip);
        newAddress.setStreet(addrStreet);
        newAddress.setNumber(Integer.parseInt(addrNumber));
        newAddress.setCompl(addrCompl);
        newAddress.setNeighborhood(addrNeighborhood);
        newAddress.setCity(addrCity);
        newAddress.setUF(addrUf);

        checkAddress(newAddress);

        DaoManager daoManager = new DaoManager(dataSource);
        daoManager.transaction(new DaoManager.DaoCommand() {
			@Override public Object execute(DaoManager manager) throws SQLException {
				ArrayList<UserAccount> accountToInsert = new ArrayList<UserAccount>();
				AccountDao accDao = manager.getAccountDao();

				accountToInsert.add(newAccount);
				int[] saved = accDao.save(accountToInsert);
				if (saved == null || saved.length == 0) {
					manager.cancelTransaction();
				}

				newAddress.setUserAccountId(newAccount.getCpf());
				ArrayList<Address> addrToInsert = new ArrayList<Address>();
				AddressDao addrDao = manager.getAddressDao();

				addrToInsert.add(newAddress);
				saved = addrDao.save(addrToInsert);
				if (saved == null || saved.length == 0) {
					manager.cancelTransaction();
				}


				return null;
			}
		});

		} catch (NamingException e) {
            Logger.error("NamingException", e);
		} catch (NumberFormatException nfe) {
			Logger.error("Erro ao inserir novo usuario", nfe);

		}

	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

	private void checkUserAccount(UserAccount acc) {
		if (acc.getCpf() <= 0 ||
			StringUtils.isEmpty(acc.getEmail()) ||
			StringUtils.isEmpty(acc.getPassword()) ||
			StringUtils.isEmpty(acc.getTel()) ||
			StringUtils.isEmpty(acc.getName())) {

			throw new IllegalArgumentException("User account nao foi corretamente preenchida! - Abortando...");
		}
	}

	private void checkAddress(Address addr) {
		if (StringUtils.isEmpty(addr.getCity()) ||
			StringUtils.isEmpty(addr.getNeighborhood()) ||
			StringUtils.isEmpty(addr.getStreet()) ||
			StringUtils.isEmpty(addr.getUF()) ||
			StringUtils.isEmpty(addr.getZipCode())) {

			throw new IllegalArgumentException("Endereo nao foi corretamente preenchida! - Abortando...");
		}

	}

}
