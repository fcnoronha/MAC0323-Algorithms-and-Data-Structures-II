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

    Exemplo:

        A monitora me explicou que eu devia utilizar a função xyz().

        O meu método xyz() foi baseada na descrição encontrada na
        página https://www.ime.usp.br/~pf/algoritmos/aulas/enumeracao.html.

    Descrição de ajuda ou indicação de fonte:

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 *
 * ADT Topological é uma "representação topológica" de digrafo.
 * Esta implementação usa ADT Digraph do EP13.
 *
 * Busque inspiração em:
 *
 *   https://algs4.cs.princeton.edu/42digraph/
 *   https://algs4.cs.princeton.edu/42digraph/DepthFirstOrder.java
 *   https://algs4.cs.princeton.edu/42digraph/Topological.java
 *   https://algs4.cs.princeton.edu/42digraph/DirectedCycle.java
 *
 * TOPOLOGICAL
 *
 * Topological é uma ¨representação topológica" de um dado digrafo.
 *
 * As principais operações são:
 *
 *      - hasCycle(): indica se o digrafo tem um ciclo (DirectedCycle.java)
 *      - isDag(): indica se o digrafo é acyclico (Topological.java)
 *
 *      - pre(): retorna a numeração pré-ordem de um vértice em relação a uma dfs
 *               (DepthFirstOrder.java)
 *      - pos(): retorna a numareção pós-ordem de um vértice em relação a uma dfs
 *               (DepthFirstOrder.java)
 *      - rank(): retorna a numeração topológica de um vértice (Topological.java)
 *
 *      - preorder(): itera sobre todos os vértices do digrafo em pré-ordem
 *                    (em relação a uma dfs, DepthFirstOrder.java)
 *      - postorder(): itera sobre todos os vértices do digrafo em pós-ordem
 *                    (em relação a uma dfs, ordenação topologica reversa,
 *                     DepthFirstOrder.java)
 *      - order(): itera sobre todos os vértices do digrafo em ordem
 *                 topologica (Topological.java)
 *      - cycle(): itera sobre os vértices de um ciclo (DirectedCycle.java)
 *
 * O construtor e "destrutor" da classe consomem tempo linear..
 *
 * Cada chama das demais operações consome tempo constante.
 *
 * O espaço gasto por esta ADT é proporcional ao número de vértices V do digrafo.
 *
 * Para documentação adicional, ver
 * https://algs4.cs.princeton.edu/42digraph, Seção 4.2 de
 * Algorithms, 4th Edition por Robert Sedgewick e Kevin Wayne.
 *
 */

/* interface para o uso da funcao deste módulo */
#include "topological.h"

#include "digraph.h" /* Digraph, vDigraph(), eDigraph(), adj(), ... */
#include "bag.h"     /* add() e itens() */
#include "util.h"    /* emalloc(), ecalloc(), ERRO(), AVISO() */

#include <stdlib.h>  /* free() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debugging */
#endif

/*----------------------------------------------------------*/
/*
 * Estrutura básica de um Topological
 *
 */
struct topological
{

    Bag top_order; /* Represents iteration order for top-sort */
    Bag pre_order; /* Represents iteration for pre-order */
    Bag pos_order; /* Represents iteration for pos-order */
    Bag cycle_order; /* Represents iteration for cycle */

    int *top_rank; /* top_rank[v] = rank of vertex v in order */
    int *pre_rank; /* pre_rank[v] = rank of vertex v in pre-order */
    int *pos_rank; /* pos_rank[v] = rank of vertex v in pos-order */

    int *on_stack; /* onStack[v] = is vertex on the stack? */
    int *marked; /* marked[v] = has vertex v been marked? */
    int *edge_to; /* edgeTo[v] = previous vertex on path to v */

    int pre_counter; /* Counter for pre-order */
    int pos_counter; /* Counter for pos-order */

    Bool is_dag;
};

