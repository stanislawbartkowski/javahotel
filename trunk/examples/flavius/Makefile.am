bin_PROGRAMS = projflav
projflav_SOURCES = projflav.cpp runflav.cpp

# set the include path found by configure
INCLUDES = -I/home/sb/boost/boost_1_40_0 $(all_includes)

# the library search path.
projflav_LDFLAGS = $(all_libraries) 
noinst_HEADERS = runflav.h test.h
projflav_LDADD = \
	/home/sb/boost/boost_1_40_0/stage/lib/libboost_unit_test_framework.a
