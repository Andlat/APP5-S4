package com.nikni.app5lab;


/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  AnalLex analLex;
  NoeudAST root;
  NoeudAST current;
  ElemAST firstVal;


/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in) {

    analLex = new AnalLex(new Reader(in));
    root = null;
    current = new NoeudAST(null);
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) throws AnalLex.IllegalFormatException {

 boolean firstNode = true;

  while(analLex.resteTerminal()){
    Terminal<Object> term = analLex.prochainTerminal();
    System.out.println(term.getValue());
    if(term.getType()==Terminal.Type.NOMBRE || term.getType()==Terminal.Type.VARIABLE){
        FeuilleAST feuille = new FeuilleAST(term);
      if(current != null) {
        if (current.left == null) {
          current.left = feuille;
          feuille.parent = current;
        } else if (current.right == null) {
          current.right = feuille;
          feuille.parent = current;
        } else
          this.ErreurSynt("Error: Too many consecutives variables/numbers");
      }
      else
        firstVal = feuille;


    }


      if(firstNode){
        if(term.getType() == Terminal.Type.PARENTH_OUV ){
          NoeudAST noeud = new NoeudAST(null);
          NoeudAST noeud2 = new NoeudAST(null);
          noeud.parent = noeud2;
          current = noeud;
          root = noeud2;
        }
        else{
          NoeudAST noeud = new NoeudAST(term);
          noeud.left = firstVal;
          current = noeud;
          root = noeud;
        }
        firstNode = false;
      }

      else if(term.getType() == Terminal.Type.SOUS || term.getType() == Terminal.Type.ADD){


        while(current.parent != null && current.terminal != null){
          current = (NoeudAST) current.parent;
        }
        if(current.terminal == null){
          current.terminal = term;
        }
        else{
          NoeudAST noeud = new NoeudAST(term);
          noeud.left = root;
          root.parent = noeud;
          root = noeud;
          current = noeud;
        }


      }
      else if(term.getType() == Terminal.Type.MULT || term.getType() == Terminal.Type.DIV){


        if(current.terminal == null){
          current.terminal = term;
        }
        else{
          NoeudAST noeud = new NoeudAST(term);
          noeud.left = current.right;
          noeud.parent = current;
          current = noeud;
        }
      }

      else if(term.getType() == Terminal.Type.PARENTH_OUV ){
        NoeudAST noeud = new NoeudAST(null);
        noeud.parent = current;
        if(current.left == null){
          current.left = noeud;
        }
        else if(current.right == null){
          current.right = noeud;
        }
        else
          ErreurSynt("Error: Misuse of brackets");
        current = noeud;

      }
      else if(term.getType() == Terminal.Type.PARENTH_FERM){
        current = (NoeudAST) current.parent;
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

