# Processador_Contas_Sicredi

Para executar, basta criar o .jar do projeto através de:
-  mvn package
Que vai gerar o arquivo em:
-  target/nome-do-seu-projeto-1.0-SNAPSHOT.jar
E então rodar o comando semelhar a:
-  java -jar SincronizacaoReceita <input-file>
Onde o input-file é o arquivo csv no formato:
-  agencia;conta;saldo;status
-  0101;12225-6;100,00;A
