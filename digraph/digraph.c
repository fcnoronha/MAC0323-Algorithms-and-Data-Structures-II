/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 *
 * ADT Digraph implementada atrevés de vetor de listas de adjacência.
 * As listas de adjacência são bag de ints que são mais restritos
 * que as bags genéricas do EP12. Veja a api bag.h e simplifique
 * o EP12 de acordo.
 *
 * Busque inspiração em:
 *
 *    https://algs4.cs.princeton.edu/42digraph/ (Graph representation)
 *    https://algs4.cs.princeton.edu/42digraph/Digraph.java.html
 *
 * DIGRAPH
 *
 * Digraph representa um grafo orientado de vértices inteiros de 0 a V-1.
 *
 * As principais operações são: add() que insere um arco no digrafo, e
 * adj() que itera sobre todos os vértices adjacentes a um dado vértice.
 *
 * Arcos paralelos e laços são permitidos.
 *
 * Esta implementação usa uma representação de _vetor de listas de adjacência_,
 * que  é uma vetor de objetos Bag indexado por vértices.

 * ATENÇÃO: Por simplicidade esses Bag podem ser int's e não de Integer's.
 *
 * Todas as operações consomen no pior caso tempo constante, exceto
 * iterar sobre os vértices adjacentes a um determinado vértice, cujo
 * consumo de tempo é proporcional ao número de tais vértices.
 *
 * Para documentação adicional, ver
 * https://algs4.cs.princeton.edu/42digraph, Seção 4.2 de
 * Algorithms, 4th Edition por Robert Sedgewick e Kevin Wayne.
 *
 */

/* interface para o uso da funcao deste módulo */
#include "digraph.h"


#include "bag.h"     /* add() e itens() */
#include <stdio.h>   /* fopen(), fclose(), fscanf(), ... */
#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include <ctype.h>   /* islnum() */
#include "util.h"    /* emalloc(), ecalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debuging */
#endif

/*----------------------------------------------------------*/
/*
 * Estrutura básica de um Digraph
 *
 * Implementação com vetor de listas de adjacência.
 */
struct digraph {

    int E;
    int V;

    Bag *adj; /* Adjacency list, bag[V] */
    int *in_degree; /* In degree of vertices */
};

/*------------------------------------------------------------*/
/*
 * Protótipos de funções administrativas: tem modificador 'static'
 *
 */

/*-----------------------------------------------------------*/
/*
 *  newDigraph(V)
 *
 *  RECEBE um inteiro V.
 *  RETORNA um digrafo com V vértices e 0 arcos.
 *
 */
Digraph newDigraph(int V) {

    int i;

    /* Allocating memory */
    Digraph new_d = emalloc(sizeof(struct digraph));

    new_d->E = 0;
    new_d->V = V;

    /* Allocating adjacency list */
    new_d->adj = emalloc( V * sizeof(Bag));
    for (i = 0; i < V; ++i)
        new_d->adj[i] = newBag();

    /* Allocating indegree array */
    new_d->in_degree = emalloc( V * sizeof(int));

    return new_d;
}

/*-----------------------------------------------------------*/
/*
 *  cloneDigraph(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA um clone de G.
 *
 */
Digraph cloneDigraph(Digraph G) {

    int i, aux;

    Digraph clone_d = newDigraph(vDigraph(G));

    /* Cloning adjacency list */
    for (i = 0; i < vDigraph(G); ++i)
        for (aux = adj(G, i, TRUE); aux >= 0; aux = adj(G, i, FALSE))
            addEdge(clone_d, i, aux);

    return clone_d;
}

/*-----------------------------------------------------------*/
/*
 *  reverseDigraph(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA o digrafo R que é o reverso de G:
 *
 *      v-w é arco de G <=> w-v é arco de R.
 *
 */
Digraph reverseDigraph(Digraph G) {

    int i, aux;

    Digraph clone_d = newDigraph(vDigraph(G));

    /* Cloning adjacency list */
    for (i = 0; i < vDigraph(G); ++i)
        for (aux = adj(G, i, TRUE); aux >= 0; aux = adj(G, i, FALSE))
            addEdge(clone_d, aux, i);

    return clone_d;
}

/*-----------------------------------------------------------*/
/*
 *  readDigraph(NOMEARQ)
 *
 *  RECEBE uma stringa NOMEARQ.
 *  RETORNA o digrafo cuja representação está no arquivo de nome NOMEARQ.
 *  O arquivo contém o número de vértices V, seguido pelo número de arestas E,
 *  seguidos de E pares de vértices, com cada entrada separada por espaços.
 *
 *  Veja os arquivos  tinyDG.txt, mediumDG.txt e largeDG.txt na página do
 *  EP e que foram copiados do algs4,
 *
 */
Digraph readDigraph(String nomeArq) {

    int e, i, u, v;
    char *aux;
    FILE *fptr;
    Digraph new;

    if ((fptr = fopen(nomeArq, "r")) == NULL)
    {
        printf("Error! opening file");
        exit(1);
    }

    aux = getLine(fptr);
    new = newDigraph(atoi(aux));
    free(aux);

    aux = getLine(fptr);
    e = atoi(aux);
    free(aux);

    /* Getting both numbers representation */
    while (e--) {

        aux = getLine(fptr);

        i = 0, u = 0, v = 0;
        while ( !isalnum(aux[i]) )
            i++;

        while ( isalnum(aux[i]) )
            u = (u*10) + (aux[i++]-48);

        while ( !isalnum(aux[i]) )
            i++;

        while ( aux[i] != EOF && isalnum(aux[i]) )
            v = (v*10) + (aux[i++]-48);

        addEdge(new, u, v);
        free(aux);
    }

    fclose(fptr);
    return new;
}


