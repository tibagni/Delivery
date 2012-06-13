package com.delivery.menu.editor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.Optional;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OptionalDao;
import com.delivery.util.StringUtils;

public class AddOptionalCmd extends MenuEditorCommand {

    private String mRedirect;

    private static final String OPTIONAL_NAME_PATTERN  = "produto\\[opcionais\\]\\[([0-9]*)\\]\\[nome\\]";
    private static final String OPTIONAL_PRICE_FORMAT  = "produto[opcionais][%d][preco]";

    @Override
    public void execute(HttpServletRequest request) {
        try {
            // Get DataSource
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/deliveryDB");

            final int productId = Integer.parseInt(request.getParameter("prodId"));
            final ArrayList<Optional> optionals = getOptionalsFromParameters(request.getParameterMap(), productId);

            DaoManager manager = new DaoManager(dataSource);
            boolean allInserted = (Boolean) manager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    OptionalDao dao = manager.getOptionalDao();
                    int[] result = dao.save(optionals);

                    // Vamos retornar o numero de registros que foram inseridos!
                    int inserted = 0;
                    if (result != null && result.length > 0) {
                        inserted = result.length;
                    }
                    return inserted == optionals.size();
                }
            });

            if (allInserted) {
                Logger.debug("Os opcionais foram salvos com sucesso!");
                // Vamos mandar o usuario para a pagina final com uma mensagem de sucesso!
                StringBuilder finalMsg = new StringBuilder();
                finalMsg.append("Opcionais adicionados com sucesso!");
                finalMsg.append(" [");
                boolean first = true;
                for (Optional o : optionals) {
                    if (!first) {
                        finalMsg.append(", ");
                    } else {
                        first = false;
                    }

                    finalMsg.append(o.getName());
                }
                finalMsg.append("]");
                request.setAttribute("finalMsg", finalMsg.toString());
                // Reforcar a passagem do id de produto pela requisicao para que ele possa ser usado
                // ao adicionar novos sabores ou opcionais
                request.setAttribute("prodId", productId);
                mRedirect = "editor/whats-next.jsp";
            } else {
                // TODO Mostrar ao usuario que nem todos os opcionais foram inseridos!
            }


        } catch (NamingException e) {
            Logger.error("NamingException", e);
        } catch (NumberFormatException e) {
            Logger.error("Opcional sem produto???", e);
            throw new RuntimeException("Id de produto inválido");
        }
    }

    private ArrayList<Optional> getOptionalsFromParameters(Map<String, String[]> parameters, int prodId) {
        ArrayList<Optional> optionals = new ArrayList<Optional>();

        Iterator<String> it = parameters.keySet().iterator();
        while (it.hasNext()) {
            String nameKey = it.next();
            if (StringUtils.matches(OPTIONAL_NAME_PATTERN, nameKey)) {
                String nameVal = parameters.get(nameKey)[0];

                Pattern p = Pattern.compile(OPTIONAL_NAME_PATTERN);
                Matcher m = p.matcher(nameKey);
                m.find(); // Sempre vai ser verdade se passou pelo if
                int index = Integer.parseInt(m.group(1)); // Nao sera lancada exception se passou no if!
                String priceKey = String.format(OPTIONAL_PRICE_FORMAT, index);
                String priceVal = parameters.get(priceKey)[0];

                if (!StringUtils.isEmpty(nameVal) && !StringUtils.isEmpty(priceVal)) {
                    Logger.debug("Opcional: [" + nameKey + " = " + nameVal + "]" +
                            "[" + priceKey + " = " + priceVal + "]");
                    Optional optional = new Optional();
                    optional.setProductId(prodId);
                    optional.setName(nameVal);
                    try {
                        // Para evitar problemas, vamos normalizar a string
                        // trocando virgulas por pontos ja que, para o usuario, nao faz diferenca
                        priceVal = priceVal.replace(',', '.');
                        optional.setPrice(Double.parseDouble(priceVal));
                    } catch (NumberFormatException e) {
                        Logger.error("Erro ao parsear o opcional!", e);
                        // Houve um problema com este opcional, continua para tentar
                        // adicionar os proximos (se houverem).
                        continue;
                    }
                    optionals.add(optional);
                }
            }
        }
        return optionals;
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
