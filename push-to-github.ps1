$ErrorActionPreference = 'Stop'

$repoUrl = 'https://github.com/Gautam677-creator/portfolio.git'
Set-Location $PSScriptRoot

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    throw 'git was not found. Install it with: winget install --id Git.Git -e --accept-source-agreements --accept-package-agreements'
}

if (-not (Test-Path '.git')) {
    git init -b main
} else {
    Write-Host 'Existing git repository found.'
}

git add -A

if (git status --porcelain) {
    git commit -m 'Add Spring Boot backend and single-container portfolio'
} else {
    Write-Host 'No new changes to commit.'
}

$remotes = @(git remote)
if ($remotes -contains 'origin') {
    git remote set-url origin $repoUrl
} else {
    git remote add origin $repoUrl
}

git push -u origin main
Write-Host "Pushed to $repoUrl"
