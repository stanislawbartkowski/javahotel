//============================================================================
// Name        : cracking circus.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <cctype>
#include <vector>

using namespace std;


class Athlete {
friend class AthleteGraph;
public:
	Athlete(int weight, int width) : weight(weight),width(width) {}
	bool below(Athlete &p) const {
		 return (weight < p.weight && width < p.width);
	}
private:
	const int weight, width;
};


static int numb = 0;


class AthleteGraph {
public:
	AthleteGraph(): graph(nullptr) {}
	void addNode(int weight, int width) {
		  Athlete pa = Athlete (weight,width);
		  // create & copy operator
		  nodes.push_back(pa);
	}

	void createGraph() {
		// initialize graph as matrix
		graph = new bool[number()*number()];
		// initialize, node nodes
		for (int i=number()-1; i>=0; i--) {
			for (int j=0; j<number(); j++) graph[elem(i,j)] = false;
		}
		// build graph
		for (int i=0; i<number(); i++)
			for (int j=0; j<number(); j++)
				// (i,j) - nodes from i to j
				if (nodes[i].below(nodes[j])) graph[elem(i,j)] = true;
	}
	// for testing only
	void print() const {
		for (int i=0; i<number(); i++)
			for (int j=0; j<number(); j++)
				if (graph[elem(i,j)]) cout << i << "->" << j << endl;
	}

	void findLongest() const {
		// two dimensional array
		//longest[i] - the longest tower with the base i
		// important: created in opposite order
		vector<int> **longest = new vector<int>*[number()];
		// initialize
		for (int i=0; i<number(); i++) {
			longest[i] = nullptr;
		}
		// calculate longest tower for all nodes
		for (int i=0; i<number(); i++)
			calcLongest(i,longest);

		// print the result
		int m = 0;
	    for (int i=1; i<number(); i++) {
//			cout << i << ":" << longest[i] << endl;

			if (longest[i]->size() > longest[m]->size()) m = i;
		}
		// cout << "Longest:" << m << endl;
		vector<int> *lo = longest[m];
		// print in opposite order
		cout << "Longest tower :" << lo->size() << endl;
		for (int i=lo->size()-1; i>=0; i--) {
			int kk = lo->at(i);
			cout << nodes[kk].weight << " " << nodes[kk].width << endl;

//			cout << kk << endl;
		}
	}
private:
	// list of athletes, nodes in the graph
	vector<Athlete> nodes;
	// two dimensional table
	// (i,j) = true, direct node from i->j
	// means that athlete i can stand below athlete j
	// creates graph from list of nodes
	bool *graph;
	// number of athletes
	int number() const {
		return nodes.size();
	}
	// return an index of (i,j) node
	int elem(int i,int j) const {
		return i*number()+j;
	}
	// calculate the longest tower with base 'node' and updates longest[node] row
	// node: index in nodes vector
	// if longest[node] not null, longest[node] calculated already
	// returning longest[node] should be not nullptr
	// important: longest[node] in opposite order, the lightest at the beginning
	void calcLongest(int node, vector<int> **longest) const {
		// longest for node calculated already ?
		if (longest[node] != nullptr) return;
		int m = -1;
		// find the longest tower for nodes adjacent
		for (int i=0; i<number(); i++)
			if (graph[elem(node,i)]) {
				numb++;
				// calculate the longest tower for j
				calcLongest(i,longest);
//				assert(longest[i] != nullptr);
				if (m == -1) m = i;
				else
					if (longest[m]->size() < longest[i]->size()) m = i;
			}
	  //longest[m], the longest subtower
	  longest[node] = new vector<int>();
      if (m != -1) *longest[node] = *longest[m];
      // the heaviest at the end
	  longest[node]->push_back(node);
	}

};


int main() {
	cout << "!!!Hello World, welcome circus!!!" << endl; // prints !!!Hello World!!!
	AthleteGraph pg;
	// create test data
	pg.addNode(65,100);
	pg.addNode(70,150);
	pg.addNode(56,90);
	pg.addNode(75,190);
	pg.addNode(60,95);
	pg.addNode(68,110);
	// prepare graph
	pg.createGraph();
//	pg.print();
	// unleash the hell
	pg.findLongest();
	cout << numb << endl;
}
