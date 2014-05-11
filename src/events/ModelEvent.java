/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import game.Player;
import java.util.EventObject;

/**
 * Параметры события игровой модели
 * @author 7
 */
public class ModelEvent extends EventObject {
    
    private Player _winner; 
    private String _message;
    
    /**
     * конструктор события
     * @param o отправитель
     * @param winner победитель
     * @param message сообщение
     */
    public ModelEvent(Object o, Player winner, String message) {
        super(o);
        _winner = winner;
        _message = message;
    }

    /**
     * @return побетель или null
     */
    public Player winner() {
        return _winner;
    }

    /**
     * @return сообщение события
     */
    public String message() {
        return _message;
    }
    
}
