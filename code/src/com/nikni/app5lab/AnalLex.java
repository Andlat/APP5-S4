package com.nikni.app5lab;

/** @author Ahmed Khoumsi */

import java.text.StringCharacterIterator;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

// Attributs
//  ...

  private enum State{E, F};

  private State m_currentState = State.E;
  private final StringCharacterIterator m_it;
  private final StringBuilder m_ulBuilder = new StringBuilder();

  public class IllegalFormatException extends Exception{
    IllegalFormatException(String error){ super(error); }
  }

/** Constructeur pour l'initialisation d'attribut(s)
 */
  public AnalLex(Reader stream) {
    m_it = new StringCharacterIterator(stream.toString().trim());
  }


/** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne 
 */
  public boolean resteTerminal( ) {
    return m_it.getIndex() < m_it.getEndIndex();
  }
  
  
/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */  
  public Terminal<Object> prochainTerminal( ) throws IllegalFormatException {
    m_currentState = State.E; // A lexical unit starts from state E

    while(resteTerminal()) {
      var c = m_it.current();
      m_it.next();

      if(c == ' ') continue; //Skip spaces

      switch (m_currentState) {
        case E:
          if(c == '+') return new Terminal<>(Terminal.Type.OPERATOR,Terminal.OperatorType.AddSub , c);
          else if(c == '0' || c == '1'){
            m_ulBuilder.setLength(0); // Reset the string builder for the next UL
            m_ulBuilder.append(c);
            m_currentState = State.F;
          }
          else throw new IllegalFormatException("Illegal character '" + c +"' at position " + m_it.getIndex() + '.');
          break;
        case F:
          if(c == '0' || c == '1') {
            m_ulBuilder.append(c);
          }else{
            m_it.previous();
            return new Terminal<>(Terminal.Type.OPERAND,Terminal.OperatorType.Null, m_ulBuilder.toString());
          }
          break;
      }
    }
    return new Terminal<>(Terminal.Type.OPERAND,Terminal.OperatorType.Null, m_ulBuilder.toString()); // Should only reach this statement if reading the end of file/string to analyze
  }

//
///** ErreurLex() envoie un message d'erreur lexicale
// */
//  public void ErreurLex(String s) {
//     //
//  }

  
  //Methode principale a lancer pour tester l'analyseur lexical
  public static void main(String[] args) {
    StringBuilder toWrite = new StringBuilder();
    System.out.println("Debut d'analyse lexicale");
    if (args.length == 0){
    args = new String [2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
    }
    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r); // Creation de l'analyseur lexical

    // Execution de l'analyseur lexical
    Terminal t = null;

    try {
      while (lexical.resteTerminal()) {
        t = lexical.prochainTerminal();
        toWrite.append(t.getValue()).append('\n');  // toWrite contient le resultat
      }
    }catch(IllegalFormatException ex){
      System.out.println("ERROR: " + ex.getMessage());
    }

    System.out.println(toWrite); 	// Ecriture de toWrite sur la console
    Writer w = new Writer(args[1],toWrite.toString()); // Ecriture de toWrite dans fichier args[1]
    System.out.println("Fin d'analyse lexicale");
  }
}
