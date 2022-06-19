import java.util.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * GUI class for the game. When  run,  it  expects  a  single  command line
 * argument which is the name of a map file to load. If this argument is
 * missing the message "Usage: java CrawlGui mapname" is to be printed to
 * standard error and the program will exit with status 1. If the argument is
 * present but the map can not be loaded for some reason, "Unable to load
 * file" is to be printed to standard error and the program will exit with
 * status 2.  (Any output to standard error should be followed by a newline).
 * @author Howie L.
 */
public class CrawlGui extends javafx.application.Application {
    /* GUI widgets */
    private Stage st;
    private VBox rootBox;
    private HBox topBox;
    private VBox bottomBox;
    private HBox canvasBox;
    private GridPane btnPane;
    private GridPane dirnBtnPane;
    private GridPane oprnBtnPane;
    private TextArea txtArea;
    private Cartographer cartographer;
    private Button saveBtn;
    private Button fightBtn;
    private Button dropBtn;
    private Button takeBtn;
    private Button lookBtn;
    private Button examineBtn;
    private Button eastBtn;
    private Button westBtn;
    private Button northBtn;
    private Button southBtn;

    /* Data field */
    private Player player;
    private Room base;
    private BoundsMapper bm;
    private enum Directions {
        EAST, WEST, NORTH, SOUTH
    }
    private Room currRoom;

    /**
     * The action to take when any of the direction buttons East, West, North
     * and South is pressed. If there is no exit in the specified direction,
     * display "No door that way". If there is an exit in that direction but
     * you can't leave, display "Something prevents you from leaving".
     * Otherwise, move to the specified room and display "You enter "
     * followed by the description of the room.
     * @param dirn Valid exit directions
     */
    private void dirnBtnAct(Directions dirn) {
        Room nextRoom;
        Map<String, Room> currExits = currRoom.getExits();
        switch (dirn) {
            case EAST:
                if (currExits.containsKey("East")) {
                    if (canPlayerLeaveRoom()) {
                        this.txtArea.appendText("Something prevents you" +
                                " from leaving\n");
                        return;
                    }
                    currRoom.leave(this.player);
                    nextRoom = currExits.get("East");
                    nextRoom.enter(this.player);
                    this.txtArea.appendText("You enter " +
                            nextRoom.getDescription() + "\n");
                } else {
                    this.txtArea.appendText("No door that way\n");
                }
                break;
            case WEST:
                if (currExits.containsKey("West")) {
                    if (canPlayerLeaveRoom()) {
                        this.txtArea.appendText("Something prevents you" +
                                " from leaving\n");
                        return;
                    }
                    currRoom.leave(this.player);
                    nextRoom = currExits.get("West");
                    nextRoom.enter(this.player);
                    this.txtArea.appendText("You enter " +
                            nextRoom.getDescription() + "\n");
                } else {
                    this.txtArea.appendText("No door that way\n");
                }
                break;
            case NORTH:
                if (currExits.containsKey("North")) {
                    if (canPlayerLeaveRoom()) {
                        this.txtArea.appendText("Something prevents you" +
                                " from leaving\n");
                        return;
                    }
                    currRoom.leave(this.player);
                    nextRoom = currExits.get("North");
                    nextRoom.enter(this.player);
                    this.txtArea.appendText("You enter " +
                            nextRoom.getDescription() + "\n");
                } else {
                    this.txtArea.appendText("No door that way\n");
                }
                break;
            case SOUTH:
                if (currExits.containsKey("South")) {
                    if (canPlayerLeaveRoom()) {
                        this.txtArea.appendText("Something prevents you" +
                                " from leaving\n");
                        return;
                    }
                    currRoom.leave(this.player);
                    nextRoom = currExits.get("South");
                    nextRoom.enter(this.player);
                    this.txtArea.appendText("You enter " +
                            nextRoom.getDescription() + "\n");
                } else {
                    this.txtArea.appendText("No door that way\n");
                }
                break;
            default:
                return;
        }
        currRoom = findPlayerRoom();
        this.updateCanvas();
    }

    /**
     * The action to take when the Look button is pressed. Display the
     * following information: "description_of_room - you see:"
     * followed by the short descriptions of each Thing in the room (add a
     * leading space for each item). Then "You are carrying:" followed by the
     * short descriptions of each Thing (add a leading space for each item)
     * you are carrying. Then "worth total_worth_of_carried_items in total"
     * Formatted for one decimal place.
     */
    private void lookBtnAct() {
        double totValue = 0;
        StringBuilder lookInfo = new StringBuilder("");
        lookInfo.append(this.currRoom.getDescription() + " - you see:\n");
        for (Thing t : currRoom.getContents()) {
            lookInfo.append(" " + t.getShortDescription() + "\n");
        }
        lookInfo.append("You are carrying:\n");
        for (Thing t : player.getContents()) {
            lookInfo.append(" " + t.getShortDescription() + "\n");
            if (t instanceof Lootable) {
                totValue += ((Lootable) t).getValue();
            }
        }
        lookInfo.append("worth " + String.format("%.1f", totValue) + " in " +
                "total\n");
        this.txtArea.appendText(lookInfo.toString());
    }

