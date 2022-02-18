/*
Cenário de Negócio:
Todo dia útil por volta das 6 horas da manhã um colaborador da retaguarda do Sicredi recebe e organiza as informações de contas para enviar ao Banco Central. Todas agencias e cooperativas enviam arquivos Excel à Retaguarda. Hoje o Sicredi já possiu mais de 4 milhões de contas ativas.
Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal, antes as 10:00 da manhã na abertura das agências.

Requisito:
Usar o "serviço da receita" (fake) para processamento automático do arquivo.

Funcionalidade:
0. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
1. Processa um arquivo CSV de entrada com o formato abaixo.
2. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
3. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma nova coluna.


Formato CSV:
agencia;conta;saldo;status
0101;12225-6;100,00;A
0101;12226-8;3200,50;A
3202;40011-1;-35,12;I
3202;54001-2;0,00;P
3202;00321-2;34500,00;B
...

*/

package com.projeto.sincronizacaoreceita;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SincronizacaoreceitaApplication {

	static ReceitaService receitaService = new ReceitaService();

	public static void main(String[] args) throws IOException, NumberFormatException, RuntimeException, InterruptedException {
		String arquivo = args[0];
        Path path = Paths.get(arquivo);
        List<String> linhas = Files.readAllLines(path);
        linhas.remove(0);
        List<String> listaResultado = new ArrayList<>();
        for (String linha : linhas) {
            String[] conta = linha.split(";");
            boolean resultado = receitaService.atualizarConta(conta[0], refatoraConta(conta[1]), stringToDouble(conta[2]), conta[3]);
            listaResultado.add(salvaResultado(linha, resultado));
        }
        gerarArquivoResultado(listaResultado, arquivo);
	}

    private static void gerarArquivoResultado(List<String> resultado, String nomeCompleto) throws IOException {
        String[] nome = nomeCompleto.split("\\.");
        String fileName = nome[0] + "-processado.csv";
        Path file = Paths.get(fileName);
        Files.write(file, resultado, StandardCharsets.UTF_8);
    }

    private static String salvaResultado(String linha, boolean resultado) {
        String estado = "Atualizado";
        if (!resultado) estado = "Falha";
        return linha + ";" + estado;
    }

    private static String refatoraConta(String conta) {
        return conta.replace("-", "");
    }

    private static Double stringToDouble(String valor) {
        valor = valor.replace(",", ".");
        return Double.parseDouble(valor);
    }

}
