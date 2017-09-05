#include <iostream>
#include <vector>
#include <algorithm>
#include <string.h>

struct Edge
{
  int from;
  int to;
  int cost;

  bool operator< (const Edge& other)
  {
    return cost < other.cost;
  }
};
typedef std::vector<Edge> Edges;

int* node_set = NULL;
void init (int n)
{
  node_set = new int[n];
  for (int i = 0; i < n; ++i) {
    node_set[i] = i;
  }
}

int find (int node)
{
  while (node != node_set[node]) {
    node = node_set[node];
  }

  return node;
}

void calcMST (const Edges& edges)
{
  for (Edges::const_iterator e = edges.begin(); e != edges.end(); ++e) {
    int from_set = find(e->from);
    int to_set = find(e->to);
    std::cerr << "[STDERR] checking edge " << e->from <<
        " " << e->to << " " << e->cost << " ";
    if (from_set != to_set) {
      std::cerr << "using edge" << std::endl;
      node_set[from_set] = to_set;    
      std::cout << e->from << " " << e->to << " " << e->cost << std::endl;
    } else {
      std::cerr << "skipping edge" << std::endl;
    }
  }
}

int main (int args, char** argv)
{
  int n_nodes = 0;
  Edges edges;
  
  std::cin >> n_nodes;
  init(n_nodes);

  while (true) {
    Edge e;
    std::cin >> e.from >> e.to >> e.cost;
    if (!std::cin.eof()) {
      edges.push_back(e);
    } else {
      break;
    }
  };

  std::sort(edges.begin(), edges.end());

  if (args <= 1) {
    std::cout << n_nodes << std::endl;
    calcMST(edges);
  } else {
    if (strcmp("nodes", argv[1]) == 0) {
      std::cout << n_nodes << std::endl;
    } else if (strcmp("edges", argv[1]) == 0) {
      std::cout << edges.size() << std::endl;
    } else if (strcmp("cost", argv[1]) == 0) {
      int total_cost = 0;
      for (Edges::const_iterator i = edges.begin(); i != edges.end(); ++i) {
        total_cost += i->cost;
      }
      std::cout << total_cost << std::endl;
    } else {
     std::cerr << "unknown command" << std::endl;
    }
  }
  
  return 0;
}

