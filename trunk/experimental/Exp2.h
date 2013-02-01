#include <stdio.h>

class Foo {
  char* _string;

 public:
  Foo (char* string) : _string(string) {}

  void print() {
    printf(">%s<\n", _string);
  }
};


class Bar {
  char* _string;

 public:
 Bar(char string[]) : _string(string) {}
  void print() {
    printf(">%s<\n", _string);
  }
};
