/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import events.ModelEvent;
import events.ModelListner;
import game.creatures.Creature;
import game.creatures.MediumCreature;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import view.Main;

/**
 * Игровая модель
 * @author 7
 */
public class Model {
    private Player _player1 = null;
    private Player _player2 = null;
    private Player _computer = null;
    private Field _field = null;
    private Player _winner = null;
    private Player[] _players = new Player[3];
    
    /**
     * размер поля
     */
    public final int FIELDSIZE = 24;

    /**
     * сколько существ можно добавить за ход
     */
    public final int CANADDPERROUND = 15;

    /**
     * сколько существ можно удалить за ход
     */
    public final int CANDELPERROUND = 15;

    /**
     * Количество шагов в жизненном цикле
     */
    public final int LIVINGSTEPS = 15;
    
    private final List<ModelListner> _listeners = new ArrayList<>();
    
    //------------------------------------------------------------
    
    /**
     * коструктор
     */   
    public Model() {
        _field = new Field(FIELDSIZE);
        _player1 = new Player(
                new Position(0, FIELDSIZE/4), 
                new Position(FIELDSIZE/2-1, FIELDSIZE-(FIELDSIZE/4)-1),
                "Player 1",
                0, CANADDPERROUND, 0
        );
        _player2 = new Player(
                new Position(FIELDSIZE/2, FIELDSIZE/4), 
                new Position(FIELDSIZE-1, FIELDSIZE-(FIELDSIZE/4)-1),
                "Player 2",
                0, CANADDPERROUND, 0
        );
        _computer = new Player(
                new Position(0, FIELDSIZE-(FIELDSIZE/4)), 
                new Position(FIELDSIZE-1, (FIELDSIZE/4)-1),
                "Meutral Player",
                0, CANADDPERROUND, 0
        );
        _players[0] = _player1;
        _players[1] = _player2;
        _players[2] = _computer;
    }
    
    /**
     * добавляет слушателя
     * @param l
     */
    public void addListner(ModelListner l) {
        _listeners.add(l);
    }
    
    /**
     * запускает новую игру
     */
    public void newGame() {
        _winner = null;
        shuffleArray(_players);
        for (int i=0; i<_players.length; ++i) {
            _players[i].setCreaturesCount(0);
            _players[i].setCanAdd(CANADDPERROUND);
            _players[i].setCanDelete(0);
        }
        _field.clear();
        generateCreatures();
        fireFieldStateChanged(_field.getAll());
        fireMessage("New game started!");
    }
    
    /**
     * завершена ли игра
     * @return завершена ли игра
     */
    public boolean isGameEnded() {
        return getWinner()!=null;
    }
    
    /**
     * действие пользователя для добавления существа
     * @param pos позиция
     * @param pl игрок
     */
    public void addCreatureByUser(Position pos, Player pl) {
        Creature c = makeNewCreature(pl);
        if (pl.getCanAdd()>0 && _field.addCreature(c, pos)) {
            pl.incCreatures();
            pl.decCanAdd();
            fireFieldStateChanged(_field.getAll());
        } else {
            Main.debug("cannot add creature in "+pos+" for "+pl);
        }  
    }
    
    /**
     * действие пользователя для удаления существа
     * @param pos позиция
     */
    public void deleteCreatureByUser(Position pos) {
        Player tp = ownerOfPosition(pos);
        Player ttp = ownerOfCreature(pos);
        if (!(tp.equals(ttp) && tp.getCanDelete()>0)) {
            return;
        }
        Creature c = _field.deleteCreature(pos);
        if (c!=null) {
            c.owner().decCreatures();
            c.owner().decCanDelete();
            fireFieldStateChanged(_field.getAll());
        } else {
            Main.debug("cannot detele creature in "+pos);
        }
    }
    
    /**
     * определяет владелеца клетки
     * @param pos позиция клетки
     * @return владелец клетки
     */
    public Player ownerOfPosition(Position pos) {
        if (pos.getY()<FIELDSIZE/4 || pos.getY()>=FIELDSIZE-(FIELDSIZE/4)) {
            return _computer;
        } else if (pos.getX()<FIELDSIZE/2) {
            return _player1;
        } else if (pos.getX()>=FIELDSIZE/2){
            return _player2;
        }
        return null;
    }
    
    /**
     * определяет владельца существа в заданной позиции
     * @param pos позиция
     * @return владелец существа
     */
    public Player ownerOfCreature(Position pos) {
        Creature c = _field.creature(pos);
        return ownerOfCreature(c);
    }
    
    /**
     * определяет владелеца существа
     * @param c существо
     * @return владелец
     */
    public Player ownerOfCreature(Creature c) {
        return c==null ? null : c.owner();
    }
    
    /**
     * получает существо в заданной позиции
     * @param pos позиция
     * @return существо в заданной позиции или null
     */
    public Creature getCreature(Position pos) {
        return _field.creature(pos);
    }
    
