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
public class SequentialExpression extends Expression{
    public Expression EXPR1,EXPR2;

    public SequentialExpression(Expression expr1, Expression expr2, SourcePosition thePosition) {
        super(thePosition);
        EXPR1 = expr1;
        EXPR2 = expr2;
    }

   
    public Object visit(Visitor v, Object o) {
        return v.visitSequentialExpression(this, o);
    }
    
}
