/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author jose
 */

//Arbol abstracto sintactico para declaracion de varibles 
//inicializadas

public class VarDeclarationInitialized extends Declaration{
    
  public Identifier I;
  public Expression E;
    
  public VarDeclarationInitialized (Identifier iAST, Expression eAST,
                         SourcePosition thePosition) {
    super (thePosition);
    I = iAST; //Identificador de la variable
    E = eAST; //Expresion de declaracion
  }
  
  //Metodo para recorrer el arblo
    @Override
    public Object visit(Visitor v, Object o) {
        return v.visitVarDeclarationInitialized(this, o); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

