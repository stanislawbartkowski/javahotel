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

/*
 * Common database routines declaration related to database
*/

#ifndef UTILLIB_HEADER
#define UTILLIB_HEADER


/*
 * Connects to database
 * Connection string inside procedure bode
 * If cannot connect then exists
*/
void ConnectTo();

/*
 * Disconnect from database
*/
void Disconnect();

struct retinfo {
  int keyid;
  int no;
} retinfo;  
  


/*
 * Calculates sum for keyid
 * @param keyid 
 * @return 0 if error, 1 if ok and *sum contains sum retrieved
*/
int  CalculateSum(float *sum,int keyid,struct retinfo *ret);


/*
 * Invokes SP
 * @par s : string containing the number
 * @par res : return value, integer values for s
 * @return 0 if error, 1 if ok 
*/
int CallProc(int *res,char *s);

/*
 * Invokes UDF
 * @par par : value
 * @par res : return value from UDF, the same as par
 * @return 0 if error, 1 if ok 
*/
int CallFunc(int *res, int par);

#endif
