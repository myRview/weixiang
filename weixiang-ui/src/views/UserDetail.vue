<template>
  <div class="profile-container">
    <h1>个人中心</h1>
    <div class="user-info">
      <div class="avatar-section">
        <img :src="user.avatar" alt="用户头像" class="avatar" />
      </div>
      <div class="user-details">
        <p><strong>用户名：</strong>{{ user.username }}</p>
        <p><strong>邮箱：</strong>{{ user.email }}</p>
        <p><strong>注册时间：</strong>{{ user.registerTime }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const user = ref({
  username: '示例用户',
  email: 'user@example.com',
  avatar: '/images/avatar-default.png',
  registerTime: '2024-01-01'
});
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.user-info {
  display: flex;
  gap: 20px;
  margin-top: 20px;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.avatar-section .avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.user-details p {
  margin: 8px 0;
  color: #333;
}
</style>