/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author Luis
 */
public class RepeatFor extends Command{
    public RepeatFor (Identifier iAST, Expression e1AST, Expression e2AST, Command cAST, SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
    E1 = e1AST;
    E2 = e2AST;
    C = cAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitRepeatFor(this, o);
  }

  public Expression E1;
  public Expression E2;
  public Command C;
  public Identifier I;
}
