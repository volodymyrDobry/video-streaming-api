const URL = process.env.RATE_LIMIT_URL || "http://localhost:8084/api/v1/movies";
const TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ1YmstYWF6UWZXcjNuamNRazFVVG9ZN0dtZ0FrcUxCaVVFZ1dHXzVycmpRIn0.eyJleHAiOjE3NzYyNDk5MjUsImlhdCI6MTc3NjI0OTYyNSwianRpIjoidHJydGNjOmE3YmVlZDE0LTQ2MTctNGQzYi04ZjMzLWExMzYwZjVjYTQ2YyIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MS9yZWFsbXMvdmlvcmEtZGV2IiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6IjZjYjhjM2U2LTM1YmYtNGViMy05OGE3LTNjYWNhNzBkM2ZlMyIsInR5cCI6IkJlYXJlciIsImF6cCI6InVzZXItbWFuYWdlbWVudC1jbGllbnQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJPTEVfU0VSVklDRSIsIlJPTEVfVVNFUiIsIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy12aW9yYS1kZXYiLCJST0xFX0FETUlOIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbIm1hbmFnZS11c2VycyIsInZpZXctdXNlcnMiLCJxdWVyeS1ncm91cHMiLCJxdWVyeS11c2VycyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRIb3N0IjoiMTcyLjE5LjAuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC11c2VyLW1hbmFnZW1lbnQtY2xpZW50IiwiY2xpZW50QWRkcmVzcyI6IjE3Mi4xOS4wLjEiLCJjbGllbnRfaWQiOiJ1c2VyLW1hbmFnZW1lbnQtY2xpZW50In0.spHsRHCsua8wGWkx4GVLdGyQRCjQSsZjD4edBFzPAAAg2gNaptoVw4dqEIFBI6p2oP0tqenX1vSanBMSfTKfAmGbwUKe2Y5LsMyT1rWvzmCJjNALkb3Ib0YF4e9bjd581Mv2sIeOUWJF4nmMiJrss7aVtT7TOMoO8iBouXE3iofvDGIDAS4YeQPRrUqDkBRCnKIzEvbahDgwS3weS5s6pUUfpwBI-BGdim2g6SyG_UhYT_0MNc9Zt1RqYyJetFu8Oa0e2G-7mOECq56amvo4NfZxKcTcxNpOLaTxOiQYVx6n4TLbZfrRwBY5_BUrNYmZ8wgbxT6vR2lBQH3vWFbViw";
const TOTAL_REQUESTS = Number(1000);
const CONCURRENCY = Number(process.env.CONCURRENCY || 25);

if (!TOKEN) {
  console.error("Missing BEARER_TOKEN env var.");
  console.error("Example:");
  console.error(
    "BEARER_TOKEN=<token> TOTAL_REQUESTS=200 CONCURRENCY=25 node client/test-rate-limiter.js"
  );
  process.exit(1);
}

async function sendRequest() {
  try {
    const res = await fetch(URL, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${TOKEN}`,
      },
    });
    return res.status;
  } catch {
    return "network_error";
  }
}

async function runLoadTest(total, concurrency) {
  const results = new Map();
  let started = 0;

  async function worker() {
    while (started < total) {
      started += 1;
      const status = await sendRequest();
      results.set(status, (results.get(status) || 0) + 1);
    }
  }

  const workers = Array.from({ length: Math.min(concurrency, total) }, () => worker());
  await Promise.all(workers);
  return results;
}

function printSummary(results, total, elapsedMs) {
  console.log(`URL: ${URL}`);
  console.log(`Requests: ${total}`);
  console.log(`Concurrency: ${Math.min(CONCURRENCY, total)}`);
  console.log(`Elapsed: ${elapsedMs} ms`);
  console.log("Status summary:");

  const sorted = [...results.entries()].sort((a, b) => String(a[0]).localeCompare(String(b[0])));
  for (const [status, count] of sorted) {
    console.log(`  ${status}: ${count}`);
  }
}

async function main() {
  const start = Date.now();
  const results = await runLoadTest(TOTAL_REQUESTS, CONCURRENCY);
  printSummary(results, TOTAL_REQUESTS, Date.now() - start);
}

main().catch((err) => {
  console.error("Unexpected error:", err);
  process.exit(1);
});
