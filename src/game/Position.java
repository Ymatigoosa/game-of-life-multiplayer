/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

/**
 * позиция
 * @author Пахомов Дмитрий
 */
public class Position {
    private int _x;
    private int _y;
    
    public Position(int _x, int _y) {
        this._x = _x;
        this._y = _y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return _x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this._x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return _y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this._y = y;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Position) {
            Position p = (Position)o;
            return this._x == p.getX() && this._y == p.getY();
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "{x:"+String.valueOf(_x)+",y:"+String.valueOf(_y)+"}";
    }
}
