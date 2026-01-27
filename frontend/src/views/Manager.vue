<template>
  <div class="user-management">
    <!-- Кнопка возврата наверх -->
    <!-- <button
        v-if="showScrollButton"
        @click="scrollToTop"
        class="scroll-to-top"
        :class="{ 'visible': showScrollButton }">
      <font-awesome-icon :icon="['fas', 'arrow-up']" />
    </button> -->

    <!-- Заголовок -->
    <div class="page-header">
      <h1 class="page-title">
        <font-awesome-icon :icon="['fas', 'users-cog']" />
        Users manager
      </h1>
      <p class="page-subtitle">
        Fetch, add, change and delete users from database
      </p>
    </div>

    <!-- Навигация по категориям -->
    <div class="category-tabs">
      <button
          v-for="category in categories"
          :key="category.id"
          class="category-tab"
          :class="{ 'active': activeCategory === category.id }"
          @click="activeCategory = category.id">
        <font-awesome-icon :icon="category.icon" />
        {{ category.name }}
        <span v-if="category.id === 'view'" class="badge">{{ users.length }}</span>
      </button>
    </div>

    <!-- Категория: Просмотр пользователей -->
    <div v-if="activeCategory === 'view'" class="category-section">
      <div class="section-header">
        <h2>
          <font-awesome-icon :icon="['fas', 'list']" />
          Список пользователей
        </h2>
        <div class="search-box">
          <input
              v-model="searchQuery"
              type="text"
              placeholder="Поиск пользователей..."
              class="search-input"
          >
          <font-awesome-icon :icon="['fas', 'search']" class="search-icon" />
        </div>
      </div>

      <!-- Таблица пользователей -->
      <div class="table-container">
        <table class="users-table">
          <thead>
          <tr>
            <th @click="sortBy('login')">
              Логин
              <font-awesome-icon v-if="sortField === 'login'" :icon="sortIcon" />
            </th>
            <th @click="sortBy('firstName')">
              Имя
              <font-awesome-icon v-if="sortField === 'firstName'" :icon="sortIcon" />
            </th>
            <th @click="sortBy('lastname')">
              Фамилия
              <font-awesome-icon v-if="sortField === 'lastname'" :icon="sortIcon" />
            </th>
            <th @click="sortBy('email')">
              Email
              <font-awesome-icon v-if="sortField === 'email'" :icon="sortIcon" />
            </th>
            <th>Отчество</th>
            <th>Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in paginatedUsers" :key="user.login">
            <td>
              <div class="user-avatar">
                {{ getInitials(user) }}
              </div>
              <strong>@{{ user.login }}</strong>
            </td>
            <td>{{ user.firstName }}</td>
            <td>{{ user.lastname }}</td>
            <td>
              <a :href="`mailto:${user.email}`" class="email-link">
                {{ user.email }}
              </a>
            </td>
            <td>{{ user.patronymicName || '-' }}</td>
            <td>
              <div class="action-buttons">
                <button
                    @click="editUser(user)"
                    class="btn-edit"
                    title="Редактировать">
                  <font-awesome-icon :icon="['fas', 'edit']" />
                </button>
                <button
                    @click="confirmDelete(user)"
                    class="btn-delete"
                    title="Удалить">
                  <font-awesome-icon :icon="['fas', 'trash']" />
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="filteredUsers.length === 0">
            <td colspan="6" class="no-data">
              {{ users.length === 0 ? 'Нет пользователей' : 'Пользователи не найдены' }}
            </td>
          </tr>
          </tbody>
        </table>

        <!-- Пагинация -->
        <div v-if="filteredUsers.length > itemsPerPage" class="pagination">
          <button
              @click="currentPage--"
              :disabled="currentPage === 1"
              class="pagination-btn">
            <font-awesome-icon :icon="['fas', 'chevron-left']" />
          </button>

          <span class="page-info">
            Страница {{ currentPage }} из {{ totalPages }}
          </span>

          <button
              @click="currentPage++"
              :disabled="currentPage === totalPages"
              class="pagination-btn">
            <font-awesome-icon :icon="['fas', 'chevron-right']" />
          </button>
        </div>
      </div>
    </div>

    <!-- Категория: Добавление пользователя -->
    <div v-if="activeCategory === 'add'" class="category-section">
      <div class="section-header">
        <h2>
          <font-awesome-icon :icon="['fas', 'user-plus']" />
          Добавить нового пользователя
        </h2>
      </div>

      <form @submit.prevent="submitForm" class="user-form">
        <div class="form-grid">
          <div class="form-group">
            <label for="login" class="form-label">
              <font-awesome-icon :icon="['fas', 'user']" />
              Логин *
            </label>
            <input
                id="login"
                v-model="newUser.login"
                type="text"
                required
                class="form-input"
                placeholder="Введите логин"
            >
          </div>

          <div class="form-group">
            <label for="email" class="form-label">
              <font-awesome-icon :icon="['fas', 'envelope']" />
              Email *
            </label>
            <input
                id="email"
                v-model="newUser.email"
                type="email"
                required
                class="form-input"
                placeholder="user@example.com"
            >
          </div>

          <div class="form-group">
            <label for="password" class="form-label">
              <font-awesome-icon :icon="['fas', 'lock']" />
              Пароль *
            </label>
            <div class="password-wrapper">
              <input
                  id="password"
                  v-model="newUser.password"
                  :type="showPassword ? 'text' : 'password'"
                  required
                  class="form-input"
                  placeholder="Введите пароль"
              >
              <button
                  type="button"
                  class="password-toggle"
                  @click="showPassword = !showPassword">
                <font-awesome-icon :icon="showPassword ? ['fas', 'eye-slash'] : ['fas', 'eye']" />
              </button>
            </div>
          </div>

          <div class="form-group">
            <label for="firstName" class="form-label">
              <font-awesome-icon :icon="['fas', 'signature']" />
              Имя *
            </label>
            <input
                id="firstName"
                v-model="newUser.firstName"
                type="text"
                required
                class="form-input"
                placeholder="Введите имя"
            >
          </div>

          <div class="form-group">
            <label for="lastname" class="form-label">
              <font-awesome-icon :icon="['fas', 'signature']" />
              Фамилия *
            </label>
            <input
                id="lastname"
                v-model="newUser.lastname"
                type="text"
                required
                class="form-input"
                placeholder="Введите фамилию"
            >
          </div>

          <div class="form-group">
            <label for="patronymicName" class="form-label">
              <font-awesome-icon :icon="['fas', 'signature']" />
              Отчество
            </label>
            <input
                id="patronymicName"
                v-model="newUser.patronymicName"
                type="text"
                class="form-input"
                placeholder="Введите отчество"
            >
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-submit" :disabled="isSubmitting">
            <span v-if="isSubmitting" class="spinner"></span>
            <span v-else>
              <font-awesome-icon :icon="['fas', 'save']" />
              Добавить пользователя
            </span>
          </button>
          <button
              type="button"
              @click="resetForm"
              class="btn-reset">
            <font-awesome-icon :icon="['fas', 'redo']" />
            Очистить форму
          </button>
        </div>
      </form>
    </div>

    <!-- Категория: Редактирование пользователя -->
    <div v-if="activeCategory === 'edit'" class="category-section">
      <div class="section-header">
        <h2>
          <font-awesome-icon :icon="['fas', 'user-edit']" />
          Редактирование пользователя
        </h2>
        <p class="section-subtitle">
          Выберите пользователя для редактирования
        </p>
      </div>

      <!-- Выбор пользователя -->
      <div class="user-selection">
        <select v-model="selectedUserLogin" class="user-select">
          <option value="" disabled>Выберите пользователя...</option>
          <option
              v-for="user in users"
              :key="user.login"
              :value="user.login">
            {{ user.lastname }} {{ user.firstName }} ({{ user.email }})
          </option>
        </select>
      </div>

      <!-- Форма редактирования -->
      <div v-if="selectedUserLogin" class="edit-form-container">
        <form @submit.prevent="updateUser" class="user-form">
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">
                <font-awesome-icon :icon="['fas', 'user']" />
                Логин
              </label>
              <input
                  :value="selectedUser?.login"
                  type="text"
                  class="form-input"
                  disabled
              >
            </div>

            <div class="form-group">
              <label for="edit-email" class="form-label">
                <font-awesome-icon :icon="['fas', 'envelope']" />
                Email *
              </label>
              <input
                  id="edit-email"
                  v-model="editUserData.email"
                  type="email"
                  required
                  class="form-input"
              >
            </div>

            <div class="form-group">
              <label for="edit-password" class="form-label">
                <font-awesome-icon :icon="['fas', 'lock']" />
                Новый пароль
              </label>
              <div class="password-wrapper">
                <input
                    id="edit-password"
                    v-model="editUserData.password"
                    :type="showEditPassword ? 'text' : 'password'"
                    class="form-input"
                    placeholder="Оставьте пустым, чтобы не менять"
                >
                <button
                    type="button"
                    class="password-toggle"
                    @click="showEditPassword = !showEditPassword">
                  <font-awesome-icon :icon="showEditPassword ? ['fas', 'eye-slash'] : ['fas', 'eye']" />
                </button>
              </div>
            </div>

            <div class="form-group">
              <label for="edit-firstName" class="form-label">
                <font-awesome-icon :icon="['fas', 'signature']" />
                Имя *
              </label>
              <input
                  id="edit-firstName"
                  v-model="editUserData.firstName"
                  type="text"
                  required
                  class="form-input"
              >
            </div>

            <div class="form-group">
              <label for="edit-lastname" class="form-label">
                <font-awesome-icon :icon="['fas', 'signature']" />
                Фамилия *
              </label>
              <input
                  id="edit-lastname"
                  v-model="editUserData.lastname"
                  type="text"
                  required
                  class="form-input"
              >
            </div>

            <div class="form-group">
              <label for="edit-patronymicName" class="form-label">
                <font-awesome-icon :icon="['fas', 'signature']" />
                Отчество
              </label>
              <input
                  id="edit-patronymicName"
                  v-model="editUserData.patronymicName"
                  type="text"
                  class="form-input"
              >
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn-submit" :disabled="isUpdating">
              <span v-if="isUpdating" class="spinner"></span>
              <span v-else>
                <font-awesome-icon :icon="['fas', 'save']" />
                Сохранить изменения
              </span>
            </button>
            <button
                type="button"
                @click="cancelEdit"
                class="btn-cancel">
              <font-awesome-icon :icon="['fas', 'times']" />
              Отмена
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Категория: Удаление пользователя -->
    <div v-if="activeCategory === 'delete'" class="category-section">
      <div class="section-header">
        <h2>
          <font-awesome-icon :icon="['fas', 'user-times']" />
          Удаление пользователей
        </h2>
        <p class="section-subtitle danger">
          <font-awesome-icon :icon="['fas', 'exclamation-triangle']" />
          Внимание: это действие нельзя отменить!
        </p>
      </div>

      <div class="delete-section">
        <!-- Быстрое удаление -->
        <div class="quick-delete">
          <h3>
            <font-awesome-icon :icon="['fas', 'bolt']" />
            Быстрое удаление по логину
          </h3>
          <div class="quick-delete-form">
            <input
                v-model="deleteLogin"
                type="text"
                placeholder="Введите логин пользователя"
                class="delete-input"
                list="user-logins"
            >
            <datalist id="user-logins">
              <option v-for="user in users" :key="user.login" :value="user.login" />
            </datalist>
            <button
                @click="quickDeleteUser"
                :disabled="!deleteLogin || isDeleting"
                class="btn-delete-quick">
              <span v-if="isDeleting" class="spinner"></span>
              <span v-else>
                <font-awesome-icon :icon="['fas', 'trash']" />
                Удалить
              </span>
            </button>
          </div>
        </div>

        <!-- Выбор пользователей -->
        <div class="delete-selection">
          <h3>
            <font-awesome-icon :icon="['fas', 'list-check']" />
            Выберите пользователей для удаления:
          </h3>

          <div class="selection-controls">
            <button
                @click="selectAllUsers"
                class="btn-select-all">
              <font-awesome-icon :icon="['fas', 'check-square']" />
              Выбрать все
            </button>
            <button
                @click="deselectAllUsers"
                class="btn-deselect-all">
              <font-awesome-icon :icon="['fas', 'square']" />
              Снять выделение
            </button>
          </div>

          <div class="delete-user-list">
            <div
                v-for="user in users"
                :key="user.login"
                class="delete-user-item"
                :class="{ 'selected': selectedUsers.includes(user.login) }"
                @click="toggleUserSelection(user.login)">
              <div class="user-checkbox">
                <font-awesome-icon
                    :icon="selectedUsers.includes(user.login) ? ['fas', 'check-circle'] : ['far', 'circle']"
                />
              </div>
              <div class="user-info">
                <div class="user-name">
                  {{ user.lastname }} {{ user.firstName }}
                  <span class="user-login">@{{ user.login }}</span>
                </div>
                <div class="user-email">{{ user.email }}</div>
              </div>
              <div class="user-avatar-sm">
                {{ getInitials(user) }}
              </div>
            </div>
          </div>

          <div class="selection-summary">
            Выбрано: {{ selectedUsers.length }} пользователей
          </div>

          <div class="delete-actions">
            <button
                @click="deleteSelectedUsers"
                :disabled="selectedUsers.length === 0 || isDeleting"
                class="btn-delete-selected">
              <span v-if="isDeleting" class="spinner"></span>
              <span v-else>
                <font-awesome-icon :icon="['fas', 'trash-alt']" />
                Удалить выбранных ({{ selectedUsers.length }})
              </span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Уведомления -->
    <div v-if="notification.show" class="notification" :class="notification.type">
      <div class="notification-content">
        <font-awesome-icon :icon="notification.icon" class="notification-icon" />
        <span>{{ notification.message }}</span>
      </div>
      <button @click="hideNotification" class="notification-close">
        <font-awesome-icon :icon="['fas', 'times']" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

