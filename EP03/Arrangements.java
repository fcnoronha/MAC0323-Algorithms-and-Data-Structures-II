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

        - A parte do código dedicada a gerar o proximo arranjo foi inspirada
        em um artigo da plataforma GeeksForGeeks. (geeksforgeeks.org/print-all-
        permutations-of-a-string-with-duplicates-allowed-in-input-string/)

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

// excessões pedidas
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

// pode ser útil
import java.util.Arrays; // Arrays.sort(), Arrays.copyOf(), ...

import java.util.Iterator; // passo 0 para criarmos um iterador

import edu.princeton.cs.algs4.StdOut;

public class Arrangements implements Iterable<String> {

    private String arj; // Arranjo

    public Arrangements(String s) {
        // Metodo construtor

        if (s == null)
            throw new IllegalArgumentException();

        // Ordenando entrada, primeira permutação possivel
        char auxSort[] = s.toCharArray();
        Arrays.sort(auxSort);
        arj = new String(auxSort);
    }

    // CONFIRMAR COM A MONITORA SE POSSE TER ISSO
    public Iterator<String> iterator() {
        // Metodo iterador
        return new ProximaPermutacao();
    }

    private class ProximaPermutacao implements Iterator<String> {
        // Itera proxima permutação

        int cnt = 0; // Contador usado para ultima permutação
        String str = arj; // Copia da permutação original

        public boolean hasNext(){
			// Checa se existe uma proxima permutação, no caso, checa se
            // a string esta em ordem decrescente.

            boolean ret = false;
            for (int i = 1; i < str.length(); i++)
                if (str.charAt(i) > str.charAt(i-1))
                    ret = true;

            // Faz com que a ultima permutação seja mostrada
            if (ret == false && (cnt == 0 || cnt == 1)){
                cnt += 1;
                ret = true;
            }
            return ret;
		}

		public String next(){

            if (!hasNext())
                throw new NoSuchElementException();

            char aux[] = str.toCharArray(); // Convertendo para array de char
            int n = str.length();

            // Achando elemento mais a direita que é menor que o elemento á
            // sua direita.
            int md;
            for (md = n - 2; md >= 0; md--)
                if (aux[md] < aux[md+1])
                    break;

            // Caso não haja uma proxima permutação.
            if (md == -1) return str;

            // Achando o menor caracter maior e a direita de aux[md].
            int mmd = md+1;
            for (int i = md+1; i < n; i++)
                if (aux[i] > aux[md] && aux[i] < aux[mmd])
                    mmd = i;

            // Trocando aux[mmd] e aux[md] de lugar
            char swap = aux[mmd];
            aux[mmd] = aux[md];
            aux[md] = swap;

            // Ordenando a array a partir da posição md+1
            Arrays.sort(aux, md+1, n);

            // Retornando o antigo estado da permutação
            String ret = str;
            str = new String(aux);
            return ret;
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}
    }

    // Unit test
    public static void main(String[] args) {

        // javac-algs4 Arrangements.java && java-algs4 Arrangements abab

        String s = args[0];
        Arrangements arr = new Arrangements(s);

        StdOut.println("Teste 1: imprime no máximo os 10 primeiros arranjos");
        Iterator<String> it = arr.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            StdOut.println(i + " : " + it.next());
        }

        StdOut.println("Teste 2: imprime todos os arranjos");
        int i = 0;
        for (String arranjo: arr) {
            StdOut.println(i + " : " + arranjo);
            i++;
        }
    }
}
