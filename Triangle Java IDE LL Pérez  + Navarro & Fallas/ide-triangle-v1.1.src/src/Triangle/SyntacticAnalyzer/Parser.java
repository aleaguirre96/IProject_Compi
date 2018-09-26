/*
 * @(#)Parser.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;

import Triangle.ErrorReporter;
import Triangle.AbstractSyntaxTrees.ActualParameter;
import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.ArrayAggregate;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoterDDot;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CaseDeclaration;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Command;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
///// eliminado el import del EmptyCommand
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.FormalParameter;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.LocalDeclaration;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.NILCommand;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RecordAggregate;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.RecursiveDeclaration;
import Triangle.AbstractSyntaxTrees.RepeatDoUntil;
import Triangle.AbstractSyntaxTrees.RepeatDoWhile;
import Triangle.AbstractSyntaxTrees.RepeatFor;
import Triangle.AbstractSyntaxTrees.RepeatUntil;
import Triangle.AbstractSyntaxTrees.RepeatWhile;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SequentialExpression;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarDeclarationInitialized;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Vname;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public Program parseProgram() {

    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();

    try {
      Command cAST = parseCommand();
      programAST = new Program(cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) { return null; }
    return programAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////

// parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }

// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }

///////////////////////////////////////////////////////////////////////////////
//
// COMMANDS
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.

  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  Command parseSingleCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);

    switch (currentToken.kind) {


    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(iAST, apsAST, commandPos);

        } else {

          Vname vAST = parseRestOfVname(iAST);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      
      // Aqui se agrega el nuevo comando en este caso el NIL
   
    case Token.NIL:
    {
      acceptIt();
      finish(commandPos);
      commandAST = new NILCommand(commandPos);   
    }
      break;
      // Eliminada la alternativa BEGIN
      // Eliminada la alternativa LET
      // Eliminada la alternativa IF
      //ELiminada la alternativa WHILE

    /////////////////////////////////////////// tipos de repeat agregados por el equipo ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    case Token.REPEAT:{
    acceptIt();
        switch(currentToken.kind){    
        case Token.WHILE: ///////// "repeat" "while" Expression "do" Command "end"
              {
                acceptIt();
                Expression eAST = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand();
                accept(Token.END);
                finish(commandPos);
                commandAST = new RepeatWhile(eAST, cAST, commandPos); // aqui se agrega un metodo para representar el AST***** (PREGUNTAR)
              }
              break;
              
        case Token.UNTIL:{ ///////// "repeat" "until" Expression "do" Command "end"
            acceptIt();
            Expression eAST = parseExpression();
            accept(Token.DO);
            Command cAST = parseCommand();
            accept(Token.END);
            finish(commandPos);
            commandAST = new RepeatUntil(eAST, cAST, commandPos); // aqui se agrega un metodo para representar el AST
        }
        break;
        
        case Token.DO:{ /////////"repeat" "do" Command "while" Expression "end"
                 acceptIt();
                 Command cAST = parseCommand();
                 switch(currentToken.kind){
                     case Token.WHILE:{
                           acceptIt();
                            Expression eAST = parseExpression();
                             accept(Token.END);
                            finish(commandPos);
                             commandAST = new RepeatDoWhile(eAST, cAST, commandPos); // aqui se agrega un metodo para representar el AST
                        }
                        break;
                
                        case Token.UNTIL:{   ////////////"repeat" "do" Command "until" Expression "end"
                            acceptIt();
                            Expression eAST = parseExpression();
                            accept(Token.END);
                            finish(commandPos);
                            commandAST = new RepeatDoUntil(eAST, cAST, commandPos); // aqui se agrega un metodo para representar el AST
                
                        }
                        break;
                
                
                        default:
                            syntacticError("\"%\" cannot start a Docommand",currentToken.spelling);
                        break;
                        }
                    }
                    break;
        
        case Token.FOR:{  /////// "repeat" "for" Identifier "from" Expression "to" Expression "do" Command "end"
                acceptIt();
                Identifier iAST = parseIdentifier();

                accept(Token.FROM); 

                Expression e1AST = parseExpression();
                accept(Token.TO);
                Expression e2AST = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand();
                accept(Token.END);
                finish(commandPos);
                commandAST = new RepeatFor(iAST, e1AST, e2AST, cAST, commandPos);
            }
            break;
            }
        }break;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////agregar "let" Declaration "in" Command "end"  /////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    case Token.LET:{
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new LetCommand(dAST, cAST, commandPos);
    }break;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /////////////////////////////////////////////////Agregar///////////////////////////////////////////////////////////////////////
    ///"if" Expression "then" Command ("elsif" Expression "then" Command)*"else" Command "end"  ///////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    case Token.IF:{///////////////////////// PREGUNTAR 
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.THEN);
        Command c1AST = parseCommand();
        Command c2AST = parseElsifCommand(); // Aqui se llama a la funcion auxiliar para procesar ("elsif" Expression "then" Command)*"else" Command "end" y que devuelva un command para poder crear el AST correctamente
        accept(Token.END);
        finish(commandPos);
        commandAST = new IfCommand(eAST,c1AST,c2AST,commandPos);
    }break;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////// "select" Expression "from" Cases "end"  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    case Token.SELECT:{
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.FROM);
        //Cases cAST = parseCases();
        accept(Token.END);
    }break;
    */
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    case Token.SEMICOLON:
    case Token.END:
    case Token.ELSE:
    case Token.IN:
    case Token.EOT:

      finish(commandPos);
      commandAST = new NILCommand(commandPos); ////////////////////////////////////Cambiado EmptyCommand por NILCommand
      break;

    default:
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }
  
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////// metodo auxiliar para el caso IF de single-command  ///////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //Aqui se procesa  - ("elsif" Expression "then" Command)*"else" Command "end" - perteneciente al caso IF de single-command ////
  
  Command parseElsifCommand() throws SyntaxError {
    Command commandAST = null; 
    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    switch(currentToken.kind){
      case Token.ELSIF:
        {
          acceptIt();
          Expression eAST = parseExpression();
          accept(Token.THEN);
          Command cAST = parseCommand();
          finish(commandPos);
          commandAST = new IfCommand(eAST, cAST, parseElsifCommand(), commandPos); // Se utiliza parseElsif porque debe llamarse recursivamente a si mismo (y devuelve un command)
        }
        break;
        
        case Token.ELSE:
        {
          acceptIt();
          Command elseAST = parseCommand();          
          finish(commandPos);
          commandAST = elseAST;
        }
        break;
      default: // error tomado del metodo parseSingleCommand
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;
    }
    return commandAST; //// Lo que se devulve aqui es lo que se toma para procesar el IF de single-command y crear el AST correctamente
  }

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start (expressionPos);

    switch (currentToken.kind) {

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
      break;

    case Token.IF:
      {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
      break;

    default:
      expressionAST = parseSecondaryExpression();
      break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
        expressionPos);
    }
    return expressionAST;
  }

  Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) {

    case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
      break;

    case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
      break;

    case Token.LBRACKET:
      {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new ArrayExpression(aaAST, expressionPos);
      }
      break;

    case Token.LCURLY:
      {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new RecordExpression(raAST, expressionPos);
      }
      break;

    case Token.IDENTIFIER:
      {
        Identifier iAST= parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(iAST, apsAST, expressionPos);

        } else {
          Vname vAST = parseRestOfVname(iAST);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }
      }
      break;

    case Token.OPERATOR:
      {
        Operator opAST = parseOperator();
        Expression eAST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
      }
      break;

    case Token.LPAREN:
      acceptIt();
      expressionAST = parseExpression();
      accept(Token.RPAREN);
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;

    }
    return expressionAST;
  }

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

  Vname parseVname () throws SyntaxError {
    Vname vnameAST = null; // in case there's a syntactic error
    Identifier iAST = parseIdentifier();
    vnameAST = parseRestOfVname(iAST);
    return vnameAST;
  }

  Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = identifierAST.position;
    Vname vAST = new SimpleVname(identifierAST, vnamePos);

    while (currentToken.kind == Token.DOT ||
           currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////

  /* Se modifica el parseDeclaration para que lea:
    Declaration
        ::= compound-Declaration
        | Declaration ";" compound-Declaration
  */
  
  Declaration parseDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    declarationAST = parseCompoundDeclaration();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration d2AST = parseCompoundDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, d2AST,
        declarationPos);
    }
    return declarationAST;
    
  }

   /*----------------------------------------------------------------------
    Se crea la regla compound-Declaration que se lee:
    ::= single-Declaration
    | "recursive" Proc-Funcs "end"
    | "local" Declaration "in" Declaration "end"
  ----------------------------------------------------------------------*/
  
  Declaration parseCompoundDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error
    
    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    
    switch(currentToken.kind){ 
      //const, var,proc, fun, type => single-Declaration
      case Token.CONST:
      case Token.VAR:
      case Token.PROC:
      case Token.FUNC:
      case Token.TYPE:
        {
          declarationAST = parseSingleDeclaration();//parseSingleDeclaration se encarga de acceptarlo
        }
        break;
        
      case Token.RECURSIVE:
        {
          acceptIt();
          Declaration pfsDeclaration = parseProcFuncsDeclaration();
          accept(Token.END);
          finish(declarationPos);
          declarationAST = new RecursiveDeclaration(pfsDeclaration, declarationPos);
        }
        break;
      case Token.LOCAL:
        {
          acceptIt();
          Declaration d1AST = parseDeclaration();
          accept(Token.IN);
          Declaration d2AST = parseDeclaration();
          accept(Token.END);
          finish(declarationPos);
          declarationAST = new LocalDeclaration(d1AST, d2AST, declarationPos);
        }
        break;
      default:
      syntacticError("\"%\" cannot start a compound declaration",
        currentToken.spelling);
      break;
    }
    return declarationAST;
  }
  
  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);

    switch (currentToken.kind) {
    case Token.CONST:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;
/*
    case Token.VAR: //Este es el original
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
      }
      break;*/
      
      //Se annada la funcionalidad de poder declarar variables de la forma
      //"var" Identifier ":=" Expression
      case Token.VAR:   ///////////// Modificado por nosotros
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        switch (currentToken.kind) { ///Se evalua que tipo de e
            case Token.COLON:{  //Declaracion :
                acceptIt();
                TypeDenoter tAST = parseTypeDenoter();
                finish(declarationPos);
                declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
            }break;    
            case Token.BECOMES:{ //Declaracion := //declaraci�n de variable inicializada
                acceptIt();
                Expression eAST = parseExpression();//Expresion 
                finish(declarationPos);
                declarationAST = new VarDeclarationInitialized(iAST,eAST,declarationPos);
            }break;
            default:
                syntacticError("\"%\" cannot start a VAR declaration",currentToken.spelling);
                break;     
        }
      }
      break;
      
    /*Se modificar la opci�n referente a proc para que se lea:
        | "proc" Identifier "(" Formal-Parameter-Sequence ")""~" Command "end"*/

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseCommand();                                              //  Cambiado el single-Command por Command////////////////////////////////////////////
        accept(Token.END);                                                          //agregado el accept del token END ///////////////////////////////////////////////////////////////
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
          declarationPos);
      }
      break;

    case Token.TYPE:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
      break;
      
      

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;

    }
    return declarationAST;
  }

  /*--------------------------------------------------------------------
    Se agrega la regla:
    ProcFunc ::= "proc" Identifier "(" Formal-Parameter-Sequence ")"
                 "~" Command "end"
                | "func" Identifier "(" Formal-Parameter-Sequence ")"
                  ":" Type-denoter "~" Expression
  ----------------------------------------------------------------------*/
  
  Declaration parseProcFuncDeclaration() throws SyntaxError{
    Declaration declarationAST = null; // in case there's a syntactic error
    SourcePosition declarationPos = new SourcePosition();
    
    start(declarationPos);
    
    switch(currentToken.kind){
      case Token.PROC:{
          declarationAST = parseSingleDeclaration();
      }break;
          
      case Token.FUNC:
        {
          declarationAST = parseSingleDeclaration();
        }
        break;
      default:
      syntacticError("\"%\" cannot start neither a process nor a function "
              + "declaration",
        currentToken.spelling);
      break;
    }
    
    return declarationAST;
  }
  
  /*--------------------------------------------------------------------
    Se agrega la regla:
    ProcFuncs ::= Proc-Func ("|" Proc-Func)+
  ----------------------------------------------------------------------*/
  
  Declaration parseProcFuncsDeclaration() throws SyntaxError{
    Declaration declarationAST = null; // in case there's a syntactic error
    SourcePosition declarationPos = new SourcePosition();
    
    start(declarationPos);
    
    declarationAST = parseProcFuncDeclaration();
    accept(Token.STICK);
    Declaration pf2AST = parseProcFuncDeclaration();
    
    declarationAST = new SequentialDeclaration(declarationAST, pf2AST, 
            declarationPos);
    
    while(currentToken.kind == Token.STICK){
      acceptIt();
      Declaration pfAuxAST = parseProcFuncDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, pfAuxAST, 
              declarationPos);
    }
    return declarationAST;
  }
  
  /*--------------------------------------------------------------------
 Se agrega la regla:
  Cases::= Case+ [elseCase]
  --------------------------------------------------------------------*/
  Command parseCases() throws SyntaxError{
     Command commandAST = null;
     SourcePosition parseCasePos = new SourcePosition();
     start(parseCasePos);
     Command commandCase = parseCase();
     
     while(currentToken.kind == Token.CASE){
         Command commandCaseNext = parseCase();
         finish(parseCasePos);
         commandAST = new SequentialCommand(commandCase, commandCaseNext,parseCasePos);
     }
     
     if(currentToken.kind == Token.CASE) {
         
        //declarationAST = 
     }else{
     }
     
     return commandAST; 
  }
  /*--------------------------------------------------------------------
  Se agrega la regla:
  Case::= "case" Case-Literals "then" Command
  --------------------------------------------------------------------*/
  Command parseCase() throws SyntaxError{
      Command declarationAST = null;
      SourcePosition parseCasePos = new SourcePosition();
      start(parseCasePos);
      accept(Token.CASE);
      Expression caseLiterals =  parseCaseLiterals();
      accept(Token.THEN);
      Command commandAST = parseCommand();
      finish(parseCasePos); 
      declarationAST = new CaseDeclaration(caseLiterals,commandAST,parseCasePos);
      return declarationAST;
  }
  /*--------------------------------------------------------------------
  Se agrega la regla:
  ElseCase::= "else" comand
  --------------------------------------------------------------------*/
  Command parseElseCase() throws SyntaxError{
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    
    accept(Token.ELSE);
    commandAST = parseCommand();           
    finish(commandPos);
    return commandAST;
  }
  /*--------------------------------------------------------------------
  Se agrega la regla:                                                  
  Case-Literals ::= Case-Literal ("|" Case-Literal)*                   
  --------------------------------------------------------------------*/
  Expression parseCaseLiterals()throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error
    SourcePosition expresionPos = new SourcePosition();
    start(expresionPos);
    
    expressionAST = parseCaseLiteral();
    while (currentToken.kind == Token.STICK) {
      acceptIt();
      Expression nextExpressionAST =  parseCaseLiteral();
      finish(expresionPos);
      expressionAST = new SequentialExpression(expressionAST,nextExpressionAST,expresionPos);
    }
    return expressionAST;
  }
  /*--------------------------------------------------------------------
  Se agrega la regla:
   Case-Literal ::= Integer-Literal | Character-Literal
  ----------------------------------------------------------------------*/
  Expression parseCaseLiteral() throws SyntaxError{
    Expression expresionAST = null; // in case there's a syntactic error
    SourcePosition expresionPos = new SourcePosition();
    
    start(expresionPos);
    
    switch(currentToken.kind){
        case Token.INTLITERAL:{
          IntegerLiteral ilAST  = parseIntegerLiteral();
          finish(expresionPos);
          expresionAST = new IntegerExpression(ilAST,expresionPos);
        }break;
        case Token.CHARLITERAL:{
          CharacterLiteral chAST  = parseCharacterLiteral();
          finish(expresionPos);
          expresionAST = new CharacterExpression(chAST,expresionPos);
        }break;
        default:
        syntacticError("\"%\" It isn't a case literal",currentToken.spelling);
        break;
    }
    
    return expresionAST;
  }
  
