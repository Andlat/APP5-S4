package com.nikni.app5lab;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  // Attribut(s)
    Terminal<Object> term;


/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST( Terminal<Object> term) {  // avec arguments
    this.term = term;
  }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST( ) {
      int res = Integer.parseInt((String)term.getValue());
      return res;
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {

      return "Feuille Contient: " + (String)term.getValue();
  }

    @Override
    public String toPostfix() {
        return term.getValue().toString();
    }

}
