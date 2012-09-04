package com.delivery.pageloader.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.account.Address;
import com.delivery.account.UserAccount;
import com.delivery.engine.command.AdminPageLoaderCommand;
import com.delivery.order.Order;
import com.delivery.persistent.AccountDao;
import com.delivery.persistent.AddressDao;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;

public class OrderManagerLoader extends AdminPageLoaderCommand {
	private String mRedirect;

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        Logger.debug("Preparing to load OrderManagerLoader");
        mRedirect = "order/order-manager.jsp";
		try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            DaoManager daoManager = new DaoManager(dataSource);
            @SuppressWarnings("unchecked")
			List<Order> orders = (List<Order>) daoManager.execute(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					// Primeiro vamos pegar a lista de pedidos abertos
					Order query = new Order();
					query.addExcludeStatusToQuery(Order.OrderStatus.FINISHED);
					query.addExcludeStatusToQuery(Order.OrderStatus.CANCELLED);
					OrderDao orderDao = manager.getOrderDao();
					List<Order> orders = orderDao.get(query);

					// Agora, para cada pedido, vamos preencher os dados do usuario
					// (que fez o pedido) e endereço (fora os dados que ja foram recuperados)
					// Os detalhes do pedido (items, sabores, opcionais ...) serao recuperados
					// em outro comando. Individual para cada pedido. Não vale a pena fazer a query agora
					// pode demorar muito tempo. Melhor fazer quando for realmente necessario!
					AddressDao addressDao = manager.getAddressDao();
					AccountDao accountDao = manager.getAccountDao();
					for (Order order : orders) {
						Address addressQuery = new Address();
						addressQuery.setId(order.getAddressId());
						UserAccount uaQuery = new UserAccount();
						uaQuery.setCpf(order.getUserAccountId());

						List<Address> addrL = addressDao.get(addressQuery);
						List<UserAccount> uaL = accountDao.get(uaQuery);

						if (addrL == null || uaL == null) return null;
						if (addrL.size() == 0 || uaL.size() == 0) {
							return null;
						}

						// onsultamos o usuario e o endereco para "cachear" os enderecos e nomes do usuarios
						// para que eles sejam exibidos no painel de pedidos
						UserAccount ua = uaL.get(0);
						Address addr = addrL.get(0);
						order.setCachedAddress(addr.getStreet() + ", " + addr.getNeighborhood());
						order.setCachedUserName(ua.getName() + " (" + ua.getEmail() + ")");
					}

					return orders;
				}
			});

            ArrayList<Order> waitingPayment = new ArrayList<Order>();
            ArrayList<Order> readyToPrepare = new ArrayList<Order>();
            ArrayList<Order> preparing = new ArrayList<Order>();
            ArrayList<Order> readyToDeliver = new ArrayList<Order>();
            ArrayList<Order> delivering = new ArrayList<Order>();

            for(Order o : orders) {
            	switch(o.getStatus()) {
            		case Order.OrderStatus.WAITING_FOR_PAYMENT:
            			waitingPayment.add(o);
            			break;
                	case Order.OrderStatus.READY_TO_PREPARE:
                		readyToPrepare.add(o);
                		break;
                	case Order.OrderStatus.PREPARING:
                		preparing.add(o);
                		break;
                	case Order.OrderStatus.READY_TO_DELIVER:
                		readyToDeliver.add(o);
                		break;
                	case Order.OrderStatus.DELIVERING:
                		delivering.add(o);
                		break;
            	}
            }


            req.setAttribute("waitingPayment", waitingPayment);
            req.setAttribute("readyToPrepare", readyToPrepare);
            req.setAttribute("preparing", preparing);
            req.setAttribute("readyToDeliver", readyToDeliver);
            req.setAttribute("delivering", delivering);
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
