/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */

public class CircleFactory
{
  private float radius_min;
  private float radius_max;

  private float speed_min;
  private float speed_max;

  public void setRadiusRange(float radius_min, float radius_max)
  {
    this.radius_min = radius_min;
    this.radius_max = radius_max;
  }

  public void setSpeedRange(float speed_min, float speed_max)
  {
    this.speed_min = speed_min;
    this.speed_max = speed_max;
  }

  public Circle newCircle(int width, int height)
  {
    Circle circ = new Circle();
    initCircle(circ, width, height);
    return circ;
  }

  public void initCircle(Circle circ, int width, int height)
  {
    // ratio to create values proportional to the size of the window
    float ratio = (width + height) / 2f * 0.01f;

    // radius
    float radius = Util.rand(radius_min * ratio, radius_max * ratio);
    circ.setRadius(radius);

    // position
    float x = Util.rand(radius, width - radius);
    float y = Util.rand(radius, height - radius);
    circ.setXY(x, y);

    // direction
    float speed = Util.rand(speed_min * ratio, speed_max * ratio);
    double direction = Util.rand(0.0, 2.0 * Math.PI);
    float dx = speed * (float) Math.cos(direction);
    float dy = speed * (float) Math.sin(direction);
    circ.setDir(dx, dy);
  }
}
