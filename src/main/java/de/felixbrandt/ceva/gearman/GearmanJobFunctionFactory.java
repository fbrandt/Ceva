package de.felixbrandt.ceva.gearman;

import org.gearman.worker.GearmanFunction;
import org.gearman.worker.GearmanFunctionFactory;

import de.felixbrandt.ceva.controller.base.Controller;

/**
 * Factory initializing GearmanJobFunction (used inside Gearman worker)
 */
public class GearmanJobFunctionFactory implements GearmanFunctionFactory
{
  private final String queue_name;
  private final Controller controller;

  public GearmanJobFunctionFactory(final String _queue_name, final Controller _controller)
  {
    queue_name = _queue_name;
    controller = _controller;
  }

  public final GearmanFunction getFunction ()
  {
    return new GearmanJobFunction(queue_name, controller);
  }

  public final String getFunctionName ()
  {
    return queue_name;
  }

}
