<template>
  <div class="home-page">
    <section class="hero">
      <div class="container">
        <img src="../../public/favicon.svg" alt="logo" class="favicon"/>
        <h1 class="hero-title">Welcome</h1>
        <p class="hero-subtitle">Origami is a simple and compact user manager that is in improving right now.</p>
        <router-link to="/login" class="btn-new">
          Log In
        </router-link>
        <router-link to="/register" class="btn-new btn-alt">
          Register
        </router-link>
        <router-link to="/manager" class="btn-new btn-primary">
          Continue without
        </router-link>
      </div>
    </section>

    <section class="features">
      <div class="container">
        <h2>Example text</h2>
      </div>
    </section>
  </div>
</template>

<script setup>
// Page logic
</script>

<style scoped>
.hero {
  min-height: calc(100vh - 80px);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 2rem;
}

.hero-title {
  font-size: 3.5rem;
  margin-bottom: 1rem;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

.features {
  padding: 5rem 0;
}

.btn-new {
  padding: 12px 32px;
  border-radius: 50px;
  background: linear-gradient(135deg, #a1b1ff 0%, #b070f1 100%);
  color: white;
  border: none;
  font-size:16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  text-decoration: none;
  margin: 5px;
}

.btn-alt {
  background: linear-gradient(135deg, #758eff 0%, #ad5eff 100%);
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: block;
  text-align: center;
  margin: 20px auto auto auto;
  max-width: 200px;
}

.favicon {
  width: 200px;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }
}
</style>