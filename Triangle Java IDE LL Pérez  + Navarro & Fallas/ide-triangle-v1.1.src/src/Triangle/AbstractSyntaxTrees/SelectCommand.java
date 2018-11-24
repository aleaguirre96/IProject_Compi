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
public class SelectCommand extends Command {
    public Expression expres;
    public Cases  casess;

    public SelectCommand(Expression expres,Cases casess,SourcePosition thePosition) {
        super(thePosition);
        this.expres = expres;
        this.casess = casess;
    }

    @Override
    public Object visit(Visitor v, Object o) {
         return v.visitSelectCommand(this, o);
    }
    
    


}
