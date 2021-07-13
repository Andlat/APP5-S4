package com.nikni.app5lab;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  public ElemAST left = null;
  public ElemAST right = null;
  public Terminal terminal = null;


  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(Terminal<Object> term ) { // avec arguments
    terminal = term;
  }

  /** Evaluation de noeud d'AST
   */
  public int EvalAST( ) {
     //
    return -1;
  }



  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
     //
    return "";
  }

}


