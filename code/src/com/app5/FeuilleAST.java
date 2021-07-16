package com.app5;

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
      try {
          return Integer.parseInt((String) term.getValue());
      }catch(NumberFormatException ex){
          throw new NumberFormatException(term.getValue().toString());
      }
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {

      return  (String)term.getValue();
  }

    @Override
    public String toPostfix() {
        return term.getValue().toString();
    }

}
