/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */
import java.awt.*;
import java.awt.geom.*;

public class Circle
{
  private float x;
  private float y;
  private float dx;
  private float dy;
  private float radius;
  private boolean off;
  enum CircColor { Red, Green, Blue };
  private CircColor color;

  public Circle()
  { init(); }

  public void init()
  {
    off = false;
    switch (Util.rand(2))
    {
      case 0: color = CircColor.Red; break;
      case 1: color = CircColor.Green; break;
      case 2: color = CircColor.Blue; break;
    }
  }

  // Getters:
  public float getX() { return x; }
  public float getY() { return y; }
  public float getRadius() { return radius; }
  public boolean isOff() { return off; }
  public CircColor getColor() { return color; }

  // Setters:
  public void setRadius(float radius) { this.radius = radius; }
  public void setOff(boolean off) { this.off = off; }
  public void setOff() { off = true; }
  public void setOn() { off = false; }
  public void setXY(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  public void setDir(float dx, float dy)
  {
    this.dx = dx;
    this.dy = dy;
  }

  public void toggleColor()
  {
    switch (color)
    {
      case Red:
        color = CircColor.Green; break;
      case Green:
        color = CircColor.Blue; break;
      case Blue:
        color = CircColor.Red; break;
    }
  }

  public boolean isInside(float x, float y)
  {
    float _x = x - this.x;
    float _y = y - this.y;
    float dist = (float) Math.sqrt(_x * _x + _y * _y);
    return (dist < this.radius);
  }

  public boolean isInside(Circle c)
  {
    float _x = x - c.getX();
    float _y = y - c.getY();
    float dist = (float) Math.sqrt(_x * _x + _y * _y);
    return (dist < (radius - c.getRadius()));
  }

  public void poke(float x, float y)
  {
    if (off) return;
    if (isInside(x, y))
      toggleColor();
  }

  public boolean near(Circle c, float near_dist)
  {
    if (this.off || c.isOff()) return false;
    if (c.getColor() != this.color) return false;
    float _x = x - c.getX();
    float _y = y - c.getY();
    float inter_dist = radius + c.getRadius() + near_dist;
    float dist = (float) Math.sqrt(_x * _x + _y * _y);
    return (dist < inter_dist);
  }

  public boolean collide(Circle c)
  {
    return near(c, 0.0f);
  }

  public void step(int width, int height)
  {
    if (off) return;
    x += dx;
    y += dy;
    if ((x + radius) > width) dx = - Math.abs(dx);
    if ((y + radius) > height) dy = - Math.abs(dy);
    if ((x - radius) < 0) dx = Math.abs(dx);
    if ((y - radius) < 0) dy = Math.abs(dy);
  }

  public void draw(Graphics2D g, int width, int height)
  {
    switch (color)
    {
      case Red:   g.setColor(Color.red); break;
      case Green: g.setColor(Color.green); break;
      case Blue:  g.setColor(Color.cyan); break;
    }
    float size = (2 * radius);
    if (off)
      g.draw(new Ellipse2D.Float(
          (x - radius),
          (y - radius),
          size, size));
    else
      g.fill(new Ellipse2D.Float(
          (x - radius),
          (y - radius),
          size, size));
  }
}
