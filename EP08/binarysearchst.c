/*
 * MAC0323 Estruturas de Dados e Algoritmo II
 *
 * Tabela de simbolos implementada atraves de vetores ordenados
 * redeminsionaveis
 *
 *     https://algs4.cs.princeton.edu/31elementary/BinarySearchST.java.html
 *
 * As chaves e valores desta implementaÃ§Ã£o sÃ£o mais ou menos
 * genÃ©ricos
 */

/* interface para o uso da funcao deste mÃ³dulo */
#include "binarysearchst.h"

#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc(), ecalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debug */
#endif

/*
 * CONSTANTES
 */

const int INIT_CAPACITY = 2;

/*----------------------------------------------------------*/
/*
 * Estrutura BÃ¡sica da Tabela de SÃ­mbolos:
 *
 * implementaÃ§Ã£o com vetores ordenados
 */
struct binarySearchST {

    void** keys;
    void** vals;
    int size;
    int max_size; /* Maximum number of elements that can be stored */

    int (*compare)(const void *key1, const void *key2);

    /*  Used for keys iterator */
    int last_k;
};

/*------------------------------------------------------------*/
/*
 * FunÃ§Ãµes administrativas
 */

void resize(BinarySearchST st, int n_size) {
    /* Resize arrays of st */

    int i = 0, n = st->size;
    void** tempk = emalloc(sizeof(void*) * n_size);
    void** tempv = emalloc(sizeof(void*) * n_size);
    for (i = 0; i < n; i++) {
        tempk[i] = st->keys[i];
        tempv[i] = st->vals[i];
    }
    free(st->keys);
    free(st->vals);
    st->vals = tempv;
    st->keys = tempk;
}


/*-----------------------------------------------------------*/
/*
 *  initST(COMPAR)
 *
 *  RECEBE uma funÃ§Ã£o COMPAR() para comparar chaves.
 *  RETORNA (referÃªncia/ponteiro para) uma tabela de sÃ­mbolos vazia.
 *
 *  Ã‰ esperado que COMPAR() tenha o seguinte comportamento:
 *
 *      COMPAR(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      COMPAR(key1, key2) retorna 0              se key1 == key2
 *      COMPAR(key1, key2) retorna um inteiro > 0 se key1 >  key2
 *
 *  TODAS OS OPERAÃ‡Ã•ES da ST criada utilizam a COMPAR() para comparar
 *  chaves.
 *
 */
BinarySearchST initST(int (*compar)(const void *key1, const void *key2))
{
    /* Symble table that will be inittialized */
    BinarySearchST new_st = emalloc(sizeof(struct binarySearchST));

    /* Initializing arrays and values */
    new_st->compare = compar;
    new_st->max_size = INIT_CAPACITY;
    new_st->size = 0;
    new_st->vals = emalloc(sizeof(void*) * INIT_CAPACITY);
    new_st->keys = emalloc(sizeof(void*) * INIT_CAPACITY);
    new_st->last_k = 0;

    return new_st;
}

/*-----------------------------------------------------------*/
/*
 *  freeST(ST)
 *
 *  RECEBE uma BinarySearchST  ST e devolve ao sistema toda a memoria
 *  utilizada por ST.
 *
 */
void freeST(BinarySearchST st)
{
    /* Freeing stuff */
    int n = st->max_size, i = 0;
    for (; i < n; i++){
        free(st->keys[i]);
        free(st->vals[i]);
    }
    free(st->vals);
    free(st->keys);
    free(st);
}

/*------------------------------------------------------------*/
/*
 * OPERAÃ‡Ã•ES USUAIS: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/*-----------------------------------------------------------*/
