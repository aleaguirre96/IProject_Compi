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
public class CasesCommand extends Case{
    public Case CasesCom;
    
    public CasesCommand(Case CasesComand,SourcePosition thePosition) {
        super(thePosition);
        CasesCom = CasesComand;
    }

    @Override
    public Object visit(Visitor v, Object o) {
         return v.visitCases(this,o);
    }
    
}
