<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register • Viora</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/style.css">
</head>
<body class="viora-bg">

<div class="container">
    <div class="card glass">
        <h1 class="logo">Viora</h1>
        <p class="subtitle">Start your cinematic journey</p>

        <form action="${url.registrationAction}" method="post">
            <div class="input-group">
                <input type="email" name="email" required />
                <label>Email</label>
            </div>

            <div class="input-group">
                <input type="password" name="password" required />
                <label>Password</label>
            </div>

            <div class="input-group">
                <input type="password" name="password-confirm" required />
                <label>Confirm Password</label>
            </div>

            <button class="btn-primary">Create Account</button>
        </form>

        <p class="footer-text">
            Already have an account?
            <a href="${url.loginUrl}">Sign in</a>
        </p>
    </div>
</div>

</body>
</html>
