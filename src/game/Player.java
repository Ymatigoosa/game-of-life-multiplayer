/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.util.concurrent.atomic.AtomicInteger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import view.Main;

/**
 * игрок
 * @author 7
 */
public class Player {
    private final IntegerProperty _creaturesCount = new SimpleIntegerProperty();
    private final IntegerProperty _canAdd = new SimpleIntegerProperty();
    private final IntegerProperty _canDelete = new SimpleIntegerProperty();
    private final StringProperty _name = new SimpleStringProperty();
    private Position _lefttop;
    private Position _rightbottom;
    
    private static final AtomicInteger UNIQUEID=new AtomicInteger();
    private final int _id;

    /**
     * конструктор
     * @param lefttop граница области игрока
     * @param rightbottom граница области игрока
     * @param name название игрока 
     * @param creaturesCount колво существ
     * @param canAdd сколько можно добавить
     * @param canDelete сколько можно удалить
     */
    public Player(Position lefttop, Position rightbottom, String name,
            int creaturesCount, int canAdd, int canDelete) {
        _lefttop = lefttop;
        _rightbottom = rightbottom;
        setName(name);
        setCreaturesCount(creaturesCount);
        setCanAdd(canAdd);
        setCanDelete(canDelete);
        Main.debug(name+" : "+lefttop+" "+rightbottom);
        _id = UNIQUEID.getAndIncrement();
    }
    
    /**
     * идентифокатор
     * @return идентифокатор
     */
    public int id() {
        return _id;
    }
    
    /**
     * @return tлекая верхняя граница
     */
    public Position getLefttop() {
        return _lefttop;
    }

    /**
     * @return правая нижняя граница
     */
    public Position getRightbottom() {
        return _rightbottom;
    }
    
    /**
     * @return колво существ
     */
    public int getCreaturesCount() {
        return _creaturesCount.get();
    }

    /**
     * @param creaturesCount колво существ
     */
    public void setCreaturesCount(int creaturesCount) {
        this._creaturesCount.set(creaturesCount);
    }

    /**
     * @return сколько можно добавить
     */
    public int getCanAdd() {
        return _canAdd.get();
    }
    
    /**
     * @param canAdd сколько можно добавить
     */
    public void setCanAdd(int canAdd) {
        this._canAdd.set(canAdd);
    }

    /**
     * @return сколько можно удалить
     */
    public int getCanDelete() {
        return _canDelete.get();
    }

    /**
     * @param canDelete сколько можно удалить
     */
    public void setCanDelete(int canDelete) {
        this._canDelete.set(canDelete);
    }

    /**
     * @return имя
     */
    public String getName() {
        return _name.get();
    }

    /**
     * @param name имя
     */
    public void setName(String name) {
        this._name.set(name);
    }

    public void decCanDelete() {
        this._canDelete.set( this._canDelete.get()-1);
    }   
    public void incCanDelete() {
        this._canDelete.set( this._canDelete.get()+1);
    }
    public void incCreatures() {
        this._creaturesCount.set( this._creaturesCount.get()+1);
    }  
    public void decCreatures() {
        this._creaturesCount.set( this._creaturesCount.get()-1);
    }
    public void decCanAdd() {
        this._canAdd.set( this._canAdd.get()-1);
    } 
    public void incCanAdd() {
        this._canAdd.set( this._canAdd.get()+1);
    }
    
    public ReadOnlyIntegerProperty creaturesCountProperty() { 
        return this._creaturesCount;
    }
    public ReadOnlyIntegerProperty canAddProperty() { 
        return this._canAdd;
    }
    public ReadOnlyIntegerProperty canDeleteProperty() { 
        return this._canDelete;
    }
    public ReadOnlyStringProperty nameProperty() { 
        return this._name;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Player) {
            Player pl = (Player)o;
            return this.id() == pl.id();
        }
        
        return false;
    }
}
