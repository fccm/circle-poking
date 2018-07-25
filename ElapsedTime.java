/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */
import java.awt.*;

public class ElapsedTime
{
  long t0;
  float end_time;  // in seconds
  final BasicStroke stroke = new BasicStroke(2.0f);

  public ElapsedTime() { init(0f); }
  public ElapsedTime(float end_time) { init(end_time); }

  public void init(float end_time)
  {
    t0 = System.currentTimeMillis();
    this.end_time = end_time;
  }

  public void draw(Graphics2D g, int x, int y, int radius)
  {
    long ms = System.currentTimeMillis() - t0;
    float c = 360f * (float)ms / (end_time * 1000f);
    int countdown = (int)end_time - ((int)ms / 1000);
    g.setColor(Color.black);
    g.setStroke(stroke);
    g.drawArc(
        x - radius,
        y - radius,
        2 * radius,
        2 * radius,
        0, (int)c);
    g.setFont(new Font("SansSerif", Font.PLAIN, 12));
    g.drawString("" + countdown, x-6, y+4);
  }

  public boolean timeElapsed()
  {
    long ms = System.currentTimeMillis() - t0;
    return ((float)ms > (end_time * 1000f));
  }
}