// Типы данных
interface UserDto {
  login: string
  firstName: string
  lastname: string
  patronymicName: string
  email: string
  password: string
}

interface ItemListResponseDto {
  items: UserDto[]
}

// Константы
const API_URL = 'http://localhost:8080/api/users'
const ITEMS_PER_PAGE = 10

// Категории управления
const categories = [
  { id: 'view', name: 'Просмотр', icon: ['fas', 'eye'] },
  { id: 'add', name: 'Добавить', icon: ['fas', 'plus'] },
  { id: 'edit', name: 'Редактировать', icon: ['fas', 'edit'] },
  { id: 'delete', name: 'Удалить', icon: ['fas', 'trash'] }
]

// Реактивные переменные
const activeCategory = ref('view')
const showScrollButton = ref(false)
const users = ref<UserDto[]>([])
const searchQuery = ref('')
const sortField = ref('login')
const sortDirection = ref<'asc' | 'desc'>('asc')
const currentPage = ref(1)
const itemsPerPage = ref(ITEMS_PER_PAGE)

// Добавление пользователя
const newUser = ref({
  login: '',
  email: '',
  password: '',
  firstName: '',
  lastname: '',
  patronymicName: ''
})
const showPassword = ref(false)
const isSubmitting = ref(false)

// Редактирование пользователя
const selectedUserLogin = ref('')
const editUserData = ref<Partial<UserDto>>({})
const showEditPassword = ref(false)
const isUpdating = ref(false)

