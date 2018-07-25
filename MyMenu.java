/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */
import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;


class MyLabel
{
  float x, y;
  String label;
  int size;

  public MyLabel(float x, float y, int size, String label)
  {
    this.x = x;
    this.y = y;
    this.label = label;
    this.size = size;
  }
 
  public void draw(Graphics2D g)
  {
    g.setColor(Color.black);
    g.setFont(new Font("SansSerif", Font.PLAIN, size));
    g.drawString(label, x, y);
  }
}


class MyOption
{
  float x1, y1, x2, y2;
  String label;
  int cb;  // used for callbacks

  public MyOption(
      float x, float y,
      float width, float height,
      String label, int cb)
  {
    if (width <= 0.0 || height <= 0.0)
    {
      System.err.println("dimensions should be positive");
      System.exit(1);
    }
    x1 = x;
    y1 = y;
    x2 = x + width;
    y2 = y + height;
    this.label = label;
    this.cb = cb;
  }
 
  public boolean isInside(float x, float y)
  {
    return (
      x >= x1 && y >= y1 &&
      x <= x2 && y <= y2);
  }

  public int activate()
  {
    return cb;
  }

  public void draw(Graphics2D g)
  {
    double width = x2 - x1;
    double height = y2 - y1;
    int x_lbl = (int) (x1 + 8);
    int y_lbl = (int) (y1 + (height / 2));
    g.setColor(Color.lightGray);
    g.fill(new RoundRectangle2D.Double(x1, y1, width, height, 8, 8));
    g.setColor(Color.black);
    g.setFont(new Font("SansSerif", Font.PLAIN, 18));
    g.drawString(label, x_lbl, y_lbl);
  }
}


public class MyMenu
{
  private Vector<MyOption> options = new Vector<>();
  private Vector<MyLabel> labels = new Vector<>();

  public void addOption(MyOption opt)
  { options.add(opt); }

  public void addLabel(MyLabel lbl)
  { labels.add(lbl); }

  public void addLabel(float x, float y, int size, String label)
  { addLabel(
      new MyLabel(x, y, size, label));
  }

  public int click(int x, int y)
  {
    MyOption clicked = null;
    for (int i = 0; i < options.size(); i++)
    {
      if (options.get(i).isInside(x, y))
      {
        clicked = options.get(i);
        break;
      }
    }
    if (clicked != null)
      return clicked.activate();
    else
      return -1;
  }

  public void draw(Graphics2D g, Dimension dim)
  {
    //for (int i = 0; i < options.size(); i++)
    //  options.get(i).draw(g);
    for (MyOption option : options)
      option.draw(g);
    for (MyLabel label : labels)
      label.draw(g);
  }
}

