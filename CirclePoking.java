/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

class MyPan extends JPanel
implements MouseListener, KeyListener
{
  private GameApp app;
  private XmlConfLoader conf;
  private MyMenu menu;
  private int level_diff = 0;
  private int round = 0;
  enum AppMode {
    APP_GAME,
    APP_MENU_START,
    APP_MENU_NEXT,
    APP_MENU_GAMEOVER
  };
  private AppMode app_mode;
  private ResourceBundle messages;  // i18n

  public MyPan(XmlConfLoader conf)
  {
    addMouseListener(this);
    addKeyListener(this);
    selectLang("en", "GB");  // default language
    this.conf = conf;
    initGameApp(
      conf.winWidth(),
      conf.winHeight());
    initMenuStart();
  }

  private void initGameApp(int width, int height)
  {
    app = new GameApp(
      width, height,
      round, conf.level(level_diff));
  }

  public void reset()
  {
    Dimension dim = getSize();
    //System.out.println("pan-dims: " + dim.width + " " + dim.height);
    initGameApp(dim.width, dim.height);
  }

  private void selectLang(String language, String country)
  {
    Locale currentLocale = new Locale(language, country);
    messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
  }

  private void initMenuStart()
  {
    app_mode = AppMode.APP_MENU_START;

    menu = new MyMenu();
    menu.addLabel(140, 60, 20, messages.getString("game_name"));
    menu.addLabel(140, 100, 16, messages.getString("explain_rules_1"));
    menu.addLabel(140, 120, 16, messages.getString("explain_rules_2"));
    menu.addLabel(140, 140, 16, messages.getString("explain_rules_3"));
    for (int i = 0; i < conf.numLevels(); i++)
    {
      int y = i * 60 + 180;
      String label = conf.level(i).getDifficulty();
      menu.addOption(
        new MyOption(160, y, 160, 40, label, i));
    }
  }

  private void initMenuNextLevel()
  {
    Level level = conf.level(level_diff);
    Level next = conf.level(level_diff);
    int num_circ = level.numCircles(round + 1);
    int round_time = level.getRoundTime(round + 1);
    int max_collided = level.maxCollided();
    String difficulty = level.getDifficulty();

    app_mode = AppMode.APP_MENU_NEXT;
    menu = new MyMenu();
    menu.addLabel(140,  80, 20, messages.getString("congrat"));
    menu.addLabel(140, 110, 16, messages.getString("finished_round") +
                                  (round + 1) + " " +
                                messages.getString("of_difficulty") +
                                  difficulty + ".");
    menu.addLabel(150, 140, 16, messages.getString("next_round"));
    menu.addLabel(150, 160, 14, messages.getString("num_circ") + num_circ);
    menu.addLabel(150, 175, 14, messages.getString("max_collided") + max_collided);
    menu.addLabel(150, 190, 14, messages.getString("round_time") + round_time +
                                messages.getString("sec"));
    menu.addOption(
      new MyOption(160, 230, 160, 40, "Play", 1));
  }

  private void initMenuGameOver()
  {
    app_mode = AppMode.APP_MENU_GAMEOVER;
    menu = new MyMenu();
    menu.addLabel(140, 80, 20, "Game Over!");
    menu.addOption(
      new MyOption(160, 180, 160, 40, "Play Again", 1));
  }

  public void setLevelDifficulty(int d)
  {
    level_diff = d;
  }

  public void step()
  {
    Dimension dim = getSize();
    if (app_mode == AppMode.APP_GAME)
    {
      if (!app.gameEnded())
        app.step(dim);
      else
        roundEnded();
    }
  }

  private void roundEnded()
  {
    Level level = conf.level(level_diff);
    int max_collided = level.maxCollided();
    int collided = app.getNumCollided();
    if (collided > max_collided)
      initMenuGameOver();
    else
      initMenuNextLevel();
  }

  public void click(int x, int y)
  {
    if (app_mode == AppMode.APP_GAME)
      app.click(x, y);
    else
    {
      int i = menu.click(x, y);
      if (i == -1) return;
      switch (app_mode)
      {
        case APP_MENU_START:
          setLevelDifficulty(i);
          app_mode = AppMode.APP_GAME;
          reset();
          break;
        case APP_MENU_NEXT:
          app_mode = AppMode.APP_GAME;
          round++;
          reset();
          break;
        case APP_MENU_GAMEOVER:
          initMenuStart();
          round = 0;
          break;
      }
    }
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Dimension dim = getSize();
    Graphics2D g2 = (Graphics2D)g;
    RenderingHints rh = new RenderingHints(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHints(rh);
    switch (app_mode)
    {
      case APP_GAME:
        app.draw(g2, dim);
        break;
      case APP_MENU_START:
      case APP_MENU_NEXT:
      case APP_MENU_GAMEOVER:
        menu.draw(g2, dim);
        break;
    }
  }

  public void mouseClicked(MouseEvent ev)
  {
    int x = ev.getX();
    int y = ev.getY();
    this.click(x, y);
  }

  public void mousePressed(MouseEvent ev) {}
  public void mouseReleased(MouseEvent ev) {}
  public void mouseEntered(MouseEvent ev) {}
  public void mouseExited(MouseEvent ev) {}

  public void keyPressed(KeyEvent ev) {}
  public void keyReleased(KeyEvent ev) {}
  public void keyTyped(KeyEvent ev)
  {
    char c = ev.getKeyChar();
    switch (c)
    {
      case 'q': System.exit(0); break;
      case 'r': reset(); break;
      case 'f': selectLang("fr", "FR"); break;
      case 'e': selectLang("en", "GB"); break;
      default: break;
    }
  }
}


class MyWin extends JFrame
{ private MyPan pan;

  public MyWin(XmlConfLoader conf, String title)
  {
    System.out.println("Dims: " + conf.winWidth() + " " + conf.winHeight());
    setSize(conf.winWidth(), conf.winHeight());
    setTitle(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pan = new MyPan(conf);
    pan.setBackground(Color.gray);
    getContentPane().add(pan);
    Timer repainter = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        pan.step();
        pan.repaint();
      }
    });
    repainter.start();
  }
}


public class CirclePoking
{
  static String conf_file;
  static XmlConfLoader conf;

  public static void main(String args[])
  {
    conf_file = "circle-poking-conf.xml";
    conf = new XmlConfLoader(conf_file);
    MyWin win = new MyWin(conf, "Circle Poking");
    win.setVisible(true);
  }
}

