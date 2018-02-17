package de.felixbrandt.autoscale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GearmanQueueLengthSensor implements Sensor
{
  private static final Logger LOGGER = LogManager.getLogger();

  Socket socket;
  PrintWriter out;
  BufferedReader in;

  String gearman_host;
  int gearman_port;
  String gearman_queue;
  int max_reconnect;

  public GearmanQueueLengthSensor(String host, int port, String queue)
  {
    gearman_host = host;
    gearman_port = port;
    gearman_queue = queue;
    max_reconnect = 10;

    connect(gearman_host, gearman_port);
  }

  private void connect (String server, int port)
  {
    try {
      socket = new Socket(server, port);
      out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  public int getValue ()
  {
    for (int reconnect = 0; reconnect < max_reconnect; reconnect++) {
      try {
        if (out == null) {
          throw new IOException("not connected");
        }

        out.println("status");

        String line = in.readLine();
        while (!line.equals(".")) {
          if (line.matches(gearman_queue + "(.*)")) {
            return Integer.parseInt(line.split("\t")[1]);
          }
          line = in.readLine();
        }

        // queue does not exist (so it has 0 length)
        return 0;
      } catch (IOException e) {
        LOGGER.warn("Could not reach Gearman, reconnecting ... ");
        connect(gearman_host, gearman_port);
      }
    }

    // we could not connect to job queue, so assume nothing has to be done
    return 0;
  }

}