/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 *
 *  RECEBE a tabela de sÃ­mbolos ST e um par KEY-VAL e pst->last_k-rocura a KEY na ST.
 *
 *     - se VAL Ã© NULL, a entrada da chave KEY Ã© removida da ST
 *
 *     - se KEY nao e' encontrada: o par KEY-VAL Ã© inserido na ST
 *
 *     - se KEY e' encontra: o valor correspondente Ã© atualizado
 *
 *  NKEY Ã© o nÃºmero de bytes de KEY e NVAL Ã© o nÃºmero de bytes de NVAL.
 *
 *  Para criar uma copia/clone de KEY Ã© usado o seu nÃºmero de bytes NKEY.
 *  Para criar uma copia/clode de VAL Ã© usado o seu nÃºmero de bytes NVAL.
 *
 */
void put(BinarySearchST st, const void *key, size_t nKey, const void *val, size_t nVal)
{
    int i = rank(st, key);
    int j = st->size;

    void* clonev = malloc(sizeof(void*));
    void* clonek = malloc(sizeof(void*));

    if (val == NULL) {
        delete(st, key);
        return;
    }

    /* Cloning const */
    memcpy(clonek, key, sizeof(void*));
    memcpy(clonev, val, sizeof(void*));

    /* Key is already in table, update its value */
    if (i < st->size && st->compare(key, st->keys[i]) == 0) {
        st->vals[i] = clonev;
        return;
    }

    /* Insert new key-value pair */
    if (st->size == st->max_size){
        st->max_size *= 2;
        resize(st, st->max_size);
    }

    for (; j > i; j--)  {
        st->keys[j] = st->keys[j-1];
        st->vals[j] = st->vals[j-1];
    }

    st->keys[i] = clonek;
    st->vals[i] = clonev;
    st->size++;
}

/*-----------------------------------------------------------*/
/*
 *  get(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 *
 *     - se KEY estÃ¡ em ST, RETORNA NULL;
 *
 *     - se KEY nÃ£o estÃ¡ em ST, RETORNA uma cÃ³pia/clone do valor
 *       associado a KEY.
 *
 */
void* get(BinarySearchST st, const void *key)
{
    int i = rank(st, key);
    if (isEmpty(st))
        return NULL;
    if (i < st->size && st->compare(st->keys[i], key) == 0)
        return st->vals[i];
    return NULL;
}

/*-----------------------------------------------------------*/
/*
 *  CONTAINS(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 *
 *  RETORNA TRUE se KEY estÃ¡ na ST e FALSE em caso contrÃ¡rio.
 *
 */
Bool contains(BinarySearchST st, const void *key)
{
    return get(st, key) != NULL;
}

/*-----------------------------------------------------------*/
/*
 *  DELETE(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 *
 *  Se KEY estÃ¡ em ST, remove a entrada correspondente a KEY.
 *  Se KEY nÃ£o estÃ¡ em ST, faz nada.
 *
 */
void delete(BinarySearchST st, const void *key)
{
    /* compute rank */
    int i = rank(st, key), j = i;

    if (isEmpty(st))
        return;

    /* Key not in table */
    if (i == st->size || st->compare(key, st->keys[i]) != 0)
        return;

    for (; j < st->size-1; j++) {
        st->keys[j] = st->keys[j+1];
        st->vals[j] = st->vals[j+1];
    }

    st->size--;
    st->keys[st->size] = NULL;
    st->vals[st->size] = NULL;

    /* Resize if 1/4 full */
    if (st->size > 0 && st->size == st->max_size/4) {
        st->max_size /= 2;
        resize(st, st->max_size);
    }
}

/*-----------------------------------------------------------*/
/*
 *  SIZE(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST.
 *
 *  RETORNA o nÃºmero de itens (= pares chave-valor) na ST.
 *
 */
int size(BinarySearchST st)
{
    return st->size;
}

/*-----------------------------------------------------------*/
/*
 *  ISEMPTY(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST.
 *
 *  RETORNA TRUE se ST estÃ¡ vazia e FALSE em caso contrÃ¡rio.
 *
 */
Bool isEmpty(BinarySearchST st)
{
    return (st->size == 0);
}


/*------------------------------------------------------------*/
/*
 * OPERAÃ‡Ã•ES PARA TABELAS DE SÃMBOLOS ORDENADAS:
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/*-----------------------------------------------------------*/
/*
 *  MIN(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e RETORNA uma cÃ³pia/clone
 *  da menor chave na tabela.
 *
 *  Se ST estÃ¡ vazia RETORNA NULL.
 *
 */
