#include "Exp.h"

int main() {
  Foo f = {1, 1.0f};
  Foo fs[] = {{1, 1.0f}, {2, 2.0f}};
  Bar(1, 1.0f);
  Foo foos[] = {f, f};
  Baz bzz(&foos[0]);
  Baz other(foos);
  return (&foos[0] != foos);

  //  Baz baz1; // this expects a xtor that can be called with no args.
  
  Link l1 = NULL;
  Link l2(2, l1);
  l1(2, l2);
}
