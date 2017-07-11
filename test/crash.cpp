#include <iostream>

int main (int args, char** argv)
{
  int* ptr = NULL;

  if (args == 1) {
    // crash program intentionally
    *ptr = 5;
  }

  std::cout << 1 << std::endl;

  return 0;
}
