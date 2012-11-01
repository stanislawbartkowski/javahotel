/*
 Copyright 2012 <copyright holder> <email>

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

#pragma once

#include <iostream>
#include <vector>

#include <Bde.DBTables.hpp>

class person {
	friend class GetPersons;

	person(int pid, const char *pname, const char *pfamilyname)
		: id(pid), name(pname), familyname(pfamilyname) {
	}

public:
	int id;

	std::string name;
	std::string familyname;
};

class hadrmodel {
	friend class ExecuteCommand;
	friend class DisconnectCommand;

	std::string lastActionStatus;

	TDatabase *database;
	bool autocommit;

public:
	hadrmodel() {
		database = NULL;
		autocommit = true;
	}
	void connect();
	void disconnect();
	void createPersons();
	void dropPersons();
	void autoCommit(bool on);
	std::vector<person>getListOfPersons();
	void addPerson(const std::string &name, const std::string &familyName);
	std::string getConnectionStatus() const ;

	std::string getLastActionStatus() const {
		return lastActionStatus;
	}

	TDatabase *T() const {
		return database;
	}
};
