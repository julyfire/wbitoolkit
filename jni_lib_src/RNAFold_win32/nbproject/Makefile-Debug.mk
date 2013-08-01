#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=Cygwin_4.x-Windows
CND_CONF=Debug
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/_ext/1239759557/mfefold.o


# C Compiler Flags
CFLAGS=-shared -fPIC -lm

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=-L.

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-Debug.mk dist/librnafold.dll

dist/librnafold.dll: ${OBJECTFILES}
	${MKDIR} -p dist
	${LINK.c} -shared -o dist/librnafold.dll ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/_ext/1239759557/mfefold.o: ../RNAFold2/mfefold.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1239759557
	${RM} $@.d
	$(COMPILE.c) -g -I../../../../../Program\ Files/Java/jdk1.6.0_23/include -I../../../../../Program\ Files/Java/jdk1.6.0_23/include/win32  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1239759557/mfefold.o ../RNAFold2/mfefold.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/Debug
	${RM} dist/librnafold.dll

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