/*-----------------------------------------------------------*/
/*
 *  freeDigraph(G)
 *
 *  RECEBE um digrafo G e retorna ao sistema toda a memória
 *  usada por G.
 *
 */
void freeDigraph(Digraph G) {

    int i;
    for (i = 0; i < vDigraph(G); ++i)
        freeBag(G->adj[i]);
    free(G->adj);
    free(G->in_degree);

    free(G);
}

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS:
 *
 *     - vDigraph(), eDigraph(): número de vértices e arcos
 *     - addEdge(): insere um arco
 *     - adj(): itera sobre os vizinhos de um dado vértice
 *     - outDegree(), inDegree(): grau de saída e de entrada
 *     - toString(): usada para exibir o digrafo
 */

/*-----------------------------------------------------------*/
/*
 *  VDIGRAPH(G)
 *
 *  RECEBE um digrafo G e RETORNA seu número de vertices.
 *
 */
int vDigraph(Digraph G) {

    if (G == NULL)
        return 0;

    return G->V;
}

/*-----------------------------------------------------------*/
/*
 *  EDIGRAPH(G)
 *
 *  RECEBE um digrafo G e RETORNA seu número de arcos (edges).
 *
 */
int eDigraph(Digraph G) {

    if (G == NULL)
        return 0;

    return G->E;
}

/*-----------------------------------------------------------*/
/*
 *  addEdge(G, V, W)
 *
 *  RECEBE um digrafo G e vértice V e W e INSERE o arco V-W
 *  em G.
 *
 */
void addEdge(Digraph G, vertex v, vertex w) {

    add(G->adj[v], w);
    G->in_degree[w]++;
    G->E++;
}


/*-----------------------------------------------------------*/
/*
 *  ADJ(G, V, INIT)
 *
 *  RECEBE um digrafo G, um vértice v de G e um Bool INIT.
 *
 *  Se INIT é TRUE,  ADJ() RETORNA o primeiro vértice na lista de adjacência de V.
 *  Se INIT é FALSE, ADJ() RETORNA o sucessor na lista de adjacência de V do
 *                   último vértice retornado.
 *  Se a lista de adjacência de V é vazia ou não há sucessor do último vértice
 *  retornada, ADJ() RETORNA -1.
 *
 *  Se entre duas chamadas de ADJ() a lista de adjacência de V é alterada,
 *  o comportamento é  indefinido.
 *
 */
int adj(Digraph G, vertex v, Bool init) {
    return itens(G->adj[v], init);
}

/*-----------------------------------------------------------*/
/*
 *  outDegree(G, V)
 *
 *  RECEBE um digrafo G e vértice V.
 *  RETORNA o número de arcos saindo de V.
 *
 */
int outDegree(Digraph G, vertex v) {
    return size(G->adj[v]);
}

/*-----------------------------------------------------------*/
/*
 *  inDegree(G, V)
 *
 *  RECEBE um digrafo G e vértice V.
 *  RETORNA o número de arcos entrando em V.
 *
 */
int inDegree(Digraph G, vertex v) {
    return G->in_degree[v];
}


/*-----------------------------------------------------------*/
/*
 *  toString(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA uma string que representa G. Essa string será usada
 *  para exibir o digrafo: printf("%s", toString(G));
 *
 *  Sigestão: para fazer esta função inspire-se no método
 *  toString() da classe Digraph do algs4.
 */
String toString(Digraph G) {

    char *str, aux[100];
    int size_buffer, i, v = 0, w;

    /* Maximum size of string */
    for (i = 0, size_buffer = 0; i < vDigraph(G); ++i)
        size_buffer += size(G->adj[i]);
    str = emalloc( size_buffer*100*sizeof(char) + vDigraph(G)*100 + 100 );

    /* Used in transfomtion of int to char */
    memset(str, 0, size_buffer*100*sizeof(char) + vDigraph(G)*100 + 100);
    memset(aux, 0, 100*sizeof(char));

    sprintf(aux, "%d vertices, ", vDigraph(G));
    strcat(str, aux);

    memset(aux, 0, 100*sizeof(char));
    sprintf(aux, "%d edges \n", eDigraph(G));
    strcat(str, aux);

    /* Adjacency list */
    for (v = 0; v < vDigraph(G); ++v) {

        memset(aux, 0, 100*sizeof(char));
        sprintf(aux, "%d: ", v);
        strcat(str, (aux));

        for (w = adj(G, v, TRUE); w >= 0; w = adj(G, v, FALSE)) {
            memset(aux, 0, 100*sizeof(char));
            sprintf(aux, "%d ", w);
            strcat(str, (aux));
        }

        strcat(str, ("\n"));
    }

    return str;
}

/*------------------------------------------------------------*/
/*
 * Implementaçao de funções administrativas: têm o modificador
 * static.
 */
