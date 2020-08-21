@echo off

title myWatchlist database setup tool

echo.
echo Welcome to myWatchlist database setup. 
echo Please enter the password created during installation of pgAdmin
echo.

"C:\Program Files\PostgreSQL\12\bin\psql.exe" -U postgres -f "%~dp0Resources\InitialiseDB.sql"

echo.
echo Setup complete!
echo.

pause