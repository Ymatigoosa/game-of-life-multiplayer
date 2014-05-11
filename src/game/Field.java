/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import game.creatures.Creature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import view.Main;

/**
 * Игровое поле
 * @author 7
 */
public class Field {
    private int _size;
    private HashMap<Integer, HashMap<Integer, Creature>> _field;
    
    //------------------------------------------------------------
    
    /**
     * конструктор
     * @param size размер поля
     */
        
    public Field(int size) {
        _size = size;
        _field = new HashMap<Integer, HashMap<Integer, Creature>>(_size);
    }
    
    /**
     * Добавить существо
     * @param c существо
     * @param p позиция
     * @return успешность добавления
     */
    public boolean addCreature(Creature c, Position p) {
        if (!_field.containsKey(p.getX())) {
            _field.put(p.getX(), new HashMap<Integer, Creature>(_size));
        }
        if (_field.get(p.getX()).containsKey(p.getY())) {
            Main.debug("creature replased at "+p);
            return false;
        }
        _field.get(p.getX()).put(p.getY(), c);
        c.setPosition(p);
        return true;
    }
    
    /**
     * удаляет существо
     * @param p позиция
     * @return успешность удаления
     */
    public Creature deleteCreature(Position p) {
        if (!_field.containsKey(p.getX())) {
            return null;
        }
        /*if (!_field.get(p.getX()).containsKey(p.getY())) {
            return null;
        }*/
        return _field.get(p.getX()).remove(p.getY());
    }
    
    /**
     * удаляет существо
     * @param c существо
     * @return успешность удаления
     */
    public boolean deleteCreature(Creature c) {
        return deleteCreature(c.getPosition())!=null;
    }
    
    /**
     * получить существо в заданной позиции
     * @param p позиция
     * @return существо в данной позиции или null
     */
    public Creature creature(Position p) {
        if (_field.containsKey(p.getX())) {
            return _field.get(p.getX()).get(p.getY());
        }
        return null;
    }
    
    /**
     * считает количество "союзников" для заданной клетки и игрока
     * @param p позиция клетки
     * @param pl игрок
     * @return количество "союзников" для заданной клетки и игрока
     */
    public int creaturesAround(Position p, Player pl) {
        int toprow =    p.getY()==0         ? _size-1   : p.getY()-1;
        int bottomrow = p.getY()==_size-1   ? 0         : p.getY()+1;
        int leftcol =   p.getX()==0         ? _size-1   : p.getX()-1;
        int rightcol =  p.getX()==_size-1   ? 0         : p.getX()+1;
        List<Creature> t_creature = new ArrayList<>();
        t_creature.add(creature(new Position(leftcol, toprow)));
        t_creature.add(creature(new Position(p.getX(), toprow)));
        t_creature.add(creature(new Position(rightcol, toprow)));
        t_creature.add(creature(new Position(rightcol, p.getY())));
        t_creature.add(creature(new Position(rightcol, bottomrow)));
        t_creature.add(creature(new Position(p.getX(), bottomrow)));
        t_creature.add(creature(new Position(leftcol, bottomrow)));
        t_creature.add(creature(new Position(leftcol, p.getY())));
        int counter = 0;
        for (Creature c : t_creature) {
            if (c!=null && c.owner().equals(pl)) {
                ++counter;
            }
        }
        return counter;
    }
    
    /**
     * очищает игровое поле
     */
    public void clear() {
        if (_field==null || !_field.isEmpty()) {
            _field = new HashMap<Integer, HashMap<Integer, Creature>>(_size);
        }
    }
    
    /**
     * получить массив копий всех существ
     * @return массив копий всех существ
     */
    public List<Creature> getAll() {
        List<Creature> list = new ArrayList<>();
        Creature ct;
        for (int i=0; i<_size; ++i) {
            for (int j=0; j<_size; ++j) {
                ct = this.creature(new Position(i, j));
                if (ct != null) {
                    list.add(ct.deepCopy());
                }
            }
        }
        return list;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Creature ct;
        for (int i=0; i<_size; ++i) {
            for (int j=0; j<_size; ++j) {
                ct = this.creature(new Position(j, i));
                if (ct != null) {
                    sb.append(String.valueOf(ct.owner().id()));
                } else {
                    sb.append(".");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
