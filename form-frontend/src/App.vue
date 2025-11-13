<script setup lang="ts">
import { ref, onMounted } from 'vue'

const formData = ref({
  firstName: '',
  lastName: '',
  patronymicName: '',
  email: '',
  login: '',
  password: ''
})

const message = ref('')
const users = ref<any[]>([])
const selectedUserEmail = ref('') // используем email как уникальный идентификатор
const selectedField = ref('firstName')
const userData = ref('')

const API_URL = 'http://localhost:8080/api/users'

const loadUsers = async () => {
  try {
    const res = await fetch(API_URL)
    if (!res.ok) throw new Error('HTTP error ' + res.status)
    const result = await res.json()
    // Бэкенд возвращает { items: [...] }
    users.value = Array.isArray(result.items) ? result.items : []
    message.value = ''
  } catch (err) {
    console.error('Ошибка загрузки пользователей:', err)
    message.value = 'Ошибка загрузки пользователей'
    users.value = []
  }
}

const submitForm = async () => {
  try {
    const res = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData.value)
    })
    if (!res.ok) throw new Error('HTTP error ' + res.status)
    message.value = 'Пользователь успешно добавлен'
    await loadUsers()
    formData.value = {
      firstName: '',
      lastName: '',
      patronymicName: '',
      email: '',
      login: '',
      password: ''
    }
  } catch (err) {
    console.error('Ошибка отправки формы:', err)
    message.value = 'Ошибка отправки формы'
  }
}

const fetchUserData = () => {
  if (!selectedUserEmail.value) {
    userData.value = ''
    return
  }
  const user = users.value.find(u => u.email === selectedUserEmail.value)
  if (user && selectedField.value in user) {
    userData.value = user[selectedField.value]
  } else {
    userData.value = user ? JSON.stringify(user, null, 2) : 'Пользователь не найден'
  }
}

const fetchAllUsers = async () => {
  try {
    const res = await fetch(API_URL)
    if (!res.ok) throw new Error('HTTP error ' + res.status)
    const data = await res.json()
    userData.value = JSON.stringify(data, null, 2)
  } catch (err) {
    console.error('Ошибка получения всех пользователей:', err)
    userData.value = 'Ошибка получения всех пользователей'
  }
}

onMounted(loadUsers)
</script>

<template>
  <div class="container">
    <h2>Добавить пользователя</h2>

    <form @submit.prevent="submitForm" class="form">
      <div class="form-group">
        <label>Имя:</label>
        <input v-model="formData.firstName" type="text" required />
      </div>
      <div class="form-group">
        <label>Фамилия:</label>
        <input v-model="formData.lastName" type="text" required />
      </div>
      <div class="form-group">
        <label>Отчество:</label>
        <input v-model="formData.patronymicName" type="text" />
      </div>
      <div class="form-group">
        <label>Email:</label>
        <input v-model="formData.email" type="email" required />
      </div>
      <div class="form-group">
        <label>Логин:</label>
        <input v-model="formData.login" type="text" required />
      </div>
      <div class="form-group">
        <label>Пароль:</label>
        <input v-model="formData.password" type="password" required />
      </div>
      <button type="submit">Добавить</button>
    </form>

    <p v-if="message" class="message">{{ message }}</p>

    <hr />

    <h3>Просмотр пользователей</h3>
    <div class="form-group">
      <label>Пользователь:</label>
      <select v-model="selectedUserEmail">
        <option disabled value="">-- выбрать --</option>
        <option v-for="user in users" :key="user.email" :value="user.email">
          {{ user.lastName }} {{ user.firstName }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label>Поле:</label>
      <select v-model="selectedField">
        <option value="firstName">Имя</option>
        <option value="lastName">Фамилия</option>
        <option value="patronymicName">Отчество</option>
        <option value="email">Email</option>
        <option value="login">Логин</option>
        <option value="password">Пароль</option>
      </select>
    </div>

    <div class="button-group">
      <button @click="fetchUserData">Получить данные</button>
      <button @click="fetchAllUsers">Получить всех</button>
    </div>

    <pre v-if="userData" class="output">{{ userData }}</pre>
  </div>
</template>

<style>
.container {
  max-width: 600px;
  margin: 30px auto;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}
.form-group {
  margin: 12px 0;
}
.form-group label {
  display: block;
  margin-bottom: 4px;
  font-weight: 600;
}
.form-group input {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
button {
  padding: 8px 16px;
  margin-top: 8px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #0056b3;
}
.message {
  color: green;
  margin: 12px 0;
  min-height: 1.5em;
}
.button-group {
  margin: 16px 0;
}
.button-group button {
  margin-right: 10px;
}
.output {
  margin-top: 16px;
  background: #f8f9fa;
  padding: 12px;
  border-radius: 4px;
  overflow-x: auto;
  white-space: pre-wrap;
}
hr {
  margin: 24px 0;
  border: 0;
  border-top: 1px solid #eee;
}
</style>