    /**
     * обрабатывает одно поколение
     */
    public void runStep() {
        if (isGameEnded()) {
            return;
        }
        Creature[] cts = new Creature[3];
        for (int k=0; k<3; ++k) {
            cts[k] = makeNewCreature(_players[k]);
        }
        
        // burning...
        for (int i=0; i<FIELDSIZE; ++i) {
            for (int j=0; j<FIELDSIZE; ++j) {
                for (int k=0; k<_players.length; ++k) {
                    Position tpos = new Position(i, j);
                    if (_field.creature(tpos)==null && cts[k].burnHere(tpos)) {
                        cts[k] = makeNewCreature(_players[k]);
                    }
                }
            }
        }
        
        //dying...
        Creature ct;
        for (int i=0; i<FIELDSIZE; ++i) {
            for (int j=0; j<FIELDSIZE; ++j) {
                ct = _field.creature(new Position(i, j));
                if (ct != null && ct.isDiying()) {
                    ct.die();
                }
            }
        }
        
        Main.debug(_field.toString());
    }

    /**
     * обрабатывает цикл поколений, проверяет, есть ли победитель
     */
    public void runCircle() {
        if (isGameEnded()) {
            return;
        }
        List<List<Creature>> to_view = new ArrayList<List<Creature>>();
        List<Creature> inner_list;
        for (int k = 0; k < LIVINGSTEPS && _winner==null; k++) {
            runStep();
            inner_list = _field.getAll();
            to_view.add(inner_list);
            _winner = findWinner();
        }
        int canadd = CANADDPERROUND;
        int candel = CANDELPERROUND;
        fireLifeCircleEnded(to_view);
        if (_winner!=null) {
            canadd = 0;
            candel = 0;
            fireGameEnded(_winner);
        }
        for (int i = 0; i < _players.length; i++) {
            _players[i].setCanAdd(canadd);
            _players[i].setCanDelete(candel);
        }
    }
    
    /**
     * возвращает победителя
     * @return победитель или null
     */
    public Player getWinner() {
        return _winner;
    }
        
    /**
     * вычисляет победителя на текущем состоянии игры
     * @return победитель (если есть) или null
     */
    public Player findWinner() {
        int players_with_creatures = 0;
        Player potential_winner = null;
        for (int i=0; i<_players.length; ++i) {
            if (_players[i].getCreaturesCount()>0) {
                ++players_with_creatures;
                potential_winner = _players[i];
            }
        }
        if (players_with_creatures==1) {
            return potential_winner;
        }
        if (players_with_creatures==0) {
            return getComputerPlayer();
        }
        return null;
    }
    
    /**
     * вызывает событие lifeCircleEnded
     * @param cl массив состояний полотна
     */
    public void fireLifeCircleEnded(List<List<Creature>> cl) {
        for(ModelListner l : _listeners) {
            l.lifeCircleEnded(cl);
        }
    }
    
    /**
     * вызывает событие gameEnded
     * @param winner победитель
     */
    public void fireGameEnded(Player winner) {
        ModelEvent e = new ModelEvent(this, winner, winner.getName()+" won!");
        for(ModelListner l : _listeners) {
            l.gameEnded(e);
        }
    }
    
    /**
     * вызывает событие messageRecieved
     * @param s сообщение
     */
    public void fireMessage(String s) {
        ModelEvent e = new ModelEvent(this, null, s);
        for(ModelListner l : _listeners) {
            l.messageRecieved(e);
        }
    }
    
    /**
     * вызывает событие fieldStateChanged
     * @param cl состояние игрового поля
     */
    public void fireFieldStateChanged(List<Creature> cl) {
        for(ModelListner l : _listeners) {
            l.fieldStateChanged(cl);
        }
    }
       
    /**
     * возвращает массив существ для заданного игрока
     * @param p игрок 
     * @return массив существ для заданного игрока
     */
    public List<Creature> getAllCreatures(Player p) {
        List<Creature> result = new ArrayList<>();
        List<Creature> all = _field.getAll();
        for (Creature c : all) {
            if (c.owner().equals(p)) {
                result.add(c);
            }
        }
        return result;
    }
    
    /**
     * генерирует случайно существ для компьютерного игрока
     */
    public void generateCreatures() {
        Player pl = getComputerPlayer();
        Random r = new Random();

        int row, col, i=0, cc = CANADDPERROUND*2;
        while (i < cc) {
            col = r.nextInt(FIELDSIZE);
            row = r.nextInt(FIELDSIZE/2);
            if (row>=FIELDSIZE/4) {
                row += FIELDSIZE/2;
            }
            if(_field.addCreature(makeNewCreature(pl), new Position(col, row))) {
                ++i;
            }
        }
        pl.setCreaturesCount(cc);
    }

    /**
     * @return первый игрок
     */
    public Player getPlayer1() {
        return _player1;
    }

    /**
     * @return tвторой игрок
     */
    public Player getPlayer2() {
        return _player2;
    }

    /**
     * @return компьютерный игрок
     */
    public Player getComputerPlayer() {
        return _computer;
    }
    
    /**
     * фабрика существ (для лёгкой подмены типа существ)
     * @param pl игрок
     * @return новое сущетсво для заданного игрока
     */
    public Creature makeNewCreature(Player pl) {
        return new MediumCreature(_field, pl);
    }
    
    /**
     * перемешивает массив существ (для случайного приоритета рождаемости)
     */
    private void shuffleArray(Player[] ar)
    {
      Random rnd = new Random();
      for (int i = ar.length - 1; i > 0; i--)
      {
        int index = rnd.nextInt(i + 1);
        // Simple swap
        Player a = ar[index];
        ar[index] = ar[i];
        ar[i] = a;
      }
    }
}
