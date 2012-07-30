project(hadrtest)

link_directories(/home/opt/ibm/db2/V10.1/lib64)
link_libraries(db2)
include_directories(/home/opt/ibm/db2/dsdriver/include)

add_executable(hadrtest hadrmodel.cpp main.cpp)

