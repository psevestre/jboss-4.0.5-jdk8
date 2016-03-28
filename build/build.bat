@echo off

REM  ======================================================================
REM
REM  This is the main entry point for the build system.
REM
REM  Users should be sure to execute this file rather than 'ant' to ensure
REM  the correct version is being used with the correct configuration.
REM
REM  ======================================================================
REM
REM $Id: build.bat 28057 2005-01-28 05:06:39Z starksm $
REM
REM Authors:
REM     Jason Dillon <jason@planet57.com>
REM     Sacha Labourey <sacha.labourey@cogito-info.ch>
REM

REM ******************************************************
REM Ignore the ANT_HOME variable: we want to use *our*
REM ANT version and associated JARs.
REM ******************************************************
REM Ignore the users classpath, cause it might mess
REM things up
REM ******************************************************

SETLOCAL

set CLASSPATH=
set ANT_HOME=

set ANT_OPTS=-Xmx256m -Dbuild.script=build.bat

REM ******************************************************
REM - "for" loops have been unrolled for compatibility
REM   with some WIN32 systems.
REM ******************************************************

set NAMES=tools;tools\ant;tools\apache\ant
set SUBFOLDERS=..;..\..;..\..\..;..\..\..\..

REM ******************************************************
REM ******************************************************

SET EXECUTED=FALSE
for %%i in (%NAMES%) do call :subLoop %%i %*

goto :EOF


REM ******************************************************
REM ********* Search for names in the subfolders *********
REM ******************************************************

:subLoop
SET SUBDIR=%1
SHIFT

set OTHER_ARGS=
:setupArgs
if %1a==a goto doneSetupArgs
set OTHER_ARGS=%OTHER_ARGS% %1
shift
goto setupArgs
:doneSetupArgs

for %%j in (%SUBFOLDERS%) do call :testIfExists %%j\%SUBDIR%\bin\ant.bat %OTHER_ARGS%

goto :EOF


REM ******************************************************
REM ************ Test if ANT Batch file exists ***********
REM ******************************************************

:testIfExists
if exist %1 call :BatchFound %*

goto :EOF


REM ******************************************************
REM ************** Batch file has been found *************
REM ******************************************************

:BatchFound
if (%EXECUTED%)==(FALSE) call :ExecuteBatch %*
set EXECUTED=TRUE

goto :EOF

REM ******************************************************
REM ************* Execute Batch file only once ***********
REM ******************************************************

:ExecuteBatch
echo Calling %*
call %*

:end

if "%NOPAUSE%" == "" pause

