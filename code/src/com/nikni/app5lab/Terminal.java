package com.nikni.app5lab;

/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal<T>{
  public enum Type{ADD, SOUS, MULT, DIV, PARENTH_OUV, PARENTH_FERM, NOMBRE, VARIABLE}

  private Type m_type = null;
  private T m_value = null;

  public Type getType(){ return m_type; }
  public T getValue(){ return m_value; }

  /** Un ou deux constructeurs (ou plus, si vous voulez)
   *   pour l'initalisation d'attributs
   */
  public Terminal(Type type, T value) {
     m_type = type;
     m_value = value;
  }
}
