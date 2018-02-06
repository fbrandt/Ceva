package de.felixbrandt.autoscale;

public class ScalingPolicy
{
  private Sensor demand;
  private InstanceManager capacity;
  private int max_size;

  public ScalingPolicy(Sensor _demand, InstanceManager _capacity, int _max_size)
  {
    demand = _demand;
    capacity = _capacity;
    max_size = _max_size;
  }

  public void check ()
  {
    int demand_value = demand.getValue();
    int capacity_value = capacity.size();

    if (demand_value > capacity_value && capacity_value < max_size) {
      capacity.start(1);
    }
  }
}
