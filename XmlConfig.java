/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */

public class XmlConfig
{
  private static final String conf_string =
  "<?xml version='1.0'?>\n" +
  "<circle_poking>\n" +
  "  <window\n" +
  "     width='640'\n" +
  "     height='480' />\n" +
  "\n" +
  "  <game_difficulty_levels>\n" +
  "\n" +
  "    <level difficulty='easy'>\n" +
  "      <time first_round='40' increase='10' />\n" +
  "      <circles number='6' increase='1' max_collided='4'>\n" +
  "        <radius_range min='3.0' max='5.0' />\n" +
  "        <speed_range min='0.01' max='0.03' />\n" +
  "      </circles>\n" +
  "    </level>\n" +
  "\n" +
  "    <level difficulty='normal'>\n" +
  "      <time first_round='60' increase='20' />\n" +
  "      <circles number='10' increase='2' max_collided='2'>\n" +
  "        <radius_range min='2.0' max='6.0' />\n" +
  "        <speed_range min='0.01' max='0.04' />\n" +
  "      </circles>\n" +
  "    </level>\n" +
  "\n" +
  "    <level difficulty='hard'>\n" +
  "      <time first_round='90' increase='30' />\n" +
  "      <circles number='12' increase='4' max_collided='0'>\n" +
  "        <radius_range min='2.0' max='6.0' />\n" +
  "        <speed_range min='0.01' max='0.04' />\n" +
  "      </circles>\n" +
  "    </level>\n" +
  "\n" +
  "  </game_difficulty_levels>\n" +
  "</circle_poking>\n";

  public String getString() { return conf_string; }
}
