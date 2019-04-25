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

#include "binarysearchst.h"

#include <stdlib.h>
#include <string.h>
#include "util.h"

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>
#endif

/*
 * Constants
 */
const int INIT_CAPACITY = 2;

/*
 * Symble table basic structure:
 *
 * Implementation with sorted array
 */
struct binarySearchST {

    void** keys;
    void** vals;
    int size;
    int max_size; /* Maximum number of elements that can be stored */
    size_t* v_sizes; /* Size of each value */
    size_t* k_sizes; /* Size of each key */

    int (*compare)(const void *key1, const void *key2);

    /*  Used for keys iterator */
    int last_k;
};

/*
 * Aux functions
 */
void resize(BinarySearchST st, int n_size) {
    /* Resize arrays of st */

    int i, n = st->size;
    /* Mallocing auxiliar stuff */
    void** tempk = emalloc(sizeof(void*) * n_size);
    void** tempv = emalloc(sizeof(void*) * n_size);
    size_t* temp_nk = emalloc(sizeof(size_t) * n_size);
    size_t* temp_nv = emalloc(sizeof(size_t) * n_size);

    /* Copying */
    for (i = 0; i < n; i++) {
        tempk[i] = st->keys[i];
        tempv[i] = st->vals[i];
        temp_nk[i] = st->k_sizes[i];
        temp_nv[i] = st->v_sizes[i];
    }

    /* Freeing old stuff */
    free(st->v_sizes);
    free(st->k_sizes);
    free(st->keys);
    free(st->vals);

    /* Assigning new references */
    st->vals = tempv;
    st->keys = tempk;
    st->v_sizes = temp_nv;
    st->k_sizes = temp_nk;
}

/*
 *  initST(COMPAR)
 *
 *  Receive a function compar() to compre keys.
 *  Return pointer to empty symble table.
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
    new_st->v_sizes = emalloc(sizeof(size_t) * INIT_CAPACITY);
    new_st->k_sizes = emalloc(sizeof(size_t) * INIT_CAPACITY);
    new_st->last_k = 0;

    return new_st;
}

/*
 *  freeST(ST)
 *
 *  Receive a ST and return all the used memory to the system
 *
 */
void freeST(BinarySearchST st)
{
    /* Freeing stuff */
    int n = st->max_size, i;
    for (i = 0; i < n; i++){
        free(st->keys[i]);
        free(st->vals[i]);
    }
    free(st->v_sizes);
    free(st->k_sizes);
    free(st->vals);
    free(st->keys);
    free(st);
}

/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 *
 *  Receive a ST and an pair key-val, to insert them in the ST.
 *
 *     - If val is NULL, the key is removed from the ST
 *
 *     - If key is not found, it is inserted in the ST
 *
 *     - If key is found, it is updated
 *
 */
void put(BinarySearchST st, const void *key, size_t nKey, const void *val, size_t nVal)
{
    int i = rank(st, key);
    int j = st->size;

    void* clonev = emalloc(nVal);
    void* clonek = emalloc(nKey);

    /* Cloning const */
    memcpy(clonek, key, nKey);
    memcpy(clonev, val, nVal);

    /* Delete if val is NULL */
    if (val == NULL) {
        free(clonev);
        free(clonek);
        delete(st, key);
        return;
    }

    /* Key is already in table, update its value */
    if (i < st->size && st->compare(key, st->keys[i]) == 0) {

        free(st->vals[i]); /* Freeing of old pointer */
        st->v_sizes[i] = nVal;
        st->vals[i] = clonev;
        free(clonek);
        return;
    }

    /* Insert new key-value pair */
    if (st->size == st->max_size){
        st->max_size *= 2;
        resize(st, st->max_size);
    }

    /* Bringing vector one position to the right */
    for (; j > i; j--)  {
        st->keys[j] = st->keys[j-1];
        st->vals[j] = st->vals[j-1];
        st->v_sizes[j] = st->v_sizes[j-1];
        st->k_sizes[j] = st->k_sizes[j-1];
    }

    /* Putting new value */
    st->keys[i] = clonek;
    st->vals[i] = clonev;
    st->v_sizes[i] = nVal;
    st->k_sizes[i] = nKey;
    st->size++;
}

/*
 *  get(ST, KEY)
 *
 *  Receives a ST and and a KEY,
 *
 *     - If the KEY is NULL, returns NULL
 *
 *     - Else, returns a copy of the value associated with that KEY
 *
 */
void* get(BinarySearchST st, const void *key)
{
    int i;
    void* aux = NULL;

    if (isEmpty(st) || key == NULL)
        return NULL;

    /* Position in ST arrays */
    i = rank(st, key);
    if (i < st->size && st->compare(st->keys[i], key) == 0) {
        aux = emalloc(st->v_sizes[i]);
        memcpy(aux, st->vals[i], st->v_sizes[i]);
        return aux;
    }

    return NULL;
}

