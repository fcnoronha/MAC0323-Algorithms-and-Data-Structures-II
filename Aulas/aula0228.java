/*

	-- Representação de arvores em vetores

- 'm' é o numero de nós.
- a[1..m] representa uma arvore e dizemos que para qualque índice i, temos que:
	- 2i é o filho esquedo de i;
	- 2i + 1 é o filho direito de i;
	- i/2 é o pai de i.

- 1 não tem pai.
- i tem filho esquedo se 2i <= m.
- i tem filho direito se 2i + 1 <= m.

- Em cada nivel 'p' os nós tem indices
	2^p, 2^p + 1, .. , 2^(p+1) - 1.

- O nivel de um nó com indice é log(i).

	-- Altura

- A altura de um nó i é o maio caminho até a folha.
- A altura de um no i é chão(log(m/i)).

	-- Max heap

- Um vetor a[1...m] é um maxheap se a[i/2] >= a[i], para todo i.

-- IMPLEMENTAÇÃO --

*/