// Удаление пользователей
const deleteLogin = ref('')
const selectedUsers = ref<string[]>([])
const isDeleting = ref(false)

// Уведомления
const notification = ref({
  show: false,
  type: 'info' as 'success' | 'error' | 'info' | 'warning',
  message: '',
  icon: ['fas', 'info-circle']
})

// Вычисляемые свойства
const filteredUsers = computed(() => {
  let result = [...users.value]

  // Поиск
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(user =>
        user.login.toLowerCase().includes(query) ||
        user.firstName.toLowerCase().includes(query) ||
        user.lastname.toLowerCase().includes(query) ||
        user.email.toLowerCase().includes(query)
    )
  }

  // Сортировка
  result.sort((a, b) => {
    const fieldA = a[sortField.value as keyof UserDto]?.toString().toLowerCase() || ''
    const fieldB = b[sortField.value as keyof UserDto]?.toString().toLowerCase() || ''

    if (sortDirection.value === 'asc') {
      return fieldA.localeCompare(fieldB)
    } else {
      return fieldB.localeCompare(fieldA)
    }
  })

  return result
})

const totalPages = computed(() =>
    Math.ceil(filteredUsers.value.length / itemsPerPage.value)
)

const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return filteredUsers.value.slice(start, end)
})

const sortIcon = computed(() =>
    sortDirection.value === 'asc' ? ['fas', 'arrow-up'] : ['fas', 'arrow-down']
)

