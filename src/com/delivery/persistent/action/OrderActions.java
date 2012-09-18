package com.delivery.persistent.action;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.account.Address;
import com.delivery.account.UserAccount;
import com.delivery.menu.Flavour;
import com.delivery.menu.Optional;
import com.delivery.menu.Product;
import com.delivery.menu.ProductSize;
import com.delivery.order.Order;
import com.delivery.order.OrderItem;
import com.delivery.order.OrderItemRelFlavour;
import com.delivery.order.OrderItemRelOptional;
import com.delivery.order.Payment;
import com.delivery.persistent.AccountDao;
import com.delivery.persistent.AddressDao;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.FlavourDao;
import com.delivery.persistent.OptionalDao;
import com.delivery.persistent.OrderDao;
import com.delivery.persistent.OrderItemDao;
import com.delivery.persistent.OrderItemFlavourRelDao;
import com.delivery.persistent.OrderItemOptionlRelDao;
import com.delivery.persistent.PaymentDao;
import com.delivery.persistent.ProductDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.StringUtils;

public class OrderActions {

	public static Order queryDetailedOrder(final long orderId) {
		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        Logger.debug("OrderDetail: id=" + orderId);

	        DaoManager daoManager = new DaoManager(dataSource);
	        Order order = (Order) daoManager.execute(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					OrderDao orderDao = manager.getOrderDao();
					Order query = new Order();
					query.setId(orderId);

					List<Order> orderResult = orderDao.get(query);
					if (orderResult == null || orderResult.size() == 0) {
						Logger.error("Pedido nao encontrado");
						return null;
					}

					Order order = orderResult.get(0);

					AddressDao addressDao = manager.getAddressDao();
					AccountDao accountDao = manager.getAccountDao();
					Address addressQuery = new Address();
					addressQuery.setId(order.getAddressId());
					UserAccount uaQuery = new UserAccount();
					uaQuery.setCpf(order.getUserAccountId());

					List<Address> addrL = addressDao.get(addressQuery);
					List<UserAccount> uaL = accountDao.get(uaQuery);

					if (addrL == null || uaL == null) return null;
					if (addrL.size() == 0 || uaL.size() == 0) {
						Logger.wtf("Pedido sem endereco");
						return null;
					}
					UserAccount ua = uaL.get(0);
					Address addr = addrL.get(0);
					String cachedAddr;
					if (StringUtils.isEmpty(addr.getCompl())) {
						cachedAddr = String.format("R: %s, %d - %s. %s-%s - Cep: %s", addr.getStreet(),
								addr.getNumber(), addr.getNeighborhood(), addr.getCity(), addr.getUF(), addr.getZipCode());
					} else {
						cachedAddr = String.format("R: %s, %d (%s) - %s. %s-%s - Cep: %s", addr.getStreet(),
								addr.getNumber(), addr.getCompl(), addr.getNeighborhood(), addr.getCity(), addr.getUF(), addr.getZipCode());
					}

					order.setAddress(addr);
					order.setCachedAddress(cachedAddr);
					order.setCachedUserName(ua.getName() + " - " + ua.getEmail());

					OrderItemDao oIDao = manager.getOrderItemDao();
					OrderItem oIQuery = new OrderItem();
					oIQuery.setOrderId(order.getId());

					List<OrderItem> items = oIDao.get(oIQuery);

					if (items == null || items.size() == 0) {
						Logger.wtf("Sem itens no pedido");
						return null;
					}

					for (OrderItem item : items) {
						// Primeiro, adiciona o item ao pedido
						order.addItem(item);

						// Agora, vamos preencher todos os dados dos itens de pedido
						OrderItemFlavourRelDao itemRelFlavDao = manager.getOrderItemFlavourRelDao();
						OrderItemRelFlavour itemRelFlavQuery = new OrderItemRelFlavour();

						itemRelFlavQuery.setOrderId(item.getOrderId());
						itemRelFlavQuery.setOrderItemId(item.getId());

						List<OrderItemRelFlavour> itemRelFlavResult = itemRelFlavDao.get(itemRelFlavQuery);
						FlavourDao flavDao = manager.getFlavourDao();
						Flavour flavQuery = new Flavour();
						for (OrderItemRelFlavour rel : itemRelFlavResult) {
							flavQuery.setId(rel.getFlavourId());
							List<Flavour> result = flavDao.get(flavQuery);
							Flavour resultFlav = result.get(0);
							item.addNewFlavour(resultFlav.getId(), resultFlav.getName());
						}

						OrderItemOptionlRelDao itemRelOptionalDao = manager.getOrderItemOptionlRelDao();
						OrderItemRelOptional itemRelOptionalQuery = new OrderItemRelOptional();

						itemRelOptionalQuery.setOrderId(item.getOrderId());
						itemRelOptionalQuery.setOrderItemId(item.getId());

						List<OrderItemRelOptional> itemRelOptionalResult = itemRelOptionalDao.get(itemRelOptionalQuery);
						OptionalDao optDao = manager.getOptionalDao();
						Optional optQuery = new Optional();
						for (OrderItemRelOptional rel : itemRelOptionalResult) {
							flavQuery.setId(rel.getOptionalId());
							List<Optional> result = optDao.get(optQuery);
							Optional resultOpt = result.get(0);
							item.addNewOptional(resultOpt.getId(), resultOpt.getName());
						}

						// Salva o produto para podermos pegar informcoes sobre ele
						ProductDao productDao = manager.getProductDao();
						Product productQuery = new Product();
						productQuery.setId(item.getProductId());

						Product product = productDao.get(productQuery).get(0);
						item.setDescription(product.getName());


						// Salva tambem o nome do tamanho em cache (para ser exibido na UI)
						ProductSizeDao sizeDao = manager.getProductSizeDao();
						ProductSize sizeQuery = new ProductSize();
						sizeQuery.setId(item.getSize());

						ProductSize size = sizeDao.get(sizeQuery).get(0);
						item.setCachedSizeName(size.getName());
					}

					// Recupera tambem o pagamento associado ao pedido
					PaymentDao paymentDao = manager.getPaymentDao();
					Payment paymentQuery = new Payment();
					paymentQuery.setOrderId(orderId);
					List<Payment> paymentResult = paymentDao.get(paymentQuery);
					if (paymentResult != null && paymentResult.size() == 1) {
						order.setPayment(paymentResult.get(0));
					} else {
						Logger.wtf("WTF! sem pagamento associado ao pedido?");
					}

					return order;
				}
			});

	        return order;
		} catch (NamingException e) {
	        Logger.error("NamingException", e);
	        return null;
	    }
	}

	public static void setOrderInManualPayment(final long orderId, final long paymentId, final double value) throws NamingException {
		// Get DataSource
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource dataSource = (DataSource) envContext.lookup("jdbc/deliveryDB");


		DaoManager daoManager = new DaoManager(dataSource);
		daoManager.transaction(new DaoManager.DaoCommand() {
			@Override
			public Object execute(DaoManager manager) throws SQLException {
				PaymentDao dao = manager.getPaymentDao();
				dao.changePaymentStatus(paymentId, Payment.PaymentStatus.MANUAL);
				dao.setPaymentManualValue(paymentId, value);

				// Ja que o pagamento sera realizado no momento da entrega
				// devemos mudar o status do pedido para "na fila para preparo"
				OrderDao oDao = manager.getOrderDao();
				oDao.changeOrderStatus(Order.OrderStatus.WAITING_FOR_PAYMENT,
						Order.OrderStatus.READY_TO_PREPARE, orderId);
				return null;
			}
		});
	}

	public static void setOrderPaymentCompleted(final long paymentId) throws NamingException {
		// Get DataSource
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource dataSource = (DataSource) envContext.lookup("jdbc/deliveryDB");


		DaoManager daoManager = new DaoManager(dataSource);
		daoManager.transaction(new DaoManager.DaoCommand() {
			@Override
			public Object execute(DaoManager manager) throws SQLException {
				Payment query = new Payment();
				query.setId(paymentId);
				PaymentDao dao = manager.getPaymentDao();
				List<Payment> p = dao.get(query);
				if (p == null || p.size() != 1) {
					manager.cancelTransaction();
				}
				Payment payment = p.get(0);
				dao.changePaymentStatus(paymentId, Payment.PaymentStatus.FINALIZED);

				// Atualiza o status do pedido j‡ que o pagamento foi validado!
				OrderDao oDao = manager.getOrderDao();
				oDao.changeOrderStatus(Order.OrderStatus.WAITING_FOR_PAYMENT,
						Order.OrderStatus.READY_TO_PREPARE, payment.getOrderId());

				return null;
			}
		});
	}
}
