/***********************************************************************************************************************
 * Copyright (c) 2003, International Barcode Consortium
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

package net.sourceforge.barbecue.linear.code128;

import net.sourceforge.barbecue.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code 128 barcode module definitions.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public final class ModuleFactory {
    private static final List A_KEYS = new ArrayList();
    private static final List B_KEYS = new ArrayList();
    private static final List C_KEYS = new ArrayList();
    private static final Map A_SET = new HashMap();
    private static final Map B_SET = new HashMap();
    private static final Map C_SET = new HashMap();
    
    static {
        initA();
        initB();
        initC();
    }
    
    ///CLOVER:OFF
    /**
     * Cannot construct.
     */
    private ModuleFactory() {
    }
    ///CLOVER:ON
    
    /**
     * Initialise the module definitions.
     */
    private static void initA() {
        A_KEYS.add(" "); A_SET.put(" ", new Module(new int[] {2, 1, 2, 2, 2, 2}));
        A_KEYS.add("!"); A_SET.put("!", new Module(new int[] {2, 2, 2, 1, 2, 2}));
        A_KEYS.add("\""); A_SET.put("\"", new Module(new int[] {2, 2, 2, 2, 2, 1}));
        A_KEYS.add("#"); A_SET.put("#", new Module(new int[] {1, 2, 1, 2, 2, 3}));
        A_KEYS.add("$"); A_SET.put("$", new Module(new int[] {1, 2, 1, 3, 2, 2}));
        A_KEYS.add("%"); A_SET.put("%", new Module(new int[] {1, 3, 1, 2, 2, 2}));
        A_KEYS.add("&"); A_SET.put("&", new Module(new int[] {1, 2, 2, 2, 1, 3}));
        A_KEYS.add("'"); A_SET.put("'", new Module(new int[] {1, 2, 2, 3, 1, 2}));
        A_KEYS.add("("); A_SET.put("(", new Module(new int[] {1, 3, 2, 2, 1, 2}));
        A_KEYS.add(")"); A_SET.put(")", new Module(new int[] {2, 2, 1, 2, 1, 3}));
        A_KEYS.add("*"); A_SET.put("*", new Module(new int[] {2, 2, 1, 3, 1, 2}));
        A_KEYS.add("+"); A_SET.put("+", new Module(new int[] {2, 3, 1, 2, 1, 2}));
        A_KEYS.add(","); A_SET.put(",", new Module(new int[] {1, 1, 2, 2, 3, 2}));
        A_KEYS.add("-"); A_SET.put("-", new Module(new int[] {1, 2, 2, 1, 3, 2}));
        A_KEYS.add("."); A_SET.put(".", new Module(new int[] {1, 2, 2, 2, 3, 1}));
        A_KEYS.add("/"); A_SET.put("/", new Module(new int[] {1, 1, 3, 2, 2, 2}));
        A_KEYS.add("0"); A_SET.put("0", new Module(new int[] {1, 2, 3, 1, 2, 2}));
        A_KEYS.add("1"); A_SET.put("1", new Module(new int[] {1, 2, 3, 2, 2, 1}));
        A_KEYS.add("2"); A_SET.put("2", new Module(new int[] {2, 2, 3, 2, 1, 1}));
        A_KEYS.add("3"); A_SET.put("3", new Module(new int[] {2, 2, 1, 1, 3, 2}));
        A_KEYS.add("4"); A_SET.put("4", new Module(new int[] {2, 2, 1, 2, 3, 1}));
        A_KEYS.add("5"); A_SET.put("5", new Module(new int[] {2, 1, 3, 2, 1, 2}));
        A_KEYS.add("6"); A_SET.put("6", new Module(new int[] {2, 2, 3, 1, 1, 2}));
        A_KEYS.add("7"); A_SET.put("7", new Module(new int[] {3, 1, 2, 1, 3, 1}));
        A_KEYS.add("8"); A_SET.put("8", new Module(new int[] {3, 1, 1, 2, 2, 2}));
        A_KEYS.add("9"); A_SET.put("9", new Module(new int[] {3, 2, 1, 1, 2, 2}));
        A_KEYS.add(":"); A_SET.put(":", new Module(new int[] {3, 2, 1, 2, 2, 1}));
        A_KEYS.add(";"); A_SET.put(";", new Module(new int[] {3, 1, 2, 2, 1, 2}));
        A_KEYS.add("<"); A_SET.put("<", new Module(new int[] {3, 2, 2, 1, 1, 2}));
        A_KEYS.add("="); A_SET.put("=", new Module(new int[] {3, 2, 2, 2, 1, 1}));
        A_KEYS.add(">"); A_SET.put(">", new Module(new int[] {2, 1, 2, 1, 2, 3}));
        A_KEYS.add("?"); A_SET.put("?", new Module(new int[] {2, 1, 2, 3, 2, 1}));
        A_KEYS.add("@"); A_SET.put("@", new Module(new int[] {2, 3, 2, 1, 2, 1}));
        A_KEYS.add("A"); A_SET.put("A", new Module(new int[] {1, 1, 1, 3, 2, 3}));
        A_KEYS.add("B"); A_SET.put("B", new Module(new int[] {1, 3, 1, 1, 2, 3}));
        A_KEYS.add("C"); A_SET.put("C", new Module(new int[] {1, 3, 1, 3, 2, 1}));
        A_KEYS.add("D"); A_SET.put("D", new Module(new int[] {1, 1, 2, 3, 1, 3}));
        A_KEYS.add("E"); A_SET.put("E", new Module(new int[] {1, 3, 2, 1, 1, 3}));
        A_KEYS.add("F"); A_SET.put("F", new Module(new int[] {1, 3, 2, 3, 1, 1}));
        A_KEYS.add("G"); A_SET.put("G", new Module(new int[] {2, 1, 1, 3, 1, 3}));
        A_KEYS.add("H"); A_SET.put("H", new Module(new int[] {2, 3, 1, 1, 1, 3}));
        A_KEYS.add("I"); A_SET.put("I", new Module(new int[] {2, 3, 1, 3, 1, 1}));
        A_KEYS.add("J"); A_SET.put("J", new Module(new int[] {1, 1, 2, 1, 3, 3}));
        A_KEYS.add("K"); A_SET.put("K", new Module(new int[] {1, 1, 2, 3, 3, 1}));
        A_KEYS.add("L"); A_SET.put("L", new Module(new int[] {1, 3, 2, 1, 3, 1}));
        A_KEYS.add("M"); A_SET.put("M", new Module(new int[] {1, 1, 3, 1, 2, 3}));
        A_KEYS.add("N"); A_SET.put("N", new Module(new int[] {1, 1, 3, 3, 2, 1}));
        A_KEYS.add("O"); A_SET.put("O", new Module(new int[] {1, 3, 3, 1, 2, 1}));
        A_KEYS.add("P"); A_SET.put("P", new Module(new int[] {3, 1, 3, 1, 2, 1}));
        A_KEYS.add("Q"); A_SET.put("Q", new Module(new int[] {2, 1, 1, 3, 3, 1}));
        A_KEYS.add("R"); A_SET.put("R", new Module(new int[] {2, 3, 1, 1, 3, 1}));
        A_KEYS.add("S"); A_SET.put("S", new Module(new int[] {2, 1, 3, 1, 1, 3}));
        A_KEYS.add("T"); A_SET.put("T", new Module(new int[] {2, 1, 3, 3, 1, 1}));
        A_KEYS.add("U"); A_SET.put("U", new Module(new int[] {2, 1, 3, 1, 3, 1}));
        A_KEYS.add("V"); A_SET.put("V", new Module(new int[] {3, 1, 1, 1, 2, 3}));
        A_KEYS.add("W"); A_SET.put("W", new Module(new int[] {3, 1, 1, 3, 2, 1}));
        A_KEYS.add("X"); A_SET.put("X", new Module(new int[] {3, 3, 1, 1, 2, 1}));
        A_KEYS.add("Y"); A_SET.put("Y", new Module(new int[] {3, 1, 2, 1, 1, 3}));
        A_KEYS.add("Z"); A_SET.put("Z", new Module(new int[] {3, 1, 2, 3, 1, 1}));
        A_KEYS.add("["); A_SET.put("[", new Module(new int[] {3, 3, 2, 1, 1, 1}));
        A_KEYS.add("\\"); A_SET.put("\\", new Module(new int[] {3, 1, 4, 1, 1, 1}));
        A_KEYS.add("]"); A_SET.put("]", new Module(new int[] {2, 2, 1, 4, 1, 1}));
        A_KEYS.add("^"); A_SET.put("^", new Module(new int[] {4, 3, 1, 1, 1, 1}));
        A_KEYS.add("_"); A_SET.put("_", new Module(new int[] {1, 1, 1, 2, 2, 4}));
        /*NUL*/	A_KEYS.add("\000"); A_SET.put("\000", new Module(new int[] {1, 1, 1, 4, 2, 2}));
        /*SOH*/	A_KEYS.add("\001"); A_SET.put("\001", new Module(new int[] {1, 2, 1, 1, 2, 4}));
        /*STX*/	A_KEYS.add("\002"); A_SET.put("\002", new Module(new int[] {1, 2, 1, 4, 2, 1}));
        /*ETX*/	A_KEYS.add("\003"); A_SET.put("\003", new Module(new int[] {1, 4, 1, 1, 2, 2}));
        /*EOT*/	A_KEYS.add("\004"); A_SET.put("\004", new Module(new int[] {1, 4, 1, 2, 2, 1}));
        /*ENQ*/	A_KEYS.add("\005"); A_SET.put("\005", new Module(new int[] {1, 1, 2, 2, 1, 4}));
        /*ACK*/	A_KEYS.add("\006"); A_SET.put("\006", new Module(new int[] {1, 1, 2, 4, 1, 2}));
        /*BEL*/	A_KEYS.add("\007"); A_SET.put("\007", new Module(new int[] {1, 2, 2, 1, 1, 4}));
        /*BS*/	A_KEYS.add("\010"); A_SET.put("\010", new Module(new int[] {1, 2, 2, 4, 1, 1}));
        /*HT*/	A_KEYS.add("\011"); A_SET.put("\011", new Module(new int[] {1, 4, 2, 1, 1, 2}));
        /*LF*/	A_KEYS.add("\012"); A_SET.put("\012", new Module(new int[] {1, 4, 2, 2, 1, 1}));
        /*VT*/	A_KEYS.add("\013"); A_SET.put("\013", new Module(new int[] {2, 4, 1, 2, 1, 1}));
        /*FF*/	A_KEYS.add("\014"); A_SET.put("\014", new Module(new int[] {2, 2, 1, 1, 1, 4}));
        /*CR*/	A_KEYS.add("\015"); A_SET.put("\015", new Module(new int[] {4, 1, 3, 1, 1, 1}));
        /*SO*/	A_KEYS.add("\016"); A_SET.put("\016", new Module(new int[] {2, 4, 1, 1, 1, 2}));
        /*SI*/	A_KEYS.add("\017"); A_SET.put("\017", new Module(new int[] {1, 3, 4, 1, 1, 1}));
        /*DLE*/	A_KEYS.add("\020"); A_SET.put("\020", new Module(new int[] {1, 1, 1, 2, 4, 2}));
        /*DC1*/	A_KEYS.add("\021"); A_SET.put("\021", new Module(new int[] {1, 2, 1, 1, 4, 2}));
        /*DC2*/	A_KEYS.add("\022"); A_SET.put("\022", new Module(new int[] {1, 2, 1, 2, 4, 1}));
        /*DC3*/	A_KEYS.add("\023"); A_SET.put("\023", new Module(new int[] {1, 1, 4, 2, 1, 2}));
        /*DC4*/	A_KEYS.add("\024"); A_SET.put("\024", new Module(new int[] {1, 2, 4, 1, 1, 2}));
        /*NAK*/	A_KEYS.add("\025"); A_SET.put("\025", new Module(new int[] {1, 2, 4, 2, 1, 1}));
        /*SYN*/	A_KEYS.add("\026"); A_SET.put("\026", new Module(new int[] {4, 1, 1, 2, 1, 2}));
        /*ETB*/	A_KEYS.add("\027"); A_SET.put("\027", new Module(new int[] {4, 2, 1, 1, 1, 2}));
        /*CAN*/	A_KEYS.add("\030"); A_SET.put("\030", new Module(new int[] {4, 2, 1, 2, 1, 1}));
        /*EM*/	A_KEYS.add("\031"); A_SET.put("\031", new Module(new int[] {2, 1, 2, 1, 4, 1}));
        /*SUB*/	A_KEYS.add("\032"); A_SET.put("\032", new Module(new int[] {2, 1, 4, 1, 2, 1}));
        /*ESC*/	A_KEYS.add("\033"); A_SET.put("\033", new Module(new int[] {4, 1, 2, 1, 2, 1}));
        /*FS*/	A_KEYS.add("\034"); A_SET.put("\034", new Module(new int[] {1, 1, 1, 1, 4, 3}));
        /*GS*/	A_KEYS.add("\035"); A_SET.put("\035", new Module(new int[] {1, 1, 1, 3, 4, 1}));
        /*RS*/	A_KEYS.add("\036"); A_SET.put("\036", new Module(new int[] {1, 3, 1, 1, 4, 1}));
        /*US*/	A_KEYS.add("\037"); A_SET.put("\037", new Module(new int[] {1, 1, 4, 1, 1, 3}));
        A_KEYS.add("\304"); A_SET.put("\304", new Module(new int[] {1, 1, 4, 3, 1, 1})); // FNC3
        A_KEYS.add("\305"); A_SET.put("\305", new Module(new int[] {4, 1, 1, 1, 1, 3})); // FNC2
        A_KEYS.add("\306"); A_SET.put("\306", new ShiftModule(new int[] {4, 1, 1, 3, 1, 1})); // SHIFT
        A_KEYS.add("\307");
        A_SET.put("\307", new CodeChangeModule(new int[] {1, 1, 3, 1, 4, 1}, Code128Barcode.C)); // CODE C
        A_KEYS.add("\310");
        A_SET.put("\310", new CodeChangeModule(new int[] {1, 1, 4, 1, 3, 1}, Code128Barcode.B)); // CODE B
        A_KEYS.add("\311");
        A_SET.put("\311", new Module(new int[] {3, 1, 1, 1, 4, 1})); // FNC4
        A_KEYS.add("\312");
        A_SET.put("\312", new Module(new int[] {4, 1, 1, 1, 3, 1})); // FNC1
    }
    
    /**
     * Initialise the module definitions.
     */
    private static void initB() {
        B_KEYS.add(" "); B_SET.put(" ", new Module(new int[] {2, 1, 2, 2, 2, 2}));
        B_KEYS.add("!"); B_SET.put("!", new Module(new int[] {2, 2, 2, 1, 2, 2}));
        B_KEYS.add("\""); B_SET.put("\"", new Module(new int[] {2, 2, 2, 2, 2, 1}));
        B_KEYS.add("#"); B_SET.put("#", new Module(new int[] {1, 2, 1, 2, 2, 3}));
        B_KEYS.add("$"); B_SET.put("$", new Module(new int[] {1, 2, 1, 3, 2, 2}));
        B_KEYS.add("%"); B_SET.put("%", new Module(new int[] {1, 3, 1, 2, 2, 2}));
        B_KEYS.add("&"); B_SET.put("&", new Module(new int[] {1, 2, 2, 2, 1, 3}));
        B_KEYS.add("'"); B_SET.put("'", new Module(new int[] {1, 2, 2, 3, 1, 2}));
        B_KEYS.add("("); B_SET.put("(", new Module(new int[] {1, 3, 2, 2, 1, 2}));
        B_KEYS.add(")"); B_SET.put(")", new Module(new int[] {2, 2, 1, 2, 1, 3}));
        B_KEYS.add("*"); B_SET.put("*", new Module(new int[] {2, 2, 1, 3, 1, 2}));
        B_KEYS.add("+"); B_SET.put("+", new Module(new int[] {2, 3, 1, 2, 1, 2}));
        B_KEYS.add(","); B_SET.put(",", new Module(new int[] {1, 1, 2, 2, 3, 2}));
        B_KEYS.add("-"); B_SET.put("-", new Module(new int[] {1, 2, 2, 1, 3, 2}));
        B_KEYS.add("."); B_SET.put(".", new Module(new int[] {1, 2, 2, 2, 3, 1}));
        B_KEYS.add("/"); B_SET.put("/", new Module(new int[] {1, 1, 3, 2, 2, 2}));
        B_KEYS.add("0"); B_SET.put("0", new Module(new int[] {1, 2, 3, 1, 2, 2}));
        B_KEYS.add("1"); B_SET.put("1", new Module(new int[] {1, 2, 3, 2, 2, 1}));
        B_KEYS.add("2"); B_SET.put("2", new Module(new int[] {2, 2, 3, 2, 1, 1}));
        B_KEYS.add("3"); B_SET.put("3", new Module(new int[] {2, 2, 1, 1, 3, 2}));
        B_KEYS.add("4"); B_SET.put("4", new Module(new int[] {2, 2, 1, 2, 3, 1}));
        B_KEYS.add("5"); B_SET.put("5", new Module(new int[] {2, 1, 3, 2, 1, 2}));
        B_KEYS.add("6"); B_SET.put("6", new Module(new int[] {2, 2, 3, 1, 1, 2}));
        B_KEYS.add("7"); B_SET.put("7", new Module(new int[] {3, 1, 2, 1, 3, 1}));
        B_KEYS.add("8"); B_SET.put("8", new Module(new int[] {3, 1, 1, 2, 2, 2}));
        B_KEYS.add("9"); B_SET.put("9", new Module(new int[] {3, 2, 1, 1, 2, 2}));
        B_KEYS.add(":"); B_SET.put(":", new Module(new int[] {3, 2, 1, 2, 2, 1}));
        B_KEYS.add(";"); B_SET.put(";", new Module(new int[] {3, 1, 2, 2, 1, 2}));
        B_KEYS.add("<"); B_SET.put("<", new Module(new int[] {3, 2, 2, 1, 1, 2}));
        B_KEYS.add("="); B_SET.put("=", new Module(new int[] {3, 2, 2, 2, 1, 1}));
        B_KEYS.add(">"); B_SET.put(">", new Module(new int[] {2, 1, 2, 1, 2, 3}));
        B_KEYS.add("?"); B_SET.put("?", new Module(new int[] {2, 1, 2, 3, 2, 1}));
        B_KEYS.add("@"); B_SET.put("@", new Module(new int[] {2, 3, 2, 1, 2, 1}));
        B_KEYS.add("A"); B_SET.put("A", new Module(new int[] {1, 1, 1, 3, 2, 3}));
        B_KEYS.add("B"); B_SET.put("B", new Module(new int[] {1, 3, 1, 1, 2, 3}));
        B_KEYS.add("C"); B_SET.put("C", new Module(new int[] {1, 3, 1, 3, 2, 1}));
        B_KEYS.add("D"); B_SET.put("D", new Module(new int[] {1, 1, 2, 3, 1, 3}));
        B_KEYS.add("E"); B_SET.put("E", new Module(new int[] {1, 3, 2, 1, 1, 3}));
        B_KEYS.add("F"); B_SET.put("F", new Module(new int[] {1, 3, 2, 3, 1, 1}));
        B_KEYS.add("G"); B_SET.put("G", new Module(new int[] {2, 1, 1, 3, 1, 3}));
        B_KEYS.add("H"); B_SET.put("H", new Module(new int[] {2, 3, 1, 1, 1, 3}));
        B_KEYS.add("I"); B_SET.put("I", new Module(new int[] {2, 3, 1, 3, 1, 1}));
        B_KEYS.add("J"); B_SET.put("J", new Module(new int[] {1, 1, 2, 1, 3, 3}));
        B_KEYS.add("K"); B_SET.put("K", new Module(new int[] {1, 1, 2, 3, 3, 1}));
        B_KEYS.add("L"); B_SET.put("L", new Module(new int[] {1, 3, 2, 1, 3, 1}));
        B_KEYS.add("M"); B_SET.put("M", new Module(new int[] {1, 1, 3, 1, 2, 3}));
        B_KEYS.add("N"); B_SET.put("N", new Module(new int[] {1, 1, 3, 3, 2, 1}));
        B_KEYS.add("O"); B_SET.put("O", new Module(new int[] {1, 3, 3, 1, 2, 1}));
        B_KEYS.add("P"); B_SET.put("P", new Module(new int[] {3, 1, 3, 1, 2, 1}));
        B_KEYS.add("Q"); B_SET.put("Q", new Module(new int[] {2, 1, 1, 3, 3, 1}));
        B_KEYS.add("R"); B_SET.put("R", new Module(new int[] {2, 3, 1, 1, 3, 1}));
        B_KEYS.add("S"); B_SET.put("S", new Module(new int[] {2, 1, 3, 1, 1, 3}));
        B_KEYS.add("T"); B_SET.put("T", new Module(new int[] {2, 1, 3, 3, 1, 1}));
        B_KEYS.add("U"); B_SET.put("U", new Module(new int[] {2, 1, 3, 1, 3, 1}));
        B_KEYS.add("V"); B_SET.put("V", new Module(new int[] {3, 1, 1, 1, 2, 3}));
        B_KEYS.add("W"); B_SET.put("W", new Module(new int[] {3, 1, 1, 3, 2, 1}));
        B_KEYS.add("X"); B_SET.put("X", new Module(new int[] {3, 3, 1, 1, 2, 1}));
        B_KEYS.add("Y"); B_SET.put("Y", new Module(new int[] {3, 1, 2, 1, 1, 3}));
        B_KEYS.add("Z"); B_SET.put("Z", new Module(new int[] {3, 1, 2, 3, 1, 1}));
        B_KEYS.add("["); B_SET.put("[", new Module(new int[] {3, 3, 2, 1, 1, 1}));
        B_KEYS.add("\\"); B_SET.put("\\", new Module(new int[] {3, 1, 4, 1, 1, 1}));
        B_KEYS.add("]"); B_SET.put("]", new Module(new int[] {2, 2, 1, 4, 1, 1}));
        B_KEYS.add("^"); B_SET.put("^", new Module(new int[] {4, 3, 1, 1, 1, 1}));
        B_KEYS.add("_"); B_SET.put("_", new Module(new int[] {1, 1, 1, 2, 2, 4}));
        B_KEYS.add("`"); B_SET.put("`", new Module(new int[] {1, 1, 1, 4, 2, 2}));
        B_KEYS.add("a"); B_SET.put("a", new Module(new int[] {1, 2, 1, 1, 2, 4}));
        B_KEYS.add("b"); B_SET.put("b", new Module(new int[] {1, 2, 1, 4, 2, 1}));
        B_KEYS.add("c"); B_SET.put("c", new Module(new int[] {1, 4, 1, 1, 2, 2}));
        B_KEYS.add("d"); B_SET.put("d", new Module(new int[] {1, 4, 1, 2, 2, 1}));
        B_KEYS.add("e"); B_SET.put("e", new Module(new int[] {1, 1, 2, 2, 1, 4}));
        B_KEYS.add("f"); B_SET.put("f", new Module(new int[] {1, 1, 2, 4, 1, 2}));
        B_KEYS.add("g"); B_SET.put("g", new Module(new int[] {1, 2, 2, 1, 1, 4}));
        B_KEYS.add("h"); B_SET.put("h", new Module(new int[] {1, 2, 2, 4, 1, 1}));
        B_KEYS.add("i"); B_SET.put("i", new Module(new int[] {1, 4, 2, 1, 1, 2}));
        B_KEYS.add("j"); B_SET.put("j", new Module(new int[] {1, 4, 2, 2, 1, 1}));
        B_KEYS.add("k"); B_SET.put("k", new Module(new int[] {2, 4, 1, 2, 1, 1}));
        B_KEYS.add("l"); B_SET.put("l", new Module(new int[] {2, 2, 1, 1, 1, 4}));
        B_KEYS.add("m"); B_SET.put("m", new Module(new int[] {4, 1, 3, 1, 1, 1}));
        B_KEYS.add("n"); B_SET.put("n", new Module(new int[] {2, 4, 1, 1, 1, 2}));
        B_KEYS.add("o"); B_SET.put("o", new Module(new int[] {1, 3, 4, 1, 1, 1}));
        B_KEYS.add("p"); B_SET.put("p", new Module(new int[] {1, 1, 1, 2, 4, 2}));
        B_KEYS.add("q"); B_SET.put("q", new Module(new int[] {1, 2, 1, 1, 4, 2}));
        B_KEYS.add("r"); B_SET.put("r", new Module(new int[] {1, 2, 1, 2, 4, 1}));
        B_KEYS.add("s"); B_SET.put("s", new Module(new int[] {1, 1, 4, 2, 1, 2}));
        B_KEYS.add("t"); B_SET.put("t", new Module(new int[] {1, 2, 4, 1, 1, 2}));
        B_KEYS.add("u"); B_SET.put("u", new Module(new int[] {1, 2, 4, 2, 1, 1}));
        B_KEYS.add("v"); B_SET.put("v", new Module(new int[] {4, 1, 1, 2, 1, 2}));
        B_KEYS.add("w"); B_SET.put("w", new Module(new int[] {4, 2, 1, 1, 1, 2}));
        B_KEYS.add("x"); B_SET.put("x", new Module(new int[] {4, 2, 1, 2, 1, 1}));
        B_KEYS.add("y"); B_SET.put("y", new Module(new int[] {2, 1, 2, 1, 4, 1}));
        B_KEYS.add("z"); B_SET.put("z", new Module(new int[] {2, 1, 4, 1, 2, 1}));
        B_KEYS.add("{"); B_SET.put("{", new Module(new int[] {4, 1, 2, 1, 2, 1}));
        B_KEYS.add("|"); B_SET.put("|", new Module(new int[] {1, 1, 1, 1, 4, 3}));
        B_KEYS.add("}"); B_SET.put("}", new Module(new int[] {1, 1, 1, 3, 4, 1}));
        B_KEYS.add("~"); B_SET.put("~", new Module(new int[] {1, 3, 1, 1, 4, 1}));
        B_KEYS.add("\303"); B_SET.put("\303", new Module(new int[] {1, 1, 4, 1, 1, 3})); // DEL 10111101000
        B_KEYS.add("\304"); B_SET.put("\304", new Module(new int[] {1, 1, 4, 3, 1, 1}));
        B_KEYS.add("\305"); B_SET.put("\305", new Module(new int[] {4, 1, 1, 1, 1, 3}));
        B_KEYS.add("\306");
        B_SET.put("\306", new ShiftModule(new int[] {4, 1, 1, 3, 1, 1})); // SHIFT
        B_KEYS.add("\307");
        B_SET.put("\307", new CodeChangeModule(new int[] {1, 1, 3, 1, 4, 1}, Code128Barcode.C)); // CODE C
        B_KEYS.add("\310");
        B_SET.put("\310", new Module(new int[] {1, 1, 4, 1, 3, 1}));
        B_KEYS.add("\311");
        B_SET.put("\311", new CodeChangeModule(new int[] {3, 1, 1, 1, 4, 1}, Code128Barcode.A)); // CODE A
        B_KEYS.add("\312");
        B_SET.put("\312", new Module(new int[] {4, 1, 1, 1, 3, 1}));
    }
    
    /**
     * Initialise the module definitions.
     */
    private static void initC() {
        C_KEYS.add("00"); C_SET.put("00", new Module(new int[] {2, 1, 2, 2, 2, 2}));
        C_KEYS.add("01"); C_SET.put("01", new Module(new int[] {2, 2, 2, 1, 2, 2}));
        C_KEYS.add("02"); C_SET.put("02", new Module(new int[] {2, 2, 2, 2, 2, 1}));
        C_KEYS.add("03"); C_SET.put("03", new Module(new int[] {1, 2, 1, 2, 2, 3}));
        C_KEYS.add("04"); C_SET.put("04", new Module(new int[] {1, 2, 1, 3, 2, 2}));
        C_KEYS.add("05"); C_SET.put("05", new Module(new int[] {1, 3, 1, 2, 2, 2}));
        C_KEYS.add("06"); C_SET.put("06", new Module(new int[] {1, 2, 2, 2, 1, 3}));
        C_KEYS.add("07"); C_SET.put("07", new Module(new int[] {1, 2, 2, 3, 1, 2}));
        C_KEYS.add("08"); C_SET.put("08", new Module(new int[] {1, 3, 2, 2, 1, 2}));
        C_KEYS.add("09"); C_SET.put("09", new Module(new int[] {2, 2, 1, 2, 1, 3}));
        C_KEYS.add("10"); C_SET.put("10", new Module(new int[] {2, 2, 1, 3, 1, 2}));
        C_KEYS.add("11"); C_SET.put("11", new Module(new int[] {2, 3, 1, 2, 1, 2}));
        C_KEYS.add("12"); C_SET.put("12", new Module(new int[] {1, 1, 2, 2, 3, 2}));
        C_KEYS.add("13"); C_SET.put("13", new Module(new int[] {1, 2, 2, 1, 3, 2}));
        C_KEYS.add("14"); C_SET.put("14", new Module(new int[] {1, 2, 2, 2, 3, 1}));
        C_KEYS.add("15"); C_SET.put("15", new Module(new int[] {1, 1, 3, 2, 2, 2}));
        C_KEYS.add("16"); C_SET.put("16", new Module(new int[] {1, 2, 3, 1, 2, 2}));
        C_KEYS.add("17"); C_SET.put("17", new Module(new int[] {1, 2, 3, 2, 2, 1}));
        C_KEYS.add("18"); C_SET.put("18", new Module(new int[] {2, 2, 3, 2, 1, 1}));
        C_KEYS.add("19"); C_SET.put("19", new Module(new int[] {2, 2, 1, 1, 3, 2}));
        C_KEYS.add("20"); C_SET.put("20", new Module(new int[] {2, 2, 1, 2, 3, 1}));
        C_KEYS.add("21"); C_SET.put("21", new Module(new int[] {2, 1, 3, 2, 1, 2}));
        C_KEYS.add("22"); C_SET.put("22", new Module(new int[] {2, 2, 3, 1, 1, 2}));
        C_KEYS.add("23"); C_SET.put("23", new Module(new int[] {3, 1, 2, 1, 3, 1}));
        C_KEYS.add("24"); C_SET.put("24", new Module(new int[] {3, 1, 1, 2, 2, 2}));
        C_KEYS.add("25"); C_SET.put("25", new Module(new int[] {3, 2, 1, 1, 2, 2}));
        C_KEYS.add("26"); C_SET.put("26", new Module(new int[] {3, 2, 1, 2, 2, 1}));
        C_KEYS.add("27"); C_SET.put("27", new Module(new int[] {3, 1, 2, 2, 1, 2}));
        C_KEYS.add("28"); C_SET.put("28", new Module(new int[] {3, 2, 2, 1, 1, 2}));
        C_KEYS.add("29"); C_SET.put("29", new Module(new int[] {3, 2, 2, 2, 1, 1}));
        C_KEYS.add("30"); C_SET.put("30", new Module(new int[] {2, 1, 2, 1, 2, 3}));
        C_KEYS.add("31"); C_SET.put("31", new Module(new int[] {2, 1, 2, 3, 2, 1}));
        C_KEYS.add("32"); C_SET.put("32", new Module(new int[] {2, 3, 2, 1, 2, 1}));
        C_KEYS.add("33"); C_SET.put("33", new Module(new int[] {1, 1, 1, 3, 2, 3}));
        C_KEYS.add("34"); C_SET.put("34", new Module(new int[] {1, 3, 1, 1, 2, 3}));
        C_KEYS.add("35"); C_SET.put("35", new Module(new int[] {1, 3, 1, 3, 2, 1}));
        C_KEYS.add("36"); C_SET.put("36", new Module(new int[] {1, 1, 2, 3, 1, 3}));
        C_KEYS.add("37"); C_SET.put("37", new Module(new int[] {1, 3, 2, 1, 1, 3}));
        C_KEYS.add("38"); C_SET.put("38", new Module(new int[] {1, 3, 2, 3, 1, 1}));
        C_KEYS.add("39"); C_SET.put("39", new Module(new int[] {2, 1, 1, 3, 1, 3}));
        C_KEYS.add("40"); C_SET.put("40", new Module(new int[] {2, 3, 1, 1, 1, 3}));
        C_KEYS.add("41"); C_SET.put("41", new Module(new int[] {2, 3, 1, 3, 1, 1}));
        C_KEYS.add("42"); C_SET.put("42", new Module(new int[] {1, 1, 2, 1, 3, 3}));
        C_KEYS.add("43"); C_SET.put("43", new Module(new int[] {1, 1, 2, 3, 3, 1}));
        C_KEYS.add("44"); C_SET.put("44", new Module(new int[] {1, 3, 2, 1, 3, 1}));
        C_KEYS.add("45"); C_SET.put("45", new Module(new int[] {1, 1, 3, 1, 2, 3}));
        C_KEYS.add("46"); C_SET.put("46", new Module(new int[] {1, 1, 3, 3, 2, 1}));
        C_KEYS.add("47"); C_SET.put("47", new Module(new int[] {1, 3, 3, 1, 2, 1}));
        C_KEYS.add("48"); C_SET.put("48", new Module(new int[] {3, 1, 3, 1, 2, 1}));
        C_KEYS.add("49"); C_SET.put("49", new Module(new int[] {2, 1, 1, 3, 3, 1}));
        C_KEYS.add("50"); C_SET.put("50", new Module(new int[] {2, 3, 1, 1, 3, 1}));
        C_KEYS.add("51"); C_SET.put("51", new Module(new int[] {2, 1, 3, 1, 1, 3}));
        C_KEYS.add("52"); C_SET.put("52", new Module(new int[] {2, 1, 3, 3, 1, 1}));
        C_KEYS.add("53"); C_SET.put("53", new Module(new int[] {2, 1, 3, 1, 3, 1}));
        C_KEYS.add("54"); C_SET.put("54", new Module(new int[] {3, 1, 1, 1, 2, 3}));
        C_KEYS.add("55"); C_SET.put("55", new Module(new int[] {3, 1, 1, 3, 2, 1}));
        C_KEYS.add("56"); C_SET.put("56", new Module(new int[] {3, 3, 1, 1, 2, 1}));
        C_KEYS.add("57"); C_SET.put("57", new Module(new int[] {3, 1, 2, 1, 1, 3}));
        C_KEYS.add("58"); C_SET.put("58", new Module(new int[] {3, 1, 2, 3, 1, 1}));
        C_KEYS.add("59"); C_SET.put("59", new Module(new int[] {3, 3, 2, 1, 1, 1}));
        C_KEYS.add("60"); C_SET.put("60", new Module(new int[] {3, 1, 4, 1, 1, 1}));
        C_KEYS.add("61"); C_SET.put("61", new Module(new int[] {2, 2, 1, 4, 1, 1}));
        C_KEYS.add("62"); C_SET.put("62", new Module(new int[] {4, 3, 1, 1, 1, 1}));
        C_KEYS.add("63"); C_SET.put("63", new Module(new int[] {1, 1, 1, 2, 2, 4}));
        C_KEYS.add("64"); C_SET.put("64", new Module(new int[] {1, 1, 1, 4, 2, 2}));
        C_KEYS.add("65"); C_SET.put("65", new Module(new int[] {1, 2, 1, 1, 2, 4}));
        C_KEYS.add("66"); C_SET.put("66", new Module(new int[] {1, 2, 1, 4, 2, 1}));
        C_KEYS.add("67"); C_SET.put("67", new Module(new int[] {1, 4, 1, 1, 2, 2}));
        C_KEYS.add("68"); C_SET.put("68", new Module(new int[] {1, 4, 1, 2, 2, 1}));
        C_KEYS.add("69"); C_SET.put("69", new Module(new int[] {1, 1, 2, 2, 1, 4}));
        C_KEYS.add("70"); C_SET.put("70", new Module(new int[] {1, 1, 2, 4, 1, 2}));
        C_KEYS.add("71"); C_SET.put("71", new Module(new int[] {1, 2, 2, 1, 1, 4}));
        C_KEYS.add("72"); C_SET.put("72", new Module(new int[] {1, 2, 2, 4, 1, 1}));
        C_KEYS.add("73"); C_SET.put("73", new Module(new int[] {1, 4, 2, 1, 1, 2}));
        C_KEYS.add("74"); C_SET.put("74", new Module(new int[] {1, 4, 2, 2, 1, 1}));
        C_KEYS.add("75"); C_SET.put("75", new Module(new int[] {2, 4, 1, 2, 1, 1}));
        C_KEYS.add("76"); C_SET.put("76", new Module(new int[] {2, 2, 1, 1, 1, 4}));
        C_KEYS.add("77"); C_SET.put("77", new Module(new int[] {4, 1, 3, 1, 1, 1}));
        C_KEYS.add("78"); C_SET.put("78", new Module(new int[] {2, 4, 1, 1, 1, 2}));
        C_KEYS.add("79"); C_SET.put("79", new Module(new int[] {1, 3, 4, 1, 1, 1}));
        C_KEYS.add("80"); C_SET.put("80", new Module(new int[] {1, 1, 1, 2, 4, 2}));
        C_KEYS.add("81"); C_SET.put("81", new Module(new int[] {1, 2, 1, 1, 4, 2}));
        C_KEYS.add("82"); C_SET.put("82", new Module(new int[] {1, 2, 1, 2, 4, 1}));
        C_KEYS.add("83"); C_SET.put("83", new Module(new int[] {1, 1, 4, 2, 1, 2}));
        C_KEYS.add("84"); C_SET.put("84", new Module(new int[] {1, 2, 4, 1, 1, 2}));
        C_KEYS.add("85"); C_SET.put("85", new Module(new int[] {1, 2, 4, 2, 1, 1}));
        C_KEYS.add("86"); C_SET.put("86", new Module(new int[] {4, 1, 1, 2, 1, 2}));
        C_KEYS.add("87"); C_SET.put("87", new Module(new int[] {4, 2, 1, 1, 1, 2}));
        C_KEYS.add("88"); C_SET.put("88", new Module(new int[] {4, 2, 1, 2, 1, 1}));
        C_KEYS.add("89"); C_SET.put("89", new Module(new int[] {2, 1, 2, 1, 4, 1}));
        C_KEYS.add("90"); C_SET.put("90", new Module(new int[] {2, 1, 4, 1, 2, 1}));
        C_KEYS.add("91"); C_SET.put("91", new Module(new int[] {4, 1, 2, 1, 2, 1}));
        C_KEYS.add("92"); C_SET.put("92", new Module(new int[] {1, 1, 1, 1, 4, 3}));
        C_KEYS.add("93"); C_SET.put("93", new Module(new int[] {1, 1, 1, 3, 4, 1}));
        C_KEYS.add("94"); C_SET.put("94", new Module(new int[] {1, 3, 1, 1, 4, 1}));
        C_KEYS.add("95"); C_SET.put("95", new Module(new int[] {1, 1, 4, 1, 1, 3}));
        C_KEYS.add("96"); C_SET.put("96", new Module(new int[] {1, 1, 4, 3, 1, 1}));
        C_KEYS.add("97"); C_SET.put("97", new Module(new int[] {4, 1, 1, 1, 1, 3}));
        C_KEYS.add("98"); C_SET.put("98", new Module(new int[] {4, 1, 1, 3, 1, 1}));
        C_KEYS.add("99"); C_SET.put("99", new Module(new int[] {1, 1, 3, 1, 4, 1}));
        C_KEYS.add("\310");
        C_SET.put("\310", new CodeChangeModule(new int[] {1, 1, 4, 1, 3, 1}, Code128Barcode.B)); // CODE B
        C_KEYS.add("\311");
        C_SET.put("\311", new CodeChangeModule(new int[] {3, 1, 1, 1, 4, 1}, Code128Barcode.A)); // CODE A
        C_KEYS.add("\312");
        C_SET.put("\312", new Module(new int[] {4, 1, 1, 1, 3, 1}));
    }
    
    /**
     * Returns the module that represents the specified character and character set.
     * @param key The data character to get the encoding module for
     * @param mode The character set mode that is currently in use (A, B or C)
     * @return The module that encodes the given char in the given character set
     */
    public static Module getModule(String key, int mode) {
        Module module = null;
        switch (mode) {
            case Code128Barcode.A:
                module = (Module) A_SET.get(key);
                break;
            case Code128Barcode.B:
                module = (Module) B_SET.get(key);
                break;
            case Code128Barcode.C:
                module = (Module) C_SET.get(key);
                break;
        }
        if (module != null) {
            module.setSymbol(key);
        }
        return module;
    }
    
    /**
     * Returns the index of the given character in the encoding tables. This is used
     * when calculating the checksum.
     * @param key The data character sequence to get the index for
     * @param mode The character set mode that is currently in use (A, B or C)
     * @return The index for the given key
     */
    public static int getIndex(String key, int mode) {
        List keys = getKeys(mode);
        return keys.indexOf(key);
    }
    
    /**
     * Returns the encoded module at the given index position. This is used to
     * get the encoded checksum character.
     * @param index The index to get the module for
     * @param mode The character set mode that is currently in use (A, B or C)
     * @return The module at the specified index
     */
    public static Module getModuleForIndex(int index, int mode) {
        List keys = getKeys(mode);
        return getModule((String) keys.get(index), mode);
    }
    
    private static List getKeys(int mode) {
        switch (mode) {
            case Code128Barcode.A:
                return A_KEYS;
            case Code128Barcode.C:
                return C_KEYS;
        }
        return B_KEYS;
    }
}