const selectedUser = computed(() =>
    users.value.find(user => user.login === selectedUserLogin.value)
)

// Методы
const handleScroll = () => {
  showScrollButton.value = window.scrollY > 300
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

const loadUsers = async () => {
  try {
    const res = await fetch(API_URL)
    if (!res.ok) throw new Error(`HTTP error ${res.status}`)

    const result: ItemListResponseDto = await res.json()
    users.value = Array.isArray(result.items) ? result.items : []
    showNotification('Пользователи загружены', 'success')
  } catch (err) {
    console.error('Ошибка загрузки пользователей:', err)
    showNotification('Ошибка загрузки пользователей', 'error')
  }
}

const showNotification = (message: string, type: 'success' | 'error' | 'info' | 'warning' = 'info') => {
  const icons = {
    success: ['fas', 'check-circle'],
    error: ['fas', 'exclamation-circle'],
    warning: ['fas', 'exclamation-triangle'],
    info: ['fas', 'info-circle']
  }

  notification.value = {
    show: true,
    type,
    message,
    icon: icons[type]
  }

  // Автоскрытие через 5 секунд
  setTimeout(() => {
    notification.value.show = false
  }, 5000)
}

const hideNotification = () => {
  notification.value.show = false
}

const sortBy = (field: string) => {
  if (sortField.value === field) {
    sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortField.value = field
    sortDirection.value = 'asc'
  }
  currentPage.value = 1
}

const getInitials = (user: UserDto) => {
  const first = user.firstName?.[0] || ''
  const last = user.lastname?.[0] || ''
  return (first + last).toUpperCase()
}

// Добавление пользователя
const submitForm = async () => {
  isSubmitting.value = true

  try {
    const res = await fetch(`${API_URL}/add`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newUser.value)
    })

    if (!res.ok) throw new Error(`HTTP error ${res.status}`)

    const result: ItemListResponseDto = await res.json()
    users.value = result.items || []

    showNotification('Пользователь успешно добавлен', 'success')
    resetForm()
    activeCategory.value = 'view'
  } catch (err) {
    console.error('Ошибка добавления пользователя:', err)
    showNotification('Ошибка добавления пользователя', 'error')
  } finally {
    isSubmitting.value = false
  }
}

