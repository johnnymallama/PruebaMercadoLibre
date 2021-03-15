package co.com.mercadolibre.test.johnnymallama;

import java.util.*;

import org.hibernate.*;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;
import com.google.gson.*;

import co.com.mercadolibre.test.johnnymallama.exception.TestMLException;
import co.com.mercadolibre.test.johnnymallama.util.HibernateUtil;

public class LambaFunctionStats implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		try {
			JsonObject resultStats = this.consultStatistic();
			response.setStatusCode(200);
			response.setBody(resultStats.toString());
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
			response.setStatusCode(500);
			response.setBody(null);
		}
		return response;
	}

	private JsonObject consultStatistic() throws TestMLException {
		JsonObject retorno = new JsonObject();
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			List<Object[]> result = session.createNativeQuery(
					"select case when valid = 1 then 'mutant' else 'human' end as tipo_adn, count(1) as cantidad from test.adn_hist group by case when valid = 1 then 'mutant' else 'human' end")
					.list();
			session.getTransaction().commit();
			int quantityMutant = 0;
			int quantityHuman = 0;
			retorno.addProperty("count_mutant_dna", quantityMutant);
			retorno.addProperty("count_human_dna", quantityHuman);
			retorno.addProperty("ratio", 0);
			for (Object[] obj : result) {
				if (obj[0].toString().equals("mutant")) {
					quantityMutant = Integer.parseInt(obj[1].toString());
					retorno.addProperty("count_mutant_dna", quantityMutant);
				} else if (obj[0].toString().equals("human")) {
					quantityHuman = Integer.parseInt(obj[1].toString());
					retorno.addProperty("count_human_dna", quantityHuman);
				}
			}
			if (quantityMutant > 0 && quantityHuman > 0) {
				retorno.addProperty("ratio", (quantityMutant / quantityHuman));
			}
		} catch (Exception e) {
			throw new TestMLException(e);
		}
		return retorno;
	}

}
