/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.creatures;

import game.Field;
import game.Player;
import game.Position;

/**
 * Существо
 * @author Пахомов Дмитрий
 */
abstract public class Creature {
    private Field _field;
    private Position _position;
    private Player _owner;
    
    /**
     * констроктор
     * @param field поле
     * @param owner игрок-владелец
     */
    public Creature(Field field, Player owner) {
        setField(field);
        setOwner(owner);
    }
    
    /**
     * констроктор
     * @param field поле
     * @param position игрок-владелец
     * @param owner позиция
     */
    public Creature(Field field, Position position, Player owner) {
        setField(field);
        setPosition(position);
        setOwner(owner);
    }
    
    /**
     * @return игровое поле
     */
    public Field field() {
        return _field;
    }

    /**
     * @param field игровое поле
     */
    public void setField(Field field) {
        this._field = field;
    }

    /**
     * @return позиция (копия)
     */
    public Position getPosition() {
        return _position==null ? null : new Position(_position.getX(), _position.getY());
    }

    /**
     * @param _position позиция
     */
    public void setPosition(Position position) {
        this._position = position==null ? null : new Position(position.getX(), position.getY());
    }

    /**
     * @return владелец
     */
    public Player owner() {
        return _owner;
    }

    /**
     * @param owner владелец
     */
    public void setOwner(Player owner) {
        this._owner = owner;
    }
    
    /**
     * проверяет умирает ли существо в этом поколении
     * @return умирает ли существо в этом поколении
     */
    public boolean isDiying() {
        int[] creatures_to_live = creaturesAroundForSurvive();
        int creaturesAround = field().creaturesAround(getPosition(), owner());
        for (int i=0; i<creatures_to_live.length; ++i) {
            if (creaturesAround == creatures_to_live[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * убивает существо и вносит соотв изменения в модель
     */
    public void die() {
        field().deleteCreature(this);
        owner().decCreatures();
        setOwner(null);
        setPosition(null);
    }
    
    /**
     * сколько существ нужно для выживания существа
     * @return сколько существ нужно для выживания существа
     */
    abstract protected int[] creaturesAroundForSurvive();
    
    /**
     * сколько существ нужно для рождения нового существа в пустой клетке
     * @return
     */
    abstract protected int creaturesNeedToBurn();
    
    /**
     * название типа существа
     * @return
     */
    abstract protected String typeName();
    
    /**
     * копия существа
     * @return новое существо
     */
    abstract public Creature deepCopy();
    
    /**
     * ставит свободное существо в заданную клетку
     * @param p позиция клетки
     * @return успешность операции
     */
    public boolean burnHere(Position p) {
        if (field().creaturesAround(p, owner())==creaturesNeedToBurn()) {
            field().addCreature(this, p);
            owner().incCreatures();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "["+typeName()+"] pos: "+getPosition()+" owner: "+owner();
    }
}
