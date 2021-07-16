package com.app5;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  public ElemAST left = null;
  public ElemAST right = null;
  public Terminal<Object> terminal = null;


  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(Terminal<Object> term ) { // avec arguments
    terminal = term;
  }

  /** Evaluation de noeud d'AST
   */
  public float EvalAST( ) {
     //
    float result =0;
    if(terminal.getType() == Terminal.Type.ADD){
      result = left.EvalAST() + right.EvalAST();
    }
    else if(terminal.getType() == Terminal.Type.SOUS){
      result = left.EvalAST() - right.EvalAST();
    }
    else if(terminal.getType() == Terminal.Type.MULT){
      result = left.EvalAST() * right.EvalAST();
    }
    else if(terminal.getType() == Terminal.Type.DIV){
      result = left.EvalAST() / right.EvalAST();
    }
    else
      return -1;
    return result;
  }



  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
     String out ="";
     out+= "("+ left.LectAST() + " " + terminal.getValue() + " " + right.LectAST() + ")" ;

    return out;
  }

  @Override
  public String toPostfix() {
    return left.toPostfix() + ' ' + right.toPostfix() + ' ' + terminal.getValue();
  }

}