/*------------------------------------------------------------*/
/*
 * Protótipos de funções administrativas: tem modificador 'static'
 *
 */

void dfs(Digraph G, Topological ts, vertex v);

/*-----------------------------------------------------------*/
/*
 *  newTopologica(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA uma representação topológica de G.
 *
 */
Topological newTopological(Digraph G)
{
    int v, i, size_v;
    Bag aux;

    /* To be returned */
    Topological top = emalloc(sizeof(struct topological));

    /* Initializing values */
    size_v = sizeof(int) * vDigraph(G);
    top->is_dag = FALSE;
    top->top_order = NULL;
    top->pre_order = newBag();
    top->pos_order = newBag();
    top->cycle_order = NULL;
    top->top_rank = ecalloc(size_v, sizeof(int));
    top->pre_rank = ecalloc(size_v, sizeof(int));
    top->pos_rank = ecalloc(size_v, sizeof(int));

    /* Setting up values and making the magic happens */

    /* DFS for cycle checking */
    top->on_stack = ecalloc(size_v, sizeof(int));
    top->marked = ecalloc(size_v, sizeof(int));
    top->edge_to = ecalloc(size_v, sizeof(int));

    top->pre_counter = 0;
    top->pos_counter = 0;

    for (v = 0; v < vDigraph(G); v++)
        if (!top->marked[v])
            dfs(G, top, v);

    top->is_dag = (top->cycle_order == NULL);

    free(top->on_stack);
    free(top->marked);
    free(top->edge_to);

    /* Rotating bags */
    aux = newBag();
    for (v = itens(top->pre_order, TRUE); v >= 0; v = itens(top->pre_order, FALSE))
        add(aux, v);
    freeBag(top->pre_order);
    top->pre_order = aux;

    aux = newBag();
    for (v = itens(top->pos_order, TRUE); v >= 0; v = itens(top->pos_order, FALSE))
        add(aux, v);
    top->top_order = top->pos_order;
    top->pos_order = aux;

    /* Topological sort rank */
    i = 0;
    for (v = itens(top->top_order, TRUE); v >= 0; v = itens(top->top_order, FALSE))
        top->top_rank[v] = i++;

    return top;
}

/*-----------------------------------------------------------*/
/*
 *  freeTopological(TS)
 *
 *  RECEBE uma representação topologica TS.
 *  DEVOLVE ao sistema toda a memória usada por TS.
 *
 */
void freeTopological(Topological ts)
{

    freeBag(ts->top_order);
    freeBag(ts->pre_order);
    freeBag(ts->pos_order);
    if (ts->cycle_order != NULL)
        freeBag(ts->cycle_order);

    free(ts->top_rank);
    free(ts->pre_rank);
    free(ts->pos_rank);

    free(ts);
}

/*------------------------------------------------------------*/
/*
 *  OPERAÇÕES:
 *
 */

/*-----------------------------------------------------------*/
/*
 *  HASCYCLE(TS)
 *
 *  RECEBE uma representação topológica TS de um digrafo;
 *  RETORNA TRUE seu o digrafo possui um ciclo e FALSE em caso
 *  contrário.
 *
 */
Bool hasCycle(Topological ts)
{
    return !ts->is_dag;
}

/*-----------------------------------------------------------*/
/*
 *  ISDAG(TS)
 *
 *  RECEBE um representação topológica TS de um digrafo.
 *  RETORNA TRUE se o digrafo for um DAG e FALSE em caso
 *  contrário.
 *
 */
Bool isDag(Topological ts)
{
    return ts->is_dag;
}

/*-----------------------------------------------------------*/
/*
 *  PRE(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  vértice V.
 *  RETORNA a numeração pré-ordem de V em TS.
 *
 */
int pre(Topological ts, vertex v)
{
    return ts->pre_rank[v];
}

/*-----------------------------------------------------------*/
/*
 *  POST(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  vértice V.
 *  RETORNA a numeração pós-ordem de V em TS.
 *
 */
