package compiler;

import gen.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;

import java.util.ArrayList;

public class ProgramPrinter  implements CoolListener {

    public int depth = -1;
    public Scope currentScope;
    public ArrayList<Object> classNameArrayList = new ArrayList<>();

    public Class class1;
    public Class class2;
    public Class class3;
    public Class class4;
    public Class class5;

    public Object temp;
    public Token temp1;

    public Let currentLet;

    @Override
    public void enterProgram(CoolParser.ProgramContext ctx) {
        depth++;
        System.out.println("program start{");

        //Initialize Of Scope
        Scope programScope = new Scope();
        programScope.parent = currentScope;
        programScope.level = depth;

        //Set CurrentScope
        currentScope = programScope;

        //INT
        class1 = new Class();
        class1.setName("Int");
        currentScope.symbolTable.add(class1.getName(), class1);

        //String
        class2 = new Class();
        class2.setName("String");
        currentScope.symbolTable.add(class2.getName(), class2);

        //Bool
        class3 = new Class();
        class3.setName("Bool");
        currentScope.symbolTable.add(class3.getName(), class3);

        //IO
        class4 = new Class();
        class4.setName("IO");
        currentScope.symbolTable.add(class4.getName(), class4);

        //Object
        class5 = new Class();
        class5.setName("Object");
        currentScope.symbolTable.add(class5.getName(), class5);

    }

    @Override
    public void exitProgram(CoolParser.ProgramContext ctx) {
        //Check The Class Existence Globally
        for (int i = 0; i < classNameArrayList.size(); i++) {
            if (currentScope.symbolTable.lookup(((Token) classNameArrayList.get(i)).getText()) == null) {
                System.err.println("Error " + "105" + ": in line [" + ((Token) classNameArrayList.get(i)).getLine() + ":" + ((Token) classNameArrayList.get(i)).getCharPositionInLine() + "], cannot find class " + ((Token) classNameArrayList.get(i)).getText());
            }
        }
        currentScope = currentScope.parent;
        depth--;
        System.out.println(depth);
        System.out.println("}");
    }

    @Override
    public void enterClasses(CoolParser.ClassesContext ctx) {

    }

    @Override
    public void exitClasses(CoolParser.ClassesContext ctx) {

    }

    @Override
    public void enterEof(CoolParser.EofContext ctx) {

    }

    @Override
    public void exitEof(CoolParser.EofContext ctx) {

    }

    @Override
    public void enterClassDefine(CoolParser.ClassDefineContext ctx) {
        depth++;

        //Initialize Of Class
        Class aClass = new Class();
        aClass.setName(ctx.className.getText());
        if (ctx.parentClass != null) {
            aClass.setParent(ctx.parentClass.getText());
            classNameArrayList.add(ctx.parentClass);
        }

        //Error Class Name has been defined already
        if (currentScope.symbolTable.lookup(aClass.getName()) != null) {
            System.err.println("Error " + "101" + ": in line [" + ctx.className.getLine() + ":" + ctx.className.getCharPositionInLine() + "], class " + aClass.getName() + " has been defined already");
            currentScope.symbolTable.add(ctx.className.getText().concat("_")
                    .concat(String.valueOf(ctx.className.getLine()))
                    .concat("_")
                    .concat(String.valueOf(ctx.className.getCharPositionInLine())), aClass);
        } else {
            currentScope.symbolTable.add(aClass.getName(), aClass);
        }

        //Create Class Scope
        Scope classScope = new Scope();
        classScope.parent = currentScope;
        classScope.level = depth;
        currentScope = classScope;

    }

