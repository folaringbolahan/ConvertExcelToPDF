/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.thread;

/**
 *
 * @author emmanuel.idoko
 */
public class VolatileData {
    private volatile boolean shutdownThreads = false;

    public boolean isShutdownThreads() {
        return shutdownThreads;
    }

    public void setShutdownThreads(boolean shutdownThreads) {
        this.shutdownThreads = shutdownThreads;
    }
}
