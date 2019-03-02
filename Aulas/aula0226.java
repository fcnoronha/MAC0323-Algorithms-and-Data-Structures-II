/*

	-- Conjuntos disjuntos
	Seja S = {s1, s2, ..., sn} uma coleção de conjuntos disjuntos, ou seja,
	Si ^ Sj = 0/, para todo i e j.

	-- Coleção disjunta dinâmica
	Conjuntos sao modificados ao longo do tempo.

	-- Tipos de union find

	- Quick find: realiza o find em O(1) e union em O(n);
	- Quick union: find em O(n) e union em O(1);
	- Weighet UF: find em O(lg n) e union em O(lg n);

	-- Função log-estrela

	ln*n é o menor k tal que lg(lg(lg(...k vezes...))) <= 1	
	- Com isso é possivel possivel implementar o union find com path compression,
	fazendo union e find em O(lg*n), que é quase constante.

	-- IMPLEMENTAÇÃO DO UNION FIND -- 

	O UF realiza operações de Union() e de find() em O(1P).

*/