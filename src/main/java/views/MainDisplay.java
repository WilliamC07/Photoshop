package views;

import javafx.scene.control.SplitPane;

/**
 * This is the view that is always shown to the user. The components are:
 * 1. TODO Editing information
 * 2. TODO Editing board
 * 3. TODO Chat
 * 4. TODO Switch to different pages
 *
 * Should only be initialized once in ApplicationStart. We are not having multiple windows, instead we have pages.
 * This is a SplitPane to allow the photo editor to choose how big he/she wants the sections to be.
 *
 * Use a SplitPane to allow the user to switch
 *
 *
 * @see ApplicationStart
 * @author William Cao
 * @since 1.0
 */
final class MainDisplay extends SplitPane{
  WHbox hbox;

  //top menus I'm not sure if this needs to be a wrapper class or whatever it is so im not going to add it for now
  Menu file = new Menu("File");
  MenuItem open = new MenuItem("Open...");
  MenuItem save = new MenuItem("Save");
  MenuItem exit = new MenuItem("Exit");
  exit.setOnAction(e -> window.close());
  file.getItems().addAll(open,save,exit);

  //Will be on left side of screen, icons for image editing tools
  WVbox tools = new WVBox(10);
  WButton crop = new WButton("Crop");
  WButton brush = new WButton("Brush");
  WButton shape = new WButton("Shape");
  WButton text = new WButton("Text");
  WButton erase = new WButton("Erase");
  tools.getChildren().addAll(crop,brush,shape,text,erase);
}