///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST;

    SourcePosition formalsPos = new SourcePosition();

    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
        formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
      break;

    }
    return formalAST;
  }


  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null; // in case there's a syntactic error

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
        actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error

    SourcePosition actualPos = new SourcePosition();

    start(actualPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
    case Token.INTLITERAL:
    case Token.CHARLITERAL:
    case Token.OPERATOR:
    case Token.LET:
    case Token.IF:
    case Token.LPAREN:
    case Token.LBRACKET:
    case Token.LCURLY:
      {
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Vname vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an actual parameter",
        currentToken.spelling);
      break;

    }
    return actualAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////

  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();

    start(typePos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        finish(typePos);
        typeAST = new SimpleTypeDenoter(iAST, typePos);
      }
      break;

    /*case Token.ARRAY:
      {
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        accept(Token.OF);
        TypeDenoter tAST = parseTypeDenoter();
        finish(typePos);
        typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
      }
      break;*/

    
    /*
      Modificar la regla concerniente a tipos-de-arreglos:
      | "array" Integer-Literal [".." Integer-Literal] "of" Type-denoter
      */
    case Token.ARRAY:{
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        switch (currentToken.kind) { ///Se evalua que tipo de e
            case Token.DDOT:{  // [".."]  aparece 0 o una vez
                acceptIt(); //acepta ".."
                IntegerLiteral ilAST2 = parseIntegerLiteral();
                accept(Token.OF); //acepta "of"
                TypeDenoter tAST = parseTypeDenoter();
                finish(typePos);
                typeAST = new ArrayTypeDenoterDDot(ilAST,ilAST2,tAST,typePos);//Se llama a su respectivo arbol syntax
            }break;    
            case Token.OF:{ //Declaracion con of
                acceptIt(); //acepta "of"
                TypeDenoter tAST = parseTypeDenoter();
                finish(typePos);
                typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
            }break;
            default:
                syntacticError("\"%\" cannot start a Array declaration",currentToken.spelling);
                break;
            }
    }break;  

    case Token.RECORD:
      {
        acceptIt();
        FieldTypeDenoter fAST = parseFieldTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, typePos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a type denoter",
        currentToken.spelling);
      break;

    }
    return typeAST;
  }

  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error

    SourcePosition fieldPos = new SourcePosition();

    start(fieldPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FieldTypeDenoter fAST = parseFieldTypeDenoter();
      finish(fieldPos);
      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
    } else {
      finish(fieldPos);
      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
    }
    return fieldAST;
  }
}
