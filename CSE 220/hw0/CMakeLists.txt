# cmake -S . -B build
# cmake --build build
# ./build/hw0_main

cmake_minimum_required(VERSION 3.10)
project(hw1 LANGUAGES C CXX)

set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 14)
add_library(common INTERFACE)
target_compile_options(common INTERFACE -gdwarf-4 -Wall -Wextra -Wshadow -Wpedantic -Wdouble-promotion -Wformat=2 -Wundef -Werror)

add_executable(hw0_main src/hw0_main.c)
target_link_libraries(hw0_main PRIVATE common)
