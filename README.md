# Projeto final da disciplina DM110

## Propósito

Criar uma aplicação capaz de fazer a varredura de uma rede de computadores para identificar os equipamentos que estão ativos, dada uma faixa de IP (endereço de rede/máscara CIDR).

### Observação

Para ativar o suporte a JMS no WildFly, verifique o [Guia de Configuração do JMS](../guides/queueconfig.md) publicado neste repositório.

## Interface de serviço

### Comando de início de varredura:

* Tipo da requisição: GET
* URL: `<context-root>/api/poller/start/{IP}/{Mask}`
* Funcionamento:
  * Calcula todos os endereços da rede (desconsiderando o endereço de rede e de broadcast).
  * Cria mensagens JMS sendo que cada mensagem deverá conter uma lista de no máximo 10 endereços IP.
  * Insere mensagens na fila.
  * Deverá existir um MDB consumindo mensagens desta fila e fazendo a verificação do status do equipamento referente ao endereço IP.
  * Para cada endereço verificado, deverá salvar uma linha em uma tabela na base de dados contendo, pelo menos, o endereço IP verificad e o status (Ativo ou Inativo).

### Verificação do status do equipamento

* Tipo de requisição: GET
* URL: `<context-root>/api/poller/status/{IP}`
* Funcionamento: Busca o equipamento na base de dados e retorna o status.

## Exemplo de código para fazer ping

```java
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TestRuntime {

	public static void main(String[] args) {
		System.out.println(execPing("192.168.60.91"));
	}

	public static boolean execPing(String address) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("ping -n 1 " + address);
			InputStream is = process.getInputStream();
			InputStream es = process.getErrorStream();
			String input = processStream(is);
			String error = processStream(es);
			int code = process.waitFor();
			if (code != 0) {
				return false;
			}
			if (error.length() > 0) {
				return false;
			}
			if (input.contains("Host de destino inacess")) {
				return false;
			}
			return true;
		} catch (IOException | InterruptedException e) {
			return false;
		}
	}

	public static String processStream(InputStream is) {
		StringBuilder input = new StringBuilder();
		try (Scanner scanner = new Scanner(is)) {
			while (scanner.hasNextLine()) {
				input.append(scanner.nextLine()).append("\n");
			}
		}
		return input.toString();
	}
}
```

## Exemplo de código que gera faixa de IPs dado o endereço de rede e CIDR

```java
package br.inatel.dm110.util;

import java.util.Scanner;

public class NetworkIpGen {
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o endereço:");
			String networkIp = scanner.next() + scanner.nextLine();
			System.out.println("Digite a máscara CIDR:");
			int cidr = scanner.nextInt();
			String[] generatedIps = generateIps(networkIp, cidr);
			for (String ip : generatedIps) {
				System.out.println(ip);
			}
		}
	}

	private static String[] generateIps(String networkIp, int cidr) {
		int rangeSize = 0;
		for (int i = 0; i < 32 - cidr; i++) {
			rangeSize |= 1 << i;
		}
		long networkAddress = fromIp(networkIp);
		String[] ips = new String[rangeSize - 1];
		for (int i = 1; i < rangeSize; i++) {
			ips[i - 1] = toIp(networkAddress + i);
		}
		return ips;
	}

	private static long fromIp(String ip) {
		String[] octs = ip.split("\\.");
		long octs1 = Long.parseLong(octs[0]);
		long octs2 = Long.parseLong(octs[1]);
		long octs3 = Long.parseLong(octs[2]);
		long octs4 = Long.parseLong(octs[3]);
		return (octs1 << 24) | (octs2 << 16) | (octs3 << 8) | octs4;
	}

	private static String toIp(long value) {
		return String.format("%s.%s.%s.%s", value >> 24, (value >> 16) & 255, (value >> 8) & 255, value & 255);
	}
}
```