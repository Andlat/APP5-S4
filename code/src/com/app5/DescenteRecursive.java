package com.app5;


/** @author Ahmed Khoumsi */

import java.util.ArrayList;

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  AnalLex analLex;
  NoeudAST root;
  NoeudAST current;
  Terminal<Object> term;
  ArrayList<String> stack;

/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive (String in) throws AnalLex.IllegalFormatException{

    analLex = new AnalLex(new Reader(in));
    root = null;
    current = new NoeudAST(null);
    term = analLex.prochainTerminal();

}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) throws AnalLex.IllegalFormatException {
  ElemAST n1;

  n1 = S();



  return n1;
}

ElemAST S()throws AnalLex.IllegalFormatException{
  System.out.println("S: " + term.getValue());
  ElemAST n1 = A();
  while (term.getType() == Terminal.Type.SOUS || term.getType() == Terminal.Type.ADD) {
    if(term.getType() == Terminal.Type.SOUS){
      System.out.println("SOUS: " + term.getValue());
      NoeudAST noeud = new NoeudAST(term);
      if(!analLex.resteTerminal()){
        ErreurSynt("No value after -");
      }
      term = analLex.prochainTerminal();
      ElemAST n2 = A();

      noeud.left = n1;
      noeud.right = n2;

      n1 = noeud;


    }
    else if(term.getType() == Terminal.Type.ADD ){
      System.out.println("ADD: " + term.getValue());
      NoeudAST noeud = new NoeudAST(term);
      if(!analLex.resteTerminal()){
        ErreurSynt("No value after +");
      }
      term = analLex.prochainTerminal();
      ElemAST n2 = A();

      noeud.left = n1;
      noeud.right = n2;

      n1 = noeud;


    }

  }





  return n1;
}
  ElemAST A()throws AnalLex.IllegalFormatException{
    System.out.println("A: " + term.getValue());
    ElemAST n1 = B();
    while(term.getType() == Terminal.Type.DIV || term.getType() == Terminal.Type.MULT){
      if(term.getType() == Terminal.Type.DIV ){
        System.out.println("Div: " + term.getValue());
        NoeudAST noeud = new NoeudAST(term);
        if(!analLex.resteTerminal()){
          ErreurSynt("No value after /");
        }
        term = analLex.prochainTerminal();
        ElemAST n2 = B();

        noeud.left = n1;
        noeud.right = n2;

        n1 = noeud;


      }
      else if(term.getType() == Terminal.Type.MULT){
        System.out.println("Mult: " + term.getValue());
        NoeudAST noeud = new NoeudAST(term);
        if(!analLex.resteTerminal()){
          ErreurSynt("No value after *");
        }
        term = analLex.prochainTerminal();
        ElemAST n2 = B();

        noeud.left = n1;
        noeud.right = n2;

        n1 = noeud;
    }



    }
    return n1;
}
  ElemAST B()throws AnalLex.IllegalFormatException{
    System.out.println("B: " + term.getValue());
    ElemAST n1 = null;
    if(term.getType() == Terminal.Type.VARIABLE || term.getType() == Terminal.Type.NOMBRE){
      n1 = new FeuilleAST(term);
      term = analLex.prochainTerminal();

    }
    else if(term.getType() == Terminal.Type.PARENTH_OUV){
      System.out.println("ParentOuv: " + term.getValue());
      if(!analLex.resteTerminal()){
        ErreurSynt("No value after (");
      }
      term = analLex.prochainTerminal();
      n1 = S();
      if(term.getType() == Terminal.Type.PARENTH_FERM){
        System.out.println("ParentFerm: " + term.getValue());
        term = analLex.prochainTerminal();
      }
    }
    else{
      ErreurSynt("Value other than number or opening bracket found");
    }


    return n1;
  }





/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s) throws AnalLex.IllegalFormatException
{
  throw new AnalLex.IllegalFormatException("Error received: " + s +" at position " + analLex.m_it.getIndex() + '.');
}



  //Methode principale a lancer pour tester l'analyseur syntaxique
  public static void main(String[] args) throws AnalLex.IllegalFormatException{
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

      try {
        toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST();
      }catch(NumberFormatException ex){
        toWriteEval += "Evaluation de l'AST impossible, puisqu'il contient une variable inconnue: " + ex.getMessage();
      }

      System.out.println(toWriteEval + '\n');

      String postfix = "Postfix: " + RacineAST.toPostfix() + '\n';

      Writer w = new Writer(args[1],toWriteLect+toWriteEval+postfix); // Ecriture de toWrite
                                                              // dans fichier args[1]
      System.out.println(postfix);

    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

