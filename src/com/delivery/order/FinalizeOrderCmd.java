package com.delivery.order;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.SessionConstants;
import com.delivery.engine.command.OrderCommand;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;
import com.delivery.persistent.OrderItemDao;
import com.delivery.persistent.OrderItemFlavourRelDao;
import com.delivery.persistent.OrderItemOptionlRelDao;

public class FinalizeOrderCmd extends OrderCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

			final Order order = (Order) request.getSession().getAttribute(SessionConstants.ORDER);
	        if (order == null) {
	        	// Nao ha pedidos nesta sessao. Talvez a sessao tenha expirado
	        	// talvez simplesmente na haja pedidos
	        	//TODO redirect
	        	return;
	        }

	        String addressIdStr = request.getParameter("selectedAddress");
	        int addressId;
	        try {
	        	addressId = Integer.parseInt(addressIdStr);
	        } catch (NumberFormatException nfe) {
	        	// TODO
	        	return;
	        }

	        order.setAddressId(addressId);

	        // Antes de mais nada o pedido precisa ser fechado
	        // Para que nao possam ser adicionados nem retirados
	        // itens de pedido a partit de agora
	        if (!order.isClosed()) {
	        	order.close();
	        }

	        // TODO recupera os dados da conta do usuario da sessao e preenche o objeto order
	        // Quando o controle de sessao estiver implementado
	        order.setUserAccountId(22114393810L); // TODO remover implementacao hard-coded

	        // Quando o pedido e inserido, ainda nao ha pagemeto entao o primeiro status
	        // e o de esperando por pagamento
	        order.setStatus(Order.OrderStatus.WAITING_FOR_PAYMENT);

	        /* Temos que persistir o pedido no banco de dados
	         * 1 - Cria um registro para o pedido na tabela de pedidos
	         *     1.a - Recupera as informacoes do usuario da sessao
	         *     1.b - Recupera o endereco de entrega do usuario (na sessao)
	         * 3 - Adicionar cada item de pedido no banco de dados
	         * 4 - Preencher os relacionamentso entre os itens de pedido e sabores
	         * 5 - Preencher os relacionamentos entre os itens de pedido e opcionais
	         *
	         * Se qualquer operacao falhar, fazemos o rollback de tudo!!
	         *
	         * Se qualquer operacao falhar (inserir pedido, item de pedido ou qualquer registro
	         * nas tabelas de relacionament) toda a transacao deve ser cancelada - ROLLBACK
	         *
	         * O entregador sera atribuido ao pedido posteriormente, quando o mesmo estiver
	         * no estado de "READY_TO_DELIVER"
	         * */

	        final FinalizeOrderResult result = new FinalizeOrderResult();
	        DaoManager daoManager = new DaoManager(dataSource);
	        daoManager.transaction(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {

					// 1 - Insere os dados do pedido no banco de dados
					ArrayList<Order> orderToInsert = new ArrayList<Order>();
					orderToInsert.add(order);
					OrderDao orderDao = manager.getOrderDao();

					int saved[] = orderDao.save(orderToInsert);
					if (saved == null || saved.length == 0) {
						result.result = FinalizeOrderResult.ORDER_SAVE_FAILED;
						manager.cancelTransaction();
					}

					// 2 - Atualiza o ID do pedido para as operacoes posteriores
					order.setId(orderDao.getLastSavedId());
					long orderId = order.getId();

					// 3 - Atualiza os campos dos itens de pedido antes de inserir no banco
					List<OrderItem> orderItemsToInsert = order.getItems();
					int orderItemId = 1;
					for (OrderItem item : orderItemsToInsert) {
						item.setOrderId(orderId);
						// O id do item de pedido e relativo ao pedido.
						// A chave primaria e composta por (id do pedido,  id do item)
						// O id do item e sequencial para cada pedido, comecando em 1
						item.setId(orderItemId++);
					}
					OrderItemDao orderItemDao = manager.getOrderItemDao();

					saved = orderItemDao.save(orderItemsToInsert);
					if (saved == null || saved.length != orderItemsToInsert.size()) {
						// Houve algum erro e nao foram inseridos todos os itens. Nesse caso
						// e melhor cancelar a transacao
						result.result = FinalizeOrderResult.ORDER_ITEM_SAVE_FAILED;
						manager.cancelTransaction();
					}

					// 4 - Para cada item de pedido. Precisamos adicionar os relacionamentos com
					// opcionais e sabores.
					ArrayList<OrderItemRelOptional> optionalRels = new ArrayList<OrderItemRelOptional>();
					ArrayList<OrderItemRelFlavour> flavourRels = new ArrayList<OrderItemRelFlavour>();
					for (OrderItem item : orderItemsToInsert) {
						Map<Integer, String> optionalsMap = item.getOptionals();
						// Precisamos dessa verificacao aqui porque um produto nao precisa,
						// obrigatoriamente, ter opcionais
						if (optionalsMap != null && optionalsMap.size() > 0) {
							Set<Integer> optionalIds = item.getOptionals().keySet();

							// Adiciona os relacionamentos com opcional
							for (Integer optionalId : optionalIds) {
								OrderItemRelOptional rel = new OrderItemRelOptional();
								rel.setOptionalId(optionalId);
								rel.setOrderId(orderId);
								rel.setOrderItemId(item.getId());

								optionalRels.add(rel);
							}
						}

						// Todo produto deve ter pelo menos um sabor, entao aqui nunca deveria
						// ser NULL (como pode acontecer com os opcionais)
						Set<Integer> flavourIds = item.getFlavours().keySet();
						// Adciona os relacionamentos com sabor
						for (Integer flavourId : flavourIds) {
							OrderItemRelFlavour rel = new OrderItemRelFlavour();
							rel.setFlavourId(flavourId);
							rel.setOrderId(orderId);
							rel.setOrderItemId(item.getId());

							flavourRels.add(rel);
						}
					}

					OrderItemOptionlRelDao optionalRel = manager.getOrderItemOptionlRelDao();
					saved = optionalRel.save(optionalRels);
					if (saved != null && saved.length != optionalRels.size()) {
						result.result = FinalizeOrderResult.OPTIONAL_REL_SAVE_FAILED;
						manager.cancelTransaction();
					}

					OrderItemFlavourRelDao flavourRel = manager.getOrderItemFlavourRelDao();
					saved = flavourRel.save(flavourRels);
					if (saved == null || saved.length != flavourRels.size()) {
						result.result = FinalizeOrderResult.FLAVOUR_REL_SAVE_FAILED;
						manager.cancelTransaction();
					}

					result.result = FinalizeOrderResult.SUCCESS;
					return null;
				}
			});

	        if (result.result == FinalizeOrderResult.SUCCESS) {
	        	// O pedido ja foi inserido no banco de dados.
	        	// nao precisamos mais te-lo na sessao!
	        	request.getSession().removeAttribute(SessionConstants.ORDER);
	        	request.setAttribute("OrderToPay", order.getId());
	        	mRedirect = "RequestPayment.jsp";
	        }

		} catch (NamingException e) {
            Logger.error("NamingException", e);
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

	private class FinalizeOrderResult {
		int result;

		static final int SUCCESS = 0;
		static final int ORDER_SAVE_FAILED = 1;
		static final int ORDER_ITEM_SAVE_FAILED = 2;
		static final int OPTIONAL_REL_SAVE_FAILED = 3;
		static final int FLAVOUR_REL_SAVE_FAILED = 4;
	}

}
