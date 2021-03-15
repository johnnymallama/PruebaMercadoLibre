package co.com.mercadolibre.test.johnnymallama;

import org.junit.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.*;

public class LambaFunctionStatsTest {

	private static APIGatewayProxyResponseEvent responseOk;
	private static APIGatewayProxyResponseEvent responseForbiden;
	private static APIGatewayProxyResponseEvent responseError;

	@BeforeClass
	public static void createReponse() {
		responseOk = new APIGatewayProxyResponseEvent();
		responseOk.setStatusCode(200);

		responseForbiden = new APIGatewayProxyResponseEvent();
		responseForbiden.setStatusCode(204);

		responseError = new APIGatewayProxyResponseEvent();
		responseError.setStatusCode(500);
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("Test Mercado Libre");
		return ctx;
	}

	@Test
	public void testOne() {
		LambaFunctionStats handler = new LambaFunctionStats();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		APIGatewayProxyResponseEvent output = handler.handleRequest(request, ctx);
		ctx.getLogger().log(output.getBody());
		Assert.assertEquals(responseOk.getStatusCode(), output.getStatusCode());
	}
}