const resetForm = () => {
  newUser.value = {
    login: '',
    email: '',
    password: '',
    firstName: '',
    lastname: '',
    patronymicName: ''
  }
  showPassword.value = false
}

// Редактирование пользователя
const editUser = (user: UserDto) => {
  selectedUserLogin.value = user.login
  editUserData.value = { ...user }
  editUserData.value.password = '' // Очищаем пароль для безопасности
  activeCategory.value = 'edit'
}

const updateUser = async () => {
  if (!selectedUser.value) return

  isUpdating.value = true

  try {
    // Создаем объект с изменениями
    const updatedUser = {
      ...selectedUser.value,
      ...editUserData.value
    }

    // Если пароль не изменен, удаляем его из объекта
    if (!editUserData.value.password) {
      delete updatedUser.password
    }

    const res = await fetch(`${API_URL}/update`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedUser)
    })

    if (!res.ok) throw new Error(`HTTP error ${res.status}`)

    const result: ItemListResponseDto = await res.json()
    users.value = result.items || []

    showNotification('Пользователь успешно обновлен', 'success')
    cancelEdit()
    activeCategory.value = 'view'
  } catch (err) {
    console.error('Ошибка обновления пользователя:', err)
    showNotification('Ошибка обновления пользователя', 'error')
  } finally {
    isUpdating.value = false
  }
}

