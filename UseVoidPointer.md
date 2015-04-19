## Top secret usage of void pointer ##

### secret.h ###

```
#include <string>

typedef void* secretpointer;

secretpointer newSecret();

void deleteSecret(secretpointer p);

std::string revealSecret(secretpointer p);
```

### secret.cpp ###

```
#include "secret.h"
#include <string>

namespace {

  typedef struct {
      std::string secret;
  } secret;

}


secretpointer newSecret() {
    return new secret;
}

void deleteSecret(secretpointer v) {
     secret *p = static_cast<secret *>(v);
     delete p;
}

std::string revealSecret(secretpointer v) {
     secret *p = static_cast<secret *>(v);
     p->secret = "top secret message";
     return p->secret;
}
```

### main.cpp ###

```
#include <iostream>
#include <cstdlib>
#include "secret.h"

using namespace std;

int main(int argc, char *argv[])
{
    secretpointer p = newSecret();
    cout << "Secret:" << revealSecret(p) << endl; 
    deleteSecret(p);
    return EXIT_SUCCESS;
}
```

## Partially secret class definition ##

### secret.h ###

```
#include <string>

class publicsecret {

public:
  publicsecret();
  ~publicsecret();
  std::string revealSecret();
  void setPublicMessage(const char *mess) {
     publicmess = mess;
  }
  const std::string getPublicMess() const {
     return publicmess;
   }
private:
  std::string publicmess;
  void *secretpointer;
};
```

### secret.cpp ###
```
 #include <string>
#include "secret.h"

namespace {

  typedef struct {
      std::string secretmess;
  } secret;

}

publicsecret::publicsecret() {
  secretpointer = new secret();
}

publicsecret::~ publicsecret() {
     secret *p = static_cast<secret *>(secretpointer);
     delete p;
}

std::string publicsecret::revealSecret() {
     secret *p = static_cast<secret *>(secretpointer);
     p->secretmess = "top secret message";
     return p->secretmess;
}
```

### main.cpp ###

```
#include <iostream>
#include <cstdlib>
#include "secret.h"

using namespace std;

int main(int argc, char *argv[])
{
  cout << "Hello, world!" << endl;
  publicsecret p;
  p.setPublicMessage("Public info");
  cout << "Public:" << p.getPublicMess() << endl;
  cout << "Secret:" << p.revealSecret() << endl; 

  return EXIT_SUCCESS;
}
```