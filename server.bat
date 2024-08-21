@echo off
setlocal

rem Function to check if the server is running
:check_server_running
tasklist /FI "IMAGENAME eq java.exe" 2>NUL | find /I "server.jar nogui" > NUL
if %ERRORLEVEL% == 0 (
    goto :server_running
) else (
    goto :server_not_running
)

:server_running
echo Server is already running. Killing existing instance.
goto :kill_server_instances

:server_not_running
goto :continue

rem Function to kill all instances of the server
:kill_server_instances
taskkill /F /IM java.exe /FI "WINDOWTITLE eq server.jar nogui"
goto :continue

:continue
rem Copy the new jar file and start the server
copy target\TwoWorlds-1.1.jar server\plugins
if %ERRORLEVEL% neq 0 exit /B 1

cd server || exit /B 1
java -Xms8192M -Xmx8192M -jar server.jar nogui

echo Press any key to continue...
pause >nul

endlocal
