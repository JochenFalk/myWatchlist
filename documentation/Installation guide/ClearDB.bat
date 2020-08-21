@echo off

title myWatchlist database cleaning tool

echo.
echo This action will clear the entire database! 
echo Please enter the password for the default account "postgres" if you wish to continue.
echo.

"C:\Program Files\PostgreSQL\12\bin\psql.exe" -U postgres -f "%~dp0Resources\ClearDB.sql"

echo.
echo Database cleared!
echo.

pause
