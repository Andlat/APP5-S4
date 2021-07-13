package com.nikni.app5lab;

/** @author Ahmed Khoumsi */

import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.stream.Stream;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

// Attributs
//  ...

  private enum State{S, E, A, B};

  private State m_currentState = State.S;
  private final StringCharacterIterator m_it;
  private final StringBuilder m_ulBuilder = new StringBuilder();

  public static class IllegalFormatException extends Exception{
    IllegalFormatException(String error){ super(error); }
  }

  private boolean isLetter(char c){ return (c >= 65 && c <= 90) || (c >= 97 && c <= 122); }
  private boolean isNumber(char c){ return c >= 48 && c <= 57; }

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
    m_currentState = State.S; // A lexical unit starts from state S

    while(resteTerminal()) {
      char c = m_it.current();
      m_it.next();

      if(c == ' ' || c == '\t' || c == '\r' || c == '\n') continue; //Skip spaces, tabs and new lines

      switch (m_currentState) {
        case S: // Etat de depart de l'automate
          switch(c){
            case '+': return new Terminal<>(Terminal.Type.ADD, c);
            case '-': return new Terminal<>(Terminal.Type.SOUS, c);
            case '*': return new Terminal<>(Terminal.Type.MULT, c);
            case '/': return new Terminal<>(Terminal.Type.DIV, c);
            case '(': return new Terminal<>(Terminal.Type.PARENTH_OUV, c);
            case ')': return new Terminal<>(Terminal.Type.PARENTH_FERM, c);

            default:
              if(isNumber(c)) { // Is c a number ?
                m_currentState = State.E;

              }else if(isLetter(c)) {// Is c a letter ? Either upper or lower case
                m_currentState = State.A;

              }else{
                throw new IllegalFormatException("Illegal starting character '" + c +"' at position " + m_it.getIndex() + '.');
              }

              // Reset the string builder for the next UL
              m_ulBuilder.setLength(0);
              m_ulBuilder.append(c);
          }
          break;

        case E:// Etat pour valider des nombres
          if(isNumber(c)) { // c est un chiffre ?
            m_ulBuilder.append(c);

          }else{ // Si ce n'est pas un chiffre, retourner la UL pour le nombre
            m_it.previous(); // Recule pointeur de lecture
            return new Terminal<>(Terminal.Type.NOMBRE, m_ulBuilder.toString());
          }
          break;

        case A: // Etat pour valider des variables
          m_ulBuilder.append(c);

          if(c == '_'){
            m_currentState = State.B;

          }else if(!isLetter(c)){ // Si c n'est pas un caractere valide pour une variable, retourner la UL
            m_ulBuilder.deleteCharAt(m_ulBuilder.length()-1); // Retirer le dernier caractere qui n'est pas une lettre
            m_it.previous(); // Recule pointeur de lecture
            return new Terminal<>(Terminal.Type.VARIABLE, m_ulBuilder.toString());
          }
          break;
        case B:
          if(isLetter(c)){ // c est une lettre ?
            m_ulBuilder.append(c);
            m_currentState = State.A;
          }else{ // Erreur, puisqu'on ne peut terminer une variable avec un "underscore"
            throw new IllegalFormatException("Impossible de terminer une variable par _. Erreur à la position " + m_it.getIndex() + '.');
          }
          break;
      }
    }

    return new Terminal<>(m_currentState==State.E ? Terminal.Type.NOMBRE : Terminal.Type.VARIABLE, m_ulBuilder.toString()); // Should only reach this statement if reading the end of file/string to analyze
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
        toWrite.append(t.getValue()).append(' ').append(t.getType().toString()).append('\n');  // toWrite contient le resultat
      }

      System.out.println(toWrite); 	// Ecriture de toWrite sur la console
      Writer w = new Writer(args[1],toWrite.toString()); // Ecriture de toWrite dans fichier args[1]
      System.out.println("Fin d'analyse lexicale");

    }catch(IllegalFormatException ex){
      System.out.println("ERROR: " + ex.getMessage());
    }
  }
}
