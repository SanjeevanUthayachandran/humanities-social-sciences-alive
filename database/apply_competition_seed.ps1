param(
    [string]$DatabaseHost = "localhost",
    [int]$DatabasePort = 5432,
    [string]$DatabaseName = "hssa_db",
    [string]$DatabaseUser = "postgres"
)

$ErrorActionPreference = "Stop"
$psqlPath = "C:\Program Files\PostgreSQL\17\bin\psql.exe"
$seedPath = Join-Path $PSScriptRoot "13_competition_content_and_access.sql"

if (-not (Test-Path -LiteralPath $psqlPath)) {
    throw "PostgreSQL 17 psql was not found at $psqlPath"
}

if (-not $env:DB_PASSWORD) {
    throw "Set DB_PASSWORD in this PowerShell session before running this script."
}

$previousPgPassword = $env:PGPASSWORD
try {
    $env:PGPASSWORD = $env:DB_PASSWORD
    $psqlArguments = @(
        "--host", $DatabaseHost,
        "--port", $DatabasePort,
        "--username", $DatabaseUser,
        "--dbname", $DatabaseName,
        "--set", "ON_ERROR_STOP=1",
        "--file", $seedPath
    )
    & $psqlPath @psqlArguments

    if ($LASTEXITCODE -ne 0) {
        throw "PostgreSQL returned exit code $LASTEXITCODE"
    }
}
finally {
    $env:PGPASSWORD = $previousPgPassword
}
