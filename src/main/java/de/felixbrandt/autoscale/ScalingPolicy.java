package de.felixbrandt.autoscale;

public class ScalingPolicy
{
  private Sensor demand;
  private InstanceManager capacity;
  private int factor;
  private int start_size;
  private int max_size;
  private int step;

  public ScalingPolicy(Sensor _demand, InstanceManager _capacity, int _start_size, int _factor,
          int _max_size, int _step)
  {
    demand = _demand;
    capacity = _capacity;
    factor = _factor;
    start_size = _start_size;
    max_size = _max_size;
    step = _step;
  }

  public void check ()
  {
    int demand_value = demand.getValue();
    int capacity_value = capacity.size();

    if (demand_value > 0 && capacity_value == 0) {
      capacity.start(start_size);
    } else {
      if (demand_value > factor * capacity_value && capacity_value < max_size) {
        capacity.start(step);
      }
    }
  }
}
