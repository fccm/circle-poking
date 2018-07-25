/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */

class Level
{
  int num_circ = -1;
  int num_circ_incr = -1;
  int max_collided = -1;

  int first_round_time = -1;
  int incr_round_time = -1;

  float radius_min = -1.0f;
  float radius_max = -1.0f;

  float speed_min = -1.0f;
  float speed_max = -1.0f;

  String difficulty = "";

  // Getters
  public int numCircles(int round)
  { return (num_circ + round * num_circ_incr); }

  public int getRoundTime(int round)
  { return (first_round_time + round * incr_round_time); }

  public float minRadius() { return radius_min; }
  public float maxRadius() { return radius_max; }

  public float minSpeed() { return speed_min; }
  public float maxSpeed() { return speed_max; }

  public int maxCollided() { return max_collided; }
  public String getDifficulty() { return difficulty; }
}

