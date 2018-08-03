package mavisBeacon.pacMan;

import android.graphics.Point;
/*
 * AI.java
 *
 * Created on May 18, 2008, 12:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Ilan Elkobi
 */
public class AI extends Point{
    int dXA=10;
    int dYA=0;
    int dontMove=0;
    /** Creates a new instance of AI */
    public AI() {
    dXA=10;
    dYA=0;
    }
    public AI(Point a,int dXA,int dYA){
        this.dXA=dXA;
        this.dYA=dYA;
    }
    public AI(AI a){
        this.dXA=a.dXA;
        this.dYA=a.dYA;
    }
    public AI(int x, int y){
        this.x=x;
        this.y=y;
    }
    
}
