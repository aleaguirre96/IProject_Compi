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
public class Cases extends Case{
    
  public Case case1;
  public Case case2; //Puede ser nulo
    
  public Cases(Case case1, Case case2,SourcePosition thePosition) {
        super(null,thePosition);
        this.case1 = case1;
        this.case2 = case2;
  }
 
    
  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitCasesCommand(this, o);
  } 
}
