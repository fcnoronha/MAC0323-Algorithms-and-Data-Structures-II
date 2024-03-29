/******************************************************************************
 *  Compilation:  javac-algs4 Combinacao.java
 *  Execution:    java Combinacao n k [opcao]
 *
 *  Enumera todas as combinações dos números em {1,2,...,n} k a k.
 *  Se opcao = 0 (defaul), gera e exibe todas as permutações em ordem
 *  lexicográfica
 *  Se opcao = 1 apenas, __gera todas__ as combinações, mas __não__ as
 *  exibe; apenas exibe o total de combinações.
 *
 * % java Combinacao 5 3 1
 * 10
 * elapsed time = 0.002
 * % java Combinacao 5 3
 * 1 2 3
 * 1 2 4
 * 1 2 5
 * 1 3 4
 * 1 3 5
 * 1 4 5
 * 2 3 4
 * 2 3 5
 * 2 4 5
 * 3 4 5
 * 10
 * elapsed time = 0.002
 * % java Combinacao 100 3 1
 * 161700
 * elapsed time = 0.004
 * % java Combinacao 1000 3 1
 * 166167000
 * elapsed time = 0.726
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Combinacao {
    private static int count = 0; // contador de combinações
    private static int opcao = 0;
        // 1 imprimir apenas o número de combinações (default)
        // 0 imprimir as combinações e o número de combinações

    private static int numbers[]; // Vai armazenar a ultima combinação impressa

    private static void combinacao(int n, int k, int ind, int atual){

        // ind - A posição que eu estou iterando da combinação
        // atual - Numero correspondente a posiçao anterior da combinação

    	if (ind == k){
            // Caso eu esteja na posição após o ultimo digito da combinação, ou
            // seja, eu terminei de montar uma combinação

    		count++; // Incrementando contador
    		if (opcao == 1) return; // Parando aqui caso a opcao seja 0

    		// Imprimo a combinação
    		for (int i = 0; i < k; i++){
    			if (i == k-1){
                    StdOut.printf("%d" ,numbers[i]);
                    continue;
                }
                StdOut.printf("%d " ,numbers[i]);
            }
    		StdOut.println();

    		return;
    	}

    	for (int a = atual; a <= n-(k-ind-1); a++){
    		// Percorro todos os numeros possiveis no intervalo [a, n-(k-ind-1)]
    		numbers[ind] = a;
    		combinacao(n, k, ind+1, a+1); // Continuando a geração
    	}
    }

    public static void combinacao(int n, int k) {

    	numbers = new int[k]; // Inicializando esse vetor com k posições

    	combinacao(n, k, 0, 1); // Chamando a função que gera as combinaçẽs
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        if (args.length == 3) {
            opcao = Integer.parseInt(args[2]);
        }
        Stopwatch timer = new Stopwatch();
        combinacao(n, k);
        StdOut.println(count);
        StdOut.println("elapsed time = " + timer.elapsedTime());
    }
}
