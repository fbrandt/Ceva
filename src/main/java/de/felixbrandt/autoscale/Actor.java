package de.felixbrandt.autoscale;

public interface Actor
{
  int getScale ();

  boolean scaleUp (int step);

  boolean scaleDown (int step);
}
