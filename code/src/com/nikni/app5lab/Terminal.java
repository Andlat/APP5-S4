package com.nikni.app5lab;

/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal<T>{
  public enum Type{OPERATOR, OPERAND}
  public enum OperatorType{Null, AddSub, MultDiv, BracketOpen, BracketClose}
  private Type m_type = null;
  private OperatorType m_operatorType = null;
  private T m_value = null;

  public Type getType(){ return m_type; }
  public OperatorType getOperatorType(){ return m_operatorType; }
  public T getValue(){ return m_value; }

  /** Un ou deux constructeurs (ou plus, si vous voulez)
   *   pour l'initalisation d'attributs
   */
  public Terminal(Type type, OperatorType opType, T value) {
     m_type = type;
     m_value = value;
     m_operatorType = opType;
  }
}
