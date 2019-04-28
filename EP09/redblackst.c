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

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
#include "redblackst.h"

#include <stdlib.h>
#include <string.h>
#include "util.h"

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>
#endif

/*
 * CONSTANTES
 */
#define RED   TRUE
#define BLACK FALSE

/*----------------------------------------------------------*/
/*
 * Estrutura Básica da Tabela de Símbolos:
 *
 * implementação com árvore rubro-negra
 */
typedef struct node Node;
struct redBlackST {

    Node *root; /* Root of RB-tree */
    int size; /* Number of nodes */

    /* Function pointer to compare elements of the tree */
    int (*compar)(const void *key1, const void *key2);
};

/*----------------------------------------------------------*/
/*
 * Estrutura de um nó da árvore
 *
 */
struct node {

    /* Data of the node */
    void *val;
    void *key;

    /* Size of generic data */
    int v_size, k_size;

    /* Children */
    Node *left;
    Node *right;

    Bool color; /* Kind of node connection */
    int size; /* Size of subtree */
};

/*------------------------------------------------------------*/
/*
 *  Protótipos de funções administrativas.
 *
 *  Entre essa funções estão isRed(), rotateLeft(), rotateRight(),
 *  flipColors(), moveRedLeft(), moveRedRight() e balance().
 *
 *  Não deixe de implmentar as funções chamadas pela função
 *  check(): isBST(), isSizeConsistent(), isRankConsistent(),
 *  is23(), isBalanced().
 *
 */

