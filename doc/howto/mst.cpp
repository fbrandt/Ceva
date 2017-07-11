#include <iostream>
#include <vector>
#include <algorithm>

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
  std::cout << n_nodes << std::endl;
  for (int i = 0; i < n_edges; ++i) {
    int from_set = find(edges[i].from);
    int to_set = find(edges[i].to);
    std::cerr << "[STDERR] checking edge " << edges[i].from <<
        " " << edges[i].to << " " << edges[i].cost << " ";
    if (from_set != to_set) {
      std::cerr << "using edge" << std::endl;
      node_set[from_set] = to_set;    
      std::cout << edges[i].from << " " << edges[i].to << " " << edges[i].cost << std::endl;
    } else {
      std::cerr << "skipping edge" << std::endl;
    }
  }
}

int main (int args, char** argv)
{
  int n_nodes, n_edges;
  Edges edges;
  
  std::cin >> n_nodes;
  init(n_nodes);

  do {
    Edge e;
    std::cin >> e.from >> e.to >> e.cost;
    edges.push_back(e);
  } while (!std::cin.eof());
  std::sort(edges.begin(), edges.end());

  if (args <= 1) {
    calcMST();
  } else {
    
  }
  
  return 0;
}
