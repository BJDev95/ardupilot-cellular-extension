#include "Exp2.h"

Foo someSub() {
  Foo foo("hej");
  return foo;
}

Foo anotherWay() {
  // This is stack allocated! Shit...
  char gedefims[] = {'h','e','y','\0'};
  Foo foo(gedefims);
  // This works OK.
  foo.print();
  return foo;
}

Bar anotherBar() {
  char gedefims[] = {'b','a','r','\0'};
  Bar bar(gedefims);
  return bar;
}

void someSub(Foo foo) {
  foo.print();
}

int main() {
  // Foo foo = anotherWay();
  // foo's pointer now points to something on the stack that is not there any more.
  // foo.print();

  //  Bar bar = anotherBar();
  Bar bar = Bar("Hundeprute");
  bar.print();
}
