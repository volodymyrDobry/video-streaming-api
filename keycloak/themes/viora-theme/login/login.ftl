<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login • Viora</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/style.css">
</head>
<body class="viora-bg">

<div class="container">
    <div class="card glass">
        <h1 class="logo">Viora</h1>
        <p class="subtitle">Welcome back. Continue your story.</p>

        <form action="${url.loginAction}" method="post">
            <div class="input-group">
                <input type="email" name="username" required/>
                <label>Email</label>
            </div>

            <div class="input-group">
                <input type="password" name="password" required/>
                <label>Password</label>
            </div>

            <button class="btn-primary">Sign In</button>
        </form>

        <p class="footer-text">
            New to Viora?
            <a href="${url.registrationUrl}">Create account</a>
        </p>
    </div>
</div>

</body>
</html>
