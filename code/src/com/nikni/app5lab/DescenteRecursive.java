package com.nikni.app5lab;


/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  AnalLex analLex;
  NoeudAST root;
  NoeudAST current;


/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in) {

    analLex = new AnalLex(new Reader(in));
    root = null;
    current = null;
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) throws AnalLex.IllegalFormatException {


  while(analLex.resteTerminal()){
    Terminal<Object> term = analLex.prochainTerminal();
    if(term.getType()==Terminal.Type.OPERAND){
      FeuilleAST feuille = new FeuilleAST(term);
      if(current.left == null){
        current.left = feuille;
      }
      else if(current.right == null){
        current.right = feuille;
      }


    }
    else if(term.getType()==Terminal.Type.OPERATOR){

      if(term.getOperatorType() == Terminal.OperatorType.AddSub){

      }
      else if(term.getOperatorType() == Terminal.OperatorType.MultDiv){

      }
      else if(term.getOperatorType() == Terminal.OperatorType.BracketOpen){

      }
      else if(term.getOperatorType() == Terminal.OperatorType.BracketClose){

      }


    }
  }

  return null;
}


// Methode pour chaque symbole non-terminal de la grammaire retenue
// ... 
// ...



/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s)
{
    //
}



  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0){
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatSyntaxique.txt";
    }
    DescenteRecursive dr = new DescenteRecursive(args[0]);
    try {
      ElemAST RacineAST = dr.AnalSynt();
      toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
      System.out.println(toWriteLect);
      toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
      System.out.println(toWriteEval);
      Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite 
                                                              // dans fichier args[1]
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

