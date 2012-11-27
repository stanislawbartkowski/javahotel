/*
 Copyright 2012 <copyright holder> <email>

 Licensed under the Apache License, Version 2.0 (the "License
 );
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

#include <tchar.h>
#include <stdio.h>

#include <iostream>

#include "hadrmodel.h"

namespace {
	void P(std::string s) {
		std::cout << s.c_str() << std::endl;
	}

	void textInterface(hadrmodel &ha) {
		while (1) {
			P("=======================");
			P(ha.getConnectionStatus());
			P("1) Connect");
			P("2) Disconnect");
			P("3) Get list of persons");
			P("4) Add person");
			P("5) Create table PERSONS");
			P("6) Drop table PERSONS");
			P("7) Switch on autommit");
			P("8) Switch off autommit");
			P("99) Exit");
			int iChoice;
			std::cout << "Enter:";
			std::cin >> iChoice;
			bool action = true;
			switch (iChoice) {
			case 99:
				return;
			case 1:
				ha.connect();
				break;
			case 2:
				ha.disconnect();
				break;
			case 3: {
					std::vector<person>pList = ha.getListOfPersons();
					std::vector<person>::iterator i = pList.begin();
					for (; i != pList.end(); i++) {
						std::cout << "id:" << i->id << " name: " <<
							i->name.c_str()
							<< " family name:" << i->familyname.c_str()
							<< std::endl;
					}
				} break;
			case 4: {
					std::string name;
					std::string familyName;
					char temp[100];
					std::cout << "Name: ";
					std::cin >> temp;
					name = temp;
					std::cout << "Family name: ";
					std::cin >> temp;
					familyName = temp;
					ha.addPerson(name, familyName);
				} break;
			case 5:
				ha.createPersons();
				break;
			case 6:
				ha.dropPersons();
				break;
			case 7:
			case 8:
				ha.autoCommit(iChoice == 7 ? true : false);
				break;
			default:
				action = false;
				break;
			} // switch
			if (!action) {
				P("Invalid action.");
			}
			else {
				P(ha.getLastActionStatus());
			}
		} // while
	}

} // namespace

// int main(int argc, char **argv) {
int _tmain(int argc, _TCHAR* argv[]) {
	P("Hello, world!");
	hadrmodel ha;
	textInterface(ha);
	P("I'm done");
}
