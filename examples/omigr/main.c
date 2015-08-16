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

#include "stdlib.h"
#include "stdio.h"

#include "utillib.h"


int main() {
  
  
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
       return 1;
    }
  }  
  
  {
      int res;
      if (CallProc(&res,"234")) printf("CallProc res=%u\n",res);
      else {
       printf("Something wrong with CallProc\n");
       Disconnect();
       return 1;
    }
  }  
  
  {
      int res;
      
      if (CallFunc(&res,55)) printf("CallFunc res=%u\n",res);
      else {
	printf("Something wrong with CallFunc\n");
	Disconnect();
	return 1;
      }
  }

  {
     char *filename = "../main.c";

     if (writeBlob(filename)) printf("Blob file %s written\n",filename);
     else  {
        printf("Something wrong with writing blob \n");
	Disconnect();
	return 1;
     }
  }
  
  {
     char *filename = "/tmp/b.txt";

     if (readBlob(filename)) printf("Blob read, check file %s\n",filename);
     else  {
        printf("Something wrong with reading blob \n");
	Disconnect();
	return 1;
     }
          
  }
  
  { 
      struct personal_data_struct data;
      
      data.id = 10;
      strcpy(data.name,"James Bond");
      if (addPersonalData(data)) printf("%s added\n",data.name);
      else {
        printf("Something wrong with adding personal data \n");
	Disconnect();
	return 1;
     }
  }
  
  {
    int all,nullno;
    
    if (calculatePersonalStat(&all, &nullno)) printf("All : %u, null: %u\n",all,nullno);
    else {
        printf("Something wrong with calculate stat \n");
	Disconnect();
	return 1;        
    }

  }
	 
      
   
  Disconnect();
  printf("\n");
  return 0;
}
