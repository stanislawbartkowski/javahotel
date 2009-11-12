bin_PROGRAMS = boosttest
boosttest_SOURCES = boosttest.cpp testcase1.cpp

# set the include path found by configure
INCLUDES = -I/home/sb/boost/boost_1_40_0 $(all_includes)

# the library search path.
boosttest_LDFLAGS = $(all_libraries) 
boosttest_LDADD = \
	/home/sb/boost/boost_1_40_0/stage/lib/libboost_test_exec_monitor.a