    @Override
    public void exitClassDefine(CoolParser.ClassDefineContext ctx) {
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterMethod(CoolParser.MethodContext ctx) {
        depth++;

        //Initialize Of Method
        Method aMethod = new Method();
        aMethod.setName(ctx.methodName.getText());
        aMethod.setReturnValue(ctx.returnType.getText());

        //Delaying Submit
        temp = aMethod;
        temp1 = ctx.methodName;

        //Skip SELF_TYPE
        if (!ctx.returnType.getText().equals("SELF_TYPE")) {
            classNameArrayList.add(ctx.returnType);
        }
    }

    @Override
    public void exitMethod(CoolParser.MethodContext ctx) {
        temp = null;
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterProperty(CoolParser.PropertyContext ctx) {
        //Initialize Of Property
        Property aProperty = new Property();
        aProperty.setName(ctx.propertyName.getText());
        aProperty.setType(ctx.propertyType.getText());

        //Error Property Name has been defined already
        if (currentScope.symbolTable.lookup(aProperty.getName()) != null) {
            System.err.println("Error " + "104" + ": in line [" + ctx.propertyName.getLine() + ":" + ctx.propertyName.getCharPositionInLine() + "], property " + aProperty.getName() + " has been defined already");
            currentScope.symbolTable.add(aProperty.getName()
                    .concat("_")
                    .concat(String.valueOf(ctx.propertyName.getLine()))
                    .concat("_")
                    .concat(String.valueOf(ctx.propertyName.getCharPositionInLine())), aProperty);
        } else {
            currentScope.symbolTable.add(aProperty.getName(), aProperty);
        }
        classNameArrayList.add(ctx.propertyType);
    }

    @Override
    public void exitProperty(CoolParser.PropertyContext ctx) {

    }

    @Override
    public void enterAtribute(CoolParser.AtributeContext ctx) {

    }

    @Override
    public void exitAtribute(CoolParser.AtributeContext ctx) {
        //Add Method To The Symbol Table After identifying the method's components

        //Error Method Name has been defined already
        if (currentScope.symbolTable.lookup(((Method) temp).getName()) != null) {
            System.err.println("Error " + "102" + ": in line [" + temp1.getLine() + ":" + temp1.getCharPositionInLine() + "], method " + ((Method) temp).getName() + " has been defined already");
            currentScope.symbolTable.add(((Method) temp).getName().concat("_")
                    .concat(String.valueOf(temp1.getLine()))
                    .concat("_")
                    .concat(String.valueOf(temp1.getCharPositionInLine())), ((Method) temp));
        } else {
            currentScope.symbolTable.add(((Method) temp).getName(), (Method) temp);
        }

        //Create Method Scope
        Scope methodScope = new Scope();
        methodScope.parent = currentScope;
        methodScope.level = depth;
        currentScope = methodScope;
    }

    @Override
    public void enterFormal(CoolParser.FormalContext ctx) {

        //Initialize Of Method Arguments Variables
        Variable aVariable = new Variable();
        aVariable.setName(ctx.formalName.getText());
        aVariable.setType(ctx.formalType.getText());


        classNameArrayList.add(ctx.formalType);
        if (((Method) temp).equalsA(aVariable)) {
            System.err.println("Error " + "104" + ": in line [" + ctx.formalName.getLine() + ":" + ctx.formalName.getCharPositionInLine() + "], field " + ctx.formalName.getText() + " has been defined already");
        }
        ((Method) temp).setArgument(aVariable);
    }

    @Override
    public void exitFormal(CoolParser.FormalContext ctx) {

    }

    @Override
    public void enterLetIn(CoolParser.LetInContext ctx) {
        depth++;

        //Initialize Of Let Argument Property
        Property aProperty;

        //Initialize Of Method
        Let alet = new Let();


        for (int i = 0; i < ctx.OBJECTID().size(); i++) {
            aProperty = new Property();
            if (alet.checkArguments(ctx.OBJECTID().get(i).getText())) {
                System.err.println("Error " + "102" + ": in line [" + ctx.OBJECTID().get(i).getSymbol().getLine() + ":" + ctx.OBJECTID().get(i).getSymbol().getCharPositionInLine() + "], variable " + ctx.OBJECTID().get(i).getSymbol().getText() + " has been defined already");
                aProperty.setName(ctx.OBJECTID().get(i).getText().concat("_")
                        .concat(String.valueOf(temp1.getLine()))
                        .concat("_")
                        .concat(String.valueOf(temp1.getCharPositionInLine())));
            } else {
                aProperty.setName(ctx.OBJECTID().get(i).getText());
            }
            aProperty.setType(ctx.TYPEID().get(i).getText());
            alet.setArguments(aProperty);
            classNameArrayList.add(ctx.TYPEID().get(i).getSymbol());

        }
        alet.setParent(currentLet);
        currentLet = alet;
        currentScope.symbolTable.add(String.format("Let%d", Let.letNumber), alet);

        //Create LetIn Scope
        Scope letScope = new Scope();
        letScope.parent = currentScope;
        letScope.level = depth;
        currentScope = letScope;
    }

    @Override
    public void exitLetIn(CoolParser.LetInContext ctx) {
        currentLet = currentLet.getParent();
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterMinus(CoolParser.MinusContext ctx) {

    }

    @Override
    public void exitMinus(CoolParser.MinusContext ctx) {

    }

    @Override
    public void enterString(CoolParser.StringContext ctx) {

    }

    @Override
    public void exitString(CoolParser.StringContext ctx) {

    }

    @Override
    public void enterIsvoid(CoolParser.IsvoidContext ctx) {

    }

    @Override
    public void exitIsvoid(CoolParser.IsvoidContext ctx) {

    }

    @Override
    public void enterWhile(CoolParser.WhileContext ctx) {
        depth++;

        //Initialize Of Property
        While aWhile = new While();

        currentScope.symbolTable.add(String.format("While%d", While.whileNumber), aWhile);

        //Create While Scope
        Scope whileScope = new Scope();
        whileScope.parent = currentScope;
        whileScope.level = depth;
        currentScope = whileScope;

    }

    @Override
    public void exitWhile(CoolParser.WhileContext ctx) {
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterDivision(CoolParser.DivisionContext ctx) {

    }

    @Override
    public void exitDivision(CoolParser.DivisionContext ctx) {

    }

    @Override
    public void enterNegative(CoolParser.NegativeContext ctx) {

    }

    @Override
    public void exitNegative(CoolParser.NegativeContext ctx) {

    }

    @Override
    public void enterBoolNot(CoolParser.BoolNotContext ctx) {

    }

    @Override
    public void exitBoolNot(CoolParser.BoolNotContext ctx) {

    }

    @Override
    public void enterLessThan(CoolParser.LessThanContext ctx) {

    }

    @Override
    public void exitLessThan(CoolParser.LessThanContext ctx) {

    }

    @Override
    public void enterBlock(CoolParser.BlockContext ctx) {
        depth++;

        //Initialize Of Property
        Block aBlock = new Block();

        //Add To Current Scope
        currentScope.symbolTable.add(String.format("Block%d", Block.blockNumber), aBlock);

        Scope blockScope = new Scope();
        blockScope.parent = currentScope;
        blockScope.level = depth;
        currentScope = blockScope;
    }

    @Override
    public void exitBlock(CoolParser.BlockContext ctx) {
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterId(CoolParser.IdContext ctx) {

        boolean naro = false;
        boolean checkMethod = true;
        boolean checkParentScope = true;

        boolean letFlag = false;
        boolean methodFlag = false;
        boolean ParentScopeFlag = false;

        //Check Let Arguments
        if (currentLet != null) {
            Let cacheTemp = currentLet;
            for (int i = depth; i >= 0; i--) {
                if (cacheTemp != null) {

                    for (int j = 0; j < currentLet.getArguments().size(); j++) {
                        if (!currentLet.getArguments().get(j).equalsN(ctx.OBJECTID().getSymbol().getText())) {
                            if (!ctx.OBJECTID().getSymbol().getText().equals("self")) {
                                letFlag = true;
                                checkMethod = true;
                                checkParentScope = true;
                            }
                        } else {
                            naro = true;
                            letFlag = false;
                            checkMethod = false;
                            checkParentScope = false;
                            break;
                        }
                    }
                    if (cacheTemp.getParent() != null || !naro) {
                        cacheTemp = cacheTemp.getParent();
                    } else {
                        break;
                    }
                }
            }

        }

        // Check Method Arguments
        if (checkMethod) {
            if (temp != null) {
                for (int j = 0; j < ((Method) temp).getArguments().size(); j++) {
                    if (!((Method) temp).getArguments().get(j).equalsN(ctx.OBJECTID().getSymbol().getText())) {
                        if (!ctx.OBJECTID().getSymbol().getText().equals("self")) {
                            methodFlag = true;
                            checkParentScope = true;
                        }
                    } else {
                        letFlag = false;
                        methodFlag = false;
                        checkParentScope = false;
                        break;
                    }
                }
            }
        }
        //Check Parent Scopes
        if (checkParentScope) {
            Scope tempScope = currentScope;
            for (int i = depth; i >= 0; i--) {
                if (tempScope != null) {
                    if (tempScope.symbolTable.lookup(ctx.OBJECTID().getSymbol().getText()) == null) {
                        if (!ctx.OBJECTID().getSymbol().getText().equals("self")) {
                            ParentScopeFlag = true;
                        }
                    } else {
                        letFlag = false;
                        methodFlag = false;
                        ParentScopeFlag = false;
                        break;
                    }
                    if (tempScope.parent != null) {
                        tempScope = tempScope.parent;
                    } else {
                        break;
                    }
                }
            }
        }
        //Print Errors
        if (letFlag || methodFlag || ParentScopeFlag) {
            System.err.println("Error " + "106" + ": in line [" + ctx.OBJECTID().getSymbol().getLine() + ":" + ctx.OBJECTID().getSymbol().getCharPositionInLine() + "]," + " Can not found Variable " + ctx.OBJECTID().getText());
        }


    }

    @Override
    public void exitId(CoolParser.IdContext ctx) {

    }

    @Override
    public void enterMultiply(CoolParser.MultiplyContext ctx) {

    }

    @Override
    public void exitMultiply(CoolParser.MultiplyContext ctx) {

    }

    @Override
    public void enterIf(CoolParser.IfContext ctx) {
        depth++;

        //Initialize Of Switch Case
        SwitchCase aIf = new SwitchCase();

        currentScope.symbolTable.add(String.format("If%d", If.ifNumber), aIf);

        Scope ifScope = new Scope();
        ifScope.parent = currentScope;
        ifScope.level = depth;
        currentScope = ifScope;

    }

    @Override
    public void exitIf(CoolParser.IfContext ctx) {
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterCase(CoolParser.CaseContext ctx) {
        depth++;

        //Initialize Of Switch Case
        SwitchCase aSwitchCase = new SwitchCase();

        currentScope.symbolTable.add(String.format("SwitchCase%d", SwitchCase.switchNumber), aSwitchCase);

        Scope caseScope = new Scope();
        caseScope.parent = currentScope;
        caseScope.level = depth;
        currentScope = caseScope;
    }

    @Override
    public void exitCase(CoolParser.CaseContext ctx) {
        currentScope = currentScope.parent;
        depth--;
    }

    @Override
    public void enterOwnMethodCall(CoolParser.OwnMethodCallContext ctx) {

    }

    @Override
    public void exitOwnMethodCall(CoolParser.OwnMethodCallContext ctx) {

    }

    @Override
    public void enterAdd(CoolParser.AddContext ctx) {

    }

    @Override
    public void exitAdd(CoolParser.AddContext ctx) {

    }

    @Override
    public void enterNew(CoolParser.NewContext ctx) {
        classNameArrayList.add(ctx.TYPEID().getSymbol());
    }

    @Override
    public void exitNew(CoolParser.NewContext ctx) {

    }

    @Override
    public void enterParentheses(CoolParser.ParenthesesContext ctx) {

    }

    @Override
    public void exitParentheses(CoolParser.ParenthesesContext ctx) {

    }

    @Override
    public void enterAssignment(CoolParser.AssignmentContext ctx) {
        boolean naro = false;
        boolean checkMethod = true;
        boolean checkParentScope = true;

        boolean letFlag = false;
        boolean methodFlag = false;
        boolean ParentScopeFlag = false;

        //Check Let Arguments
        if (currentLet != null) {
            Let cacheTemp = currentLet;
            for (int i = depth; i >= 0; i--) {
                if (cacheTemp != null) {

                    for (int j = 0; j < currentLet.getArguments().size(); j++) {
                        if (!currentLet.getArguments().get(j).equalsN(ctx.OBJECTID().getSymbol().getText())) {
                            if (!ctx.OBJECTID().getSymbol().getText().equals("self")) {
                                letFlag = true;
                                checkMethod = true;
                                checkParentScope = true;
                            }
                        } else {
                            naro = true;
                            letFlag = false;
                            checkMethod = false;
                            checkParentScope = false;
                            break;
                        }
                    }
                    if (cacheTemp.getParent() != null || !naro) {
                        cacheTemp = cacheTemp.getParent();
                    } else {
                        break;
                    }
                }
            }

        }

        // Check Method Arguments
        if (checkMethod) {
            if (temp != null) {
                for (int j = 0; j < ((Method) temp).getArguments().size(); j++) {
                    if (!((Method) temp).getArguments().get(j).equalsN(ctx.OBJECTID().getSymbol().getText())) {
                        if (!ctx.OBJECTID().getSymbol().getText().equals("self")) {
                            methodFlag = true;
                            checkParentScope = true;
                        }
                    } else {
                        letFlag = false;
                        methodFlag = false;
                        checkParentScope = false;
                        break;
                    }
                }
            }
        }
        //Check Parent Scopes
        if (checkParentScope) {
            Scope tempScope = currentScope;
            for (int i = depth; i >= 0; i--) {
                if (tempScope != null) {
                    if (tempScope.symbolTable.lookup(ctx.OBJECTID().getSymbol().getText()) == null) {
                        if (!ctx.OBJECTID().getSymbol().getText().equals("self")) {
                            ParentScopeFlag = true;
                        }
                    } else {
                        letFlag = false;
                        methodFlag = false;
                        ParentScopeFlag = false;
                        break;
                    }
                    if (tempScope.parent != null) {
                        tempScope = tempScope.parent;
                    } else {
                        break;
                    }
                }
            }
        }
        //Print Errors
        if (letFlag || methodFlag || ParentScopeFlag) {
            System.err.println("Error " + "106" + ": in line [" + ctx.OBJECTID().getSymbol().getLine() + ":" + ctx.OBJECTID().getSymbol().getCharPositionInLine() + "]," + " Can not found Variable " + ctx.OBJECTID().getText());
        }

    }

    @Override
    public void exitAssignment(CoolParser.AssignmentContext ctx) {

    }

    @Override
    public void enterFalse(CoolParser.FalseContext ctx) {

    }

    @Override
    public void exitFalse(CoolParser.FalseContext ctx) {

    }

    @Override
    public void enterInt(CoolParser.IntContext ctx) {

    }

    @Override
    public void exitInt(CoolParser.IntContext ctx) {

    }

    @Override
    public void enterEqual(CoolParser.EqualContext ctx) {

    }

    @Override
    public void exitEqual(CoolParser.EqualContext ctx) {

    }

    @Override
    public void enterTrue(CoolParser.TrueContext ctx) {

    }

    @Override
    public void exitTrue(CoolParser.TrueContext ctx) {

    }

    @Override
    public void enterLessEqual(CoolParser.LessEqualContext ctx) {

    }

    @Override
    public void exitLessEqual(CoolParser.LessEqualContext ctx) {

    }

    @Override
    public void enterMethodCall(CoolParser.MethodCallContext ctx) {

    }

    @Override
    public void exitMethodCall(CoolParser.MethodCallContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
