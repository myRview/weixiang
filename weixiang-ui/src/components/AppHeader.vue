<template>
   <el-header class="app-header">
     <div class="logo">xxxxx系统管理平台</div>
     <div class="user-info">
       <el-icon class="notification-icon">
         <BellFilled/>
       </el-icon>
       <el-dropdown trigger="click">
         <div class="user-avatar">
           <el-avatar :src="user.avatar" size="medium"></el-avatar>
           <span class="user-name">{{ user.name }}</span>
           <el-icon class="arrow-down"><ArrowDown /></el-icon>
         </div>
         <template #dropdown>
           <el-dropdown-menu>
             <el-dropdown-item>个人中心</el-dropdown-item>
             <el-dropdown-item>修改密码</el-dropdown-item>
             <el-dropdown-item divided>退出登录</el-dropdown-item>
           </el-dropdown-menu>
         </template>
       </el-dropdown>
     </div>
   </el-header>
 </template>
  
 <script setup lang="ts">
 import { ArrowDown,BellFilled } from "@element-plus/icons-vue";
 import { ref } from 'vue';
 // 临时用户数据，实际项目中应替换为真实的用户信息获取逻辑
 const user = ref({
   avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
   name: '管理员'
 });
 </script>
  
 <style scoped>
 .app-header {
   background-color: #fdfdfd;
   color: #333;
   padding: 0 30px;
   display: flex;
   align-items: center;
   justify-content: space-between;
   box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
   height: 64px;
   transition: all 0.3s ease;
 }
  
 .logo {
   font-size: 22px;
   font-weight: 600;
   color: #1890ff;
   padding: 0 15px;
   height: 100%;
   display: flex;
   align-items: center;
   letter-spacing: 0.5px;
 }
  
 .user-info {
   display: flex;
   align-items: center;
 }
  
 .notification-icon {
   margin-right: 16px;
   font-size: 20px;
   color: #909399;
   cursor: pointer;
   transition: color 0.2s ease;
 }
  
 .notification-icon:hover {
   color: #1890ff;
 }
  
 .user-avatar {
   display: flex;
   align-items: center;
   cursor: pointer;
   padding: 8px 12px;
   border-radius: 20px;
   transition: background-color 0.2s ease;
 }
  
 .user-avatar:hover {
   background-color: #f5f7fa;
 }
  
 .user-name {
   margin: 0 10px;
   font-size: 14px;
   color: #303133;
   font-weight: 500;
 }
  
 .arrow-down {
   font-size: 14px;
   color: #909399;
   transition: transform 0.2s ease;
 }
  
 .el-dropdown.open .arrow-down {
   transform: rotate(180deg);
 }
 </style>