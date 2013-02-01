struct Foo {
  int a;
  float b;
};

struct Bar {
  int a;
  float b;

Bar(int _a, float _b) : a(_a), b(_b) {}
};

struct Baz {
  Foo* fooze;
Baz(struct Foo* _foosz) : fooze(_foosz) {}
};

struct Link {
  int data;
  Link* other;

Link(int _data, Link& _other) :
  data(_data),
    other(_other) {}
};
