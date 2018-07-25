/* Copyright (c) 2013 Florent Monnier

 This software is provided 'as-is', without any express or implied warranty.
 In no event will the authors be held liable for any damages arising from
 the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely. */
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import static org.w3c.dom.Node.*;
import java.io.*;
import java.util.Vector;
import java.io.Reader;
import org.xml.sax.InputSource;


/** Load configuration from a simple XML file. */
public class XmlConfLoader
{
  private int window_width = 640;
  private int window_height = 480;

  private Vector<Level> levels = new Vector<>();

  // Getters

  /** Window width requested in the conf file. */
  public int winWidth() { return window_width; }

  /** Window height requested in the conf file. */
  public int winHeight() { return window_height; }

  /** Number of levels defined in the conf file. */
  public int numLevels() { return levels.size(); }

  /** Get a level
      @param i between 0 and (numLevels() - 1) */
  public Level level(int i) { return levels.get(i); }

  private Document loadXMLFromString(String xml) throws Exception
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return builder.parse(is);
  }

  public XmlConfLoader(String conf_file)
  {
    DocumentBuilderFactory builderFactory =
      DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(false);
    builderFactory.setValidating(false);
    builderFactory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder = null;
    try {
      builder = builderFactory.newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      e.printStackTrace();
      System.exit(1);
    }
    File xmlFile = new File(conf_file);
    if (!xmlFile.exists())
    {
      System.err.println("conf file doesn't exists: " + xmlFile);
      System.exit(1);
    }
    Document xmlDoc = null;
    try {
      xmlDoc = builder.parse(xmlFile);
    }
    catch (SAXException | IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    /*
    try {
      //xmlDoc = builder.parse(
      //  new InputSource(new StringReader(
      //    XmlConfig.conf_string)));
      String conf_string = (new XmlConfig()).getString();
      xmlDoc = loadXMLFromString(conf_string);
      //ByteArrayInputStream input =
      //  new ByteArrayInputStream(
      //    XmlConfig.conf_string.getBytes());
      //xmlDoc = builder.parse(input);
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    */
    Element root = xmlDoc.getDocumentElement();
    NodeList children = root.getChildNodes();
    if (root.getNodeName() != "circle_poking")
    {
      System.err.println("conf file: wrong root element");
      System.exit(1);
    }
    for (int i = 0; i < children.getLength(); i++)
    {
      Node node = children.item(i);
      String name = node.getNodeName();
      short type = node.getNodeType();
      if (type == ELEMENT_NODE)
      {
        switch (name)
        {
          case "window":
            loadWindowSize(node);
            break;
          case "game_difficulty_levels":
            loadGameDiffLevels(node);
            break;
        }
      }
    }
  }

  private void loadGameDiffLevels(Node node)
  {
    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();
      short type = child.getNodeType();
      if (type == ELEMENT_NODE &&
          name == "level")
      {
        Level level = new Level();
        loadLevel(level, child);
        levels.add(level);
      }
    }
  }

  private void loadCircles(Level level, Node node)
  {
    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();
      short type = child.getNodeType();
      if (type == ELEMENT_NODE)
      {
        switch (name)
        {
          case "speed_range":
            if (node.hasAttributes())
            {
              NamedNodeMap attrs = child.getAttributes();
              Attr speed_min = (Attr) attrs.getNamedItem("min");
              Attr speed_max = (Attr) attrs.getNamedItem("max");
              level.speed_min = Float.valueOf(speed_min.getValue());
              level.speed_max = Float.valueOf(speed_max.getValue());
            }
            break;
          case "radius_range":
            if (node.hasAttributes())
            {
              NamedNodeMap attrs = child.getAttributes();
              Attr radius_min = (Attr) attrs.getNamedItem("min");
              Attr radius_max = (Attr) attrs.getNamedItem("max");
              level.radius_min = Float.valueOf(radius_min.getValue());
              level.radius_max = Float.valueOf(radius_max.getValue());
            }
            break;
        }
      }
    }
  }

  private void loadWindowSize(Node node)
  {
    if (node.hasAttributes())
    {
      NamedNodeMap attrs = node.getAttributes();
      Attr width = (Attr) attrs.getNamedItem("width");
      Attr height = (Attr) attrs.getNamedItem("height");
      window_width = Integer.valueOf(width.getValue());
      window_height = Integer.valueOf(height.getValue());
    }
  }

  private void loadLevel(Level level, Node node)
  {
    if (node.hasAttributes())
    {
      NamedNodeMap attrs = node.getAttributes();
      Attr diff = (Attr) attrs.getNamedItem("difficulty");
      level.difficulty = diff.getValue();
    }
    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();
      short type = child.getNodeType();
      switch (type)
      {
        case ELEMENT_NODE:
          switch (name)
          {
            case "time":
              if (node.hasAttributes())
              { NamedNodeMap attrs = child.getAttributes();
                Attr fst_rnd = (Attr) attrs.getNamedItem("first_round");
                Attr incr = (Attr) attrs.getNamedItem("increase");
                level.first_round_time = Integer.valueOf(fst_rnd.getValue());
                level.incr_round_time = Integer.valueOf(incr.getValue());
              }
              break;
            case "circles":
                NamedNodeMap attrs = child.getAttributes();
                Attr num = (Attr) attrs.getNamedItem("number");
                Attr incr = (Attr) attrs.getNamedItem("increase");
                Attr coll = (Attr) attrs.getNamedItem("max_collided");

                level.num_circ = Integer.valueOf(num.getValue());
                level.num_circ_incr = Integer.valueOf(incr.getValue());
                level.max_collided = Integer.valueOf(coll.getValue());

                loadCircles(level, child);
              break;
          }
          break;
        default: ; break;
      }
    }
  }
}
