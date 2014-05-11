/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import events.ModelEvent;
import events.ModelListner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import game.Model;
import game.Player;
import game.Position;
import game.creatures.Creature;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.util.Duration;

/**
 * Предствление + точка входа
 * @author Пахомов Дмитрий
 */
public class Main extends Application implements ModelListner {
    
    private final int CELL_SIZE = 16;
    private final int BORDER_SIZE = 1;
    private final Color BORDER_COLOR = Color.gray(0.4);
    private final Color COMP_COLOR = Color.LIGHTGRAY;
    private final Color P1_COLOR = Color.GOLD;
    private final Color P2_COLOR = Color.LIGHTSALMON;
    private final int FIELD_SIZE;
    private final int PLAYER_1 = 0x1;
    private final int PLAYER_2 = 0x2;
    public static final boolean DEBUG_MODE = false;
    private boolean _controls_disabled = false;
    private final int ANIMATION_TIME = 500;
    
    private final HBox _top_menu = new HBox();
    private final HBox _middle = new HBox();
    private final HBox _bottom = new HBox();
    
    private final Model _model;
    
    private EventHandler<Event> _event_blocker = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            event.consume();
        }
    };
    
    private SequentialTransition _seqT = null;
    public Main() {
        this._model = new Model();
        _model.addListner(this);
        _model.newGame();
        this.FIELD_SIZE = _model.FIELDSIZE;
    }
    
    @Override
    public void start(Stage primaryStage) {
        initInterface(primaryStage);
    }
    
    private Model model() {
        return _model;
    }
    
    private void disableControls() {
        root.addEventFilter(Event.ANY, _event_blocker);
    }
    
    private void enableControls() {
        root.removeEventFilter(Event.ANY, _event_blocker);
    }
    
    private VBox root = new VBox();
    private void initInterface(Stage primaryStage) {
        
        buildMiddle();
        buildMenu();
        buildButtom();
        
        
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(_top_menu, _middle, _bottom);
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Game of Life (Multiplayer)");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private final Label btm_msg = new Label("Welcome!");
    private void buildButtom() {
        btm_msg.setFont(new Font(16));
        
        _bottom.setStyle("-fx-background-radius: 10px; -fx-background-color: palegreen;");
        _bottom.setSpacing(20);
        _bottom.setPrefHeight(75);
        _bottom.setAlignment(Pos.TOP_CENTER);
        _bottom.getChildren().addAll(btm_msg);
    }
    
    private void buildMenu() {
        Button newgame_btn = new Button();
        newgame_btn.setText("New game");
        newgame_btn.setStyle("-fx-font-size: 10pt;");
        newgame_btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //if (_controls_disabled) return;
                model().newGame();
                _generationcounter = 0;
            }
        });
        
        Button launch_btn = new Button();
        launch_btn.setText("Begin life!");
        launch_btn.setStyle("-fx-font-size: 20pt;");
        launch_btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //if (_controls_disabled) return;
                disableControls();
                model().runCircle();
            }
        });
        Region top_menu_spacer = new Region();
        top_menu_spacer.prefWidthProperty().bind(newgame_btn.widthProperty());
        
        _top_menu.setSpacing(200);
        _top_menu.setAlignment(Pos.CENTER);
        _top_menu.getChildren().addAll(newgame_btn, launch_btn, top_menu_spacer);
    }
    
    private final VBox _p1_vbox = new VBox();
    private void buildP1Panel() {
        Label p1_name = new Label();
        p1_name.textProperty().bind(model().getPlayer1().nameProperty());
        p1_name.setFont(new Font(20));
        Label p1_can_add = new Label();
        p1_can_add.setFont(new Font(14));
        p1_can_add.textProperty().bind(new SimpleStringProperty("can add ").concat(model().getPlayer1().canAddProperty().asString()).concat(" creatures"));
        Label p1_can_delete = new Label();
        p1_can_delete.textProperty().bind(new SimpleStringProperty("can delete ").concat(model().getPlayer1().canDeleteProperty().asString()).concat(" creatures"));
        p1_can_delete.setFont(new Font(14));
        
        _p1_vbox.setPrefWidth(150);
        _p1_vbox.setAlignment(Pos.CENTER_RIGHT);
        _p1_vbox.setSpacing(10);
        _p1_vbox.getChildren().addAll(p1_name, p1_can_add, p1_can_delete);
    }
    
    private final VBox _p2_vbox = new VBox();
    private void buildP2Panel() {
        Label p2_name = new Label();
        p2_name.textProperty().bind(model().getPlayer2().nameProperty());
        p2_name.setFont(new Font(20));
        Label p2_can_add = new Label();
        p2_can_add.textProperty().bind(new SimpleStringProperty("can add ").concat(model().getPlayer2().canAddProperty()).concat(" creatures"));
        p2_can_add.setFont(new Font(14));
        Label p2_can_delete = new Label();
        p2_can_delete.textProperty().bind(new SimpleStringProperty("can delete ").concat(model().getPlayer2().canDeleteProperty()).concat(" creatures"));
        p2_can_delete.setFont(new Font(14));
        
        _p2_vbox.setPrefWidth(150);
        _p2_vbox.setAlignment(Pos.CENTER_LEFT);
        _p2_vbox.setSpacing(10);
        _p2_vbox.getChildren().addAll(p2_name, p2_can_add, p2_can_delete);
    }
    
    private Group drawFieldGrid(int size) {
        Group grid = new Group();
        Line line;
        for (int i=0; i<FIELD_SIZE+1;++i) {
            line = new Line((CELL_SIZE+1)*i, 0, (CELL_SIZE+1)*i, size);
            line.setStroke(BORDER_COLOR);
            line.setStrokeWidth(BORDER_SIZE);
            grid.getChildren().add(line);
        }
        for (int i=0; i<FIELD_SIZE+1;++i) {
            line = new Line(0, (CELL_SIZE+1)*i, size, (CELL_SIZE+1)*i);
            line.setStrokeWidth(BORDER_SIZE);
            line.setStroke(BORDER_COLOR);
            grid.getChildren().add(line);
        }
        return grid;
    }
    
    private final Group _field = new Group();
    private final Group _creatures = new Group();
    private void buildField() {
        int size = CELL_SIZE*FIELD_SIZE+BORDER_SIZE*(FIELD_SIZE);
        Region t_field = new Region();
        t_field.setPrefWidth(size);
        t_field.setPrefHeight(size);
        t_field.setStyle("-fx-background-color: red;");
        
        final int compheight = FIELD_SIZE/4;
        int realcompheight = compheight*CELL_SIZE+compheight*BORDER_SIZE;
        Rectangle comp1 = new Rectangle(0, 0, size, realcompheight);
        comp1.setFill(COMP_COLOR);
        Rectangle comp2 = new Rectangle(0, size-realcompheight, size, realcompheight);
        comp2.setFill(COMP_COLOR);
        
        Rectangle p1 = new Rectangle(0, realcompheight, size/2, size-realcompheight*2);
        p1.setFill(P1_COLOR);
        p1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                //if (_controls_disabled) return;
                cellClicked(t.getX(), t.getY(), PLAYER_1);
            }
        });
        
        Rectangle p2 = new Rectangle(size/2, realcompheight, size/2, size-realcompheight*2);
        p2.setFill(P2_COLOR);
        p2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                //if (_controls_disabled) return;
                cellClicked(t.getX(), t.getY(), PLAYER_2);
            }
        });
        
        _field.getChildren().addAll(t_field, comp1, comp2, p1, p2, _creatures, drawFieldGrid(size));
    }
    
    private void cellClicked(double x, double y, int player_num) {
        int row = -1;
        int col = -1;
        if ( (y % (CELL_SIZE+BORDER_SIZE)) != 0) {
            row = (int) ((y / (CELL_SIZE+BORDER_SIZE)));
        } else {
            return;
        }

        if ( (x % (CELL_SIZE+BORDER_SIZE)) != 0) {
            col = (int) ((x / (CELL_SIZE+BORDER_SIZE)));
        } else {
            return;
        }
        debug(String.valueOf(col)+" "+String.valueOf(row));
        model().addCreatureByUser(new Position(col, row), player_num==1 ? model().getPlayer1() : model().getPlayer2());
    }
    
    private Rectangle drawCreature(final int row, final int col, Player pl) {
        final Rectangle creature = new Rectangle(col*(CELL_SIZE+BORDER_SIZE), row*(CELL_SIZE+BORDER_SIZE), CELL_SIZE+BORDER_SIZE, CELL_SIZE+BORDER_SIZE);
        Color creature_color;
        if      (pl.equals(model().getPlayer1()))   creature_color=P1_COLOR.darker();
        else if (pl.equals(model().getPlayer2()))   creature_color=P2_COLOR.darker();
        else                                        creature_color=COMP_COLOR.darker();
        creature.setFill(creature_color);
        creature.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                //if (_controls_disabled) return;
                model().deleteCreatureByUser(new Position(col, row));
            }
        });
        return creature;
    }
    
    private void buildMiddle() {
        buildP1Panel();
        buildP2Panel();
        buildField();
        
        _middle.setSpacing(20);
        _middle.setAlignment(Pos.CENTER);
        _middle.getChildren().addAll(_p1_vbox, _field, _p2_vbox);
    }
    
    public static void debug(String s) {
        if (DEBUG_MODE) {
            System.out.println(s);
        }
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void showMessage(String s) {
        btm_msg.setText(s);
    }

    @Override
    public void gameEnded(ModelEvent e) {
        showMessage(e.winner()+" won this game!\nPress 'New game' if you want to play again.");
        enableControls();
    }

    @Override
    public void messageRecieved(ModelEvent e) {
        showMessage(e.message());
    }
    
    private int _generationcounter = 0;
    
    @Override
    public void lifeCircleEnded(List<List<Creature>> cl) {
        PauseTransition pt;
        _seqT = new SequentialTransition();
        _seqT.setOnFinished(new EventHandler<ActionEvent>(){
 
            @Override
            public void handle(ActionEvent arg0) {
                enableControls();
                //showMessage("end of "+String.valueOf(model().LIVINGSTEPS)+" generations...");
            }
        });
        
        for (List<Creature> outer : cl) {
            final List<Rectangle> rl = new ArrayList<>();
            final int gn = ++_generationcounter;
            for (Creature c : outer) {
                rl.add(drawCreature(c.getPosition().getY(), c.getPosition().getX(), c.owner()));
            }
            pt = new PauseTransition(Duration.millis(ANIMATION_TIME));
            pt.setOnFinished(new EventHandler<ActionEvent>(){
 
                @Override
                public void handle(ActionEvent arg0) {
                    _creatures.getChildren().setAll(rl);
                    if (!model().isGameEnded()) {
                        showMessage(String.valueOf(gn)+"tn generation.");
                    }
                }
            });
            _seqT.getChildren().add(pt);
        }
        _seqT.play();
    }

    @Override
    public void fieldStateChanged(List<Creature> cl) {
        final List<Rectangle> rl = new ArrayList<>();
        for (Creature c : cl) {
            rl.add(drawCreature(c.getPosition().getY(), c.getPosition().getX(), c.owner()));
        }
        _creatures.getChildren().setAll(rl);
    }
}