    /**
     * The action to take when the Examine button is pressed. Show a dialog
     * box to get the short description of the Thing to examine. The first
     * matching item will have its long description displayed. Check the
     * player's inventory first, then if no match is found, the contents of
     * the current room. If no match is found, display "Nothing found with
     * that name".
     */
    private void examineBtnAct() {
        TextInputDialog tid = new TextInputDialog();
        tid.initStyle(StageStyle.UTILITY);  // Problem of OS, can't use UNIFIED
        tid.setTitle("Examine what?");
        tid.setHeaderText(null);
        tid.setContentText(null);
        Optional<String> result = tid.showAndWait();
        if (result.isPresent()) {
            String itemShortDesc = result.get();
            for (Thing t : player.getContents()) {
                if (itemShortDesc.equals(t.getShort())) {
                    this.txtArea.appendText(t.getLong()+"\n");
                    return;
                }
            }
            for (Thing t : currRoom.getContents()) {
                if (itemShortDesc.equals(t.getShort())) {
                    this.txtArea.appendText(t.getLong()+"\n");
                    return;
                }
            }
            this.txtArea.appendText("Nothing found with that name\n");
        }
    }

    /**
     * The action to take when the Drop button is pressed. Show a dialog box
     * to get the short description of the item to remove from the player's
     * inventory and add to the current room. If the player is not carrying a
     * matching item, display "Nothing found with that name".
     */
    private void dropBtnAct() {
        TextInputDialog tid = new TextInputDialog();
        tid.initStyle(StageStyle.UTILITY);  // Problem of OS, can't use UNIFIED
        tid.setTitle("Item to drop?");
        tid.setHeaderText(null);
        tid.setContentText(null);
        Optional<String> result = tid.showAndWait();
        if (result.isPresent()) {
            String itemShortDesc = result.get();
            for (Thing t : player.getContents()) {
                if (itemShortDesc.equals(t.getShort())) {
                    player.drop(t);
                    currRoom.enter(t);
                    break;
                }
            }
            this.updateCanvas();
        }
    }

    /**
     * The action to take when the Take button is pressed. Similar to Drop
     * but the dialog box is to be titled "Take what?". Objects of type
     * Player should be skipped when looking for short description matches.
     * There are additional cases where the operation will fail (silently):
     * -If an attempt is made to pick up a live Mob
     * -If the leave call to remove the item returns false. [Remember that to
     * remove an item from a room, you will need to call leave on the room].
     */
    private void takeBtnAct() {
        TextInputDialog tid = new TextInputDialog();
        tid.initStyle(StageStyle.UTILITY);  // Problem of OS, can't use UNIFIED
        tid.setTitle("Take what?");
        tid.setHeaderText(null);
        tid.setContentText(null);
        Optional<String> result = tid.showAndWait();
        if (result.isPresent()) {
            String itemShortDesc = result.get();
            for (Thing t : currRoom.getContents()) {
                if (itemShortDesc.equals(t.getShort()) &&
                        !(t instanceof Player)) {
                    if (t instanceof Mob) {
                        if(!(((Mob) t).isAlive()) && currRoom.leave(t)) {
                            player.add(t);
                        }  // Silent fail in some conditions
                    } else {
                        currRoom.leave(t);
                        player.add(t);
                    }
                    break;
                }
            }
            this.updateCanvas();
        }
    }

    /**
     * The action to take when the Fight button is pressed. Show a dialog box
     * (titled "Fight what?") to get the short description of a Critter in
     * the current room to fight. If the fight occurs, display either "You
     * won" or "Game over" as appropriate. (If you lose the fight, all of the
     * buttons on the GUI should be disabled). Silent failures will occur if:
     * -There is no matching Critter.
     * -There is a matching Critter but it is not alive.
     */
    private void fightBtnAct() {
        TextInputDialog tid = new TextInputDialog();
        tid.initStyle(StageStyle.UTILITY);  // Problem of OS, can't use UNIFIED
        tid.setTitle("Fight what?");
        tid.setHeaderText(null);
        tid.setContentText(null);
        Optional<String> result = tid.showAndWait();
        if (result.isPresent()) {
            String itemShortDesc = result.get();
            for (Thing t : currRoom.getContents()) {
                if (itemShortDesc.equals(t.getShort()) &&
                        t instanceof Critter) {
                    if (((Critter) t).isAlive()) {
                        player.fight((Critter) t);
                        if (player.isAlive()) {
                            this.txtArea.appendText("You won\n");
                        } else {
                            this.txtArea.appendText("Game over\n");
                            this.disableBtns();
                        }
                        this.updateCanvas();
                        return;
                    }
                }
            }
        }
    }

