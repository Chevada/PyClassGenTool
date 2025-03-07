<template>
    <div id="btn">
        <span>Welcome to this tool to generate Python class level code!</span>
    </div>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
// #btn {
//   /* 容器基础样式 */
//   width: 80vw;
//   height: 80px;
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   color: black;
//   font-size: 30px;
//   position: relative;
//   overflow: hidden; /* 隐藏旋转时溢出的部分 */
//   margin:0 auto;
//   border-radius: 10px;
//   margin-top: 10px;
//   font-weight: 1000;
//   text-align: center;
// }

#btn {
  /* 毛玻璃效果核心代码 */
  background: rgba(64, 158, 255, 0.3); // 半透明白色背景
  backdrop-filter: blur(10px);          // 背景模糊
  -webkit-backdrop-filter: blur(10px);  // Safari 兼容
  
  /* 增强玻璃质感 */
  border: 1px solid rgba(255, 255, 255, 0.2); // 半透明边框
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);   // 柔和阴影
  
  /* 原有样式保持不变 */
  width: 80vw;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: black;
  font-size: 30px;
  position: relative;
  overflow: hidden;
  margin: 0 auto;
  border-radius: 10px;
  margin-top: 10px;
  font-weight: 1000;
  text-align: center;

  /* 文字阴影增强可读性 */
  // span {
  //   text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  // }
}

// #btn::before {
//   content: "";
//   position: absolute;
//   width: 200%; /* 扩大宽度确保旋转时覆盖整个容器 */
//   height: 2000%;
//   background: linear-gradient(
//     90deg, 
//     yellow 20%,  /* 左半部分颜色 */
//     #5D23FF 40%,    /* 右半部分颜色 */
//     yellow 60%,
//     #5D23FF 80%, 
//   );
//   animation: rotate 10s linear infinite; /* 添加旋转动画 */
//   transform-origin: center; /* 围绕中心旋转 */
// }

// @keyframes rotate {
//   from { transform: rotate(0deg); }
//   to { transform: rotate(360deg); }
// }

// /* 确保文字在背景之上 */
// #btn span {
//   position: relative;
//   z-index: 1;
// }
</style>