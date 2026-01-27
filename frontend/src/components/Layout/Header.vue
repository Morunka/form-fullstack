<template>
  <header class="header" :class="{ 'scrolled': isScrolled, 'mobile-menu-open': isMobileMenuOpen }">
    <div class="container header-container">
      <!-- Логотип слева -->
      <div class="logo-wrapper">
        <router-link to="/" class="logo-link">
          <img
              src="../../../public/logo.svg"
              class="logo"
          />
          <!-- <span class="logo-text">MyCompany</span> -->
        </router-link>
      </div>

      <!-- Навигация справа -->
      <Navbar
          :is-mobile-menu-open="isMobileMenuOpen"
          @toggle-mobile-menu="toggleMobileMenu"
      />

      <!-- Кнопка для мобильного меню -->
      <button
          class="mobile-menu-toggle"
          @click="toggleMobileMenu"
          aria-label="Menu"
      >
        <span class="hamburger" :class="{ 'active': isMobileMenuOpen }">
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </span>
      </button>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import Navbar from '../UI/Navbar.vue'

const isScrolled = ref(false)
const isMobileMenuOpen = ref(false)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
  // Блокируем скролл при открытом меню
  document.body.style.overflow = isMobileMenuOpen.value ? 'hidden' : 'auto'
}

// Закрываем меню при изменении маршрута
import { useRouter } from 'vue-router'
const router = useRouter()
router.afterEach(() => {
  isMobileMenuOpen.value = false
  document.body.style.overflow = 'auto'
})

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.body.style.overflow = 'auto'
})
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 1);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.header.scrolled {
  background: rgba(255, 255, 255, 0.98);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  padding: 0.5rem 0;
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
  padding: 0 2rem;
}

.logo-wrapper {
  flex-shrink: 0;
}

.logo-link {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: inherit;
}

.logo {
  height: 60px;
  width: auto;
  transition: transform 0.3s ease;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.mobile-menu-toggle {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  z-index: 1001;
}

.hamburger {
  display: block;
  position: relative;
  width: 24px;
  height: 18px;
}

.hamburger .bar {
  position: absolute;
  left: 0;
  width: 100%;
  height: 2px;
  background: #333;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.hamburger .bar:nth-child(1) {
  top: 0;
}

.hamburger .bar:nth-child(2) {
  top: 8px;
}

.hamburger .bar:nth-child(3) {
  top: 16px;
}

.hamburger.active .bar:nth-child(1) {
  transform: rotate(45deg);
  top: 8px;
}

.hamburger.active .bar:nth-child(2) {
  opacity: 0;
}

.hamburger.active .bar:nth-child(3) {
  transform: rotate(-45deg);
  top: 8px;
}

/* Адаптивность */
@media (max-width: 1024px) {
  .header-container {
    padding: 0 1rem;
  }
}

@media (max-width: 768px) {
  .mobile-menu-toggle {
    display: block;
  }

  .header-container {
    height: 70px;
  }

  .logo-text {
    font-size: 1.3rem;
  }
}

@media (max-width: 480px) {
  .logo-text {
    display: none;
  }
}
</style>