void* min(BinarySearchST st)
{
    return st->keys[0];
}

/*-----------------------------------------------------------*/
/*
 *  MAX(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e RETORNA uma cÃ³pia/clone
 *  da maior chave na tabela.
 *
 *  Se ST estÃ¡ vazia RETORNA NULL.
 *
 */
void* max(BinarySearchST st)
{
    return st->keys[st->size - 1];
}

/*-----------------------------------------------------------*/
/*
 *  RANK(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 *  RETORNA o nÃºmero de chaves em ST menores que KEY.
 *
 *  Se ST estÃ¡ vazia RETORNA NULL.
 *
 */
int rank(BinarySearchST st, const void *key)
{
    /* Return the index in the array that key is located */

    int lo = 0, hi = st->size-1;

    if (st->size == 0 || key == NULL)
        return 0;

    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        int cmp = st->compare(key, st->keys[mid]); /* Talvez mudar a orden */
        if (cmp < 0) hi = mid - 1;
        else if (cmp > 0) lo = mid + 1;
        else return mid;
    }
    return lo;
}

/*-----------------------------------------------------------*/
/*
 *  SELECT(ST, K)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e um inteiro K >= 0.
 *  RETORNA a (K+1)-Ã©sima menor chave da tabela ST.
 *
 *  Se ST nÃ£o tem K+1 elementos RETORNA NULL.
 *
 */
void* select(BinarySearchST st, int k)
{
    if (k >= st->size)
        return NULL;

    return st->keys[k];
}

/*-----------------------------------------------------------*/
/*
 *  deleteMIN(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e remove a entrada correspondente
 *  Ã  menor chave.
 *
 *  Se ST estÃ¡ vazia, faz nada.
 *
 */
void deleteMin(BinarySearchST st)
{
    delete(st, min(st));
}

/*-----------------------------------------------------------*/
/*
 *  deleteMAX(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e remove a entrada correspondente
 *  Ã  maior chave.
 *
 *  Se ST estÃ¡ vazia, faz nada.
 *
 */
void deleteMax(BinarySearchST st)
{
    delete(st, max(st));
}

/*-----------------------------------------------------------*/
/*
 *  KEYS(ST, INIT)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e um Bool INIT.
 *
 *  Se INIT Ã© TRUE, KEYS() RETORNA uma cÃ³pia/clone da menor chave na ST.
 *  Se INIT Ã© FALSE, KEYS() RETORNA a chave sucessora da Ãºltima chave retornada.
 *  Se ST estÃ¡ vazia ou nÃ£o hÃ¡ sucessora da Ãºltima chave retornada, KEYS() RETORNA NULL.
 *
 *  Se entre duas chamadas de KEYS() a ST Ã© alterada, o comportamento Ã©
 *  indefinido.
 *
 */
void* keys(BinarySearchST st, Bool init)
{
    void* clone = malloc(sizeof(void*));
    if (init) {

        memcpy(clone, min(st), sizeof(void*));

        st->last_k++;
        return clone;
    }

    if (isEmpty(st) || st->last_k-1 == st->size)
        return NULL;

    st->last_k++;

    memcpy(clone, select(st, st->last_k), sizeof(void*));
    return clone;
}

/*-----------------------------------------------------------*/
/*
  Visit each entry on the ST.

  The VISIT function is called, in-order, with each pair key-value in the ST.
  If the VISIT function returns zero, then the iteration stops.

  visitST returns zero if the iteration was stopped by the visit function,
  nonzero otherwise.
*/
int visitST(BinarySearchST st, int (*visit)(const void *key, const void *val))
{
    int i = 0;
    for (; i < st->size; i++){
        if (visit(st->keys[i], st->vals[i]) == 0)
            return 0;
    }

    return EXIT_SUCCESS;
}


/*------------------------------------------------------------*/
/*
 * FunÃ§Ãµes administrativas
 */
