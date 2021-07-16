package com.app5;


/** @author Ahmed Khoumsi */

import java.util.ArrayList;

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  private final AnalLex analLex;
  private Terminal<Object> term;
  private final ArrayList<String> prevTerm = new ArrayList<>();
  private int bracketStackCount = 0;// Stack pour les parentheses. +1 quand parenthese ouvrante. -1 quand parenthese fermante. Le total à la fin de l'analyse doit être de 0

  private void prochainTerminal() throws AnalLex.IllegalFormatException {
    term = analLex.prochainTerminal();
    prevTerm.add(term.getValue().toString());

    // Stack de parentheses
    final var ul = term.getValue().toString();
    if(ul.equals("(")) ++bracketStackCount;
    else if(ul.equals(")")) --bracketStackCount;
  }

  /** Constructeur de DescenteRecursive :
        - recoit en argument le nom du fichier contenant l'expression a analyser
        - pour l'initalisation d'attribut(s)
   */
  public DescenteRecursive (String in) throws AnalLex.IllegalFormatException{
      analLex = new AnalLex(new Reader(in));

      prochainTerminal();
  }


  /** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
   *    Elle retourne une reference sur la racine de l'AST construit
   */
  public ElemAST AnalSynt( ) throws AnalLex.IllegalFormatException {
    ElemAST n1 = S();
    if(bracketStackCount != 0) ErreurSynt("Le nombre de parenthèses ouvrantes et fermantes ne correspond pas.", false);

    return n1;
  }

  private ElemAST S()throws AnalLex.IllegalFormatException{
    System.out.println("S: " + term.getValue());
    ElemAST n1 = A();
    while (term.getType() == Terminal.Type.SOUS || term.getType() == Terminal.Type.ADD) {
      if(term.getType() == Terminal.Type.SOUS){
        System.out.println("SOUS: " + term.getValue());
        NoeudAST noeud = new NoeudAST(term);
        if(!analLex.resteTerminal()){
          ErreurSynt("Aucune valeur après '-'");
        }
        prochainTerminal();
        ElemAST n2 = A();

        noeud.left = n1;
        noeud.right = n2;

        n1 = noeud;


      }
      else if(term.getType() == Terminal.Type.ADD ){
        System.out.println("ADD: " + term.getValue());
        NoeudAST noeud = new NoeudAST(term);
        if(!analLex.resteTerminal()){
          ErreurSynt("Aucune valeur après '+'");
        }
        prochainTerminal();
        ElemAST n2 = A();

        noeud.left = n1;
        noeud.right = n2;

        n1 = noeud;
      }
    }

    return n1;
  }

  private ElemAST A()throws AnalLex.IllegalFormatException{
    System.out.println("A: " + term.getValue());
    ElemAST n1 = B();
    while(term.getType() == Terminal.Type.DIV || term.getType() == Terminal.Type.MULT){
      if(term.getType() == Terminal.Type.DIV ){
        System.out.println("Div: " + term.getValue());
        NoeudAST noeud = new NoeudAST(term);
        if(!analLex.resteTerminal()){
          ErreurSynt("Aucune valeur après '/'");
        }
        prochainTerminal();
        ElemAST n2 = B();

        noeud.left = n1;
        noeud.right = n2;

        n1 = noeud;
      } else if(term.getType() == Terminal.Type.MULT){
        System.out.println("Mult: " + term.getValue());
        NoeudAST noeud = new NoeudAST(term);
        if(!analLex.resteTerminal()){
          ErreurSynt("Aucune valeur après '*'");
        }
        prochainTerminal();
        ElemAST n2 = B();

        noeud.left = n1;
        noeud.right = n2;

        n1 = noeud;
      }
    }
    return n1;
  }

  private ElemAST B()throws AnalLex.IllegalFormatException{
    System.out.println("B: " + term.getValue());
    ElemAST n1 = null;
    if(term.getType() == Terminal.Type.VARIABLE || term.getType() == Terminal.Type.NOMBRE){
      n1 = new FeuilleAST(term);
      prochainTerminal();

    }
    else if(term.getType() == Terminal.Type.PARENTH_OUV){
      System.out.println("ParentOuv: " + term.getValue());
      if(!analLex.resteTerminal()){
        ErreurSynt("Aucune valeur après '('");
      }

      prochainTerminal();
      n1 = S();
      if(term.getType() == Terminal.Type.PARENTH_FERM){
        System.out.println("ParentFerm: " + term.getValue());
        prochainTerminal();
      }
    } else{
      ErreurSynt("Une unité lexicale autre qu'un nombre/variable ou une parenthèse ouvrante trouvée");
    }

    return n1;
  }

  /** ErreurSynt() envoie un message d'erreur syntaxique
   */
  private void ErreurSynt(String s, boolean showPos) throws AnalLex.IllegalFormatException
  {
    StringBuilder error = new StringBuilder("\nErreur: ").append(s);

    if(showPos) error.append(" à la position ").append(analLex.m_it.getIndex()).append(" de");

    error.append(' ').append(String.join(" ", prevTerm)).append('.');

    throw new AnalLex.IllegalFormatException(error.toString());
  }
  private void ErreurSynt(String s) throws AnalLex.IllegalFormatException { ErreurSynt(s, true); }

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

      Writer w = new Writer(args[1],toWriteLect+toWriteEval+'\n'+postfix); // Ecriture de toWrite
                                                              // dans fichier args[1]
      System.out.println(postfix);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