int post(Topological ts, vertex v)
{
    return ts->pos_rank[v];
}

/*-----------------------------------------------------------*/
/*
 *  RANK(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  vértice V.
 *  RETORNA a posição de V na ordenação topológica em TS;
 *  retorna -1 se o digrafo não for um DAG.
 *
 */
int rank(Topological ts, vertex v)
{
    if (!ts->is_dag)
        return -1;

    return ts->top_rank[v];
}

/*-----------------------------------------------------------*/
/*
 *  PREORDER(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  Bool INIT.
 *
 *  Se INIT é TRUE,  PREORDER() RETORNA o primeiro vértice na ordenação pré-ordem de TS.
 *  Se INIT é FALSE, PREORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação pré-ordem de TS; se todos os vértices já foram retornados,
 *                   a função retorna -1.
 */
vertex preorder(Topological ts, Bool init)
{
    return itens(ts->pre_order, init);
}

/*-----------------------------------------------------------*/
/*
 *  POSTORDER(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  Bool INIT.
 *
 *  Se INIT é TRUE,  POSTORDER() RETORNA o primeiro vértice na ordenação pós-ordem de TS.
 *  Se INIT é FALSE, POSTORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação pós-ordem de TS; se todos os vértices já foram retornados,
 *                   a função retorna -1.
 */
vertex postorder(Topological ts, Bool init)
{
    return itens(ts->pos_order, init);
}

/*-----------------------------------------------------------*/
/*
 *  ORDER(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um Bool INIT.
 *
 *  Se INIT é TRUE,  ORDER() RETORNA o primeiro vértice na ordenação topológica
 *                   de TS.
 *  Se INIT é FALSE, ORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação topológica de TS; se todos os vértices já foram
 *                   retornados, a função retorna -1.
 *
 *  Se o digrafo _não_ é um DAG, ORDER() RETORNA -1.
 */
vertex order(Topological ts, Bool init)
{
    if (!ts->is_dag)
        return -1;

    return itens(ts->top_order, init);
}

/*-----------------------------------------------------------*/
/*
 *  CYCLE(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um Bool INIT.
 *
 *  Se INIT é TRUE,  CYCLE() RETORNA um vértice em um ciclo do digrafo.
 *  Se INIT é FALSE, CYCLE() RETORNA o vértice  no ciclo que é sucessor do
 *                   último vértice retornado; se todos os vértices no ciclo já
 *                   foram retornados, a função retorna -1.
 *
 *  Se o digrafo é um DAG, CYCLE() RETORNA -1.
 *
 */
vertex cycle(Topological ts, Bool init)
{
    if (ts->is_dag)
        return -1;

    return itens(ts->cycle_order, init);
}


/*------------------------------------------------------------*/
/*
 * Implementaçao de funções administrativas: têm o modificador
 * static.
 */

void dfs(Digraph G, Topological ts, vertex v) {

    vertex w, x;

    ts->on_stack[v] = TRUE;
    ts->marked[v] = TRUE;

    /* Pre-order stuff */
    ts->pre_rank[v] = ts->pre_counter++;
    add(ts->pre_order, v);

    for (w = adj(G, v, TRUE); w >= 0; w = adj(G, v, FALSE)) {


        /* short circuit if directed cycle found
        if (ts->cycle_order != NULL) continue;*/

        /* Found new vertex, so recur */
        if (!ts->marked[w]) {

            ts->edge_to[w] = v;
            dfs(G, ts, w);
        }

        /* Trace back directed cycle */
        else if (ts->on_stack[w] && ts->cycle_order == NULL) {
            ts->cycle_order = newBag();
            for (x = v; x != w; x = ts->edge_to[x])
                add(ts->cycle_order, x);

            add(ts->cycle_order, w);
        }

    }
    ts->on_stack[v] = FALSE;

    /* Pos-order stuff */
    ts->pos_rank[v] = ts->pos_counter++;
    add(ts->pos_order, v);
}
