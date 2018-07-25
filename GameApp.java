/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */

import java.awt.*;
import java.awt.geom.*;
//import javax.swing.*;

class GameApp
{
  private Circle[] circles;
  private int collided;
  private ElapsedTime elapsed_time = new ElapsedTime();

  public GameApp(
      int width, int height, int round, Level level)
  { init(width, height, round, level); }

  public int getNumCollided() { return collided; }
  public int getNumCircles() { return circles.length; }

  public void init(
      int width, int height, int round, Level level)
  {
    // first initialise the circles
    int num_circ = level.numCircles(round);
    circles = new Circle[num_circ];
    CircleFactory circFactory = new CircleFactory();
    circFactory.setRadiusRange(
      level.minRadius(),
      level.maxRadius());
    circFactory.setSpeedRange(
      level.minSpeed(),
      level.maxSpeed());
    for (int i = 0; i < circles.length; i++)
      circles[i] = circFactory.newCircle(width, height);
    collided = 0;

    // no circles should collide at the beginning
    boolean ok = false;
    while (!ok) {
      ok = true;
      for (Circle circ_a : circles)
        for (Circle circ_b : circles)
          if (circ_a != circ_b)
            if (circ_a.near(circ_b, 60f))
            {
              circFactory.initCircle(circ_a, width, height);
              ok = false;
            }
    }

    // initialise the time-out
    int round_time = level.getRoundTime(round);
    elapsed_time.init(round_time);
  }

  private void fillBackground(Graphics2D g, Dimension dim)
  {
    g.setColor(Color.darkGray);
    g.fillRect(0, 0, dim.width, dim.height);
  }

  private void drawCircles(Graphics2D g, Dimension dim)
  {
    for (Circle circ : circles)
      if (circ.isOff())
        circ.draw(g, dim.width, dim.height);
    for (Circle circ : circles)
      if (!circ.isOff())
        circ.draw(g, dim.width, dim.height);
  }

  private void drawReport(Graphics2D g, Dimension dim)
  {
    final int num_circ = circles.length;
    final int safe = num_circ - collided;
    g.setColor(Color.black);
    g.setFont(new Font("SansSerif", Font.PLAIN, 18));
    g.drawString("safe: " + safe + "/" + num_circ, 12, 22);
  }

  public void draw(Graphics2D g, Dimension dim)
  {
    fillBackground(g, dim);
    drawCircles(g, dim);
    drawReport(g, dim);
    elapsed_time.draw(g, dim.width - 26, 22, 14);
  }

  public void step(Dimension dim)
  {
    for (Circle circ : circles)
      circ.step(dim.width, dim.height);
    for (Circle circ_a : circles)
      for (Circle circ_b : circles)
        if (circ_a != circ_b)
          if (circ_a.collide(circ_b))
          { circ_a.setOff();
            circ_b.setOff();
            collided += 2;
          }
  }

  public boolean gameEnded()
  {
    return elapsed_time.timeElapsed();
  }

  public void click(int x, int y)
  {
    for (Circle circ : circles)
      circ.poke(x, y);
  }
}

