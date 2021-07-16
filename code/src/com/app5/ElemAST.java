package com.app5;

/** @author Ahmed Khoumsi */

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST
 */
public abstract class ElemAST {
  public ElemAST parent = null;
  /** Evaluation d'AST
   */
  public abstract int EvalAST() throws NumberFormatException;


  /** Lecture d'AST
   */
  public abstract String LectAST();

  public abstract String toPostfix();

  /** ErreurEvalAST() envoie un message d'erreur lors de la construction d'AST
 */  
//  public void ErreurEvalAST(String s) {
//    //
//  }

}