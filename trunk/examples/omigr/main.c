/*
    Copyright 2015 stanislawbartkowski@fmail.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

#include "utillib.h"

void main() {
  
  
  ConnectTo();

  {
    float sum;
    struct retinfo ret;
    
    if (CalculateSum(&sum,10,&ret)) {
      printf("Calculated sum : %f\n",sum);
      printf("Id: %u no : %u \n",ret.keyid,ret.no);
    }
    else {
       printf("Something wrong with sum\n");
       Disconnect();
       exit(1);
    }
  }  
  
  {
      int res;
      if (CallProc(&res,"234")) printf("CallProc res=%u\n",res);
      else {
       printf("Something wrong with CallProc\n");
       Disconnect();
       exit(1);
    }
  }  
  
  {
      int res;
      
      if (CallFunc(&res,55)) printf("CallFunc res=%u\n",res);
      else {
	printf("Something wrong with CallFunc\n");
	Disconnect();
	exit(1);
      }
  }
  
  Disconnect();
  printf("\n");
}