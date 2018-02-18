package de.felixbrandt.autoscale;

/**
 * Policy when to start new worker instances based on a sensor value.
 */
public class ScalingPolicy
{
  private Sensor demand_sensor;
  private InstanceManager capacity_manager;
  private int demand_factor;
  private int capacity_start_size;
  private int capacity_max_size;
  private int capacity_step;

  public ScalingPolicy(final Sensor demand, final InstanceManager capacity,
          final int start_size, final int factor, final int max_size,
          final int step)
  {
    demand_sensor = demand;
    capacity_manager = capacity;
    demand_factor = factor;
    capacity_start_size = start_size;
    capacity_max_size = max_size;
    capacity_step = step;
  }

  public final void check ()
  {
    final int demand_value = demand_sensor.getValue();
    final int capacity_value = capacity_manager.size();

    if (demand_value > 0 && capacity_value == 0) {
      capacity_manager.start(capacity_start_size);
    } else {
      if (demand_value > demand_factor * capacity_value
              && capacity_value < capacity_max_size) {
        capacity_manager.start(capacity_step);
      }
    }
  }
}
