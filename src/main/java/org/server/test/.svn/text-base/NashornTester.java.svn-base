/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Administrator
 */
public class NashornTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        // TODO code application logic here
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        engine.eval(new FileReader("BufferTester.java"));
    }

}
