#include <iostream>

#include "hadrmodel.h"

namespace {
void P(std::string s) {
    std::cout << s << std::endl;
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
        case 3:
        {
            std::vector<person> pList = ha.getListOfPersons();
            std::vector<person>::iterator i = pList.begin();
            for (; i != pList.end(); i++) {
                std::cout<< "id:" << i->id << " name: " << i->name << " family name:" << i->familyname << std::endl;
            }
        }
        break;
        case 4:
        {
            std::string name;
            std::string familyName;
            std::cout << "Name: ";
            std::cin >> name;
            std::cout << "Family name: ";
            std::cin >> familyName;
            ha.addPerson(name,familyName);
        }
        break;
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

int main(int argc, char **argv) {
    P("Hello, world!");
    hadrmodel ha;
    textInterface(ha);
    P("I'm done");
}
