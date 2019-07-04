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

    public static int lis[]; // Armazena a maior sequencia crescente
    public static int lds[]; // Armazena a maior sequencia descrescente
    public static int s; // Limite para sequencia crescente
    public static int t; // Limite para sequencia decrescente

    private static int cnt = 0; // Contador para quantidade de permutações (s, t)
    private static int op = 0; // 0 == permutações / 1 == quantidade / 2 == per+qtd

    public  static void gerador(String permu){
        gerador("", permu);
    }

    private static void gerador(String prefix, String permu){

    	// Vai gerar todas as permutações das primeiras n letras do alfabeto,
    	// contando a maior sequencia crescente (LIS) e a maior sequencia
    	// descrescente (LDS) da permutação atual. Fazendo ações de acordo
    	// com a operação inserida pelo usuario.

        int n = permu.length();

        if (n == 0){
           	// Caso em que tem-se uma permutação completa

            int cre = 0;
            int decre = 0;

            // Achando maiores valores
            for (int i = 0; i < prefix.length(); i++){
                if(lis[i] > cre) cre = lis[i];
                if(lds[i] > decre) decre = lds[i];
            }

            // Checando se estas estão dentro dos limites
            if (cre <= s && decre <= t){
                cnt++;
                if (op != 1)
                    StdOut.println(prefix);
            }

            return;
        }

        for (int i = 0; i < n; i++){
        	// Percorrendo todas as opções disponiveis de letras para a
        	// permutação atual, em ordem lexicografica.

        	// Tamanho da permutação gerada ate este passo
            int aux = prefix.length();

            // Por definição, o tamanho da LIS e LDS de um caractere é 1;
            lis[aux] = 1;
            lds[aux] = 1;

            // Flag que sinaliza se devo gerar permutações a patir da atual
            boolean contn = true;
            for (int j = aux-1; j >= 0; j--){
            	// Percorrendo todo o prefixo

                if (lis[j] > s || lds[j] > t){
                	// Não se deve continuar, deadends
                    contn = false;
                    break;
                }

                // Adicionando sequecia crescente
                if (prefix.charAt(j) < permu.charAt(i))
                    if (lis[j] >= lis[aux])
                        lis[aux] = lis[j]+1;

                // Adicionando sequencia descrescente
                if (prefix.charAt(j) > permu.charAt(i))
                    if (lds[j] >= lds[aux])
                        lds[aux] = lds[j]+1;

            }

            if (!contn) continue;

            // Continuando a execução com uma chamada recursiva
            gerador(prefix + permu.charAt(i), permu.substring(0, i) + permu.substring(i+1, n));
        }
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        s = Integer.parseInt(args[1]);
        t = Integer.parseInt(args[2]);
        if (args.length == 4) {
            op = Integer.parseInt(args[3]);
        }

        // Construindo arrays
        lis = new int[n];
        lds = new int[n];

        String alfabeto = "abcdefghijklmnopqrstuvwxyz";
        String in = alfabeto.substring(0, n);

        gerador(in);

        if (op != 0)
            StdOut.println(cnt);
    }
}
