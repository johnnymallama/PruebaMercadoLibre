package co.com.mercadolibre.test.johnnymallama;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {
	
	private static APIGatewayProxyResponseEvent responseOk;
	private static APIGatewayProxyResponseEvent responseForbiden;
	private static APIGatewayProxyResponseEvent responseError;

	@BeforeClass
	public static void createReponse () {
		responseOk = new APIGatewayProxyResponseEvent();
		responseOk.setStatusCode(200);
		responseOk.setBody("true");
		
		responseForbiden = new APIGatewayProxyResponseEvent();
		responseForbiden.setStatusCode(403);
		responseForbiden.setBody("false");
		
		responseError = new APIGatewayProxyResponseEvent();
		responseError.setStatusCode(500);
		responseError.setBody("false");
	}
	
	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("Test Mercado Libre");
		return ctx;
	}

	@Test
	public void testOne() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		String test = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";
		request.setBody(test);
		APIGatewayProxyResponseEvent output = handler.handleRequest(request, ctx);
		Assert.assertEquals(responseOk, output);
	}

	@Test
	public void testTwo() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		String test = "{\"dna\":[\"TTCCGA\",\"AAGCAC\",\"ATATCT\",\"AGGGGC\",\"ATCTTA\",\"TCACTG\"]}";
		request.setBody(test);
		APIGatewayProxyResponseEvent output = handler.handleRequest(request, ctx);
		Assert.assertEquals(responseOk, output);
	}

	@Test
	public void testThree() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		String test = "{\"dna\":[\"TACCGA\",\"AAGCAC\",\"ATATGT\",\"TGGGGC\",\"ATCTTA\",\"TCACTG\"]}";
		request.setBody(test);
		APIGatewayProxyResponseEvent output = handler.handleRequest(request, ctx);
		Assert.assertEquals(responseOk, output);
	}

	@Test
	public void testFour() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		String test = "{\"dna\":[\"TACCGC\",\"AGGGGC\",\"ATATAC\",\"TAGCGC\",\"ACATCA\",\"TCAATG\"]}";
		request.setBody(test);
		APIGatewayProxyResponseEvent output = handler.handleRequest(request, ctx);
		Assert.assertEquals(responseOk, output);
	}

	@Test
	public void testFive() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		String test = "{\"dna\":[\"TACTGC\",\"AGTCGA\",\"ATATAC\",\"TGGCGC\",\"ACGTCA\",\"TCAATG\"]}";
		request.setBody(test);
		APIGatewayProxyResponseEvent output = handler.handleRequest(request, ctx);
		Assert.assertEquals(responseForbiden, output);
	}
}