/*---------------------------------------------------------------*/
static Bool
isBST(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
isSizeConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
isRankConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
is23(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
isBalanced(RedBlackST st);

/*-----------------------------------------------------------*/
/*
 *  Funções auxiliares
 */

/* Size of a Node */
int n_size(Node *t) {

    /* Asserting readbility */
    if (t == NULL)
        return 0;

    return t->size;
}

/* Rotate the node to the right */
Node *rotateRight(Node *h) {

    Node *aux = h->left;
    h->left = aux->right;
    aux->right = h;
    aux->color = aux->right->color;
    aux->right->color = RED;
    aux->size = h->size;
    h->size = n_size(h->left) + n_size(h->right) + 1;
    return aux;
}

/* Rotate the node to the left */
Node *rotateLeft(Node *h) {

    Node *aux = h->right;
    h->right = aux->left;
    aux->left = h;
    aux->color = aux->left->color;
    aux->left->color = RED;
    aux->size = h->size;
    h->size = n_size(h->left) + n_size(h->right) + 1;
    return aux;
}

/* Flip the colors of an node and its children */
void flipColors(Node *h) {

    h->color = !h->color;
    h->left->color = !h->left->color;
    h->right->color = !h->right->color;
}

Bool isRed(Node *n) {

    if (n == NULL)
        return FALSE;

    return (n->color == RED);

}

/*-----------------------------------------------------------*/
/*
 *  initST(COMPAR)
 *
 *  RECEBE uma função COMPAR() para comparar chaves.
 *  RETORNA (referência/ponteiro para) uma tabela de símbolos vazia.
 *
 *  É esperado que COMPAR() tenha o seguinte comportamento:
 *
 *      COMPAR(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      COMPAR(key1, key2) retorna 0              se key1 == key2
 *      COMPAR(key1, key2) retorna um inteiro > 0 se key1 >  key2
 *
 *  TODAS OS OPERAÇÕES da ST criada utilizam a COMPAR() para comparar
 *  chaves.
 *
 */
RedBlackST initST(int (*compar)(const void *key1, const void *key2))
{

    /* Allocating memory for the new RB-tree and init values */
    RedBlackST new = emalloc(sizeof(struct redBlackST));
    new->size = 0;
    new->compar = compar;
    new->root = NULL;

    return new;
}

/*-----------------------------------------------------------*/
/*
 *  freeST(ST)
 *
 *  RECEBE uma RedBlackST  ST e devolve ao sistema toda a memoria
 *  utilizada por ST.
 *
 */
void freeST(RedBlackST st)
{

    /* Vou ter que fazer algo em pos-ordem */

}


/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/*-----------------------------------------------------------*/
/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 *
 *  RECEBE a tabela de símbolos ST e um par KEY-VAL e procura a KEY na ST.
 *
 *     - se VAL é NULL, a entrada da chave KEY é removida da ST
 *
 *     - se KEY nao e' encontrada: o par KEY-VAL é inserido na ST
 *
 *     - se KEY e' encontra: o valor correspondente é atualizado
 *
 *  NKEY é o número de bytes de KEY e NVAL é o número de bytes de NVAL.
 *
 *  Para criar uma copia/clone de KEY é usado o seu número de bytes NKEY.
 *  Para criar uma copia/clode de VAL é usado o seu número de bytes NVAL.
 *
 */

Node *r_put(RedBlackST st, Node *act, Node *new) {

    int cmp;

    if (act == NULL)
        return new;

    cmp = st->compar(new->key, act->key);

    if      (cmp < 0) act->left = r_put(st, act->left, new);
    else if (cmp > 0) act->right = r_put(st, act->right, new);
    else {

        free(act->val);
        act->val = new->val;
        act->v_size = new->v_size;
        free(new->key);
        free(new);

    }

    if (isRed(act->right) && !isRed(act->left))  act = rotateLeft(act);
    if (isRed(act->left)  &&  isRed(act->left->left)) act = rotateRight(act);
    if (isRed(act->left)  &&  isRed(act->right))     flipColors(act);
    act->size = n_size(act->left) + n_size(act->right) + 1;

    return act;
}

void put(RedBlackST st, const void *key, size_t sizeKey, const void *val, size_t sizeVal)
{
    void *auxk, *auxv;
    Node *new_node;
    
    /* To be removed */
    if (val == NULL) {
        delete(st, key);
        return;
    }

    auxk = emalloc(sizeKey);
    auxv = emalloc(sizeVal);

    memcpy(auxk, key, sizeKey);
    memcpy(auxv, val, sizeVal);

    new_node = emalloc(sizeof(Node));
    new_node->val = auxv;
    new_node->key = auxk;
    new_node->v_size = sizeVal;
    new_node->k_size = sizeKey;
    new_node->left = NULL;
    new_node->right = NULL;
    new_node->color = RED;
    new_node->size = 1;

    st->root = r_put(st, st->root, new_node);
    st->root->color = BLACK;

}


/*-----------------------------------------------------------*/
/*
 *  get(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *     - se KEY está em ST, RETORNA NULL;
 *
 *     - se KEY não está em ST, RETORNA uma cópia/clone do valor
 *       associado a KEY.
 *
 */
void *get(RedBlackST st, const void *key)
{
    Node *aux = st->root; /* Used for traversing the tree */
    Bool find = FALSE; /* Flag to tell if something is find */
    void *ans; /* Will receive the clone of found value */

    /* Going down the tree */
    while (aux != NULL && !find) {

        int cmp = st->compar(key, aux->key);

        if      (cmp < 0) aux = aux->left;
        else if (cmp > 0) aux = aux->left;
        else    find = TRUE;
    }

    if (!find)
        return NULL;

    /* Cloning val */
    ans = emalloc(aux->v_size);
    memcpy(ans, aux->val, aux->v_size);

    return ans;
}


/*-----------------------------------------------------------*/
/*
 *  CONTAINS(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *  RETORNA TRUE se KEY está na ST e FALSE em caso contrário.
 *
 */
Bool contains(RedBlackST st, const void *key)
{
    return (get(st, key) != NULL);
}

/*-----------------------------------------------------------*/
/*
 *  DELETE(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *  Se KEY está em ST, remove a entrada correspondente a KEY.
 *  Se KEY não está em ST, faz nada.
 *
 */
void
delete(RedBlackST st, const void *key)
{
}


/*-----------------------------------------------------------*/
/*
 *  SIZE(ST)
 *
 *  RECEBE uma tabela de símbolos ST.
 *
 *  RETORNA o número de itens (= pares chave-valor) na ST.
 *
 */
int size(RedBlackST st)
{
    return st->size;
}


/*-----------------------------------------------------------*/
/*
 *  ISEMPTY(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST.
 *
 *  RETORNA TRUE se ST está vazia e FALSE em caso contrário.
 *
 */
Bool isEmpty(RedBlackST st)
{
    return (st->size == 0);
}

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES PARA TABELAS DE SÍMBOLOS ORDENADAS:
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/*-----------------------------------------------------------*/
/*
 *  MIN(ST)
 *
 *  RECEBE uma tabela de símbolos ST e RETORNA uma cópia/clone
 *  da menor chave na tabela.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
void *min(RedBlackST st)
{
    void *ans; /* Store the clone to be returned */
    Node *aux; /* Node to traverse tree */

    /* Start from the root */
    aux = st->root;

    while (aux != NULL) {

        /* Going to the left to the tree */
        if (aux->left != NULL)
            aux = aux->left;

        /* Already on minimal */
        else
            break;
    }

    if (aux == NULL)
        return NULL;

    /* Cloning */
    ans = emalloc(aux->v_size);
    memcpy(ans, aux->val, aux->v_size);

    return ans;
}


/*-----------------------------------------------------------*/
/*
 *  MAX(ST)
 *
 *  RECEBE uma tabela de símbolos ST e RETORNA uma cópia/clone
 *  da maior chave na tabela.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
void *max(RedBlackST st)
{
    void *ans; /* Store the clone to be returned */
    Node *aux; /* Node to traverse tree */

    /* Start from the root */
    aux = st->root;

    while (aux != NULL) {

        /* Going to the left to the tree */
        if (aux->right != NULL)
            aux = aux->left;

        /* Already on minimal */
        else
            break;
    }

    if (aux == NULL)
        return NULL;

    /* Cloning */
    ans = emalloc(aux->v_size);
    memcpy(ans, aux->val, aux->v_size);

    return ans;
}


