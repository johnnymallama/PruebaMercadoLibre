package co.com.mercadolibre.test.johnnymallama;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.*;

import co.com.mercadolibre.test.johnnymallama.entity.AdnHist;
import co.com.mercadolibre.test.johnnymallama.exception.TestMLException;
import co.com.mercadolibre.test.johnnymallama.util.HibernateUtil;

public class LambdaFunctionHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final int COINCIDENCE = 3;
	private int lengthSearch = 0;

	public int getLengthSearch() {
		return lengthSearch;
	}

	public void setLengthSearch(int lengthSearch) {
		this.lengthSearch = lengthSearch - COINCIDENCE;
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		try {
			context.getLogger().log("input = " + input.getBody());
			JsonObject element = JsonParser.parseString(input.getBody()).getAsJsonObject();
			JsonArray elementArray = element.get("dna").getAsJsonArray();
			boolean result = this.isMutant(this.createArray(elementArray));

			if (result) {
				response.setStatusCode(200);
				response.setBody(String.valueOf(result));
			} else {
				response.setStatusCode(403);
				response.setBody(String.valueOf(result));
			}
			this.saveAdn(element, result);
		} catch (TestMLException e) {
			context.getLogger().log(e.getMessage());
			response.setStatusCode(500);
			response.setBody(String.valueOf(false));
		}
		return response;
	}

	private String[] createArray(JsonArray elementArray) {
		String[] retorno = new String[elementArray.size()];
		for (int i = 0; i < elementArray.size(); i++) {
			retorno[i] = elementArray.get(i).getAsString();
		}
		return retorno;
	}

	private boolean isMutant(String[] dna) {
		boolean retorno = false;
		try {
			String[][] adn = this.createMatrizDna(dna);
			this.setLengthSearch(adn.length);
			boolean valHorizontal = this.validateAdnHorizontal(adn);
			boolean valVertical = this.validateAdnVertical(adn);
			boolean valOblicua = this.validateAdnOblicua(adn);
			if (valHorizontal || valVertical || valOblicua) {
				retorno = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	private String[][] createMatrizDna(String[] dna) throws TestMLException {
		int totalString = dna.length;
		String[][] matrizDna = new String[totalString][totalString];
		for (int rowIndex = 0; rowIndex < totalString; rowIndex++) {
			String row = dna[rowIndex];
			if (row.length() != totalString) {
				throw new TestMLException("Cadena no cumple NxN");
			}
			for (int colIndex = 0; colIndex < totalString; colIndex++) {
				String letter = String.valueOf(row.charAt(colIndex));
				if (letter.equals("A") || letter.equals("T") || letter.equals("C") || letter.equals("G")) {
					matrizDna[rowIndex][colIndex] = letter;
				} else {
					throw new TestMLException("Letra no permitida para analizar [".concat(letter).concat("]"));
				}
			}
		}
		return matrizDna;
	}

	private boolean validateAdnHorizontal(String[][] adn) {
		boolean retorno = false;
		for (int k = 0; k < this.getLengthSearch() - 1; k++) {
			for (int i = 0; i < adn.length; i++) {
				int j = k + 1;
				int equalLetter = 0;
				String letter = adn[i][k];
				String nextLetter = adn[i][j];
				while (letter.equals(nextLetter)) {
					equalLetter++;
					j++;
					nextLetter = adn[i][j];
				}
				if (equalLetter >= COINCIDENCE) {
					retorno = true;
					break;
				}
			}
			if (retorno) {
				break;
			}
		}
		return retorno;
	}

	private boolean validateAdnVertical(String[][] adn) {
		boolean retorno = false;
		for (int k = 0; k < this.getLengthSearch() - 1; k++) {
			for (int i = 0; i < adn.length; i++) {
				int j = k + 1;
				int equalLetter = 0;
				String letter = adn[k][i];
				String nextLetter = adn[j][i];
				while (letter.equals(nextLetter)) {
					equalLetter++;
					j++;
					nextLetter = adn[j][i];
				}
				if (equalLetter >= COINCIDENCE) {
					retorno = true;
					break;
				}
			}
			if (retorno) {
				break;
			}
		}
		return retorno;
	}

	private boolean validateString(int row, int col, String letter, String[][] adn) {
		boolean retorno = true;
		int i = row + 1;
		int j = col + 1;
		int equalLetter = 0;
		String nextLetter = adn[i][j];
		while (letter.equals(nextLetter) && retorno) {
			equalLetter++;
			if (equalLetter >= COINCIDENCE) {
				retorno = false;
			} else {
				i++;
				j++;
				nextLetter = adn[i][j];
			}
		}
		return retorno;
	}

	private boolean validateAdnOblicua(String[][] adn) {
		boolean retorno = false;
		boolean isExist = false;
		for (int k = 0; k < this.getLengthSearch() && !retorno; k++) {
			for (int l = 0; l < this.getLengthSearch() && !isExist && !retorno;) {
				String letter = adn[k][l];
				isExist = this.validateString(k, l, letter, adn);
				retorno = !isExist;
			}
			isExist = false;
		}

		for (int k = 0; k < this.getLengthSearch() && !retorno; k++) {
			for (int l = 0; l < this.getLengthSearch() && !isExist && !retorno;) {
				String letter = adn[l][k];
				isExist = this.validateString(l, k, letter, adn);
				retorno = !isExist;
			}
			isExist = false;
		}

		return retorno;
	}

	private void saveAdn(JsonObject element, boolean result) throws TestMLException {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			AdnHist adnHist = new AdnHist(element.toString(), (result ? 1 : 0));
			session.save(adnHist);
			session.getTransaction().commit();
		} catch (Exception e) {
			throw new TestMLException(e);
		}
	}

}
