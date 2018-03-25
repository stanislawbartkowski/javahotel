//============================================================================
// Name        : findnumber.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <cstdint>
#include <math.h>
using namespace std;

typedef uint32_t uint;


const uint N = 1200; // upper limit
const uint MISSING = 789; // missing number
const int BN = 32; // number if bits

const uint MISSING1 = 333; // two numbers missing, the first one
const uint MISSING2 = 1001; // the second missing

class ByteCounter {
public:
	ByteCounter() {
		for (int i=0; i<BN; i++) one_sum[i] = 0;
	}
	// break down the number to bits and update the 1/0 counter
	void AddNumber(uint number) {
		for (int i=0; i<BN; i++) {
			if (number & 0x1 == 1) one_sum[i]++;
			// shift the number one position right to be ready to extract next bit
			number >>= 1;
		}
	}
	// reconstruct the single number
	uint ReconstructMissing(ByteCounter &all) const {
		uint missing = 0;
		for (int i=BN-1; i>=0; i--) {
			missing <<= 1;
			// only 1 matter
			if (one_sum[i] != all.one_sum[i]) missing += 1;
		}
		return missing;
	}
	// reconstruct sum of two missing numbers
	uint ReconstructMissingSum(ByteCounter &all) const {
		uint missing1 = 0;
		uint missing2 = 0;
		for (int i=BN-1; i>=0; i--) {
			missing1 <<= 1;
			missing2 <<= 1;
			if (all.one_sum[i] != one_sum[i]) missing1 += 1;
			// Sum have two ones at the position. Move the second 1 to the second number.
			// We are calculating the sum, the order does not matter
			if (all.one_sum[i] - one_sum[i] == 2) missing2 += 1;
		}
		// it is not the reconstruction of numbers but the sum only.
		return missing1 + missing2;
	}
private:
	// bits are put in the order from right to left
	// [0] - the most right bit
	// [1] - the bit one position left to the most right
	// [BN-1] - the most left bit
	// sum of 1 in the binary representation
	int one_sum[BN];
};

// Resolve quadratic equation using well known calculation
int ResolveSquareEquation(int a, int b, int c) {
	int delta = (b*b) - 4*a*c;
	// return one solution
	return (-b + sqrt(delta))/(2*a);
}

int main() {
	cout << "!!!Hello World, find missing number !!!" << endl; // prints !!!Hello World!!!
	ByteCounter all_number, miss_number, miss2_number;
	ByteCounter all_squarenumber, miss2_squarenumber;
	for (uint i=0; i < N; i++) {
		all_number.AddNumber(i);
		if (i != MISSING) miss_number.AddNumber(i);
		if (i != MISSING1 && i != MISSING2) miss2_number.AddNumber(i);
	}
	uint sum2 =  miss2_number.ReconstructMissingSum(all_number);
	cout << "Missing:" << miss_number.ReconstructMissing(all_number) << endl;
	cout << "Missing sum 2:" << sum2 << endl;

	for (uint i=0; i < N; i++) {
		all_squarenumber.AddNumber(i*i);
		if (i != MISSING1 && i != MISSING2) miss2_squarenumber.AddNumber(i*i);
	}
	uint sum2square = miss2_squarenumber.ReconstructMissingSum(all_squarenumber);
	cout << "Missing square sum:" << sum2square << endl;
	int a = 2;
	int b = -2 * sum2;
	int c = (sum2 * sum2) - sum2square;
	int x = ResolveSquareEquation(a,b,c);

	cout << "x=" << x << " y=" << (sum2 - x) << endl;

	return 0;
}
