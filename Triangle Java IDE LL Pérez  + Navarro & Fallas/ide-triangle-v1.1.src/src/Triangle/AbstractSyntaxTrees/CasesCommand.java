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
public class CasesCommand extends Command{
    CasesCommand CasesCom;
    Command elseCom;

    public CasesCommand(SourcePosition thePosition) {
        super(thePosition);
    }
    
    public CasesCommand(CasesCommand Cases,SourcePosition thePosition) {
        super(thePosition);
        CasesCom = Cases;
        elseCom = null;
    }

    @Override
    public Object visit(Visitor v, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
