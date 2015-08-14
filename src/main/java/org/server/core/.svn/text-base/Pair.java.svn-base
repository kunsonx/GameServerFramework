/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core;

import java.util.Objects;

public class Pair<F, S> {

    F o1;
    S o2;

    public Pair(F o1, S o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public static boolean same(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public F getFirst() {
        return o1;
    }

    public S getSecond() {
        return o2;
    }

    public void setFirst(F o) {
        o1 = o;
    }

    public void setSecond(S o) {
        o2 = o;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair p = (Pair) obj;
        return same(p.o1, this.o1) && same(p.o2, this.o2);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.o1);
        hash = 59 * hash + Objects.hashCode(this.o2);
        return hash;
    }

    @Override
    public String toString() {
        return "Pair{" + o1 + ", " + o2 + "}";
    }
}
