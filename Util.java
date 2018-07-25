/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */

public class Util
{
  public static float rand(float bound)
  { return bound * (float) Math.random();
  }
  public static float rand(float bound_down, float bound_up)
  { float m = bound_up - bound_down;
    return bound_down + m * (float) Math.random();
  }
  public static int rand(int bound_down, int bound_up)
  { int m = bound_up - bound_down;
    return bound_down + Math.round((float)m * (float)Math.random());
  }
  public static int rand(int bound)
  { return Math.round((float)bound * (float)Math.random());
  }
  public static float rand(double bound)
  { return rand((float) bound);
  }
  public static float rand(double bound_down, double bound_up)
  { return rand((float) bound_down, (float) bound_up);
  }
}