/*
 *  CONTAINS(ST, KEY)
 *
 *  Receives a ST and a KEY
 *
 *  Returns TRUE if KEY is in ST, FALSE otherwise
 *
 */
Bool contains(BinarySearchST st, const void *key)
{
    return get(st, key) != NULL;
}

/*
 *  DELETE(ST, KEY)
 *
 *  Receives a ST and a KEY
 *
 *  If KEY is in ST, removes it
 *  Does nothing, otherwise.
 *
 */
void delete(BinarySearchST st, const void *key)
{
    /* compute rank */
    int i = rank(st, key), j = i;

    if (isEmpty(st) || key == NULL)
        return;

    /* Key is not in the table */
    if (i == st->size || st->compare(key, st->keys[i]) != 0)
        return;

    /* Freeing deleted values */
    free(st->keys[i]);
    free(st->vals[i]);

    /* Bringing array one position to the front */
    for (; j < (st->size)-1; j++) {
        st->keys[j] = st->keys[j+1];
        st->vals[j] = st->vals[j+1];
        st->v_sizes[j] = st->v_sizes[j+1];
        st->k_sizes[j] = st->k_sizes[j+1];
    }

    st->size--;
}

/*
 *  SIZE(ST)
 *
 *  Receives a ST
 *
 *  Returns it's size.
 *
 */
int size(BinarySearchST st)
{
    return st->size;
}

/*
 *  ISEMPTY(ST, KEY)
 *
 *  Receives a ST.
 *
 *  Returns TRUE if it's empty, FALSE otherwise.
 *
 */
Bool isEmpty(BinarySearchST st)
{
    return (st->size == 0);
}

/*
 *  MIN(ST)
 *
 *  Receives a ST and returns a copy of the minimal element of it.
 *
 *  If ST is empty, returns NULL
 *
 */
void* min(BinarySearchST st)
{
    void* aux;
    if (isEmpty(st))
        return NULL;

    /* Creating a copy */
    aux = emalloc(st->k_sizes[0]);
    memcpy(aux, st->keys[0], st->k_sizes[0]);
    return aux;
}

/*
 *  MAX(ST)
 *
 *  Receives a ST and returns a copy of the maximum element of it.
 *
 *  If ST is empty, returns NULL
 */
void* max(BinarySearchST st)
{
    void* aux;
    if (isEmpty(st))
        return NULL;

    /* Creating a copy */
    aux = emalloc(st->k_sizes[st->size - 1]);
    memcpy(aux, st->keys[st->size - 1], st->k_sizes[st->size - 1]);
    return aux;
}

/*
 *  RANK(ST, KEY)
 *
 *  Receives a ST and a KEY.
 *  Returns the number of keys smaller than KEY.
 *
 *  If ST is empty, returns NULL.
 *
 */
int rank(BinarySearchST st, const void *key)
{
    int lo = 0, hi = st->size-1;
    if (isEmpty(st) || key == NULL)
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

/*
 *  SELECT(ST, K)
 *
 *  Receives a ST and an integer K >= 0
 *  Returns a copy of the (K+1)-th smallest key of ST.
 *
 *  Return NULL if K is bigger than the ST.
 *
 */
void* select(BinarySearchST st, int k)
{
    void* aux;
    if (k >= st->size)
        return NULL;

    aux = emalloc(st->k_sizes[k]);
    memcpy(aux, st->keys[k], st->k_sizes[k]);
    return aux;
}

/*
 *  deleteMIN(ST)
 *
 *  Receives a ST and remove the entry corresponding to the minimal key.
 *
 */
void deleteMin(BinarySearchST st)
{
    void* aux = min(st);
    delete(st, aux);
    free(aux);
}

/*
 *  deleteMAX(ST)
 *
 *  Receives a ST and remove the entry corresponding to the maximal key.
 *
 */
void deleteMax(BinarySearchST st)
{
    void* aux = max(st);
    delete(st, aux);
    free(aux);
}

/*
 *  KEYS(ST, INIT)
 *
 *  Receives a ST and a bool INIT.
 *
 *  If INIT is TRUE returns a copy of the value associated with the minimal key.
 *  Else, returns a copy of the next value.
 *  Returns NULL if the ST is empty nor there are no more elements.
 *
 */
void* keys(BinarySearchST st, Bool init)
{
    void* clone;
    void* aux;

    if (init) {

        clone = emalloc(st->k_sizes[0]);
        aux = min(st);
        memcpy(clone, aux, st->k_sizes[0]);

        free(aux);
        st->last_k = 1;
        return clone;
    }

    if (isEmpty(st) || st->last_k == st->size)
        return NULL;

    clone = emalloc(st->k_sizes[st->last_k]);
    aux = select(st, st->last_k);
    memcpy(clone, aux, st->k_sizes[st->last_k]);

    st->last_k++;
    free(aux);
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
    for (; i < st->size; i++)
        if (visit(st->keys[i], st->vals[i]) == 0)
            return 0;


    return EXIT_SUCCESS;
}