/*-----------------------------------------------------------*/
/*
 *  RANK(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *  RETORNA o número de chaves em ST menores que KEY.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
int
rank(RedBlackST st, const void *key)
{
    return 0;
}


/*-----------------------------------------------------------*/
/*
 *  SELECT(ST, K)
 *
 *  RECEBE uma tabela de símbolos ST e um inteiro K >= 0.
 *  RETORNA a (K+1)-ésima menor chave da tabela ST.
 *
 *  Se ST não tem K+1 elementos RETORNA NULL.
 *
 */
void *
select(RedBlackST st, int k)
{
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  deleteMIN(ST)
 *
 *  RECEBE uma tabela de símbolos ST e remove a entrada correspondente
 *  à menor chave.
 *
 *  Se ST está vazia, faz nada.
 *
 */
void
deleteMin(RedBlackST st)
{
}


/*-----------------------------------------------------------*/
/*
 *  deleteMAX(ST)
 *
 *  RECEBE uma tabela de símbolos ST e remove a entrada correspondente
 *  à maior chave.
 *
 *  Se ST está vazia, faz nada.
 *
 */
void
deleteMax(RedBlackST st)
{
}


/*-----------------------------------------------------------*/
/*
 *  KEYS(ST, INIT)
 *
 *  RECEBE uma tabela de símbolos ST e um Bool INIT.
 *
 *  Se INIT é TRUE, KEYS() RETORNA uma cópia/clone da menor chave na ST.
 *  Se INIT é FALSE, KEYS() RETORNA a chave sucessora da última chave retornada.
 *  Se ST está vazia ou não há sucessora da última chave retornada, KEYS() retorna NULL.
 *
 *  Se entre duas chamadas de KEYS() a ST é alterada, o comportamento é
 *  indefinido.
 *
 */
void *
keys(RedBlackST st, Bool init)
{
    return NULL;
}



/*------------------------------------------------------------*/
/*
 * Funções administrativas
 */

/***************************************************************************
 *  Utility functions.
 ***************************************************************************/

/*
 * HEIGHT(ST)
 *
 * RECEBE uma RedBlackST e RETORNA a sua altura.
 * Uma BST com apenas um nó tem altura zero.
 *
 */
int
height(RedBlackST st)
{
        return 0;
}


/***************************************************************************
 *  Check integrity of red-black tree data structure.
 ***************************************************************************/

/*
 * CHECK(ST)
 *
 * RECEBE uma RedBlackST ST e RETORNA TRUE se não encontrar algum
 * problema de ordem ou estrutural. Em caso contrário RETORNA
 * FALSE.
 *
 */
Bool
check(RedBlackST st)
{
    if (!isBST(st))            ERROR("check(): not in symmetric order");
    if (!isSizeConsistent(st)) ERROR("check(): subtree counts not consistent");
    if (!isRankConsistent(st)) ERROR("check(): ranks not consistent");
    if (!is23(st))             ERROR("check(): not a 2-3 tree");
    if (!isBalanced(st))       ERROR("check(): not balanced");
    return isBST(st) && isSizeConsistent(st) && isRankConsistent(st) && is23(st) && isBalanced(st);
}


/*
 * ISBST(ST)
 *
 * RECEBE uma RedBlackST ST.
 * RETORNA TRUE se a árvore é uma BST.
 *
 */
static Bool
isBST(RedBlackST st)
{
    return FALSE;
}


/*
 *  ISSIZECONSISTENT(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se para cada nó h
 *  vale que size(h) = 1 + size(h->left) + size(h->right) e
 *  FALSE em caso contrário.
 */
static Bool
isSizeConsistent(RedBlackST st)
{
    return FALSE;
}


/*
 *  ISRANKCONSISTENT(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se seus rank() e
 *  select() são consistentes.
 */
/* check that ranks are consistent */
static Bool
isRankConsistent(RedBlackST st)
{
    return FALSE;
}

/*
 *  IS23(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA FALSE se há algum link RED
 *  para a direta ou se ha dois links para esquerda seguidos RED
 *  Em caso contrário RETORNA TRUE (= a ST representa uma árvore 2-3).
 */
static Bool
is23(RedBlackST st)
{
    return FALSE;
}


/*
 *  ISBALANCED(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se st satisfaz
 *  balanceamento negro perfeiro.
 */
static Bool
isBalanced(RedBlackST st)
{
    return FALSE;
}
