package com.app5;

/** @author Ahmed Khoumsi */

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST
 */
public abstract class ElemAST {
  public ElemAST parent = null;
  /** Evaluation d'AST
   */
  public abstract float EvalAST() throws NumberFormatException;


  /** Lecture d'AST
   */
  public abstract String LectAST();

  public abstract String toPostfix();

}
