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


public class VarDeclarationInitialized extends Declaration{
    
  public Identifier I;
  public Expression E;
    
  public VarDeclarationInitialized (Identifier iAST, Expression eAST,
                         SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
    E = eAST; //Expresion de declaracion
  }
    @Override
    public Object visit(Visitor v, Object o) {
        return v.visitVarDeclarationInitialized(this, o); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

