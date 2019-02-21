import edu.princeton.cs.algs4.*;

public static increment(int[] a){
	// 'a' é um vetor de 0's e 1's
	// Transfora 0111 em 1000
	int k = a.length;
	int i = 0;                         // 1
	while (i < k && a[i] == 1){		   // 2
		a[i] = 0;					   // 3
		i = i + 1;                     // 4
	}
	if (i < k)						   // 5
		a[i] = 1;					   // 6
}

/*
	-- Consumo de tempo:

	Linha 1 = O(1)
	Linha 2 = O(k)
	Linha 3 = O(k)
	Linha 4 = O(k)
	Linha 5 = O(1)
	Linha 6 = O(1)

	Logo, o consumo de tempo total é O(k).

	-- Consumo de tempo para um numero N de chamadas 
	(começa com o vetor a preenchido por zeros)

	Uma complexidade exagerada é O(n*k).
	Agora, uma analise um pouco melhor:
		a[0] muda de estado n vezes
		a[1] muda de estado n/2 vezes
		a[2] muda de estado n/4 vezes
		.
		.
		.
		a[n] muda de estado n/(2^n) vezes

	Logo, o custo de mudança de estados de a[] é igual a 
	somatorio de i = 0, ate n, de 1/(2^i), que é igual a 2n.

	Logo, o custo real desse programa é O(n).


	-- Custo amortizado
	O custo amortizado de uma operação é o custo medio da 
	operação quando considerada uma sequencia de operações.
	Usando o exemplo anterior, temos que o custo para N 
	operações é O(N), logo, o custo amortizado dessa 
	aplicação é O(1).
*/

// Aqui foi implementado a classe stack.java

/*
	-- Analise de custo para sequencia de operações push()
	
	s0 -> s1 -> ... -> sm
	si = estado da pilha apos o i-esimo push

	Custo do push O(m) - Exagero
	Custo da sequencia O(m²) == EXAGERO

	Agora, uma analise mais cuidadosa
		1 => a.lenght = 1; custo = 1
		2 => a.lenght = 2; custo = 2
		3 => a.lenght = 4; custo = 3
		4 => a.lenght = 4; custo = 1
		5 => a.lenght = 8; custo = 5
		6 => a.lenght = 8; custo = 1

	Logo, a soma desses custos vai ser igual a:
		m + sum(i = 0 -> k) 2^i = m + 2^(k+1)-1, onde k = log(m),
		~= 3*m.

	Assim, o consumo de tempo de m operações de push() é O(m).
	Ja o custo de tempo amortizado sera O(1), ou seja, constante.

	Esses custos são analogos para o operação de Pop() da stack.
*/