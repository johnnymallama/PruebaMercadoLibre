# PruebaMercadoLibre
Prueba mercado libre

#Codigo fuente 

	NIVEL 2 => Ruta de clase ejecucion (co.com.mercadolibre.test.johnnymallama.LambdaFunctionHandler)
	NIVEL 3 => Ruta de clase ejecucion (co.com.mercadolibre.test.johnnymallama.LambaFunctionStats)
	
#Ejecucion REST API (AWS)

	NIVEL 2 => 
		POST - https://uthtxmq9e9.execute-api.us-east-2.amazonaws.com/PruebaTecnica/mutant
		BODY - {"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}
		CONTENT-TYPE - application/json
		AUTHENTICATION - No
	
	NIVEL 3 =>
		GET - https://uthtxmq9e9.execute-api.us-east-2.amazonaws.com/PruebaTecnica/stats
		CONTENT-TYPE - application/json
		AUTHENTICATION - No
		
#Base de datos MySql (RDS - AWS)

	HOST => database-1.cwcdlr1p5rfv.us-east-2.rds.amazonaws.com
	PORT => 3306
	USER => read
	PASS => read2021*
	