const cancelEdit = () => {
  selectedUserLogin.value = ''
  editUserData.value = {}
  showEditPassword.value = false
}

// Удаление пользователей
const confirmDelete = (user: UserDto) => {
  if (confirm(`Вы уверены, что хотите удалить пользователя ${user.login}?`)) {
    deleteSingleUser(user.login)
  }
}

const deleteSingleUser = async (login: string) => {
  try {
    const res = await fetch(`${API_URL}/delete?login=${encodeURIComponent(login)}`, {
      method: 'DELETE'
    })

    if (!res.ok) throw new Error(`HTTP error ${res.status}`)

    const result: ItemListResponseDto = await res.json()
    users.value = result.items || []

    // Убираем удаленного пользователя из выбранных
    selectedUsers.value = selectedUsers.value.filter(l => l !== login)

    showNotification('Пользователь успешно удален', 'success')
  } catch (err) {
    console.error('Ошибка удаления пользователя:', err)
    showNotification('Ошибка удаления пользователя', 'error')
  }
}

const quickDeleteUser = async () => {
  if (!deleteLogin.value) return

  isDeleting.value = true
  await deleteSingleUser(deleteLogin.value)
  deleteLogin.value = ''
  isDeleting.value = false
}

const toggleUserSelection = (login: string) => {
  const index = selectedUsers.value.indexOf(login)
  if (index > -1) {
    selectedUsers.value.splice(index, 1)
  } else {
    selectedUsers.value.push(login)
  }
}

const selectAllUsers = () => {
  selectedUsers.value = users.value.map(user => user.login)
}

const deselectAllUsers = () => {
  selectedUsers.value = []
}

const deleteSelectedUsers = async () => {
  if (selectedUsers.value.length === 0) return

  if (!confirm(`Вы уверены, что хотите удалить ${selectedUsers.value.length} пользователей?`)) {
    return
  }

  isDeleting.value = true

  for (const login of selectedUsers.value) {
    await deleteSingleUser(login)
  }

  selectedUsers.value = []
  isDeleting.value = false
}

