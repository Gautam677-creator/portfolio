# Push to GitHub and deploy on Render

## 1. Push the code to GitHub

Repository: `https://github.com/Gautam677-creator/portfolio.git` (default branch `main`, currently empty).

From PowerShell, run:

```powershell
cd "C:\Users\Lakshya\Documents\Default Project"
git init -b main
git add .
git commit -m "Add Spring Boot backend and single-container portfolio"
git remote add origin https://github.com/Gautam677-creator/portfolio.git
git push -u origin main
```

Git will ask you to sign in to GitHub. Use browser sign-in (Git Credential Manager) or a Personal Access Token with `repo` scope when it asks for a password.

If `git` is not installed:

```powershell
winget install --id Git.Git -e --accept-source-agreements --accept-package-agreements
# close and reopen PowerShell, then rerun the commands above
```

Alternatively run the included helper: `.\push-to-github.ps1`.

If you already pushed once and just need to send newer changes:

```powershell
cd "C:\Users\Lakshya\Documents\Default Project"
git add .
git commit -m "Update portfolio"
git push
```

## 2. Deploy on Render

The repo contains a `render.yaml` Blueprint, so Render builds and configures everything automatically.

1. Sign in at <https://dashboard.render.com>.
2. Click **New → Blueprint Instance**.
3. Connect your GitHub account and select `Gautam677-creator/portfolio`.
4. Render detects `render.yaml` and proposes one service: `gautam-portfolio` (Docker, free plan, Singapore region, health check `/actuator/health`).
5. Click **Apply** / **Create resources**.
6. Wait for the first build (usually 3–6 minutes). Watch the logs if it seems stuck.
7. Render gives you a URL such as `https://gautam-portfolio.onrender.com`. Open it.

### If "Blueprint Instance" is unavailable

Use **New → Web Service** instead:

- Repository: `Gautam677-creator/portfolio`
- Runtime: **Docker** (Render detects the root `Dockerfile`)
- Plan: **Free**
- Region: **Singapore** (or any region you prefer)
- Health Check Path: `/actuator/health`
- Environment variable: `SPRING_PROFILES_ACTIVE = prod`

## 3. Verify the deployment

```powershell
$url = "https://gautam-portfolio.onrender.com"   # replace with your URL
Invoke-RestMethod "$url/actuator/health"
Invoke-RestMethod "$url/api/v1/gautam/profile"
Invoke-RestMethod -Method Post "$url/api/v1/gautam/contact" -ContentType "application/json" -Body '{"name":"Test","email":"test@example.com","subject":"Hello","message":"Checking the API"}'
```

Then open the site and try the **Ping API** button and the contact form.

## 4. Important notes

- Render's free plan spins a service down after about 15 minutes of inactivity. The first request after that can take up to a minute while the container wakes up. The frontend has a 15–20 second timeout and falls back to a clearly-labelled "demo mode" rather than hanging.
- Contact messages are stored in an embedded in-memory H2 database and written to the application log. They do not survive a redeploy. To keep them permanently, add a managed Postgres database and set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` in Render's environment (plus the PostgreSQL driver in `pom.xml`).
- To change region or plan, edit `render.yaml`, commit, and push — Render redeploys automatically.
- `.github/workflows/ci.yml` builds and tests the backend on every push to `main`.