    /**
     * The action to take when the Save button is pressed. Show a dialog box
     * (titled "Save filename?") for a file name to use with MapIO.saveMap.
     * Display either "Saved" or "Unable to save" as appropriate.
     */
    public void saveBtnAct() {
        TextInputDialog tid = new TextInputDialog();
        tid.initStyle(StageStyle.UTILITY);  // Problem of OS, can't use UNIFIED
        tid.setTitle("Save filename?");
        tid.setHeaderText(null);
        tid.setContentText(null);
        Optional<String> result = tid.showAndWait();
        if (result.isPresent()) {
            String filename = result.get();
            if (MapIO.saveMap(base, filename)) {
                this.txtArea.appendText("Saved\n");
            } else {
                this.txtArea.appendText("Unable to save\n");
            }
        }
    }

    /* Main function */
    public static void main(String[] args) {
        /* Check if argument is empty */
        if (args.length == 0) {
            System.err.println("Proper Usage is: java program filename\n");
            System.exit(1);
        }
        launch(args);
    }

    /* Display the GUI */
    public void start(Stage stage) {
        List<String> arguments = getParameters().getRaw();
        Object[] playerAndRoom = MapIO.loadMap(arguments.get(0));
        /* Check if room loaded correctly */
        if (playerAndRoom == null) {
            System.err.println("Unable to load file");
            System.exit(2);
        }

        /* Set up gaming */
        this.player = (Player) playerAndRoom[0];
        this.base = (Room) playerAndRoom[1];
        base.enter(player);
        bm = new BoundsMapper(base);
        bm.walk();
        this.currRoom = base;

        /* Set and display GUI widgets */
        int scale = 50;
        int margin = 60;
        int canvasWidth = scale*(bm.xMax-bm.xMin) + margin;
        int canvasHeight = scale*(bm.yMax-bm.yMin) + margin;
        stage.setTitle("Crawl - Explore");
        this.st = stage;
        this.addAllWidgets(canvasWidth, canvasHeight);
        this.cartographer.draw(bm);
        this.txtArea.appendText("You find yourself in " +
                currRoom.getDescription() + "\n");
        Scene scene = new Scene(this.rootBox);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create all the button objects on the GUI.
     */
    private void setButtons() {
        this.saveBtn = new Button("Save");
        this.fightBtn = new Button("Fight");
        this.dropBtn = new Button("Drop");
        this.takeBtn = new Button("Take");
        this.lookBtn = new Button("Look");
        this.examineBtn = new Button("Examine");
        this.eastBtn = new Button("East");
        this.westBtn = new Button("West");
        this.northBtn = new Button("North");
        this.southBtn = new Button("South");
    }

    /**
     * Establish the layout for the buttons.
     */
    private void setButtonGrid() {
        this.btnPane = new GridPane();
        this.dirnBtnPane = new GridPane();
        this.oprnBtnPane = new GridPane();

        final int numCols = 3;
        final int numRowsDirn = 3;
        final int numRowsOprn = 4;
        for (int i = 0; i < numCols; ++i) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setFillWidth(true);
            dirnBtnPane.getColumnConstraints().add(colConst);
            oprnBtnPane.getColumnConstraints().add(colConst);
        }
        for (int j = 0; j < numRowsDirn; ++j) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setFillHeight(true);
            dirnBtnPane.getRowConstraints().add(rowConst);
        }
        for (int k = 0; k < numRowsOprn; ++k) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setFillHeight(true);
            oprnBtnPane.getRowConstraints().add(rowConst);
        }

        addButtonToPane();
    }

    /**
     * Add buttons to the button layout.
     */
    private void addButtonToPane() {
        this.dirnBtnPane.add(northBtn, 1, 0);
        this.dirnBtnPane.add(westBtn, 0, 1);
        this.dirnBtnPane.add(eastBtn, 2, 1);
        this.dirnBtnPane.add(southBtn, 1, 2);
        this.oprnBtnPane.add(lookBtn, 0, 0);
        this.oprnBtnPane.add(examineBtn, 1, 0);
        this.oprnBtnPane.add(dropBtn, 0, 1);
        this.oprnBtnPane.add(takeBtn, 1, 1);
        this.oprnBtnPane.add(fightBtn, 0, 2);
        this.oprnBtnPane.add(saveBtn, 0, 3);

        this.btnPane.add(dirnBtnPane, 0, 0);
        this.btnPane.add(oprnBtnPane, 0, 1);
    }

    /**
     * Set the container for the cartographer object.
     * @param width Canvas width
     * @param height Canvas height
     */
    private void setCanvasBox(int width, int height) {
        this.cartographer = new Cartographer(width, height);
        this.canvasBox = new HBox();
        this.canvasBox.getChildren().add(cartographer);
        this.canvasBox.setAlignment(Pos.CENTER);
    }

    /**
     * Set the layout containing the cartographer and the button layout.
     */
    private void setTopBox() {
        this.topBox = new HBox();
        this.topBox.getChildren().add(0, this.canvasBox);
        this.topBox.getChildren().add(1, this.btnPane);
        this.topBox.setHgrow(canvasBox, Priority.ALWAYS);
        this.topBox.setHgrow(btnPane, Priority.NEVER);
    }

    /**
     * Set the container for the bottom text area.
     */
    private void setBottomBox() {
        this.txtArea = new TextArea();
        txtArea.setEditable(false);
        this.bottomBox = new VBox(txtArea);
        this.bottomBox.setFillWidth(true);
        this.bottomBox.setVgrow(txtArea, Priority.NEVER);
    }

    /**
     * Set the highest level layout.
     */
    private void setRootBox() {
        this.rootBox = new VBox();
        this.rootBox.getChildren().add(0, this.topBox);
        this.rootBox.getChildren().add(1, this.bottomBox);
        this.rootBox.setFillWidth(true);
        this.rootBox.setVgrow(this.topBox, Priority.ALWAYS);
        this.rootBox.setVgrow(this.bottomBox, Priority.NEVER);
    }

    /**
     * Add all the required GUI Widgets.
     * @param canvasWidth Cartographer width
     * @param canvasHeight Cartographer height
     */
    private void addAllWidgets(int canvasWidth, int canvasHeight) {
        this.setButtons();
        this.setBtnActions();
        this.setButtonGrid();
        this.setCanvasBox(canvasWidth, canvasHeight);
        this.setTopBox();
        this.setBottomBox();
        this.setRootBox();
    }

    /* Return the room where the player is currently at. */
    private Room findPlayerRoom() {
        Map<Room, Pair> roomCoords = bm.coords;
        for (Map.Entry<Room, Pair> roomEntry : roomCoords.entrySet()) {
            Room r = roomEntry.getKey();
            if (r.getContents().contains(player)) {
                return r;
            }
        }
        return null;
    }

    /* Determine whether anything is stopping the player from leaving. */
    private Boolean canPlayerLeaveRoom() {
        for (Thing t : currRoom.getContents()) {
            if (t instanceof Mob) {
                if (((Mob) t).wantsToFight(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Disable all the buttons if game is over. */
    private void disableBtns() {
        this.eastBtn.setDisable(true);
        this.westBtn.setDisable(true);
        this.northBtn.setDisable(true);
        this.southBtn.setDisable(true);
        this.lookBtn.setDisable(true);
        this.examineBtn.setDisable(true);
        this.dropBtn.setDisable(true);
        this.takeBtn.setDisable(true);
        this.fightBtn.setDisable(true);
        this.saveBtn.setDisable(true);
    }

    /**
     * Lambda expression of button clicking events assignments to their
     * corresponding actions.
     */
    private void setBtnActions() {
        eastBtn.setOnAction(event -> dirnBtnAct(Directions.EAST));
        westBtn.setOnAction(event -> dirnBtnAct(Directions.WEST));
        northBtn.setOnAction(event -> dirnBtnAct(Directions.NORTH));
        southBtn.setOnAction(event -> dirnBtnAct(Directions.SOUTH));
        lookBtn.setOnAction(event -> lookBtnAct());
        examineBtn.setOnAction(event -> examineBtnAct());
        dropBtn.setOnAction(event -> dropBtnAct());
        takeBtn.setOnAction(event -> takeBtnAct());
        fightBtn.setOnAction(event -> fightBtnAct());
        saveBtn.setOnAction(event -> saveBtnAct());
    }

    /**
     * Redraw the cartographer if anything in the map changed.
     */
    private void updateCanvas() {
        bm.reset();
        bm.walk();
        this.cartographer.clear();
        this.cartographer.draw(bm);
    }
}
