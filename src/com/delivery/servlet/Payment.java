package com.delivery.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.com.uol.pagseguro.domain.Currency;
import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.domain.ShippingType;
import br.com.uol.pagseguro.properties.PagSeguroConfig;

import com.delivery.Logger;
import com.delivery.SessionUtils;
import com.delivery.account.Address;
import com.delivery.account.UserAccount;
import com.delivery.order.OrderItem;
import com.delivery.persistent.AddressDao;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.PaymentDao;

public class Payment extends HttpServlet {
	private static final long serialVersionUID = 2988878806865251470L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handlePaymentRequest(req);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handlePaymentRequest(req);
	}

	private void handlePaymentRequest(HttpServletRequest request) {
		Logger.debug("PaymentServlet!");
		// Primeiro verifica se a sessao ainda e valida e se o pedido
		// esta realmente no estado para pagamento
		final UserAccount account = SessionUtils.getLoggedUser(request.getSession());
		if (account == null || !account.isValid()) {
			Logger.debug("User nao esta logado!");
			// TODO
			return;
		}

		final com.delivery.order.Order order = SessionUtils.getActiveOrder(request.getSession());
		if (order == null || order.getStatus() != com.delivery.order.Order.OrderStatus.WAITING_FOR_PAYMENT ||
				!order.isSuccessfullyFinalized()) {
			Logger.debug("Pedido com problemas!");
			return;
		}

		// A partr daqui temos ume sessao valida e pedido pronto para pagamento!
		final PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setCurrency(Currency.BRL);

		List<OrderItem> orderItems = order.getItems();
		for (OrderItem item : orderItems) {
			StringBuilder itemDescription = new StringBuilder();
			itemDescription.append(item.getDescription());
			Collection<String> flavours = item.getFlavours().values();
			itemDescription.append(" - " + item.getCachedSizeName() + " -");
			boolean first = true;
			for (String f : flavours) {
				if (first) {
					first = false;
				} else {
					itemDescription.append(",");
				}
				itemDescription.append(" " + f);
			}
			itemDescription.append("");
			//TODO opcionais

			paymentRequest.addItem(String.valueOf(item.getId()),
								   itemDescription.toString(),
								   new Integer(1),
								   new BigDecimal(item.getPrice()),
								   null,
								   null);
		}

		String tel = account.getTel(); // (##) ####-####
		tel = tel.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", ""); // ##########
		String areaCode = tel.substring(0, 2);
		tel = tel.substring(2);
		paymentRequest.setSender(account.getName(), account.getEmail(), areaCode, tel);
		paymentRequest.setShippingType(ShippingType.NOT_SPECIFIED);

		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        DaoManager daoManager = new DaoManager(dataSource);
	        Boolean success = (Boolean) daoManager.transaction(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					com.delivery.order.Payment payment = new com.delivery.order.Payment();
					payment.setOrderId(order.getId());
					List<com.delivery.order.Payment> toInsert = new ArrayList<com.delivery.order.Payment>();
					toInsert.add(payment);

					PaymentDao pDao = manager.getPaymentDao();

					payment.setId(pDao.getLastSavedId());
					payment.generateReferenceFromId();

					int[] result = pDao.save(toInsert);

					if (result == null || result.length == 0) {
						manager.cancelTransaction();
					}

					// Seta a reference recem gerada
					paymentRequest.setReference(payment.getReference());

					Address query = new Address();
					query.setId(order.getAddressId());

					AddressDao aDao = manager.getAddressDao();
					List<Address> addrs = aDao.get(query);

					if (addrs == null || addrs.size() == 0) {
						Logger.wtf("Cade o endereco?!?!");
						manager.cancelTransaction();
					}
					Address addr = addrs.get(0);

					// Seta o endereco de entrega
					paymentRequest.setShippingAddress("BRA",
													   addr.getUF(),
													   addr.getCity(),
													   addr.getNeighborhood(),
													   addr.getZipCode(),
													   addr.getStreet(),
													   String.valueOf(addr.getNumber()),
													   addr.getCompl());

					return true;
				}
	        });

	        if (success) {
	        	URL paymentURL = paymentRequest.register(PagSeguroConfig.getAccountCredentials());
	        	Logger.debug(paymentURL.toString());
	        	SessionUtils.setActiveOrder(request.getSession(), null);
	        } else {
	        	Logger.debug("sei la, fodeu!");
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
