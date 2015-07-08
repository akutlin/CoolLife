cmake_minimum_required (VERSION 2.8)

add_subdirectory(firststep)

set(PROJNAME "coollife")
set(JARNAME ${PROJNAME})

find_package(Java REQUIRED)
include(cmake_patches/UseJavaCustom.cmake)

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")
set(CMAKE_JAVA_INCLUDE_PATH "${CMAKE_BINARY_DIR}/firststep/firststep-java.jar")

file(GLOB_RECURSE SOURCES_JAVA "${CMAKE_CURRENT_SOURCE_DIR}/src/java/*.java")

add_jar(${JARNAME} ${SOURCES_JAVA})


add_dependencies(${PROJNAME} firststep)