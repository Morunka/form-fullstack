<template>
  <nav class="navbar" :class="{ 'mobile-open': isMobileMenuOpen }">
    <ul class="nav-list">
      <li class="nav-item" v-for="route in navigationRoutes" :key="route.name">
        <router-link
            :to="route.path"
            class="nav-link"
            :class="{ 'active': isActive(route) }"
            @click="handleNavClick"
        >
          <span class="nav-icon" v-if="route.icon">
            <font-awesome-icon :icon="route.icon" />
          </span>
          <span class="nav-text">{{ route.name }}</span>
          <span class="nav-indicator"></span>
        </router-link>
      </li>
    </ul>

    <!-- Дополнительные элементы в навигации -->
    <div class="nav-actions" v-if="showActions">
      <router-link to="manager" class="btn btn-primary">
        Continue without
      </router-link>
      <!-- <LanguageSwitcher />
      <ThemeToggle /> -->
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  isMobileMenuOpen: Boolean,
  showActions: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['nav-click', 'contact-click'])

const route = useRoute()

const navigationRoutes = computed(() => [
  {
    name: 'Main',
    path: '/',
    icon: ['fas', 'home']
  },
  {
    name: 'Login',
    path: '/login',
    icon: ['fas', 'sign-in-alt']
  },
  {
    name: 'Register',
    path: '/register',
    icon: ['fas', 'user-plus']
  }
])

const isActive = (navRoute) => {
  if (navRoute.path === '/') {
    return route.path === '/'
  }
  return route.path.startsWith(navRoute.path)
}

const handleNavClick = () => {
  emit('nav-click')

  // Прокрутка к началу страницы на мобильных
  if (window.innerWidth < 768) {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
}

.nav-list {
  display: flex;
  gap: 15px;
  list-style: none;
  margin: 0;
  padding: 0;
  background: white;
}

.nav-item {
  position: relative;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 0;
  text-decoration: none;
  color: #4a5568;
  font-weight: 500;
  font-size: 15px;
  position: relative;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.nav-link:hover {
  color: #667eea;
}

.nav-link.active {
  color: #667eea;
  font-weight: 600;
}

.nav-link.active .nav-indicator {
  width: 100%;
}

.nav-icon {
  font-size: 15px;
}

.nav-indicator {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
  transition: width 0.3s ease;
}

.nav-link:hover .nav-indicator {
  width: 100%;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: 10px;
}

.btn {
  padding: 6px 16px;
  border-radius: 50px;
  font-weight: 500;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  margin: 5px;
  text-decoration: none;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* Мобильное меню */
@media (max-width: 768px) {
  .navbar {
    position: fixed;
    top: 0;
    right: -100%;
    bottom: 0;
    width: 80%;
    max-width: 320px;
    flex-direction: column;
    align-items: stretch;
    justify-content: flex-start;
    padding: 80px 20px 20px;
    transition: right 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 999;
  }

  .nav-icon {
    margin-left: 20px;
  }

  .navbar.mobile-open {
    right: 0;
  }

  .nav-list {
    flex-direction: column;
  }

  .nav-item {
    border-bottom: 1px solid #f1f1f1;
  }

  .nav-link {
    padding: 20px 0;
    font-size: 15px;
  }

  .nav-actions {
    flex-direction: column;
    margin-left: 0;
    gap: 20px;
  }

  .btn {
    width: 100%;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .navbar {
    width: 90%;
  }

  .nav-icon {
    display: none;
  }
}
</style>