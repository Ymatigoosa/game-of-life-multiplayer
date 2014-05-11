/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import game.creatures.Creature;
import java.util.EventListener;
import java.util.List;

/**
 * Слушатель событий игровой модели
 * @author 7
 */
public interface ModelListner extends EventListener {

    /**
     * игра закончена
     * @param e параметры события
     */
    void gameEnded(ModelEvent e);

    /**
     * принято сообщение
     * @param e параметры события (с сообщением)
     */
    void messageRecieved(ModelEvent e);

    /**
     * закончен жизненный цикл
     * @param cl массив состояний игрового поля
     */
    void lifeCircleEnded(List<List<Creature>> cl);

    /**
     * изменилось поле
     * @param cl сосояние игрового поля
     */
    void fieldStateChanged(List<Creature> cl);
}
