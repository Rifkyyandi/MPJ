@echo off

REM Set MPJ_HOME to the directory where MPJ Express is installed
set MPJ_HOME=D:\mpj-v0_44

REM Set the classpath to include the MPJ Express jar and the project's build directory
set CLASSPATH=%MPJ_HOME%\lib\mpj.jar;D:\Netbeans\Alpha\build\classes

REM Output debugging information
echo MPJ_HOME=%MPJ_HOME%
echo CLASSPATH=%CLASSPATH%
echo Running MPJ with file: %1

REM Record start time before starting the MPI process
set STARTTIME=%TIME%

REM Execute the MPJ Express runtime
call %MPJ_HOME%\bin\mpjrun.bat -np 4 -cp %CLASSPATH% Alpha %1

REM Record end time after MPI process completes
set ENDTIME=%TIME%

REM Calculate execution time
call :GetMillis STARTMS "%STARTTIME%"
call :GetMillis ENDMS "%ENDTIME%"
set /A DURATION=ENDMS - STARTMS

REM Output execution time
echo Total MPI process execution time: %DURATION% milliseconds

REM Output after process completes
echo MPI process completed.
pause

exit /b

:GetMillis
REM Function to extract milliseconds from TIME variable format (HH:MM:SS.CC)
set "TIMESTR=%~1"
for /f "tokens=1-4 delims=:.," %%a in ("%TIMESTR%") do (
  set /a "H=10%%a %% 100, M=10%%b %% 100, S=10%%c %% 100, C=10%%d %% 100"
)
set /a "RESULT=(((H*60+M)*60+S)*1000+C)"
set "%~2=%RESULT%"
exit /b