// Хуки жизненного цикла
onMounted(() => {
  loadUsers()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.user-management {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
}

/* Кнопка наверх */
.scroll-to-top {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  cursor: pointer;
  font-size: 18px;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  z-index: 1000;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.scroll-to-top.visible {
  opacity: 1;
  visibility: visible;
}

.scroll-to-top:hover {
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* Заголовок страницы */
.page-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 30px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 15px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.page-title {
  font-size: 2.5rem;
  margin: 0 0 15px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.page-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
  max-width: 600px;
  margin: 0 auto;
}

/* Категории */
.category-tabs {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  margin-bottom: 40px;
}

.category-tab {
  padding: 20px;
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  color: #4a5568;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  position: relative;
}

.category-tab:hover {
  border-color: #667eea;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.category-tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #f56565;
  color: white;
  font-size: 0.8rem;
  padding: 4px 8px;
  border-radius: 12px;
  min-width: 24px;
}

/* Секции категорий */
.category-section {
  background: white;
  border-radius: 15px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.section-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #e2e8f0;
}

.section-header h2 {
  font-size: 1.8rem;
  color: #2d3748;
  margin: 0 0 10px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-subtitle {
  color: #718096;
  font-size: 1rem;
}

.section-subtitle.danger {
  color: #f56565;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Поиск */
.search-box {
  position: relative;
  max-width: 400px;
  margin-top: 15px;
}

.search-input {
  width: 100%;
  padding: 12px 15px 12px 45px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  color: #a0aec0;
}

/* Таблица */
.table-container {
  overflow-x: auto;
}

.users-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.users-table th {
  background: #f7fafc;
  padding: 16px;
  text-align: left;
  font-weight: 600;
  color: #4a5568;
  border-bottom: 2px solid #e2e8f0;
  cursor: pointer;
  transition: background 0.3s ease;
  white-space: nowrap;
}

.users-table th:hover {
  background: #edf2f7;
}

.users-table td {
  padding: 16px;
  border-bottom: 1px solid #e2e8f0;
  vertical-align: middle;
}

.users-table tr:hover {
  background: #f7fafc;
}

.user-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  color: white;
  font-weight: 600;
  margin-right: 12px;
  vertical-align: middle;
}

.email-link {
  color: #667eea;
  text-decoration: none;
}

.email-link:hover {
  text-decoration: underline;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn-edit, .btn-delete {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.btn-edit {
  background: #48bb78;
  color: white;
}

.btn-edit:hover {
  background: #38a169;
}

.btn-delete {
  background: #f56565;
  color: white;
}

.btn-delete:hover {
  background: #e53e3e;
}

.no-data {
  text-align: center;
  color: #a0aec0;
  font-style: italic;
  padding: 40px !important;
}

/* Пагинация */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
  padding: 20px;
}

.pagination-btn {
  width: 40px;
  height: 40px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  background: white;
  color: #4a5568;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.pagination-btn:hover:not(:disabled) {
  border-color: #667eea;
  color: #667eea;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #4a5568;
  font-weight: 500;
}

/* Формы */
.user-form {
  max-width: 800px;
  margin: 0 auto;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}

.form-group {
  position: relative;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #4a5568;
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input:disabled {
  background: #f7fafc;
  cursor: not-allowed;
}

.password-wrapper {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 10px;
  top: 50%;
  background: none;
  border: none;
  color: #a0aec0;
  cursor: pointer;
  padding: 5px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 30px;
}

.btn-submit, .btn-reset, .btn-cancel {
  padding: 14px 28px;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  min-width: 200px;
}

.btn-submit {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  color: white;
}

.btn-submit:hover:not(:disabled) {
  box-shadow: 0 6px 20px rgba(56, 161, 105, 0.3);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-reset {
  background: #edf2f7;
  color: #4a5568;
  border: 2px solid #e2e8f0;
}

.btn-reset:hover {
  background: #e2e8f0;
}

.btn-cancel {
  background: #fed7d7;
  color: #c53030;
}

.btn-cancel:hover {
  background: #feb2b2;
}

/* Спиннер */
.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Выбор пользователя */
.user-selection {
  margin: 30px 0;
}

.user-select {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 1rem;
  background: white;
  cursor: pointer;
}

/* Удаление */
.delete-section {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 30px;
}

@media (max-width: 1024px) {
  .delete-section {
    grid-template-columns: 1fr;
  }
}

.quick-delete {
  background: #fff5f5;
  padding: 25px;
  border-radius: 12px;
  border: 2px solid #fed7d7;
}

.quick-delete h3 {
  color: #c53030;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.quick-delete-form {
  display: flex;
  gap: 10px;
}

.delete-input {
  flex: 1;
  padding: 12px 15px;
  border: 2px solid #fed7d7;
  border-radius: 8px;
  font-size: 1rem;
}

.delete-input:focus {
  outline: none;
  border-color: #c53030;
}

.btn-delete-quick {
  padding: 12px 24px;
  background: linear-gradient(135deg, #f56565 0%, #e53e3e 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-width: 120px;
}

.btn-delete-quick:hover:not(:disabled) {
  box-shadow: 0 4px 15px rgba(229, 62, 62, 0.3);
}

.btn-delete-quick:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Выбор пользователей для удаления */
.delete-selection {
  background: white;
  padding: 25px;
  border-radius: 12px;
  border: 2px solid #e2e8f0;
}

.delete-selection h3 {
  margin: 0 0 20px 0;
  color: #2d3748;
  display: flex;
  align-items: center;
  gap: 10px;
}

.selection-controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.btn-select-all, .btn-deselect-all {
  padding: 10px 20px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  background: white;
  color: #4a5568;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-select-all:hover {
  background: #48bb78;
  color: white;
  border-color: #48bb78;
}

.btn-deselect-all:hover {
  background: #f56565;
  color: white;
  border-color: #f56565;
}

.delete-user-list {
  max-height: 400px;
  overflow-y: auto;
  margin: 20px 0;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

.delete-user-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #e2e8f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.delete-user-item:hover {
  background: #f7fafc;
}

.delete-user-item.selected {
  background: #fff5f5;
  border-left: 4px solid #f56565;
}

.delete-user-item:last-child {
  border-bottom: none;
}

.user-checkbox {
  margin-right: 15px;
  color: #a0aec0;
  font-size: 1.2rem;
}

.delete-user-item.selected .user-checkbox {
  color: #f56565;
}

.user-info {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 5px;
}

.user-login {
  font-weight: normal;
  color: #718096;
  font-size: 0.9rem;
}

.user-email {
  color: #667eea;
  font-size: 0.9rem;
}

.user-avatar-sm {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  color: white;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.9rem;
}

.selection-summary {
  text-align: center;
  padding: 15px;
  background: #f7fafc;
  border-radius: 8px;
  margin: 20px 0;
  font-weight: 600;
  color: #4a5568;
}

.delete-actions {
  text-align: center;
}

.btn-delete-selected {
  padding: 15px 30px;
  background: linear-gradient(135deg, #f56565 0%, #c53030 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  min-width: 300px;
}

.btn-delete-selected:hover:not(:disabled) {
  box-shadow: 0 6px 20px rgba(197, 48, 48, 0.3);
}

.btn-delete-selected:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Уведомления */
.notification {
  position: fixed;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  padding: 15px 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 15px;
  min-width: 300px;
  max-width: 500px;
  z-index: 1001;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

.notification.success {
  background: #48bb78;
  color: white;
}

.notification.error {
  background: #f56565;
  color: white;
}

.notification.warning {
  background: #ed8936;
  color: white;
}

.notification.info {
  background: #667eea;
  color: white;
}

.notification-content {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
}

.notification-icon {
  font-size: 1.2rem;
}

.notification-close {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  opacity: 0.7;
  padding: 5px;
}

.notification-close:hover {
  opacity: 1;
}

/* Адаптивность */
@media (max-width: 1024px) {
  .category-tabs {
    grid-template-columns: repeat(2, 1fr);
  }

  .delete-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .user-management {
    padding: 10px;
  }

  .page-title {
    font-size: 2rem;
    flex-direction: column;
    gap: 10px;
  }

  .category-tabs {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }

  .btn-submit, .btn-reset, .btn-cancel {
    width: 100%;
  }

  .scroll-to-top {
    bottom: 20px;
    right: 20px;
    width: 40px;
    height: 40px;
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 1.5rem;
  }

  .page-subtitle {
    font-size: 1rem;
  }

  .category-section {
    padding: 20px;
  }

  .quick-delete-form {
    flex-direction: column;
  }

  .btn-delete-quick {
    width: 100%;
  }
}
</style>