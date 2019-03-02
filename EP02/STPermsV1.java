/****************************************************************
	Nome: Felipe Castro de Noronha
	NUSP: 10737032

	Ao preencher esse cabeçalho com o meu nome e o meu número USP,
	declaro que todas as partes originais desse exercício programa (EP)
	foram desenvolvidas e implementadas por mim e que portanto não 
	constituem desonestidade acadêmica ou plágio.
	Declaro também que sou responsável por todas as cópias desse
	programa e que não distribui ou facilitei a sua distribuição.
	Estou ciente que os casos de plágio e desonestidade acadêmica
	serão tratados segundo os critérios divulgados na página da 
	disciplina.
	Entendo que EPs sem assinatura devem receber nota zero e, ainda
	assim, poderão ser punidos por desonestidade acadêmica.

	Abaixo descreva qualquer ajuda que você recebeu para fazer este
	EP.  Inclua qualquer ajuda recebida por pessoas (inclusive
	monitoras e colegas). Com exceção de material de MAC0323, caso
	você tenha utilizado alguma informação, trecho de código,...
	indique esse fato abaixo para que o seu programa não seja
	considerado plágio ou irregular.

	Descrição de ajuda ou indicação de fonte:

	1º: O algoritmo para a geração das proximas permutações foi ensinado
	por um tutorial na plataforma GeeksForGeeks, com o link a seguir:
	(geeksforgeeks.org/lexicographic-permutations-of-string/).
		Aprendi este algoritmo estudando topicos á respeito de strings e 
	permutações, para o grupo de extensão ao qual faço parte, o MaratonUSP.

	2º: As funções cntCrescente() e cntDecrescente() são inspiradas no
	algoritmo de LIS (Longest Increasing subsequence), que é muito usado
	em programação competitiva.
	
	3º: A estruta base da função main() do programa toma como base o 
	esqueleto disponibilizado para a realização do EP1.

	Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

	1º: A analise de complexidade de meu algoritmo mostra uma complexidade
	em torno de O((n^2)*n!), logo, a partir de um n = 10, a execução do codigo
	começa a demorar muito tempo.

****************************************************************/

/******************************************************************************
 *  Compilation:  javac-algs4 STPerms.java
 *  Execution:    java STPerms n s t opcao
 *  
 *  Enumera todas as (s,t)-permutações das n primeiras letras do alfabeto.
 *  As permutações devem ser exibidas em ordem lexicográfica.
 *  Sobre o papel da opcao, leia o enunciado do EP.
 *
 *  % java STPerms 4 2 2 0
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  4
 *  % java STPerms 4 2 2 1
 *  4
 *  % java STPerms 4 2 2 2
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  4
 *   
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

public class STPerms {

	// Neste EP, ao inves de utilizar um String para representar as permutações, 
	// usei uma array de inteiros, deixando o codigo mais claro e simples. Ao final, 
	// quando necessario realizar a impressão de tal array, realizo a conversão de inteiro 
	// para char.

	private static int cnt = 0; // Contador para quantidade de permutações (s, t)
	private static int op = 0; // 0 == permutações / 1 == quantidade / 2 == per+qtd

	private static int permu[]; // Armazena a permutação atual em forma de Int[]

	public static boolean isFinal(int n){
		// Checa se ainda há permutações pra fazer. Utilizando a idea que a permutação
		// final, em ordem lexicografica, é estritamente decrescrente.

		boolean flag = true;

		for (int i = 1; i < n; i++)
			if (permu[i] > permu[i-1])
				flag = false;

		return flag == true;
	}

	public static int cntCrescente(int n){
		// Conta o tamanho da maior sequencia crescente. O(n*n).
		
		int lis[] = new int[n];

		for (int i = 0; i < n; i++)
			lis[i] = 1;

		for (int i = 1; i < n; i++)
			for (int j = 0; j < i; j++)
				if (permu[i] > permu[j] && lis[j] >= lis[i])
					lis[i] = lis[j] + 1;

		int ret = 0;
		for (int i = 0; i < n; i++)
			if (lis[i] > ret)
				ret = lis[i];
 		
		return ret;
	}

	public static int cntDescrescente(int n){
		// Conta o tamanho da maior sequencia decrescente. O(n*n).

		int lds[] = new int[n];

		for (int i = 0; i < n; i++)
			lds[i] = 1;

		for (int i = 1; i < n; i++)
			for (int j = 0; j < i; j++)
				if (permu[i] < permu[j] && lds[j] >= lds[i])
					lds[i] = lds[j] + 1;

		int ret = 0;
		for (int i = 0; i < n; i++)
			if (lds[i] > ret)
				ret = lds[i];
 		
		return ret;
	}

	public static void imprime(int n){
		// Imprime a permutação, transformando os inteiros em chars.

		for (int i = 0; i < n; i++)
			StdOut.printf("%c", permu[i] + 'a');
		StdOut.println();
	}

	public static void proximaPermu(int n){
		// Gera a proxima permutação seguindo a ordem lexicografica.

		int i, j = -1;

		for (i = n-2; i >= 0; i--)
			if (permu[i] < permu[i+1])
				break;

		for (int k = i+1; k < n; k++)
			if (j == -1 || (permu[k] < permu[j] && permu[k] > permu[i]))
				j = k;

		int aux = permu[j];
		permu[j] = permu[i];
		permu[i] = aux;
		
		i++;
		j = n-1;
		while (i < j){
			aux = permu[j];
			permu[j] = permu[i];
			permu[i] = aux;
			i++;
			j--;
		}
	}

	public static void gerador(int n, int s, int t){
		// Confere a atual permutação, e se ela é um (s, t)-permutação.
		// Caso ela seja, imprime esta permutação.
		// Gera a proxima permutação em order lexicografica.
		// A execução termina quando ja se esta na ultima permutação, aque que é
		// estritamente decrescente.

		while (true){
			int cre = cntCrescente(n);
			int decre = cntDescrescente(n);

			if (cre <= s && decre <= t){
				cnt++;
				if (op != 1)
					imprime(n);
			}

			if (isFinal(n)) 
				return;

			proximaPermu(n);
		}
	}
	
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int s = Integer.parseInt(args[1]);
		int t = Integer.parseInt(args[2]);
		if (args.length == 4) {
			op = Integer.parseInt(args[3]);
		}

		permu = new int[n]; // Criando uma nova array com tamanho n
		for (int i = 0; i < n; i++) // {0, 1, ..., n-1}
			permu[i] = i;

		gerador(n, s, t); 

		if (op != 0)
			StdOut.println(cnt);  
	}
}